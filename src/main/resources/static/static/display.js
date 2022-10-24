const container = document.querySelector(".slides-container");
const canvas = container.querySelector("canvas");
const displayApiBaseUrl = "../api" + window.location.pathname;
const fileUrl = displayApiBaseUrl + "/file";
const metaUrl = displayApiBaseUrl + "/meta";
const eventsUrl = displayApiBaseUrl + "/events";

window.addEventListener("resize", updateDisplay);
document.querySelector("body").addEventListener("keydown", keydown);

let pdf = null;
let futurePageNum = null;
let pageNum = null;

loadMeta();
loadFile();
subscribe();

function loadMeta() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener("readystatechange", function() {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            const resp = JSON.parse(xhr.responseText);
            const num = resp.currentSlideIndex + 1;
            if (pdf === null) {
                futurePageNum = num;
            } else {
                trySetPage(num);
            }
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
    sse.addEventListener("error", function() { alert("SSE connection failed") });
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
    pdf
        .getPage(pageNum)
        .then(function(page) {
            display(page, canvas, container);
        });
}

function display(page, canvas, container) {
    const initialViewport = page.getViewport({ scale: 1, });
    const widthRatio = container.clientWidth / initialViewport.width;
    const heightRatio = container.clientHeight / initialViewport.height;
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
    page.render(renderContext);
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
