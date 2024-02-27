package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResultModel;
import com.alc.archivemanager.parsers.IParser;
import com.alc.archivemanager.searchers.luceneInfrastructure.LuceneIndexer;
import org.apache.jena.base.Sys;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApacheLuceneSearcher extends SearchProcesses{

    public static final int DEFAULT_LIMIT = 10;

    private static Document createWith(final String titleStr, final String bodyStr) {
        final Document document = new Document();

        final FieldType textIndexedType = new FieldType();
        textIndexedType.setStored(true);
        textIndexedType.setIndexOptions(IndexOptions.DOCS);
        textIndexedType.setTokenized(true);

        //index title
        Field title = new Field("title", titleStr, textIndexedType);
        //index body
        Field body = new Field("body", bodyStr, textIndexedType);

        document.add(title);
        document.add(body);
        return document;
    }

    public void fuzzySearch(final String toSearch, final String searchField, final int limit, final IndexReader reader) throws IOException, ParseException {
        final IndexSearcher indexSearcher = new IndexSearcher(reader);

        final Term term = new Term(searchField, toSearch);

        final int maxEdits = 2; // This is very important variable. It regulates fuzziness of the query
        final Query query = new FuzzyQuery(term, maxEdits);
        final TopDocs search = indexSearcher.search(query, limit);
        final ScoreDoc[] hits = search.scoreDocs;

        System.out.println("\nПараметр поиска: " + toSearch);
        for (ScoreDoc hit : hits) {
            final String title = reader.document(hit.doc).get("title");
            final String body = reader.document(hit.doc).get("body");
            System.out.println("\n\tDocument Id = " + hit.doc + "\n\ttitle = " + title);
        }

    }

    public void fuzzySearch(final String toSearch, final IndexReader reader) throws IOException, ParseException {
        fuzzySearch(toSearch, "body", DEFAULT_LIMIT, reader);
    }

    @Override
    public List<SearchResultModel> SearchProcess(String mainPath, String searchParam) {
        File dir = new File(mainPath);

        if(!dir.exists())
            return null;

        List<String> files = new ArrayList<>(getContent(dir));

        List<SearchResultModel> searchResults;

        List<Document> documents = new ArrayList<>();

        for (String file : files){
            try {
                IParser parser = ParserFactory(file);
                if (parser != null) {
                    documents.add(createWith(file, parser.Parse(file)));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        //documents.add(createWith("test", "Мама мыла раму, рамам мыла маму"));

        long start  = System.currentTimeMillis();
        try {
            LuceneIndexer indexer = new LuceneIndexer();
            indexer.index(true, documents);
            fuzzySearch(searchParam, indexer.readIndex());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        long end  = System.currentTimeMillis();
        System.out.println(end - start);


        //searchResults = Search(returnedFiles, texts, searchParams);

        return null;
    }
}
