package com.alc.archivemanager.controllers;

import com.alc.archivemanager.config.ArchiveUserDetails;
import com.alc.archivemanager.model.*;
import com.alc.archivemanager.repository.UserRepository;
import com.alc.archivemanager.searchers.ApacheLuceneSearcher;
import com.alc.archivemanager.searchers.ComboSearcher;
import com.alc.archivemanager.searchers.DeepPavlovSearcher;
import com.alc.archivemanager.searchers.ISearcher;
import com.alc.archivemanager.config.FilePaths;
import com.alc.archivemanager.servises.ArchiveUserService;
import com.alc.archivemanager.util.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    ArchiveUserService archiveUserService;

    @GetMapping("/user_info")
    public String[] userInfo(HttpServletRequest request){
        return ((ArchiveUserDetails)((Authentication)request.getUserPrincipal()).getPrincipal()).getRoles();
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(name = "method", required = false) String method,
                                                     @RequestParam("search_param") String search_param,
                                                     @RequestParam("destination") String destination
    ) throws Exception {

        destination = destination.equals("null") ? FilePaths.STORAGE_SUFFIX : destination;
        if(destination.contains("..") || !destination.contains(FilePaths.STORAGE_SUFFIX)) return new ResponseEntity<>("Отказано в доступе", HttpStatus.LOCKED);

        try {
            if (search_param == null || search_param.isEmpty()) throw new Exception("Параметр поиска не задан");

            ISearcher searcher = switch (method) {
                case "neural" -> new DeepPavlovSearcher();
                case "combo" -> new ComboSearcher();
                default -> new ApacheLuceneSearcher();
            };

            List<SearchResult> searchResult = searcher.searchProcess(FilePaths.MAIN_PATH + destination, search_param);
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
        List<FileSystemItem> files = FileUtil.getFiles(path == null || path.isEmpty() ? FilePaths.MAIN_PATH + FilePaths.STORAGE_SUFFIX : FilePaths.MAIN_PATH + path);
        if(files.isEmpty()){
            files.add(new FileSystemItem(path, FileType.SHADOW, path));
        }
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("/get_trashed_files")
    public ResponseEntity<List<TrashFileItem>> getTrashedFiles() {

        List<TrashFileItem> files = FileUtil.getTrashedFiles();
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
            destination = FilePaths.MAIN_PATH + FilePaths.STORAGE_SUFFIX;
        }
        else destination  = FilePaths.MAIN_PATH + destination;

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
            fullPath = FilePaths.MAIN_PATH + FilePaths.STORAGE_SUFFIX;
        }
        else fullPath  = FilePaths.MAIN_PATH + destination;
        fullPath += "/" + folderName;

        if (!fullPath.contains("storage") || fullPath.contains("..")) return ResponseEntity.status(HttpStatus.LOCKED).body("Отказано в доступе.");

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

    @DeleteMapping("/move_to_trash_items")
    public ResponseEntity<String> moveToTrashItems(@RequestBody String[] items){


        for (String item : items) {
            File file = new File(FilePaths.MAIN_PATH + item);

            if (!file.getPath().contains("storage") || item.contains("..")) return new ResponseEntity<>("Отказано в доступе", HttpStatus.LOCKED);

            if(file.exists()){
                if(!FileUtil.moveToTrashFile(file)) return new ResponseEntity<>("Ошибка удаления.", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Удалено.", HttpStatus.OK);
    }

    @DeleteMapping("/delete_from_trash_items")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteFromTrashItems(@RequestBody String[] items){


        for (String item : items) {
            File file = new File(FilePaths.MAIN_PATH + item);

            if (!file.getPath().contains(FilePaths.TRASH_SUFFIX) || item.contains("..")) return new ResponseEntity<>("Отказано в доступе", HttpStatus.LOCKED);

            if(file.exists()){
                if(!FileUtil.deleteFileFromTrash(file)) return new ResponseEntity<>("Ошибка удаления.", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Удалено.", HttpStatus.OK);
    }


    @PutMapping("/recover_items")
    public ResponseEntity<String> recoverItems(@RequestBody String[] items){

        for (String item : items) {
            File file = new File(FilePaths.MAIN_PATH + item);

            if (item.contains("..") || !item.contains(FilePaths.TRASH_SUFFIX)) return new ResponseEntity<>("Отказано в доступе", HttpStatus.LOCKED);

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
            String fileFullPath = FilePaths.MAIN_PATH + filePath;

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
            return ResponseEntity.internalServerError().body("Внутренняя ошибка сервера: " + e.getMessage());
        }
    }

    @GetMapping("/download_file")
    public ResponseEntity<Resource> downloadFile(@RequestParam("path") String filePath) throws IOException {
        // Здесь необходимо указать путь к папке, в которой хранятся ваши файлы
        Path file = Path.of(FilePaths.MAIN_PATH, filePath);

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

    @PostMapping("/add_user")
    public String addUser(@RequestBody ArchiveUser user){
        return archiveUserService.addUser(user);
    }

    @GetMapping("/get_users")
    public List<ArchiveUser> getUsers(){
        return archiveUserService.getAll();
    }
}
