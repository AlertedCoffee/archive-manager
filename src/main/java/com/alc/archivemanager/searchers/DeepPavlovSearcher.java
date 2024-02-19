package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.DeepPavlovApiRequest;
import com.alc.archivemanager.model.SearchResultModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class DeepPavlovSearcher extends SearchProcesses{

    @Override
    public SearchResultModel Search(String fileName, String text, String searchParam) {
        String apiUrl = "http://127.0.0.1:5088/model";


        DeepPavlovApiRequest apiRequest = new DeepPavlovApiRequest(
                List.of(text),
                List.of(searchParam)
        );

        // Преобразуйте объект запроса в JSON-строку с использованием Gson
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonRequest = gson.toJson(apiRequest);

        // Создайте HTTP-клиент
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Создайте объект запроса и установите метод POST
            HttpPost httpPost = new HttpPost(apiUrl);

            // Установите заголовок Content-Type
            httpPost.setHeader("Content-Type", "application/json");

            // Преобразуйте объект запроса обратно в JSON-строку и установите ее в тело запроса
            StringEntity requestEntity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);
            httpPost.setEntity(requestEntity);

            // Выполните запрос
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // Обработайте ответ
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()))) {
                        String line;
                        StringBuilder responseString = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            responseString.append(line);
                        }

                        Object[] responseArray = gson.fromJson(responseString.toString(), Object[].class);


                        List<SearchResultModel> searchResultModels = List.of(
                                new SearchResultModel(fileName,
                                        GetSubPage(text, (String) ((List<?>) responseArray[0]).get(0)),
                                        (double) ((List<?>) responseArray[1]).get(0),
                                        (double) ((List<?>) responseArray[2]).get(0))
                        );

                        return searchResultModels.get(0);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private String GetSubPage(String text, String template){
        String[] answerPages = text.split("\r\n\r\n");
        int findedIndex = 0;
        for (int i = 0; i < answerPages.length; i++){
            if(answerPages[i].indexOf(template) != -1)
                findedIndex = i;
        }

        return  answerPages[findedIndex];
    }

    @Override
    public List<SearchResultModel> PDFSearchProcess(String mainPath, String searchParam) {
        return super.PDFSearchProcess(mainPath, searchParam);
    }
}
