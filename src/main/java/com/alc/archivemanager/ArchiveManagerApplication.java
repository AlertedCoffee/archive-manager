package com.alc.archivemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ArchiveManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchiveManagerApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "читатель Skillbox Media") String name) {
		return String.format("Hello, %s!", name);
	}
}
