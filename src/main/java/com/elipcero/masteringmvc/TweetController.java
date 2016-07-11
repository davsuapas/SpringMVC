package com.elipcero.masteringmvc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

final class Tweet {
	public String user;
	public String text;
	public String image;
}

@Controller
public class TweetController {
	
	@RequestMapping("/")
	public String home() {
		return "searchPage";
	}	
	
	@RequestMapping("/result")
	public String hello(@RequestParam(defaultValue = "MasteringMVC") String find, Model model) {
		
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
				
		model.addAttribute("tweets", tweets);
		model.addAttribute("search", find);
		return "resultPage";
	}
	
	@RequestMapping(value = "/postSearch", method = RequestMethod.POST)
	public String postSearch(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		
		String search = request.getParameter("search");
		
		if (search.toLowerCase().contains("struts")) {
			redirectAttributes.addFlashAttribute("error", "Try using spring instead!");
			return "redirect:/";
		}
		
		redirectAttributes.addAttribute("search", search);
		return "redirect:result";
	}	
}
