package com.alc.archivemanager.controllers;

import com.alc.archivemanager.config.ArchiveUserDetails;
import com.alc.archivemanager.model.ArchiveUser;
import com.alc.archivemanager.servises.ArchiveUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserApiController {

    @Autowired
    ArchiveUserService archiveUserService;

    @PostMapping("/add_user")
    public String addUser(@RequestBody ArchiveUser user){
        return archiveUserService.add(user);
    }

    @DeleteMapping("/delete_user")
    public String deleteUser(@RequestParam("id") Long id){
        return archiveUserService.delete(archiveUserService.getById(id));
    }

    @GetMapping("/get_users")
    public List<ArchiveUser> getUsers(){
        return archiveUserService.getAll();
    }

    @GetMapping("/user_info")
    public ArchiveUser userInfo(HttpServletRequest request){
        return ((ArchiveUserDetails)((Authentication)request.getUserPrincipal()).getPrincipal()).getUser();
    }

    @PutMapping("/edit_user_name")
    public String editName(@RequestParam("id") Long id, @RequestParam("name") String name){
        ArchiveUser user = archiveUserService.getById(id);
        return archiveUserService.editName(user, name);
    }

    @PutMapping("/edit_user_password")
    public String editPassword(@RequestParam("id") Long id, @RequestParam("old_password") String oldPassword, @RequestParam("password") String password){
        ArchiveUser user = archiveUserService.getById(id);
        return archiveUserService.editPassword(user, oldPassword, password);
    }
}
