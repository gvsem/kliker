const uploadFileInput = document.querySelector(".upload-file-input");
const keynoteSettings = document.querySelector(".keynote-settings");
const displayLink = document.querySelector(".display-link");
const clickerLink = document.querySelector(".clicker-link");
const displayQrElement = document.querySelector(".display-qr");
const clickerQrElement = document.querySelector(".clicker-qr");
const displayQr = new QRCode(displayQrElement);
const clickerQR = new QRCode(clickerQrElement);

uploadFileInput.addEventListener("change", uploadFileInputChange);

function uploadFileInputChange(e) {
    keynoteSettings.classList.add("hidden");

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

    if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status >= 400) {
            alert("Cannot upload this file. Is it a properly formatted PDF?");
            return;
        }
        const resp = JSON.parse(xhr.responseText);
        const displayUrl = window.location.href + "display/" + resp.displayId;
        const clickerUrl = window.location.href + "clicker/" + resp.clickerId;
        displayLink.href = displayUrl;
        displayLink.innerText = displayUrl;
        clickerLink.href = clickerUrl;
        clickerLink.innerText = clickerUrl;
        displayQr.makeCode(displayUrl);
        clickerQR.makeCode(clickerUrl);
        keynoteSettings.classList.remove("hidden");
    }
}
