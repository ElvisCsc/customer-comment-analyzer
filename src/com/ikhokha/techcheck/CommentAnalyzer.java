package com.ikhokha.techcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentAnalyzer extends java.lang.Thread {

	private File[] commentFiles;
	private int startIndex, endIndex;

	public Map<String, Integer> fileResults;

	public CommentAnalyzer(File[] commentFiles, int startIndex, int endIndex) {
		this.commentFiles = commentFiles;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	public  void analyze(File file) {
		
		Map<String, Integer> resultsMap = new HashMap<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			
			String line;
			while ((line = reader.readLine()) != null) {
				metrics(resultsMap, line);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Error processing file: " + file.getAbsolutePath());
			e.printStackTrace();
		}

		if (fileResults == null) {
			fileResults = resultsMap;
		} else{
			addReportResults(resultsMap, fileResults);
		}
	}

	public void run() {
		for (int i = startIndex; i < endIndex; i++) {
			analyze(commentFiles[i]);
		}
	}

	/**
	 * This method increments a counter by 1 for a match type on the countMap. Uninitialized keys will be set to 1
	 * @param countMap the map that keeps track of counts
	 * @param key the key for the value to increment
	 */
	private synchronized void  incOccurrence(Map<String, Integer> countMap, String key) {
		
		countMap.putIfAbsent(key, 0);
		countMap.put(key, countMap.get(key) + 1);
	}

	/**
	 * This method check if a line contains a URL
	 * @param line a string of text
	 * @return boolean value
	 */
	public boolean containsUrl(String line) {
		String regex = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(line);
		return matcher.find();
	}

	public void metrics(Map<String, Integer> resultsMap, String line) {
		if (line.length() < 15) {
			incOccurrence(resultsMap, "SHORTER_THAN_15");
		}
		// note: made an assumption that case sensitivity is important
		if (line.contains("Mover")) {

			incOccurrence(resultsMap, "MOVER_MENTIONS");
		}

		if (line.contains("Shaker")) {
			incOccurrence(resultsMap, "SHAKER_MENTIONS");
		}

		if (line.contains("?")) {
			incOccurrence(resultsMap, "QUESTIONS");
		}

		if (containsUrl(line)) {
			incOccurrence(resultsMap, "SPAM");
		}
	}

	private static void addReportResults(Map<String, Integer> source, Map<String, Integer> target) {

		for (Map.Entry<String, Integer> entry : source.entrySet()) {
			target.putIfAbsent(entry.getKey(), 0);
			target.put(entry.getKey(), target.get(entry.getKey()) + entry.getValue());
		}

	}

}
