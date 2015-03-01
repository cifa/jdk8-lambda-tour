package coding.dojo.streams;

import java.util.function.Function;

public class Currying {

	public static void main(String[] args) {
		Function<Integer, Function<Integer, Integer>> max = (x) -> (y) -> Math.max(x, y);
		
		Function<Integer, Integer> maxOfFive = max.apply(5);
		
		System.out.println(maxOfFive.apply(4));
		System.out.println(maxOfFive.apply(6));
	}

}
