$(document).ready(function() {
    // Получение информации о пользователе при загрузке страницы
    fetch('/api/user_info')
        .then(response => response.json())
        .then(data => {
            $('#user-id').text(data.id);
            $('#user-name').text(data.name);
        })
        .catch(error => console.error('Ошибка:', error));

    // Показать форму для изменения имени
    $('#edit-name-btn').click(function() {
        $('#user-info-section').hide();
        $('#edit-name-section').show();
    });

    // Отмена изменения имени
    $('#cancel-edit-name').click(function() {
        $('#edit-name-section').hide();
        $('#user-info-section').show();
    });

    // Обработка формы изменения имени
    $('#edit-name-form').submit(function(event) {
        event.preventDefault();
        const id = $('#user-id').text();
        const newName = $('#new-name').val();

        $.ajax({
            url: '/api/edit_user_name',
            type: 'PUT',
            data: { id: id, name: newName },
            success: function(response) {
                openErrorPopup(response);
                $('#user-name').text(newName);
                $('#edit-name-section').hide();
                $('#user-info-section').show();
            },
            error: function(error) {
                alert('Ошибка при изменении имени');
                console.error('Ошибка:', error);
            }
        });
    });

    // Показать форму для изменения пароля
    $('#edit-password-btn').click(function() {
        $('#user-info-section').hide();
        $('#edit-password-section').show();
    });

    // Отмена изменения пароля
    $('#cancel-edit-password').click(function() {
        $('#edit-password-section').hide();
        $('#user-info-section').show();
    });

    // Обработка формы изменения пароля
    $('#edit-password-form').submit(function(event) {
        event.preventDefault();
        const id = $('#user-id').text();
        const oldPassword = $('#old-password').val();
        const newPassword = $('#new-password').val();

        $.ajax({
            url: '/api/edit_user_password',
            type: 'PUT',
            data: { id: id, old_password: oldPassword, password: newPassword },
            success: function(response) {
                openErrorPopup(response);
                $('#edit-password-section').hide();
                $('#user-info-section').show();
            },
            error: function(error) {
                alert('Ошибка при изменении пароля');
                console.error('Ошибка:', error);
            }
        });
    });
});