const slidesContainer = document.querySelector(".slides-container");
const canvas = slidesContainer.querySelector("canvas");
const displayApiBaseUrl = "../api" + window.location.pathname;
const fileUrl = displayApiBaseUrl + "/file";
const metaUrl = displayApiBaseUrl + "/meta";
const eventsUrl = displayApiBaseUrl + "/events";

const error_messages = {
    404: "This display does not exist",
    0: "Request failed, please check your network connectivity and try again",
};
const unknown_error = "Unknown error occurred. Please try again or contact the developers";

window.addEventListener("resize", updateDisplay);
document.querySelector("body").addEventListener("keydown", keydown);

let pdf = null;
let futurePageNum = null;
let pageNum = null;

let renderedSlidesCache = {};
let renderedSlidesCacheWidth = slidesContainer.clientWidth;
let renderedSlidesCacheHeight = slidesContainer.clientHeight;
let renderingTasksStack = [];

loadMeta();
loadFile();
subscribe();
renderer();

function renderer() {
    if (!renderingTasksStack.length) {
        setTimeout(renderer, 200);
        return;
    }

    const task = renderingTasksStack.pop();
    if (
        task.width !== slidesContainer.clientWidth
        || task.height !== slidesContainer.clientHeight
    ) {
        setTimeout(renderer, 0);
        return;
    }

    renderPage(task.page, task.canvas, function() {
        setTimeout(renderer, 0);
    });
}

function loadMeta() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener("readystatechange", function() {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status !== 200) {
            alert(error_messages[xhr.status] || unknown_error);
        }

        const resp = JSON.parse(xhr.responseText);
        const num = resp.currentSlideIndex + 1;
        if (pdf === null) {
            futurePageNum = num;
        } else {
            trySetPage(num);
        }
    });
    xhr.open("GET", metaUrl);
    xhr.send();
}

function loadFile() {
    pdfjsLib.getDocument(fileUrl).promise.then(function(loadedPdf) {
        pdf = loadedPdf;
        trySetPage(futurePageNum !== null ? futurePageNum : 1);
    });
}

function subscribe() {
    const sse = new EventSource(eventsUrl);
    sse.addEventListener("message", handleMessage);
}

function trySetPage(num) {
    if (pdf === null) {
        return;
    }
    if (num < 1 || num > pdf.numPages) {
        return;
    }
    pageNum = num;
    updateDisplay();
}

function updateDisplay() {
    if (pdf === null) {
        return;
    }
    const slide = getRenderedPage(pageNum);
    slidesContainer.removeChild(slidesContainer.lastChild);
    slidesContainer.appendChild(slide);
}

function getRenderedPage(num) {
    ensureRenderedSlidesCacheValid();
    const cached = renderedSlidesCache[num];
    if (cached !== undefined) {
        return cached;
    }

    const canvas = document.createElement("canvas");
    renderedSlidesCache[num] = canvas;

    pdf
        .getPage(num)
        .then(function(page) {
            renderingTasksStack.push({
                page: page,
                canvas: canvas,
                width: slidesContainer.clientWidth,
                height: slidesContainer.clientHeight,
            });
        });

    return canvas;
}

function renderPage(page, canvas, renderedCallback) {
    const initialViewport = page.getViewport({ scale: 1, });
    const widthRatio = slidesContainer.clientWidth / initialViewport.width;
    const heightRatio = slidesContainer.clientHeight / initialViewport.height;
    const scale = Math.min(widthRatio, heightRatio);
    const scaledViewport = page.getViewport({ scale: scale });

    const height = Math.floor(scaledViewport.height);
    const width = Math.floor(scaledViewport.width);

    canvas.height = height;
    canvas.width = width;
    canvas.style.height = height;
    canvas.style.width = width;

    var renderContext = {
        canvasContext: canvas.getContext("2d"),
        viewport: scaledViewport,
    };
    page.render(renderContext).promise.then(renderedCallback);
}

function ensureRenderedSlidesCacheValid() {
    if (
        renderedSlidesCacheWidth === slidesContainer.clientWidth
        && renderedSlidesCacheHeight === slidesContainer.clientHeight
    ) {
        return;
    }

    renderedSlidesCache = {};
    renderedSlidesCacheWidth = slidesContainer.clientWidth;
    renderedSlidesCacheHeight = slidesContainer.clientHeight;
}

function handleMessage(e) {
    const data = JSON.parse(e.data);
    if (data.event === "set-slide") {
        trySetPage(data.payload.index + 1);
    } else {
        alert("Unknown SSE event: " + eventKind);
    }
}

function keydown(e) {
    if (e.key === "ArrowRight" || e.key === " " || e.key === "Enter") {
        trySetPage(pageNum + 1);
    } else if (e.key === "ArrowLeft") {
        trySetPage(pageNum - 1);
    }
}
