// Функция для открытия всплывающего окна ошибки с заданным текстом
function openErrorPopup(errorMessage) {
    var errorPopup = document.getElementById("errorPopup");
    var errorMessageSpan = document.getElementById("errorMessage");
    errorMessageSpan.innerText = errorMessage;
    errorPopup.style.display = "block";
    // Закрыть окно через 5 секунд
    setTimeout(function() {
        errorPopup.style.display = "none";
    }, 5000);
}

// Функция для закрытия всплывающего окна ошибки
function closeErrorPopup() {
    var errorPopup = document.getElementById("errorPopup");
    errorPopup.style.display = "none";
}