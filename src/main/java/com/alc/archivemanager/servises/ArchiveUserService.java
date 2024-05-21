package com.alc.archivemanager.servises;

import com.alc.archivemanager.model.ArchiveUser;
import com.alc.archivemanager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArchiveUserService {

    private UserRepository repository;

    private PasswordEncoder passwordEncoder;


    public String add(ArchiveUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            repository.save(user);
        }
        catch (DataIntegrityViolationException e){
            return "Пользователь с таким именем уже существует.";
        }
        return "Добавлено.";
    }

    public String editName(ArchiveUser user, String name){
        user.setName(name);
        try {
            repository.save(user);
        }
        catch (Exception e){
            return e.getMessage();
        }
        return "Изменено.";
    }

    public String editPassword(ArchiveUser user, String oldPassword, String password) {
        if(!passwordEncoder.matches(oldPassword, user.getPassword())) return  "Пароли не совпали";
        user.setPassword(passwordEncoder.encode(password));
        try {
            repository.save(user);
        }
        catch (Exception e){
            return e.getMessage();
        }
        return "Изменено.";
    }

    public String delete(ArchiveUser user){
        try {
            repository.delete(user);
        }
        catch (Exception e){
            return e.getMessage();
        }
        return "Удалено.";
    }

    public ArchiveUser getById(long id){
        return repository.getById(id);
    }

    public List<ArchiveUser> getAll(){
        return repository.findAll();
    }
}
