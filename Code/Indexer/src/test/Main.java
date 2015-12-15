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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.path.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import indexer.Engine;
import mysqldc.DC;

public class Main {

	public static void main(String[] args) {

		DC dc = new DC();
		if (dc.connect()) {
			System.out.print("OK");
		} else {
			System.out.print("NOT OK");
		}

		Engine engine = new Engine();
		if (engine.Init()) {
			engine.Indexing();
		} else {

		}

		Analyzer analyzer = new StandardAnalyzer();

		Path path = FileSystems.getDefault().getPath("IndexFile");

		try {
			Directory directory = FSDirectory.open(path);
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter iwriter = new IndexWriter(directory, config);
			Document doc = new Document();
			String text = "Trần Minh Luận";
			doc.add(new Field("id", text, TextField.TYPE_STORED));
			iwriter.addDocument(doc);
			iwriter.close();

						
			
								
			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);
			// Parse a simple query that searches for "text":
			QueryParser parser = new QueryParser("id", analyzer);
			Query query = parser.parse("Luận Trần");
			ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;

			// Iterate through the results:
			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);			
				System.out.print(hitDoc.get("id") + "\n");
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
