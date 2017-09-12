package cn.itcast.LuceneDemo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
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
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Demo {
//创建索引
	@Test
	public void Demo01() throws IOException{
		//创建一个Directory对象,指定索引库保存的目录,可以保存到磁盘也可以保存到内存,通常保存到磁盘
		//Directory directory= new RAMDirectory();(保存到内存中)
		Directory directory = FSDirectory.open(new File("G:/temp/index"));
		//indexWriterConfig对需要两个参数1.lucene版本号,2分析器
		Analyzer analyzer = new IKAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,analyzer);
		
		//创建一个IndexWriter对象(两个参数1.indexWriterConfig对象,2分析器对象)
		IndexWriter indexWriter = new IndexWriter(directory,config);
		//读取磁盘上的文档
		 File path=new File("G:/学习课程就业班/Mybatis/lucene&solrday01/01.参考资料/searchsource");
		 //遍历获取里边的所有文档
		 for (File f : path.listFiles()) {
			//获取文档的名称
			 String name = f.getName();
			 //获取文档的长度
			 long size = FileUtils.sizeOf(f);
			 //获取文档的内容
			 String fileContent = FileUtils.readFileToString(f);
			 //获取文档的路径
			 String filepath = f.getPath();
			 //创建Document文档对象
			 Document document = new Document();
			 //把文档对象添加到Field(参数一:域的名称 参数二:域的值参数三:是否存取)
			 Field fieldName = new TextField("name", name, Store.YES);
			 Field fieldContent = new TextField("fileContent", fileContent, Store.NO);
			 Field fieldPath = new TextField("filepath", filepath, Store.YES);
			 Field fieldSize = new TextField("size", size+"", Store.YES);
			 //向文档对象中添加Field
			 document.add(fieldName);
			 document.add(fieldPath);
			 document.add(fieldContent);
			 document.add(fieldSize);
			 //把文档对象写入索引库
			 indexWriter.addDocument(document);
			 
		}
		 //提交.关闭indexWriter
		 indexWriter.commit();
		 indexWriter.close();
	}
	@Test
	public void Demo02() throws IOException{
		//创建Directory对象,指定索引库的位置
		Directory directory = FSDirectory.open(new File("G:/temp/index"));
		//创建一个IndexReader对象,一读的形式打开索引库
		IndexReader indexReader =DirectoryReader.open(directory);
		//创建一个IndexSearcher对象,需要参数:IndexReader
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//创建一个Query对象,需要指定要搜索的区域和关键字
		Query query = new TermQuery(new Term("name","apache"));
		//执行搜索,得到一个TopDocs数组
		TopDocs topDocs = indexSearcher.search(query, 10);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		int totalHits = topDocs.totalHits;
		//遍历TopDocs列表
			for (ScoreDoc scoreDoc : scoreDocs) {
				//取文档id取Document对象
				int doc = scoreDoc.doc;
				//从Document 对象中取Filed的内容
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
	@Test
	public void Demo03() throws Exception{
		//创建一个分析器对象
//		Analyzer analyzer =new StandardAnalyzer();
		//lucene自带的
		//Analyzer analyzer =new CJKAnalyzer();
		//Analyzer analyzer =new SmartChineseAnalyzer();
		Analyzer analyzer =new IKAnalyzer();
		
		TokenStream tokenStream = analyzer.tokenStream(null,"马克龙表示，法方致力于维护朝鲜半岛和平稳定，重视中方在解决朝鲜半岛核问题上的立场和重要作用，愿同中方加强合作，推动妥善解决朝鲜半岛核问题。");
		//设置一个指针
		CharTermAttribute addAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		//指向第一个
		tokenStream.reset();
		//遍历
		while(tokenStream.incrementToken()){
			System.out.println(addAttribute.toString());
		}
		
		tokenStream.close();
	}
	//冒泡
	@Test
	public void Demo(){
		int[] arr={55,11,121,2,33,58};
		
		
		for(int x=0;x<arr.length-1;x++){
			
			for (int i=0; i <arr.length-1-x; i++) {
				if(arr[i]>arr[i+1]){
					int tem=arr[i];
					arr[i]=arr[i+1];
					arr[i+1]=tem;
				}
			}
		}
		
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
			
		}
		
		
	}
	//选择
	@Test
	public void demo(){
		
		int[] arr={55,11,121,2,33,58};
		
		for(int x=0;x<arr.length-1;x++){
			for(int y=x+1;y<arr.length;y++){
				if(arr[y]>arr[x]){
					int trem=arr[x];
					arr[x]=arr[y];
					arr[y]=trem;
				}
			}
		}
			for (int j = 0; j < arr.length; j++) {
				System.out.println(arr[j]);
			}
	}
	
	
}
