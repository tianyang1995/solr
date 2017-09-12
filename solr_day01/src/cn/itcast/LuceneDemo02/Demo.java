package cn.itcast.LuceneDemo02;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

//维护索引库
public class Demo {
	
	
	public IndexWriter getIndexWirter() throws IOException{
		//创建Directory对象
				Directory directory= FSDirectory.open(new File("G:/temp/index"));
				//创建indexWriter对象
				
				IndexWriter indexWriter = new IndexWriter(directory,new IndexWriterConfig(Version.LATEST,new IKAnalyzer()));
					return indexWriter;
		
	}
	
	//添加文档
	@Test
	public void add() throws IOException{
		//创建Directory对象
		Directory directory= FSDirectory.open(new File("G:/temp/index"));
		//创建indexWriter对象
		
		IndexWriter indexWriter = new IndexWriter(directory,new IndexWriterConfig(Version.LATEST,new IKAnalyzer()));
		//创建一个文档对象
		
		Document document = new Document();
		
		//向文档对象中添加域
		//名称
		Field fieldName= new TextField("name","测试boost后apache",Store.YES);
		fieldName.setBoost(100);
		//内容
		Field fieldContent = new TextField("fileContext","新添加的文本内容2",Store.NO);
		//路径
		Field fieldPath = new StoredField("filepath","g:/temp/1.text");
		//长度
		Field fieldSize = new LongField("size",1000l, Store.YES);
		 //向文档对象中添加Field
		 document.add(fieldName);
		 document.add(fieldPath);
		 document.add(fieldContent);
		 document.add(fieldSize);
		//把文档对象写入索引库
		 indexWriter.addDocument(document);
		 //提交
		 indexWriter.commit();
		 indexWriter.close();
	}
	//删除全部文档
	@Test
	public void del() throws IOException{
			//创建InddexWirter对象
		IndexWriter indexWriter = getIndexWirter();
		//使用IndexWriter中的删除方法
		indexWriter.deleteAll();
		indexWriter.commit();
		indexWriter.close();
	}
	//根据查询删除
	@Test
	public void delByQuery() throws IOException{
		//创建InddexWirter对象
		IndexWriter indexWriter = getIndexWirter();
		//创建一个Query对象
		Query query = new TermQuery(new Term("name","apache"));
		indexWriter.deleteDocuments(query);
		indexWriter.commit();
		indexWriter.close();
	}
	//修改文档(更新 ;先删除,后添加)
	@Test
	public void update() throws IOException{
		//创建InddexWirter对象
				IndexWriter indexWriter = getIndexWirter();
		//因为要添加,所以要一个新的Document
				Document document = new Document();
				document.add(new TextField("name","更新后的文档名",Store.YES));
				document.add(new TextField("fileContent","更新后的文档内容",Store.NO));
				document.add(new LongField("size",1000l,Store.YES));
				document.add(new StoredField("filepath","G:/index"));
		//使用IndexWriter更新方法
			indexWriter.updateDocument(new Term("name","web"),document);	
			indexWriter.commit();
			indexWriter.close();
	}
}
