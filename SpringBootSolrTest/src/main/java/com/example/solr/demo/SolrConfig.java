package com.example.solr.demo;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan
public class SolrConfig {
	@Bean
    public SolrClient solrClient() {
		HttpSolrClient solr = 
				new HttpSolrClient.Builder("http://c-solr-qa.copart.com/solr/c_qa04_serviceorders").build();
		solr.setParser(new XMLResponseParser());
		return solr;
    }
}
