package com.example.solr.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolrService {

	@Autowired
	SolrClient solrClient;

	public Map<Integer,List<SolrDocument>> getServicerOrderDocumentsOfLotNumber(String yardNumber, String lotId) {
		SolrQuery query = getQueryForYardAndLot(yardNumber, lotId);
	    return getServiceOrderDocuments(query);
	}

	private SolrQuery getQueryForYardAndLot(String yardNumber, String lotId) {
		SolrQuery query = new SolrQuery();
		query.setRequestHandler("/" + "lotsserviceorderslist");
		query.addFilterQuery("so_stage_code:300");
		query.addFilterQuery("lot_stage:(28 30 40 50 91)");
		query.addFilterQuery("yard_number:" + yardNumber);
		query.addFilterQuery("lot_number:" + lotId);
		query.setRows(30000);
		return query;
	}

	private Map<Integer, List<SolrDocument>> getServiceOrderDocuments(SolrQuery query) {
		QueryResponse response = null;
		try {
			response = solrClient.query(query);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}

		Map<Integer,List<SolrDocument>> lotServiceOrdersMap = new HashMap<>();
		if (response.getGroupResponse() != null) {
			GroupResponse gr = response.getGroupResponse();
			for (GroupCommand gc : gr.getValues()) {
				System.out.println("Group details " + gc.getValues().size());
				for (Group g : gc.getValues()) {
					Integer lotNumber = Integer.valueOf(g.getGroupValue());
				    lotServiceOrdersMap.put(lotNumber, new ArrayList<SolrDocument>());
					for (SolrDocument d : g.getResult()) {
						lotServiceOrdersMap.get(lotNumber).add(d);
					}
				}
			}
		}

		return lotServiceOrdersMap;
	}

	public Map<Integer, List<SolrDocument>> getServiceOrdersForLot(String yardNumber, boolean isSublot) {
		SolrQuery query = new SolrQuery();
		query.setRequestHandler("/" + "lotsserviceorderslist");
		query.setQuery("so_stage_code:300");
		if(isSublot) {
			query.addFilterQuery("g2_facility_id:" + yardNumber);
		}else {
			query.addFilterQuery("yard_number:" + yardNumber);
		}
		query.addFilterQuery("lot_stage:(28 30 40 50 91)");
		query.setRows(30000);
		return getServiceOrderDocuments(query);
	}
}
