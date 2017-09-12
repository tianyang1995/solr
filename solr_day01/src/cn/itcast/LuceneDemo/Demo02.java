package cn.itcast.LuceneDemo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class Demo02 {
	
	//创建索引库
	@Test
	public void createIndex() throws IOException{
		//创建Directory对象
		Directory directory= FSDirectory.open(new File("G:/temp/index"));
		// 创建IndexWriter对象(两个参数,directory,indexConfig)
		//创建IndexConfig对象(两个参数,分析器,lucene版本号)
		Analyzer analyzer = new	StandardAnalyzer();
		IndexWriterConfig indexWriterConfig= new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);
		//读取磁盘上的文档
		File path = new File("G:/学习课程就业班/Mybatis/lucene&solrday01/01.参考资料/searchsource");
		//遍历获取里边所用的文档
		for (File f : path.listFiles()) {
			//获取文档的名称
			String name = f.getName();
			long sizeOf = FileUtils.sizeOf(f);
			
			String content = FileUtils.readFileToString(f);
			
			String path2 = f.getPath();
			//创建Document对象
			Document document = new Document();
			
			//把文档中的内容添加域中Filed
				Field fieldName = new TextField("name", name, Store.YES);
			 Field fieldContent = new TextField("fileContent", content, Store.NO);
			 Field fieldPath = new TextField("filepath", path2, Store.YES);
			 Field fieldSize = new TextField("size", sizeOf+"", Store.YES);
			 //向文档对象中添加Field
			 document.add(fieldName);
			 document.add(fieldContent);
			 document.add(fieldPath);
			 document.add(fieldSize);
			 //把文档写到索引库
			 indexWriter.addDocument(document);
		}
		//提交关闭流
		indexWriter.commit();
		indexWriter.close();
	}
	//查看索引库
	@Test
	public void Demo() throws IOException{
		////创建Directory对象
		Directory directory = FSDirectory.open(new File("G:/temp/index"));
		//创建 indexReader对象
		IndexReader indexReader = DirectoryReader.open(directory);
		//创建一个IndexSearcher对象,需要参数IndexReader
		IndexSearcher indexSearcher= new IndexSearcher(indexReader);
		//创建一个Query对象,需要指定要搜索的域和关键词(Term来指定)
		Query query= new TermQuery(new Term("name","apache"));
		//执行搜索,得到有个TopDocs数组
		TopDocs search = indexSearcher.search(query, 10);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		
		//遍历TopDocs列表
		for (ScoreDoc scoreDoc : scoreDocs) {
			int doc = scoreDoc.doc;
			Document document = indexSearcher.doc(doc);
			//打印结果
			System.out.println(document.get("name"));
			System.out.println(document.get("fileContent"));
			System.out.println(document.get("size"));
			System.out.println(document.get("filepath"));
		}
		
		//关闭流
				indexReader.close();
		
		
		
		
	}
	
	
	
	
	

}
