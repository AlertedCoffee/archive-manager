package com.alc.archivemanager.repository;

import com.alc.archivemanager.model.ArchiveUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ArchiveUser, Long> {
    Optional<ArchiveUser> findByName(String userName);
}
