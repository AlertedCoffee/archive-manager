# Archive Manager
Приложение из себя представляет [облачное хранилище](https://i.imgur.com/XXlISiE.png), в которое пользователи отгружают документы в форматах pdf, odt и docx. 

**Основная функция системы** - обеспечение удобного доступа к файлам. Для этого реализованы системы контекстного поиска:
+ **Apache Lucene** - полнотекстовый поиск. 
![Пример работы поиска](https://i.imgur.com/7Wa31dk.png)
![Пример развернутого результата поиска](https://i.imgur.com/AqW8NbY.png)
+ **DeepPavlov Context Question Answering** - нейронный контекстный поиск на модели squard_ru_bert. Эта нейронная система находит ответ на вопрос пользователя к представленном ей контексте. Ответом является фрагмент текста, отвечающий на вопрос.
![Пример работы поиска](https://i.imgur.com/GP08Rqf.png)
![Пример развернутого результата поиска](https://i.imgur.com/IKnf7LS.png)
<br>
<br><br>
<br>
**Дополнительные функции:**
+ В файловой системе реализованы функции управления хранилищем. Панель управления файлами: 
![панель управления файлами](https://i.imgur.com/CWZY3Ww.png)
+ Безопасное удаление в корзину.
![корзину](https://i.imgur.com/a4nCS8K.png)
+ Система авторизации на SpringSecurity (Не забудьте настроить доступ через <code>SecurityConfig.java</code>. В репозитории он закомментирован)
![Авторизация](https://i.imgur.com/EBIhtGh.png)

Инициализация пользователя админа:
```
curl -X POST http://localhost:8080/api/add_user -H 'Content-Type: application/json' -d '
{
    "name" : "admin",
    "password" : "admin",
    "roles" : "ROLE_ADMIN"
}'
```
<br>
<br>
<br>
____________________

**Build:**
+ Postgres
```
docker run -d \
  --name postgres_container \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=admin \
  -e POSTGRES_DB=archive-users \
  -p 5432:5432 \
  -v <your_data_path>:/var/lib/postgresql/data \
  --restart always \
  postgres:16
```
+ [DeepPavlov](https://docs.deeppavlov.ai/en/master/features/models/SQuAD.html#2.-Get-started-with-the-model) <br>
    Пример запуска [Rest API](https://docs.deeppavlov.ai/en/master/integrations/rest_api.html#rest-api-usage-example) <br>
    <code>server_config.json</code>
    ```
    {
        "common_defaults": {
            "host": "127.0.0.1",
            "port": 5088,
            "model_args_names": [],
            "https": false,
            "https_cert_path": "",
            "https_key_path": "",
            "socket_type": "TCP",
            "unix_socket_file": "/tmp/deeppavlov_socket.s",
            "socket_launch_message": "launching socket server at"
        }
    }
    ```


**Futures:**
+ Docker container