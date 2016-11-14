package com.elipcero.masteringmvc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchSocketController {

	private final ParallelSearchService parallelSearchService;
	private SimpMessagingTemplate webSocket;
	
	@Autowired
	public SearchSocketController(ParallelSearchService parallelSearchService, SimpMessagingTemplate webSocket) {
		this.parallelSearchService = parallelSearchService;
		this.webSocket = webSocket;
	}
	
	@MessageMapping("/search")
	public void search(@RequestParam List<String> keywords) throws Exception {
		
		webSocket.convertAndSend("/topic/searchResults", parallelSearchService.search(null, keywords));	
	}
	
}