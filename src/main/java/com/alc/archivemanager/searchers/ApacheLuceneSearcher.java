package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResultModel;
import com.alc.archivemanager.parsers.IParser;
import com.alc.archivemanager.searchers.luceneInfrastructure.LuceneIndexer;
import org.apache.jena.base.Sys;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.automaton.LevenshteinAutomata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApacheLuceneSearcher extends SearchProcesses{

    private static final String TITLE = "title";
    private static final String BODY = "body";


    private static Document createWith(final String titleStr, final String bodyStr) {
        final Document document = new Document();

        final FieldType textIndexedType = new FieldType();
        textIndexedType.setStored(true);
        textIndexedType.setIndexOptions(IndexOptions.DOCS);
        textIndexedType.setTokenized(true);

        //index title
        Field title = new Field(TITLE, titleStr, textIndexedType);

        //index body
        Field body = new Field(BODY, bodyStr, textIndexedType);

        document.add(title);
        document.add(body);
        return document;
    }


    public List<SearchResultModel> fuzzySearch(final String toSearch, final String searchField, final int limit, final IndexReader reader) throws IOException, ParseException {
        final IndexSearcher indexSearcher = new IndexSearcher(reader);
        final Term term = new Term(searchField, toSearch);

        final Query query = new FuzzyQuery(term);
        final TopDocs search = indexSearcher.search(query, limit);
        final ScoreDoc[] hits = search.scoreDocs;

        List<SearchResultModel> result = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            final String title = reader.document(hit.doc).get(TITLE);
            final String body = reader.document(hit.doc).get(BODY);

            result.add(new SearchResultModel(
                    title,
                    body,
                    hit.score
            ));
        }

        return result;
    }

    public List<SearchResultModel> searchInBody(final String toSearch, final int limit, final IndexReader reader) throws IOException, ParseException {
        final IndexSearcher indexSearcher = new IndexSearcher(reader);

        final QueryParser queryParser = new QueryParser(BODY, new RussianAnalyzer());
        final Query query = queryParser.parse(toSearch);
        System.out.println("Type of query: " + query.getClass().getSimpleName());

        final TopDocs search = indexSearcher.search(query, limit);
        final ScoreDoc[] hits = search.scoreDocs;

        List<SearchResultModel> result = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            final String title = reader.document(hit.doc).get(TITLE);
            final String body = reader.document(hit.doc).get(BODY);

            result.add(new SearchResultModel(
                    title,
                    body,
                    hit.score
            ));
        }

        return result;
    }

    public List<SearchResultModel> fuzzySearch(final String toSearch, final int limit, final IndexReader reader) throws IOException, ParseException {
        return fuzzySearch(toSearch, BODY, limit, reader);
    }

    @Override
    public List<SearchResultModel> SearchProcess(String mainPath, String searchParam) {
        File dir = new File(mainPath);

        if(!dir.exists())
            return null;

        List<String> files = new ArrayList<>(getContent(dir));

        List<SearchResultModel> searchResults = new ArrayList<>();

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


        long start  = System.currentTimeMillis();
        try {
            LuceneIndexer indexer = new LuceneIndexer();
            indexer.index(true, documents);
            searchResults = searchInBody(searchParam, files.size(), indexer.readIndex());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        long end  = System.currentTimeMillis();
        System.out.println(end - start);


        return searchResults;
    }
}
