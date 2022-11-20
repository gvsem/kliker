const buttonPrev = document.querySelector(".clicker-button-prev");
const buttonNext = document.querySelector(".clicker-button-next");
const clickerApiBaseUrl = "../api" + window.location.pathname;
const prevSlideUrl = clickerApiBaseUrl + "/prev_slide";
const nextSlideUrl = clickerApiBaseUrl + "/next_slide";

const error_messages = {
    404: "Cannot send the command, this clicker does not exist",
    0: "Request failed, please check your network connectivity and try again",
};
const unknown_error = "Unknown error occurred. Please try again or contact the developers";

buttonPrev.addEventListener("click", function() { sendSignal(prevSlideUrl); });
buttonNext.addEventListener("click", function() { sendSignal(nextSlideUrl); });

function sendSignal(url) {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener("readystatechange", requestReadyStateChange);
    xhr.open("POST", url);
    xhr.send();
}

function requestReadyStateChange(e) {
    const xhr = e.target;

    if (xhr.readyState === XMLHttpRequest.DONE && xhr.status !== 200) {
        alert(error_messages[xhr.status] || unknown_error);
    }
}
