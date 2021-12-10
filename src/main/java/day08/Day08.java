package day08;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day08 {
	
	public static void main(String[] args) throws IOException {
		List<String> lines = Files.lines(Paths
				.get("C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day08\\input.txt"))
				.collect(Collectors.toList());
		int onefourseveneights = 0;
		int sumOfDigits = 0;
		for (String line : lines) {
			Set<String> specialDigits = Arrays.stream(line.split(" ")).filter(digit -> digit.length()==2 || digit.length()==4 || digit.length()==3 || digit.length()==7).collect(Collectors.toSet());
			onefourseveneights += specialDigits.size();
			
			Set<String> specialDigitsSorted = new TreeSet<String>(Comparator.comparing(String::length));
			specialDigitsSorted.addAll(specialDigits);
			String[] digits = line.split("\\|")[1].trim().split(" ");
			List<List<Character>> specialDigitsCharacters = new ArrayList<>();
			for (String specialDigit : specialDigitsSorted) {
				specialDigitsCharacters.add(specialDigit.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
			}
			String numericalValue = "";
			for (String digit : digits) {
				numericalValue+=getNumericalValueForDigit(digit, specialDigitsCharacters);
				System.out.println(digit + ": " + getNumericalValueForDigit(digit, specialDigitsCharacters));
			}
			System.out.println(numericalValue);
			sumOfDigits += Integer.parseInt(numericalValue);
		}
		System.out.println(onefourseveneights);
		System.out.println(sumOfDigits);
		
	}

		
	private static String getNumericalValueForDigit(String digit, List<List<Character>> specialDigitsCharacters) {
		Set<Character> digitCharacters = digit.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
		switch (digit.length()) {
			case 2: return "1";
			case 3: return "7";
			case 4: return "4";
			case 5:
				// contains 1
				if (digitCharacters.containsAll(specialDigitsCharacters.get(0))) {return "3";}
				else {
					Set<Character> intersectionWith4 = new HashSet<Character>(digitCharacters);
					intersectionWith4.retainAll(specialDigitsCharacters.get(2));
					if (intersectionWith4.size()==2) {return "2";}
					else {return "5";}
					}
			case 6:
				// enthaelt 7 und 4
				if (digitCharacters.containsAll(specialDigitsCharacters.get(1)) && digitCharacters.containsAll(specialDigitsCharacters.get(2))) {
					return "9";
				}
				// enthaelt 1
				else if (digitCharacters.containsAll(specialDigitsCharacters.get(0))) {
					return "0";
				}
				else {
					return "6";
				}
			case 7:
				return "8";
			default: 
				System.out.println("Nichts gefunden fuer den String " + digit + ". Ist das okay?");
				return "";
		}
	}
}
