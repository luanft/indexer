package test;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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

import indexer.Config;
import indexer.DataColumn;
import indexer.DataTable;
import indexer.Engine;
import mysqldc.DC;

public class Main {

	public static void main(String[] time) {
		Engine eng = new Engine();
		int hour = 23;
		if (time.length > 0) {
			hour = Integer.parseInt(time[0]);
		}
		if (eng.Init()) {
			System.out.print("\n Started!");
			try {
				Date date = new Date();
				boolean is_update = false;
				while (true) {
					if (date.getHours() == hour) {
						if (!is_update) {
							System.out.print("\n Updating for " + date);
							eng.createIndexFile();
							is_update = true;
							System.out.print("\n Updated for " + new Date());
						}
					} else {
						is_update = false;
					}
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.print("Error when indexing data!");
			}
		} else {
			System.out.print("Cann't connect database server!");
		}

	}

}
