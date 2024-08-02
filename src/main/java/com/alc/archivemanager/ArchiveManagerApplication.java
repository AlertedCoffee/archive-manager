package com.alc.archivemanager;

import com.alc.archivemanager.model.FileSystemItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@SpringBootApplication
public class ArchiveManagerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ArchiveManagerApplication.class, args);
	}
}
