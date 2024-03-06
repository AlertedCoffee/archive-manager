package com.alc.archivemanager;

import com.alc.archivemanager.model.FileSystemItem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class ArchiveManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchiveManagerApplication.class, args);
		FileSystemItem fi = new FileSystemItem(new File("C:/WebPractice/archive-manager/src/main/resources/storage/test/Техническое задание.docx"));
//		ISearcher searcher = new ApacheLuceneSearcher();
//		searcher.searchProcess("C:/WebPractice/archive-manager/src/main/resources/storage", "руководство оператора");
	}
}
