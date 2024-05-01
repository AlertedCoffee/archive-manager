function search(method){
    // Блокировка кнопки
    showForm('search_overlay');
    let btn = document.getElementById("search-button");
    btn.disabled = true; 

    let destination = document.getElementById('tbody').getAttribute('parent');
    let searchParam = document.getElementById('search-bar').value

    // Отобразить колесо загрузки
    $('.loading-spinner').show();

    // Отправить AJAX-запрос на бэкенд
    $.ajax({
        type: 'GET',
        url: '/api/search',
        data: { 'method' : method, 'search_param' : searchParam, 'destination' : destination},
        success: function(response) {
            // Скрыть колесо загрузки
            $('.loading-spinner').hide();

            // Очистить текущие результаты
            $('#search-results').empty();

            try{
                // Обработать полученные результаты
                response.forEach(function(result) {
                    var resultElement = $('<div class="search_res"></div>');
                    if(result.path == null){
                        resultElement.append('<p>Имя файла: <span>' + result.shortFileName + '</span></p>');
                    }
                    else{
                        resultElement.append('<p>Имя файла: <a  href="' + result.path + '" target="_blank" rel="noopener noreferrer">' + result.shortFileName + '</a></p>')
                    }

                    resultElement.append('<details><summary>Краткий ответ: <span>' + (result.Answer ? result.Answer : "") + '</span></summary><p>' + result.PageText + '</p></details>');
                    resultElement.append('<p>Вероятность: <span>' + result.Coincidence + '</span></p>');

                    // Добавить результат в контейнер для результатов поиска
                    $('#search-results').append(resultElement);
                });
            }
            catch{
                $('#search-results').append("<h2>Упс! Что-то пошло не так :( </h2><br><p>Перезагрузите страницу.");
            }
            if(response.length == 0){
                $('#search-results').append("<h2>Упс! По запросу ничего не найдено :(</h2>");
            }

            btn.disabled = false;
        },
        error: function(error) {
            // Скрыть колесо загрузки в случае ошибки
            $('.loading-spinner').hide();
            $('#search-results').empty();

            if( error.status == 400 && searchParam == ""){
                $('#search-results').append("<h2>Упс! Запрос поиска не задан :( </h2>");
            }
            else if(error.status == 400){
                $('#search-results').append("<h2>Упс! Что-то пошло не так :( </h2>");
            }
            else if(error.status == 423){
                $('#search-results').append("<h2>Ай-ай-ай! Отказано в доступе. </h2>");
            }

            console.error('Error:', error);
            btn.disabled = false;
        }
    });
}