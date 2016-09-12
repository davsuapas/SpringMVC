package com.elipcero.masteringmvc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {
	
	@RequestMapping("/search/{searchType}")
	public ModelAndView search(@PathVariable String searchType, @MatrixVariable List<String> keywords) {
		
		List<Tweet> tweets = new ArrayList<Tweet>();
		
		Tweet tweet = new Tweet();
		tweet.user = "David Suarez";
		tweet.text = "David Suarez es un tio estupendo";
		tweet.image = "aa";
		
		tweets.add(tweet);
		
		Tweet tweet1 = new Tweet();
		tweet1.user = "Pepe Suarez";
		tweet1.text = "Pepe Suarez es un tio estupendo";
		tweet1.image = "aa";
		
		tweets.add(tweet);
		
		ModelAndView modelAndView = new ModelAndView("resultPage");
		modelAndView.addObject("tweets", tweets);
		modelAndView.addObject("search", String.join(",", keywords));
		return modelAndView;
	}
}