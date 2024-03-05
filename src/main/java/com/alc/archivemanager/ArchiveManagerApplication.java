package com.alc.archivemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArchiveManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchiveManagerApplication.class, args);
//		ISearcher searcher = new ApacheLuceneSearcher();
//		searcher.searchProcess("C:/WebPractice/archive-manager/src/main/resources/storage", "руководство оператора");
	}
}
