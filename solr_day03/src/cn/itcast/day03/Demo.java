package cn.itcast.day03;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

public class Demo {
	
	@Test
	public void demo() throws SolrServerException{
		//创建一个SolrServer
		String string= "http://127.0.1:8080/solr/collection1";
		SolrServer solrServer= new HttpSolrServer(string);
		//创建SolrQuery对象封装查询条件
		SolrQuery query= new SolrQuery();
		//设置查询条件
		query.set("q","小黄人");
		//设置过滤条件
		query.addFilterQuery("product_price:[0 TO *]");
		//设置排序条件
		query.addSort("product_price", ORDER.asc);
		//设置分页
		query.setStart(0);
		query.setRows(10);
		//设置默认域
		query.set("df", "product_keywords");
		//开启高亮
		query.setHighlight(true);
		//设置高亮显示的域
		query.addHighlightField("product_name");
		//设置后缀和前缀
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		//执行查询
		
		QueryResponse response = solrServer.query(query);
		//处理结果(获取结果
		SolrDocumentList results = response.getResults();
		//获取总的条数
		long numFound = results.getNumFound();
		System.out.println("总的条数是"+numFound);
		//获取高亮
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		//遍历
		for (SolrDocument solrDocument : results) {
			String productName="";
			List<String> list = highlighting.get(solrDocument.get("id")).get("product_name");
			if (null != list) {
				productName = list.get(0);
			} else {
				productName = (String) solrDocument.get("product_name");
			}
			
			System.out.println(productName);
			System.out.println(solrDocument.get("product_price"));
			System.out.println(solrDocument.get("product_picture"));
			System.out.println(solrDocument.get("product_catalog_name"));
			
		}
		
		
		//
		
	}

}
