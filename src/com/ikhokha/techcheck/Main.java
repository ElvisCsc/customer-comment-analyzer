package com.ikhokha.techcheck;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		
		Map<String, Integer> totalResults = new HashMap<>();
				
		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));
		
		for (File commentFile : commentFiles) {
			
			CommentAnalyzer commentAnalyzer = new CommentAnalyzer(commentFile);
			Map<String, Integer> fileResults = commentAnalyzer.analyze();
		//	System.out.println(fileResults);
			System.out.println(commentFile.getName());
			/*System.out.println("___ Total _____");
			System.out.println(totalResults);
			System.out.println("end ___ Total _____ end");*/
			addReportResults(fileResults, totalResults);
						
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

			/*
			countMap.putIfAbsent(key, 0);
		countMap.put(key, countMap.get(key) + 1);
			 */
		}
		
	}

}
