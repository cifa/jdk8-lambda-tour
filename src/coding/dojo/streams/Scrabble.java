package coding.dojo.streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Scrabble {

	private static final int[] scrabbleENScore = {
			// a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,  q, r, s, t, u, v, w, x, y, z
			   1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10 };

	private static final int[] scrabbleENDistribution = {
			// a, b, c, d, e,  f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z
			   9, 2, 2, 1, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1 };

	/**
	 * HOW GOOD WOULD SHAKESPEARE BE IF HE PLAYED SCRABBLE???
	 */
	public static void main(String... args) throws Exception {
		Set<String> scrabbleWords =  readFile("files", "ospd.txt");
		Set<String> shakespeareWords = readFile("files", "words.shakespeare.txt");
		
		System.out.println("# of words in the Scrabble dictionnary : " + scrabbleWords.size()) ;
		System.out.println("# of words used by Shakespeare : " + shakespeareWords.size()) ;
		
		// TASK 4 - But what about those double 'zz'? Could we use blanks?
		// 4.1 Write a function that takes a word and returns a set of letters mapped to the number of occurrences in the word 
		Function<String, Map<Integer, Long>> letterHisto = 
						word -> word.chars()
								.mapToObj(Integer::new)
								.collect(Collectors.groupingBy(
										Function.identity(),
										Collectors.counting()
									));
						
		// 4.2 Write a function that takes a word and computes the number of blanks required for that word in scrabble
		Function<String, Integer> nBlanks = 
						word -> letterHisto.apply(word)
								.entrySet().stream()
								.mapToInt(
									entry -> Math.max(0, entry.getValue().intValue() - scrabbleENDistribution[entry.getKey() - 'a'])
								)
								.sum();
						
		// TASK 5 - There are only 2 blanks in scrabble - Modify code in task 2 to ensure that we only consider words with up to 2 blanks
		
		// TASK 6 - Write a score function that takes blanks into account (blank => 0 points) and use it in task 2
		Function<String, Integer> score2 =
				word -> letterHisto.apply(word)
				.entrySet().stream()
				.mapToInt(
					entry -> Math.min(
								entry.getValue().intValue(),
								scrabbleENDistribution[entry.getKey() - 'a']
							) * scrabbleENScore[entry.getKey() - 'a']
				)
				.sum();
				
		// TASK 1 - How many point is a word worth in scrabble
		Function<String, Integer> score =
				word -> word.chars().map(letter -> scrabbleENScore[letter - 'a']).sum();
		
		// TASK 2 - Group words by their scrabble score
		// 2.1 First, we just want to count those words in each score group
		NavigableMap<Integer, Long> shakespeareScores = 
				shakespeareWords.stream()
					.filter(scrabbleWords::contains)
					.filter(word -> nBlanks.apply(word) <= 2)
					.collect(
							Collectors.groupingBy(
									score2,
									TreeMap::new,
									Collectors.counting()
							)
					).descendingMap();
		System.out.println("# of Words of Shakespeare grouped by their Scrabble score : " + shakespeareScores);
		
		// 2.2 And second, can we list the words in the top three groups
		Map<Integer, List<String>> shakespeareScores2 = 
				shakespeareWords.stream()
					.filter(scrabbleWords::contains)
					.filter(word -> nBlanks.apply(word) <= 2)
					.collect(
							Collectors.groupingBy(
									score2,
									TreeMap::new,
									Collectors.toList()
							)
					).descendingMap().headMap(getNthKey(shakespeareScores, 3));
		System.out.println("Words of Shakespeare grouped by their Scrabble score : " + shakespeareScores2);
		
		// TASK 3 - Hmmm, we are getting some strange words here.  Modify code in task 2 to process only words that are listed in the scrabble dictionary
	
	
		// BONUS (If we have time) Can you find the best word that Shakespeare could have played as the first move?
        // scoring function 
        Function<String, Integer> scoreOnBoard = 
        		word -> 2*( // the first word scores double at Scrabble 
	        				score2.apply(word) + 
	        				Stream.of(
	        				    word.chars().skip(4), 
	        				    word.chars().limit(Integer.max(0, word.length() - 4))
	        				)
	        				.flatMapToInt(Function.identity())
	        				.map(letter -> scrabbleENScore[letter - 'a'])
	        				.max()
	        				.orElse(0)
	        			) +
	        			(word.length() == 7 ? 50 : 0) ; // there is a 50 pts bonus for a 7 letters word
        Map<Integer, List<String>> mapOnBoard = 
                shakespeareWords.stream()
                        .filter(scrabbleWords::contains)
                        .filter(word -> nBlanks.apply(word) <= 2L)
                        .filter(word -> scoreOnBoard.apply(word ) >= 114)
                        .collect(
                            Collectors.groupingBy(
                        		scoreOnBoard, 
                        		() -> new TreeMap<Integer, List<String>>(Comparator.<Integer>naturalOrder().reversed()),
                                Collectors.toList()
                            )
                        ) ;
         System.out.println("Best words of Shakespeare played as first move : " + mapOnBoard) ;
	}
	
	private static Set<String> readFile(String folder, String file) throws IOException {
		return Files.lines(Paths.get(folder, file))
				.map(String::toLowerCase)
				.collect(Collectors.toSet());
	}
	
	private static <K> K getNthKey(NavigableMap<K, ? extends Object> map, int index) {
		final AtomicReference<K> key = new AtomicReference<K>(map.firstKey());
		IntStream.range(0, index).forEach(
					ind -> key.set(map.higherKey(key.get()))
				);
		return key.get();
	}

}
