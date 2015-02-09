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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ScrabbleSolved {

	private static final int[] scrabbleENScore = {
			// a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,  q, r, s, t, u, v, w, x, y, z
			   1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10 };

	private static final int[] scrabbleENDistribution = {
			// a, b, c, d, e,  f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z
			   9, 2, 2, 1, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1 };

	/**
	 * HOW GOOD WOULD SHAKESPEAR BE IF HE PLAYED SCRABBLE???
	 */
	public static void main(String... args) throws Exception {
		Set<String> scrabbleWords =  readFile("files", "ospd.txt");
		Set<String> shakespeareWords = readFile("files", "words.shakespeare.txt");
		
		System.out.println("# of words in the Scrabble dictionnary : " + scrabbleWords.size()) ;
		System.out.println("# of words used by Shakespeare : " + shakespeareWords.size()) ;
        
		// TASK 1 - How many point is a word worth in scrabble
		Function<String, Integer> score =
				word -> word.chars().map(letter -> scrabbleENScore[letter - 'a']).sum();
		
		// TASK 2 - Group words by their scrabble score
		// 2.1 First, we just want to count those words in each score group
		NavigableMap<Integer, Long> shakespeareScores = 
				shakespeareWords.stream()
					.collect(
							Collectors.groupingBy(
									score,
									TreeMap::new,
									Collectors.counting()
							)
					).descendingMap();
		System.out.println("# of Words of Shakespeare grouped by their Scrabble score : " + shakespeareScores);
		
		// 2.2 And second, can we list the words in the top three groups
		Map<Integer, List<String>> shakespeareScores2 = 
				shakespeareWords.stream()
					.collect(
							Collectors.groupingBy(
									score,
									TreeMap::new,
									Collectors.toList()
							)
					).descendingMap().headMap(getNthKey(shakespeareScores, 3), true);
		System.out.println("Words of Shakespeare grouped by their Scrabble score : " + shakespeareScores2);
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
