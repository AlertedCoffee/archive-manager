package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResultModel;
import com.alc.archivemanager.parsers.IParser;
import com.alc.archivemanager.searchers.luceneInfrastructure.LuceneIndexer;
import org.apache.lucene.analysis.TokenStream;
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
import org.apache.lucene.search.highlight.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApacheLuceneSearcher extends SearchProcesses{

    private static final String TITLE = "title";
    private static final String BODY = "body";


    private static Document createDocument(final String titleStr, final String bodyStr) {
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

    public List<SearchResultModel> searchInBody(final String toSearch, final int limit, final IndexReader reader) throws IOException, ParseException, InvalidTokenOffsetsException {
        final IndexSearcher indexSearcher = new IndexSearcher(reader);

        final QueryParser queryParser = new QueryParser(BODY, new RussianAnalyzer());
        final Query query = queryParser.parse(toSearch);

        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
        Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));

        final TopDocs search = indexSearcher.search(query, limit);
        final ScoreDoc[] hits = search.scoreDocs;

        List<SearchResultModel> result = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            final String title = reader.document(hit.doc).get(TITLE);
            final String body = reader.document(hit.doc).get(BODY);

            String text = reader.document(hit.doc).get(BODY);
            TokenStream tokenStream = TokenSources.getAnyTokenStream(indexSearcher.getIndexReader(), hit.doc, BODY, new RussianAnalyzer());
            TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, text, false, limit);//highlighter.getBestFragments(tokenStream, text, 3, "...");
            StringBuilder highlightedText = new StringBuilder();
            for (TextFragment textFragment : frag) {
                if ((textFragment != null) && (textFragment.getScore() > 0)) {
                    highlightedText.append(textFragment).append("\n\n");
                }
            }

            result.add(new SearchResultModel(
                    title,
                    highlightedText.toString(),
                    hit.score
            ));
        }

        return result;
    }

    public List<SearchResultModel> fuzzySearch(final String toSearch, final int limit, final IndexReader reader) throws IOException, ParseException {
        return fuzzySearch(toSearch, BODY, limit, reader);
    }

    @Override
    public List<SearchResultModel> searchProcess(String mainPath, String searchParam) {
        File dir = new File(mainPath);

        if(!dir.exists())
            return null;

        List<String> files = new ArrayList<>(getContent(dir));

        return searchProcess(files, searchParam);
    }

    public List<SearchResultModel> searchProcess(List<String> files, String searchParam) {
        List<SearchResultModel> searchResults = new ArrayList<>();

        List<Document> documents = new ArrayList<>();

        for (String file : files){
            try {
                IParser parser = parserFactory(file);
                if (parser != null) {
                    documents.add(createDocument(file, parser.parse(file)));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


        try {
            LuceneIndexer indexer = new LuceneIndexer();
            indexer.index(true, documents);
            searchResults = searchInBody(searchParam, files.size(), indexer.readIndex());
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return searchResults;
    }
}
