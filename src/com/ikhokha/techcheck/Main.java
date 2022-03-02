package com.ikhokha.techcheck;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		Map<String, Integer> totalResults = new HashMap<>();
				
		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));

		int noThreads = commentFiles.length/3;
		CommentAnalyzer[] cAnalyzer = new CommentAnalyzer[noThreads];


		for (int i = 0; i < noThreads; i++) {
			cAnalyzer[i] = new CommentAnalyzer(commentFiles, ( i * commentFiles.length)/noThreads, ( (i+1) * commentFiles.length)/noThreads);
			cAnalyzer[i].start();
		}

		for (int i = 0; i < noThreads; i++) {
			cAnalyzer[i].join();
			addReportResults(cAnalyzer[i].fileResults, totalResults);
		}
		
		System.out.println("RESULTS\n=======");
		totalResults.forEach((k,v) -> System.out.println(k + " : " + v));

	}
	
	/**
	 * This method adds the result counts from a source map to the target map 
	 * @param source the source map
	 * @param target the target map
	 */
	private static void addReportResults(Map<String, Integer> source, Map<String, Integer> target) {

		for (Map.Entry<String, Integer> entry : source.entrySet()) {
			target.putIfAbsent(entry.getKey(), 0);
			target.put(entry.getKey(), target.get(entry.getKey()) + entry.getValue());
		}
		
	}

}
