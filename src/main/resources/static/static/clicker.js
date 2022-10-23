const buttonPrev = document.querySelector(".clicker-button-prev");
const buttonNext = document.querySelector(".clicker-button-next");
const clickerApiBaseUrl = "../api" + window.location.pathname;
const prevSlideUrl = clickerApiBaseUrl + "/prev_slide";
const nextSlideUrl = clickerApiBaseUrl + "/next_slide";

buttonPrev.addEventListener("click", function() { sendSignal(prevSlideUrl); });
buttonNext.addEventListener("click", function() { sendSignal(nextSlideUrl); });

function sendSignal(url) {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", url);
    xhr.send();
}
