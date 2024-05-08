package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.DeepPavlovApiRequest;
import com.alc.archivemanager.model.FileNamePairParsed;
import com.alc.archivemanager.model.SearchResult;
import com.alc.archivemanager.parsers.IParser;
import com.google.common.io.Files;
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
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DeepPavlovSearcher extends SearchProcesses{

    public List<SearchResult> search(List<String> fileNames, List<String> texts, List<String> searchParams) {
        String apiUrl = "http://127.0.0.1:5088/model";


        DeepPavlovApiRequest apiRequest = new DeepPavlovApiRequest(
                texts,
                searchParams
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

                        List<SearchResult> searchResults = new ArrayList<>();

                        for (int i = 0; i < ((List<?>)responseArray[0]).size(); i++){

                            String pattern = (String) ((List<?>) responseArray[0]).get(i);
                            String findedSubString;
                            String text = texts.get(i);
                            int pageNumber = -666;

                            if(Files.getFileExtension(fileNames.get(i)).equals(SearchProcesses.PDF)){
                                pageNumber = getPage(text, pattern);
                            }

                            int subStringCoordinate = text.indexOf(pattern);

                            List<Integer> nIndexes = new ArrayList<>();

                            char targetChar = '\n';
                            // Ищем индексы символа в строке
                            for (int j = 0; j < text.length(); j++) {
                                if (text.charAt(j) == targetChar) {
                                    nIndexes.add(j);
                                }
                            }
                            int lIndex = 1;

                            for (int j = nIndexes.size() - 1; j >= 0; j--) {
                                if (nIndexes.get(j) < subStringCoordinate) {
                                    lIndex = nIndexes.get(j);
                                    break;
                                }
                            }

                            int rIndex = text.length();
                            for (int j = 0; j < nIndexes.size(); j++) {
                                if (nIndexes.get(j) > subStringCoordinate + pattern.length()) {
                                    rIndex = nIndexes.get(j);
                                    break;
                                }
                            }

                            findedSubString = text.substring(lIndex, rIndex);

                            searchResults.add(
                                    new SearchResult(fileNames.get(i),
                                            (String) ((List<?>) responseArray[0]).get(i),
                                            (double) ((List<?>) responseArray[1]).get(i),
                                            pageNumber,
                                            findedSubString,
                                            (double) ((List<?>) responseArray[2]).get(i))
                            );
                        }

                        return searchResults;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return null;
    }
    private String StringFormer(String[] answerPages, int j){
        if (j > answerPages.length - 1){
            j -=2;
        }
        return answerPages[j + 1] + '\n' + answerPages[j + 2] + '\n' + answerPages[j + 3] + '\n' + answerPages[j + 4] + '\n';


    }

    private Integer getPage(String text, String template){
        String[] answerPages = text.split("\r\n\r\n");
        int findedIndex = 0;
        for (int i = 0; i < answerPages.length; i++){
            if(answerPages[i].contains(template))
                findedIndex = i;
        }

        return  ++findedIndex;
    }

    @Override
    public List<SearchResult> searchProcess(String mainPath, String searchParam) {
        File dir = new File(mainPath);

        if(!dir.exists())
            return null;

        List<FileNamePairParsed> files = new ArrayList<>(getContent(dir));

        return searchProcess(files, searchParam);
    }

    public List<SearchResult> searchProcess(List<FileNamePairParsed> files, String searchParam){
        List<SearchResult> searchResults;

        List<String> texts = new ArrayList<>();
        List<String> searchParams = new ArrayList<>();
        List<String> returnedFiles = new ArrayList<>();

        for (FileNamePairParsed file : files){
            try {
                IParser parser = parserFactory(file.getNameForParse());
                if (parser != null) {
                    texts.add(parser.parse(file.getNameForParse()));
                    searchParams.add(searchParam);
                    returnedFiles.add(file.getFileName());
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        searchResults = search(returnedFiles, texts, searchParams);

        return searchResults;
    }
}
