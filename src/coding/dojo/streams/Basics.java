package coding.dojo.streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.*;

public class Basics {

	/*
	 *  Use Files and Pattern to read a list of unique words from the file
	 *  Also make sure that all words are lower case and they are in alphabetical order
	 */
	public List<String> readWords() throws IOException {
		Pattern p = Pattern.compile(",");
		Path path = Paths.get("files", "words.csv");
		return Files.lines(path)
				.flatMap(p::splitAsStream)
				.map(String::toLowerCase)
				.distinct()
				.sorted()
				.collect(toList()); 
	}
	
	/*
	 * 'Filter, map, reduce' is a well-known programming model. How could we use 
	 * it to count all words (from readWords()) longer than X characters? 
	 */
	public int countWordsStrictlyLongerThan(int noOfChars) throws IOException {
		return readWords().stream()
				.filter(w -> w.length() > noOfChars)
				.mapToInt(w -> 1)
				.reduce(0, (x, y) -> x+y);
	}
	
	/*
	 * What about reducing all words into one string?
	 */
	public String joinWordsLongerThanBy(int noOfChars, String pattern) throws IOException {
		return readWords().stream()
				.filter(w -> w.length() > noOfChars)
				.collect(joining(pattern));
	}
}
