const uploadFileInput = document.getElementById("upload-file-input");
const uploadFileTextDefault = document.querySelector(".upload-file-text-default");
const uploadFileTextOther = document.querySelector(".upload-file-text-other");
const chosenFileName = document.querySelector(".chosen-file-name");
const keynoteSettings = document.querySelector(".keynote-settings");
const displayLink = document.querySelector(".display-link");
const clickerLink = document.querySelector(".clicker-link");
const displayQrElement = document.querySelector(".display-qr");
const clickerQrElement = document.querySelector(".clicker-qr");
const qrOptions = {
    width: 250,
    height: 250,
};

const error_messages = {
    413: "Upload failed, this file is too large",
    415: "Upload failed, looks like this file is not a proper PDF",
    0: "Upload failed, please check your network connectivity and try again",
};
const unknown_error = "Unknown error occurred. Please try again or contact the developers";

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

    keynoteSettings.classList.add("hidden");

    chosenFileName.innerText = file.name;
    chosenFileName.classList.remove("hidden");
    uploadFileTextDefault.classList.add("hidden");
    uploadFileTextOther.classList.remove("hidden");

    const xhr = new XMLHttpRequest();
    xhr.addEventListener("readystatechange", uploadRequestReadyStateChange);
    xhr.open("POST", "api/keynote/upload");
    xhr.send(formData);
}

function uploadRequestReadyStateChange(e) {
    const xhr = e.target;

    if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status !== 201) {
            alert(error_messages[xhr.status] || unknown_error);
            return;
        }
        const resp = JSON.parse(xhr.responseText);
        displayKeynoteInfo(resp);
    }
}

function displayKeynoteInfo(keynote) {
    const displayUrl = window.location.href + "display/" + keynote.displayId;
    const clickerUrl = window.location.href + "clicker/" + keynote.clickerId;
    displayLink.href = displayUrl;
    displayLink.innerText = displayUrl;
    clickerLink.href = clickerUrl;
    clickerLink.innerText = clickerUrl;
    QRCode.toCanvas(displayQrElement, displayUrl, qrOptions);
    QRCode.toCanvas(clickerQrElement, clickerUrl, qrOptions);
    keynoteSettings.classList.remove("hidden");
}
