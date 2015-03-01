package coding.dojo.streams;

import static java.util.stream.Collectors.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class RulesOfTheGame {

	public static class Interference {
		public static void main(String... args) {
			List<String> words = getWords();
			words.stream()
					.filter(w -> w.length() > 4)
					.forEach(words::add);
		}
	}
	
	public static class State {
		public static void main(String... args) {
			List<String> words = getWords();
			List<String> filteredWords = new ArrayList<>();
			words.stream()
					.filter(w -> w.length() > 4)
					.forEach(filteredWords::add);
			
			System.out.println(filteredWords.size());
		} 
	}
	
	public static class StateChange {
		public static void main(String... args) {
			List<String> words = getWords();
			Set<String> seen = Collections.synchronizedSet(new LinkedHashSet<>());
			
			List<String> uniqueWords = words.stream()
					.filter(w -> !seen.contains(w))
					.peek(seen::add)
					.collect(toList());
			
			System.out.printf("%s %s %s\n", seen.size(), uniqueWords.size(), words.size());
			System.out.println(seen);
			System.out.println(uniqueWords);
		}
	}
	
	public static class UnderTheHood {
		public static void main(String... args) {
//			System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "0");
			
			Map<String, List<Integer>> m = IntStream.range(0, 128)
					.parallel()
					.boxed()
					.collect(groupingBy(i -> Thread.currentThread().getName()));
			
			System.out.println(m.getClass());
			m.forEach((k, v) -> System.out.printf("%s -> %s\n", k, v));
		}
	}
	
	public static class UnderTheHoodControlled {
		public static void main(String... args) {
			ForkJoinPool fjp = new ForkJoinPool(2);
			
			fjp.submit(() -> {
				Map<String, List<Integer>> m = IntStream.range(0, 1024)
						.parallel()
						.boxed()
						.collect(groupingBy(i -> Thread.currentThread().getName()));
				
				System.out.println(m.getClass());
				m.forEach((k, v) -> System.out.printf("%s -> %s\n", k, v));
			}).join();
		}
	}
	
	private static List<String> getWords() {
		Pattern p = Pattern.compile(",");
		Path path = Paths.get("files", "words.csv");
		try {
			return Files.lines(path)
					.flatMap(p::splitAsStream)
					.map(String::toLowerCase)
					.collect(toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
