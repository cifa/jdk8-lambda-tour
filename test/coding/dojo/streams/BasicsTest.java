package coding.dojo.streams;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class BasicsTest {
	
	private static final int UNIQUE_WORD_COUNT = 266;

	private static final int NO_OF_WORDS_LONGER_THAN_4 = 218;

	private static final int NO_OF_WORDS_LONGER_THAN_7 = 128;
	
	private Basics b;
	
	@Before
	public void setUp() {
		b = new Basics();
	}

	@Test
	public void listOfWordsHasCorrectSize() throws IOException {
		List<String> words = b.readWords();
		assertNotNull(words);
		assertEquals(UNIQUE_WORD_COUNT, words.size());
	}
	
	@Test
	public void allWordsAreUnique() throws IOException {
		List<String> words = b.readWords();
		assertNotNull(words);
		assertEquals(UNIQUE_WORD_COUNT, new HashSet<String>(words).size());
	}

	@Test
	public void noWordsLongerThan50Chars() throws IOException {
		int count = b.countWordsStrictlyLongerThan(100);
		assertEquals(0, count);
	}
	
	@Test
	public void allWordsAreLongerThan0Chars() throws IOException {
		int count = b.countWordsStrictlyLongerThan(0);
		assertEquals(UNIQUE_WORD_COUNT, count);
	}
	
	@Test
	public void checkWordsLongerThan4Chars() throws IOException {
		int count = b.countWordsStrictlyLongerThan(4);
		assertEquals(NO_OF_WORDS_LONGER_THAN_4, count);
	}
	
	@Test
	public void checkWordsLongerThan7Chars() throws IOException {
		int count = b.countWordsStrictlyLongerThan(7);
		assertEquals(NO_OF_WORDS_LONGER_THAN_7, count);
	}
	
	@Test
	public void checkJoiningWordsByComma() throws IOException {
		String pattern = ", ";
		String joined = b.joinWordsLongerThanBy(4, pattern);
		assertNotNull(joined);
		assertEquals(NO_OF_WORDS_LONGER_THAN_4, joined.split(pattern).length);
	}
	
	@Test
	public void checkBonusResult() throws IOException {
		long sum = b.getSumOfMultiplications();
		assertEquals(3025, sum);
	}
	
	@Test
	public void checkGenericBonusFor1() throws IOException {
		long sum = b.getSumOfMultiplicationsGeneric(1);
		assertEquals(55, sum);
	}
	
	@Test
	public void checkGenericBonusFor2() throws IOException {
		long sum = b.getSumOfMultiplicationsGeneric(2);
		assertEquals(3025, sum);
	}
	
	@Test
	public void checkGenericBonusFor3() throws IOException {
		long sum = b.getSumOfMultiplicationsGeneric(3);
		assertEquals(166375, sum);
	}
	
	@Test
	public void checkGenericBonusFor4() throws IOException {
		long sum = b.getSumOfMultiplicationsGeneric(4);
		assertEquals(9150625, sum);
	}
}
