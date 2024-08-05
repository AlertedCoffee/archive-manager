# Archive Manager

The application serves as a [cloud storage](https://i.imgur.com/XXlISiE.png) where users can upload documents in pdf, odt, and docx formats.

**The main function of the system** is to provide convenient access to files. To achieve this, the following context search systems are implemented:
+ **Apache Lucene** - full-text search.
![Search example](https://i.imgur.com/7Wa31dk.png)
![Expanded search result example](https://i.imgur.com/AqW8NbY.png)
+ **DeepPavlov Context Question Answering** - neural context search based on the squad_ru_bert model. This neural system finds the answer to the user's question within the provided context. The answer is a text fragment that answers the question.
![Search example](https://i.imgur.com/GP08Rqf.png)
![Expanded search result example](https://i.imgur.com/IKnf7LS.png)

**Additional features:**
+ Storage management functions are implemented in the file system. File management panel:
![File management panel](https://i.imgur.com/CWZY3Ww.png)
+ Secure deletion to the recycle bin.
![Recycle bin](https://i.imgur.com/a4nCS8K.png)
+ Authorization system using SpringSecurity (Don't forget to configure access through <code>SecurityConfig.java</code>. In the repository, it is commented out)
![Authorization](https://i.imgur.com/EBIhtGh.png)

Admin user initialization:

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
    Example of launching [Rest API](https://docs.deeppavlov.ai/en/master/integrations/rest_api.html#rest-api-usage-example) <br>
    <code>server_config.json</code> for this
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