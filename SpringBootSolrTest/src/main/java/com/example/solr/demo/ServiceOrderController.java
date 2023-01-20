package com.example.solr.demo;

import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceOrderController {
	
	@Autowired
	private SolrService solrService;

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello from ServiceOrderController"; 
	}
	
	@GetMapping("/ServiceOrders")
	public Map<Integer,List<SolrDocument>> getServiceOrder(@RequestParam("yardNumber") String yardNumber, 
			@RequestParam("lotId") String lotId) {
		return solrService.getServicerOrderDocumentsOfLotNumber(yardNumber, lotId);
	}
	
	@GetMapping("/{yardNumber}")
	public Map<Integer,List<SolrDocument>> getYardSoForAllLotSuffix(@PathVariable String yardNumber) {
		return solrService.getServiceOrdersForLot(yardNumber, false);
	}
	
	@GetMapping("/sublot/{yardNumber}")
	public Map<Integer,List<SolrDocument>> getSublotSoForAllLotSuffix(@PathVariable String sublotId) {
		return solrService.getServiceOrdersForLot(sublotId, true);
	}
}
