package com.alc.archivemanager.controllers;

import com.alc.archivemanager.model.FileSystemItem;
import com.alc.archivemanager.model.FileType;
import com.alc.archivemanager.model.SearchResult;
import com.alc.archivemanager.searchers.ApacheLuceneSearcher;
import com.alc.archivemanager.searchers.ComboSearcher;
import com.alc.archivemanager.searchers.DeepPavlovSearcher;
import com.alc.archivemanager.searchers.ISearcher;
import com.alc.archivemanager.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestApiController {

    private final String MAIN_PATH = "C:/WebPractice/archive-manager/src/main/resources/";
    private final String STORAGE_SUFFIX = "storage/";
    private final String TRASH_SUFFIX = "trashFolder/";

    @GetMapping("/search")
    public ResponseEntity<List<SearchResult>> search(@RequestParam(name = "method", required = false) String method,
                                     @RequestParam("search_param") String search_param) throws Exception {

        try {
            if (search_param == null || search_param.isEmpty()) throw new Exception("Параметр поиска не задан");

            ISearcher searcher = switch (method) {
                case "neural" -> new DeepPavlovSearcher();
                case "combo" -> new ComboSearcher();
                default -> new ApacheLuceneSearcher();
            };

            List<SearchResult> searchResult = searcher.searchProcess(MAIN_PATH + STORAGE_SUFFIX, search_param);
            searchResult.sort(Comparator.comparingDouble(SearchResult::getCoincidence).reversed());
            return new ResponseEntity<>(searchResult, HttpStatus.OK);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get_files")
    public ResponseEntity<List<FileSystemItem>> getFiles(
            @RequestParam(name = "path", required = false) String path
    ) {
        if (path != null) if (path.contains("..")) return new ResponseEntity<>(HttpStatus.LOCKED);
        List<FileSystemItem> files = FileUtil.getFiles(path == null || path.isEmpty() ? MAIN_PATH + STORAGE_SUFFIX : MAIN_PATH + path);
        if(files.isEmpty()){
            files.add(new FileSystemItem(path, FileType.SHADOW, path));
        }
        return new ResponseEntity<>(files, HttpStatus.OK);
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("destination") String destination) {
        // Проверяем, был ли передан файл
        if (file.isEmpty()) {
            return new ResponseEntity<>("Пожалуйста, выберите файл для загрузки.", HttpStatus.BAD_REQUEST);
        }

        // Проверяем, было ли передано место для сохранения файла
        if (destination.isEmpty() || destination.equals("null")) {
            destination = MAIN_PATH + STORAGE_SUFFIX;
        }
        else destination  = MAIN_PATH + destination;

        if (!destination.contains("storage") || destination.contains("..")) return new ResponseEntity<>("Отказано в доступе", HttpStatus.LOCKED);

        try {
            FileUtil.saveFile(file, destination);

            return new ResponseEntity<>("Файл успешно загружен.", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Ошибка при загрузке файла: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create_folder")
    public ResponseEntity<String> createFolder(@RequestParam("folder_name") String folderName,
                                               @RequestParam("destination") String destination) {
        String fullPath;

        if (destination.isEmpty() || destination.equals("null")) {
            fullPath = MAIN_PATH + STORAGE_SUFFIX;
        }
        else fullPath  = MAIN_PATH + destination;
        fullPath += "/" + folderName;

        if (!fullPath.contains("storage") || destination.contains("..")) return new ResponseEntity<>("Отказано в доступе", HttpStatus.LOCKED);

        try {
            File folder = new File(fullPath);
            if (!folder.exists()) {
                if (folder.mkdirs()) {
                    return ResponseEntity.ok("Папка успешно создана.");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Не удалось создать папку.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Папка уже существует.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла ошибка при создании папки. " + e.getMessage());
        }
    }

    @DeleteMapping("/delete_items")
    public ResponseEntity<String> deleteItems(@RequestBody String[] items){


        for (String item : items) {
            File file = new File(MAIN_PATH + item);

            if (!file.getPath().contains("storage") || item.contains("..")) return new ResponseEntity<>("Отказано в доступе", HttpStatus.LOCKED);

            if(file.exists()){
                if(!FileUtil.moveToTrashFile(file)) return new ResponseEntity<>("Ошибка удаления.", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Удалено.", HttpStatus.OK);
    }


    @PutMapping("/recover_items")
    public ResponseEntity<String> recoverItems(@RequestBody String[] items){

        for (String item : items) {
            File file = new File(MAIN_PATH + TRASH_SUFFIX + item);

            if (item.contains("..")) return new ResponseEntity<>("Отказано в доступе", HttpStatus.LOCKED);

            if(file.exists()){
                if(!FileUtil.recoverFile(file)) return new ResponseEntity<>("Ошибка восстановления.", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Восстановлено.", HttpStatus.OK);
    }


    @PutMapping("/rename")
    public ResponseEntity<String> renameFile(
            @RequestParam("destination") String filePath,
            @RequestParam("new_name") String newName) {
        try {
            // Путь к файлу
            String fileFullPath = MAIN_PATH + filePath;

            if (!fileFullPath.contains("storage") || fileFullPath.contains("..") || newName.contains("..") || newName.contains("\\") || newName.contains("/")) return new ResponseEntity<>("Отказано в доступе", HttpStatus.LOCKED);

            String collisionPrefix = FileUtil.getCollisionFilePrefix(fileFullPath);

            // Переименование файла
            File file = new File(fileFullPath);
            String parentPath = file.getParent();
            File newFile = new File(parentPath, collisionPrefix + newName);

            FileUtil.getParsed(file).delete();

            if (file.renameTo(newFile)) {
                return ResponseEntity.ok("Файл успешно переименован.");
            } else {
                return ResponseEntity.badRequest().body("Не удалось переименовать файл.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Внутренняя ошибка сервера: " + e.getMessage());
        }
    }

    @GetMapping("/download_file")
    public ResponseEntity<Resource> downloadFile(@RequestParam("path") String filePath) throws IOException {
        // Здесь необходимо указать путь к папке, в которой хранятся ваши файлы
        Path file = Path.of(MAIN_PATH, filePath);

        if (!file.toString().contains("storage") || file.toString().contains("..")) return new ResponseEntity<>(null, HttpStatus.LOCKED);

        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
