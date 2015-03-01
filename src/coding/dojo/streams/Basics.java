package coding.dojo.streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Basics {

	/*
	 *  Use Files and Pattern to read a list of unique words from the file
	 *  Also make sure that all words are lower case and they are in alphabetical order
	 */
	public List<String> readWords() throws IOException {
		Pattern p = Pattern.compile(",");
		Path path = Paths.get("files", "words.csv");
		return null; 
	}
	
	/*
	 * 'Filter, map, reduce' is a well-known programming model. How could we use 
	 * it to count all words (from readWords()) longer than X characters? 
	 */
	public int countWordsStrictlyLongerThan(int noOfChars) throws IOException {
		return -1;
	}
	
	/*
	 * What about reducing all words into one string?
	 */
	public String joinWordsLongerThanBy(int noOfChars, String pattern) throws IOException {
		return null;
	}
	
	//********************* BONUS IF WE HAVE TIME ***************
	
	/*
	 * Given 2 lists of number from 1 to 10 return 
	 * the sum of all possible combinations multiplied, e.g.
	 * (1 x 1) + (1 x 2) + ... + (10 x 9) + (10 * 10)    
	 */
	public long getSumOfMultiplications() {
		return getStream1To10()
				.flatMap(i -> getStream1To10().map(x -> x * i))
				.sum();
	}
	
	/*
	 * Could we use map -> reduce for N lists?
	 */
	public long getSumOfMultiplicationsGeneric(int N) {
		long sum = IntStream.range(0, N)
				.parallel()
				.boxed()
				.map(i -> getStream1To10())
				.reduce(1l, (a, b) -> b.mapToLong(i -> a * i).sum(), (a, b) -> a * b);
		return sum;
	}
	
	private IntStream getStream1To10() {
		return IntStream.rangeClosed(1, 10);
	}
}
