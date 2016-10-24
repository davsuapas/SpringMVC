package com.elipcero.masteringmvc;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private final ParallelSearchService parallelSearchService;
	
	@Autowired
	public SearchController(ParallelSearchService parallelSearchService) {
		this.parallelSearchService = parallelSearchService;
	}
	
	@RequestMapping("/search/{searchType}")
	public ModelAndView search(@PathVariable String searchType, @MatrixVariable List<String> keywords) {
	

		ModelAndView modelAndView = new ModelAndView("resultPage");
		modelAndView.addObject("tweets", this.parallelSearchService.search(searchType, keywords));
		modelAndView.addObject("search", String.join(",", keywords));
		return modelAndView;
	}
}