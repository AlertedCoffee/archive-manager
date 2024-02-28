package com.alc.archivemanager;

import com.alc.archivemanager.model.SearchResultModel;
import com.alc.archivemanager.parsers.ApachePOIHelper;
import com.alc.archivemanager.parsers.Docx4jHelper;
import com.alc.archivemanager.parsers.IParser;
import com.alc.archivemanager.searchers.ApacheLuceneSearcher;
import com.alc.archivemanager.searchers.DeepPavlovSearcher;
import com.alc.archivemanager.searchers.ISearcher;
import com.alc.archivemanager.searchers.SearchProcesses;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ArchiveManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchiveManagerApplication.class, args);
//		ISearcher searcher = new ApacheLuceneSearcher();
//		searcher.SearchProcess("C:/WebPractice/archive-manager/src/main/resources/storage", "руководство оператора");
	}
}
