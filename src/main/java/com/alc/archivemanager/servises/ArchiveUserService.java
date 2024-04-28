package com.alc.archivemanager.servises;

import com.alc.archivemanager.model.ArchiveUser;
import com.alc.archivemanager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArchiveUserService {

    private UserRepository repository;

    private PasswordEncoder passwordEncoder;


    public String addUser(ArchiveUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            repository.save(user);
        }
        catch (DataIntegrityViolationException e){
            return "Пользователь с таким именем уже существует.";
        }
        return "Добавлено.";
    }

    public List<ArchiveUser> getAll(){
        return repository.findAll();
    }
}
