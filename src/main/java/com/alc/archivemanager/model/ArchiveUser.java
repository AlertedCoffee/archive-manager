package com.alc.archivemanager.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.common.reflection.qual.GetMethod;

@Data
@Entity
@Table(name="users")
public class ArchiveUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String password;
    private String roles;
}
