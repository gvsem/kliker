const uploadFileInput = document.querySelector(".upload-file-input");
const displayLink = document.querySelector(".display-link");
const clickerLink = document.querySelector(".clicker-link");

uploadFileInput.addEventListener("change", uploadFileInputChange);

function uploadFileInputChange(e) {
    const filesList =  e.target.files;
    if (!filesList.length) {
        return;
    }

    const file = filesList[0];
    const formData = new FormData();
    formData.append("file", file);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener("readystatechange", uploadRequestReadyStateChange);
    xhr.open("POST", "api/keynote/upload");
    xhr.send(formData);
}

function uploadRequestReadyStateChange(e) {
    const xhr = e.target;

    if (xhr.readyState === XMLHttpRequest.DONE && xhr.status < 400) {
        const resp = JSON.parse(xhr.responseText);
        const displayUrl = window.location.href + "display/" + resp.displayId;
        const clickerUrl = window.location.href + "clicker/" + resp.clickerId;
        displayLink.href = displayUrl;
        displayLink.innerText = displayUrl;
        clickerLink.href = clickerUrl;
        clickerLink.innerText = clickerUrl;
    }
}
