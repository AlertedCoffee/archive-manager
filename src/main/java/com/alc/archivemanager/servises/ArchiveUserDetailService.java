package com.alc.archivemanager.servises;

import com.alc.archivemanager.config.ArchiveUserDetails;
import com.alc.archivemanager.model.ArchiveUser;
import com.alc.archivemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArchiveUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ArchiveUser> user = repository.findByName(username);
        return user.map(ArchiveUserDetails::new).orElseThrow(()-> new UsernameNotFoundException(username + " not found"));
    }
}
