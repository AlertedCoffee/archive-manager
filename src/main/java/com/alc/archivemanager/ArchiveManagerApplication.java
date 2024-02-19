package com.alc.archivemanager;

import com.alc.archivemanager.searchers.DeepPavlovSearcher;
import com.alc.archivemanager.searchers.SearchProcesses;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ArchiveManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchiveManagerApplication.class, args);

		SearchProcesses ss = new DeepPavlovSearcher();
		ss.PDFSearchProcess("C:/WebPractice/archive-manager/src/main/resources/storage/", "Что писать в руководстве оператора?");
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "default") String name) {
		return String.format("Hello, %s!", name);
	}
}
