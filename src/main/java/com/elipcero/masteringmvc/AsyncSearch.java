package com.elipcero.masteringmvc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
public class AsyncSearch {

	protected final Log logger = LogFactory.getLog(getClass());

	// Aunque le puedo pasar el keyword a buscar. Como ejemplo me da igual
	@Async
	public ListenableFuture<List<Tweet>> asyncFetch(String keyword) {
		
		logger.info(Thread.currentThread().getName() + " - Searching for " + keyword);
		
		return new AsyncResult<>(getTweets());
	}
	
	public List<Tweet> getTweets() {
	
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
		
		tweets.add(tweet1);
		
		return tweets;
	}
}
