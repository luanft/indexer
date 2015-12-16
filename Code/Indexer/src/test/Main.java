package test;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.path.PathHierarchyTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.path.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermContext;

import indexer.Config;
import indexer.Engine;
import mysqldc.DC;

public class Main {

	
	
	public static void main2(String[] args) {


		Analyzer analyzer = new StandardAnalyzer();

		Path path = FileSystems.getDefault().getPath("IndexFile");

		try {
			Directory directory = FSDirectory.open(path);
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter iwriter = new IndexWriter(directory, config);
			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);

			TermQuery termqr = new TermQuery(new Term("id","1"));
			TopDocs docs = isearcher.search(termqr, 1);
			if (docs.totalHits == 0) {
				Document doc = new Document();
				String text = "Trần Minh Luận";
				doc.add(new StringField("id", "1", Field.Store.YES));
				doc.add(new TextField("content", text, Field.Store.YES));
				doc.add(new TextField("c1", text, Field.Store.YES));
				iwriter.addDocument(doc);
			}
			iwriter.close();

			// Parse a simple query that searches for "text":
			QueryParser parser = new QueryParser("content", analyzer);
			Query query = parser.parse("Luận Trần");
			ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;

			// Iterate through the results:
			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				System.out.print(hitDoc.get("content") + "\n");
			}
			ireader.close();
			directory.close();

			System.out.print("Done! \n");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
