package day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day14 {

	static Map<String, String> replacements = new HashMap<>();

	public static void main(String[] args) throws IOException {
		Solver solverA = new Day14().new SolverA();
		solverA.solve();
		Solver solverB = new Day14().new SolverB();
		solverB.solve();
	}

	public class SolverA implements Solver {

		public void solve() throws IOException {
			readReplacements();
			String start = getStartingPolymer();			
			String result = replace(start,10);
			
			Map<String, Long> frequencyMap = Stream.of(result.split("")).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			Object[] frequencies = frequencyMap.values().stream().sorted().toArray();
			System.out.println("Part A: " + ((Long) frequencies[frequencies.length-1] - (Long) frequencies[0]));
		}

		private String replace(String start, int n) {
			for (int i = 0; i<n; i++) {
				start = replaceOnce(start);
			}
			return start;
		}

		private String replaceOnce(String start) {
			String end = "";
			for (int i = 0; i<start.length()-1;i++) {
				String character = start.split("")[i];
				String nextCharacter = start.split("")[i+1];
				String replacement = replacements.get(character+nextCharacter);
				String replaced = character+replacement;
				end += replaced;
			}
			end += start.split("")[start.length()-1];
			return end;
		}

	}

	public class SolverB implements Solver {
		public void solve() throws IOException {
			readReplacements();
			String start = getStartingPolymer();

			Map<String, Long> polymerAsDictionary = polymerStringToDictionary(start);
			
			polymerAsDictionary = update(polymerAsDictionary, 40);
			System.out.println(polymerAsDictionary);
						
			Set<String> occuringLetters = Stream.of(polymerAsDictionary.keySet().stream().reduce((a,b) -> {return a+b;}).get().split("")).distinct().collect(Collectors.toSet());
			Map<String, Long> frequencyMap = new HashMap<>();
			for (String letter : occuringLetters) {
				long frequency = 0;
				for (Entry<String,Long> entry : polymerAsDictionary.entrySet()) {
					frequency+=entry.getKey().replaceAll("[^"+letter+"]","").length() * entry.getValue();
				}
				frequency = (frequency+1)/2;
				frequencyMap.put(letter, frequency);
			}
			Object[] frequencies = frequencyMap.values().stream().sorted().toArray();
			System.out.println("Part B: " + ((Long) frequencies[frequencies.length-1] - (Long) frequencies[0]));
		}

		private Map<String, Long> polymerStringToDictionary(String start) {
			Map<String,Long> polymerAsDictionary = new HashMap<>();
			for (int i = 0; i<start.length()-1;i++) {
				String character = start.split("")[i];
				String nextCharacter = start.split("")[i+1];
				polymerAsDictionary.compute(character+nextCharacter,(x, currentValue) -> {return currentValue == null? 1 : currentValue+1;});
			}
			return polymerAsDictionary;
		}
		
		private Map<String,Long> update(Map<String,Long> polymerAsDictionary, int n) {
			for (int i = 0; i<n;i++) {
				polymerAsDictionary = updateOnce(polymerAsDictionary);
			}
			return polymerAsDictionary;
		}
		
		private Map<String,Long> updateOnce(Map<String,Long> polymerAsDictionary) {
			Map<String,Long> polymerAfterModification = new HashMap<>();
			polymerAfterModification.putAll(polymerAsDictionary);
			for (Entry<String,String> replacement : replacements.entrySet()) {
				String pairToReplace = replacement.getKey();
				String letterToInsert = replacement.getValue();
				String firstNewPair = pairToReplace.split("")[0]+letterToInsert;
				String secondNewPair = letterToInsert + pairToReplace.split("")[1];
				Long currentFrequencyOfPairToReplace = polymerAsDictionary.get(pairToReplace);
				if (currentFrequencyOfPairToReplace != null && currentFrequencyOfPairToReplace != 0) {
					polymerAfterModification.compute(pairToReplace, (x, currentValue) -> {return currentValue - currentFrequencyOfPairToReplace;});
					polymerAfterModification.compute(firstNewPair, (x,currentValue) -> {return currentValue==null? currentFrequencyOfPairToReplace : currentValue+currentFrequencyOfPairToReplace;});
					polymerAfterModification.compute(secondNewPair, (x,currentValue) -> {return currentValue == null? currentFrequencyOfPairToReplace  : currentValue+currentFrequencyOfPairToReplace;});
				}
			}
			return polymerAfterModification;
		}
	}
	
	public interface Solver {
		
		public static final String INPUT_PATH = "C:\\Users\\00010550\\Documents\\GitHub\\AoC21\\src\\main\\resources\\day14\\input.txt";

		public void solve() throws IOException;

		default String getStartingPolymer() throws IOException {
			List<String> input = Files.lines(Paths.get(INPUT_PATH)).collect(Collectors.toList());
			String start = input.get(0);
			return start;
		}

		default List<String> readReplacements() throws IOException {
			List<String> input = Files.lines(Paths.get(INPUT_PATH)).collect(Collectors.toList());
			for (String replacementCommand : input) {
				if (replacementCommand.contains(" -> ")) {
					replacements.put(replacementCommand.split(" -> ")[0],replacementCommand.split(" -> ")[1]);
				}
			}
			return input;
		}
	}
}
