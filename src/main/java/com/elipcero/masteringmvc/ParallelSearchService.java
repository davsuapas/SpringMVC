package com.elipcero.masteringmvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class ParallelSearchService {
	
	private final AsyncSearch asyncSearch;
	
	@Autowired
	public ParallelSearchService(AsyncSearch asyncSearch) {
		this.asyncSearch = asyncSearch;
	}
	
	public List<Tweet> search(String searchType, List<String> keywords) {
	
		CountDownLatch latch = new CountDownLatch(keywords.size());
		
		List<Tweet> allTweets = Collections.synchronizedList(new ArrayList<>());
		
		keywords
			.stream()
			.forEach(keyword -> asyncFetch(latch, allTweets, searchType, keyword));
		
		await(latch);
		
		return allTweets;
		
	}
	
	private void asyncFetch(CountDownLatch latch, List<Tweet> allTweets, String searchType, String keyword) {
		
		asyncSearch.asyncFetch(keyword)
			.addCallback(
			tweets -> onSuccess(allTweets, latch, tweets),
			ex -> onError(latch, ex));
	}
	
	private void await(CountDownLatch latch) {
		
		try {
			latch.await();
			
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private static void onSuccess(List<Tweet> results,	CountDownLatch latch, List<Tweet> tweets) {
		results.addAll(tweets);
		latch.countDown();
	}
	
	private static void onError(CountDownLatch latch, Throwable ex) {
		ex.printStackTrace();
		latch.countDown();
	}
}