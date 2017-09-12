package cn.itcast.LuceneDemo02;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

//查询
public class Demo02 {
	
	private IndexSearcher getIndexSearcher() throws IOException{
	////创建Directory对象
				Directory directory = FSDirectory.open(new File("G:/temp/index"));
			//创建一个IndexReader对象
				IndexReader indexReader = DirectoryReader.open(directory);
				//创建一个indexSearcher对象
				IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		return indexSearcher;
	}
	private void exquery(IndexSearcher indexSearcher,Query query) throws IOException{
		//执行查询
		TopDocs search = indexSearcher.search(query, 12);
		System.out.println("查询结果总记录数"+search.totalHits);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			int doc = scoreDoc.doc;
			Document document = indexSearcher.doc(doc);
			String name = document.get("name");
			String content = document.get("fileContent");
			String size = document.get("size");
			String filepath = document.get("filepath");
			System.out.println(name);
			System.out.println(content);
			System.out.println(size);
			System.out.println(filepath);
		}
	}
	
	
	@Test
	public void demo() throws IOException{
//	////创建Directory对象
//			Directory directory = FSDirectory.open(new File("G:/temp/index"));
//		//创建一个IndexReader对象
//			IndexReader indexReader = DirectoryReader.open(directory);
//			//创建一个indexSearcher对象
//			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
//			//创建一个Query对象
		IndexSearcher indexSearcher = getIndexSearcher();
			Query query = new MatchAllDocsQuery();
			System.out.println(query);
			exquery(indexSearcher,query);
//			//执行查询
//			TopDocs search = indexSearcher.search(query, 12);
//			System.out.println("查询结果总记录数"+search.totalHits);
//			ScoreDoc[] scoreDocs = search.scoreDocs;
//			for (ScoreDoc scoreDoc : scoreDocs) {
//				int doc = scoreDoc.doc;
//				Document document = indexSearcher.doc(doc);
//				String name = document.get("name");
//				String content = document.get("fileContent");
//				String size = document.get("size");
//				String filepath = document.get("filepath");
//				System.out.println(name);
//				System.out.println(content);
//				System.out.println(size);
//				System.out.println(filepath);
//			}
	}
	@Test
	public void Num() throws IOException{
		IndexSearcher indexSearcher = getIndexSearcher();
		//创建Query
		Query query = NumericRangeQuery.newLongRange("size",10l,1000l,false, true);
		System.out.println(query);
		exquery(indexSearcher, query);
	}
	@Test
	public void bool() throws IOException{
		
		IndexSearcher indexSearcher = getIndexSearcher();
		
		//创建Query
		BooleanQuery booleanQuery = new  BooleanQuery();
		
		//条件一
		Query query =new TermQuery(new Term("name","apache"));
		Query query2 =new TermQuery(new Term("fileContent","apache"));
		
		booleanQuery.add(query,Occur.MUST);
		booleanQuery.add(query2,Occur.MUST);
		System.out.println(booleanQuery);
		exquery(indexSearcher, query);
	}
	//分析搜索语句后
	@Test
	public void query() throws ParseException, IOException{//不支持数字类型
		//创建一个Queryparser对象(两个参数:一个个默认的域,一个是分析器
		QueryParser queryParser= new QueryParser("name", new IKAnalyzer());
		//调用parse方法参数就是要搜索的内容
		//Query query2 = query.parse("luence是apache下的一个项目");
		
		Query query= queryParser.parse("+name:apache +fileContent:apache");//在制定域上查询
		//使用query查询
		exquery(getIndexSearcher(), query);
	}
	@Test
	public void query2() throws ParseException, IOException{//不支持数字类型
		String[] fields = {"name","fileContent"};
		//创建一MultiFiledQuerParser对象，指定多个默认搜索域
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, new IKAnalyzer());
		//使用parser方法创建Query对象
		Query query = queryParser.parse("lucene是apache旗下的一个开源的全文检索工具包");
		System.out.println(query);
		//执行查询
		exquery(getIndexSearcher(), query);
		
	}
	@Test
	public void query3() throws ParseException, IOException{//不支持数字类型
		String[] fields = {"name","fileContent"};
		//创建一MultiFiledQuerParser对象，指定多个默认搜索域
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, new IKAnalyzer());
		//使用parser方法创建Query对象
		Query query = queryParser.parse("lucene是apache旗下的一个开源的全文检索工具包");
		System.out.println(query);
		//执行查询
		exquery(getIndexSearcher(), query);
		
	}

}
