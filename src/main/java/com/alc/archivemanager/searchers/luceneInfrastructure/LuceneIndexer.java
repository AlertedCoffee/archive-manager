package com.alc.archivemanager.searchers.luceneInfrastructure;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuceneIndexer {

    private final String pathToIndexFolder = System.getProperty("user.dir") + "/src/main/resources/index/";

    /**
     * Indexing documents using provided Analyzer
     *
     * @param create to decide create new or append to previous one
     * @throws IOException
     */
    public void index(final Boolean create, List<Document> documents, Analyzer analyzer) throws IOException {
        final Directory dir = FSDirectory.open(Paths.get(pathToIndexFolder));
        final IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        if (create) {
            // Create a new index in the directory, removing any
            // previously indexed documents:
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        }
        else {
            // Add new documents to an existing index:
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }

        final IndexWriter w = new IndexWriter(dir, iwc);
        w.addDocuments(documents);
        w.close();
    }

    /**
     * Indexing documents with RussianAnalyzer as analyzer
     *
     * @param create to decide create new or append to previous one
     * @throws IOException
     */
    public void index(final Boolean create, List<Document> documents) throws IOException {
        final Analyzer analyzer = new RussianAnalyzer();
        index(create, documents, analyzer);
    }

    /**
     * Indexing one document
     *
     * @param create to decide create new or append to previous one
     * @throws IOException
     */
    public void index(final Boolean create, Document document) throws IOException {
        final List<Document> oneDocumentList = new ArrayList<>();
        oneDocumentList.add(document);
        index(create, oneDocumentList);
    }

    /**
     * Get IndexReader by using pathToIndexFolder
     *
     * @return IndexReader or IOException if any
     * @throws IOException
     */
    public IndexReader readIndex() throws IOException {
        final Directory dir = FSDirectory.open(Paths.get(pathToIndexFolder));
        return DirectoryReader.open(dir);
    }

    public String getPathToIndexFolder() {
        return pathToIndexFolder;
    }
}
