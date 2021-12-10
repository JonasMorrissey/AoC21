package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day10 {

	public static void main(String[] args) throws IOException {
		Solver solver = new Day10().new Solver();
		solver.teilA();
		solver.teilB();
	}

	public class Solver {
		public void teilA() throws IOException {
			List<String> lines = Files.lines(Paths.get(
					"C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day10\\input.txt"))
					.collect(Collectors.toList());
			int points = 0;
			for (String line : lines) {
				char corruptingBrace = isLineCorrupted(line);
				switch (corruptingBrace) {
				case ')':
					points += 3;
					break;
				case ']':
					points += 57;
					break;
				case '}':
					points += 1197;
					break;
				case '>':
					points += 25137;
					break;
				default:
				}
			}
			System.out.println(points);
		}

		public boolean isOpeningBrace(char brace) {
			return brace == 40 || brace == 60 || brace == 91 || brace == 123;
		}

		public char getOpeningBrace(char brace) {
			return brace == 41 ? 40 : (char) (brace - 2);
		}

		public char getClosingBrace(char brace) {
			return brace == 40 ? 41 : (char) (brace + 2);
		}

		public char isLineCorrupted(String line) {
			Stack<Character> myStack = new Stack<>();
			for (char brace : line.toCharArray()) {
				if (isOpeningBrace(brace)) {
					myStack.push(brace);
				} else {
					if (myStack.peek() != null && myStack.peek().equals(getOpeningBrace(brace))) {
						myStack.pop();
					} else {
						System.out.println("Brace " + brace + " is illegal in line " + line);
						return brace;
					}
				}
			}
			return ' ';
		}

		public void teilB() throws IOException {
			List<String> lines = Files.lines(Paths.get(
					"C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day10\\input.txt"))
					.collect(Collectors.toList());
			lines = lines.stream().filter(line -> isLineCorrupted(line) == ' ').collect(Collectors.toList());
			long[] pointsArray = lines.stream().map(line -> getCompletion(line)).mapToLong(completion -> getPointsForCompletion(completion)).sorted().toArray();
			System.out.println(pointsArray[pointsArray.length/2]);

		}

		public String getCompletion(String line) {
			Stack<Character> myStack = new Stack<>();
			for (char brace : line.toCharArray()) {
				if (isOpeningBrace(brace)) {
					myStack.push(brace);
				} else {
					myStack.pop();
				}
			}
			String completion = myStack.stream().map(brace -> String.valueOf(getClosingBrace(brace)))
					.reduce((a, b) -> b + a).orElse("");
			System.out.println("Line " + line + " is completed by " + completion);
			if (completion != null && completion != "") {
				System.out.println("Test: " + getCompletion(line+completion));
			}
			return completion;
		}

		public Long getPointsForCompletion(String completion) {
			long points = 0;
			for (char closingBracket : completion.toCharArray()) {
				points *= 5;
				switch (closingBracket) {
					case ')':
						points += 1;
						break;
					case ']':
						points += 2;
						break;
					case '}':
						points += 3;
						break;
					case '>':
						points += 4;
						break;
				}
			}
			return points;
		}
	}
}
