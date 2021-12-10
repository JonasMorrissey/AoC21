package day03;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day3 {

	public static void main(String[] args) throws IOException {
		List<String> lines = Files
				.lines(Paths
						.get("C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day3\\input.txt"))
				.collect(Collectors.toList());
//		extracted(lines);
		teil2(lines);

	}

	private static void teil2(List<String> lines) {
		List<String> lines2 = new ArrayList<String>();
		lines2.addAll(lines);
		for (int i = 0; i < lines.get(0).length();i++) {
			lines = filterByPosition(i, lines, false);
		}
		System.out.println(lines);
		for (int i = 0; i < lines.get(0).length();i++) {
			lines2 = filterByPosition(i, lines2, true);
		}
		System.out.println(lines2);
	}
	
	public static List<String> filterByPosition(int position, List<String> lines, boolean least) {
		
		System.out.println("Bit " + position);
		int countingOnes = 0;
		int countingZeroes = 0;
		for (String line : lines) {
			System.out.println(line);
			if (line.charAt(position) == '1') {
				countingOnes += 1;
			}
			else {
				countingZeroes += 1;
			}
		}

		char leadingChar;
		if (countingOnes < countingZeroes) {
			leadingChar = '0';
		}
		else {
			leadingChar = '1';
		}
		if (least) {
			return lines.stream().filter(line -> line.charAt(position) != leadingChar).collect(Collectors.toList());
		}
		return lines.stream().filter(line -> line.charAt(position) == leadingChar).collect(Collectors.toList());
		
	}
	
	
	private static void extracted(List<String> lines) {
		String deltaString = "";
		String epsilonString = "";
		double delta = 0;
		double epsilon = 0;
//		double faktor = 1;
		int countingOnes;
		int countingZeroes;
		int maxLength = 1337;
		for (int i = 0; i<maxLength; i++) {
			System.out.println("Bit " + i);
			countingOnes = 0;
			countingZeroes = 0;
			for (String line : lines) {
				maxLength = line.length();
				System.out.println(line);
				if (line.charAt(i) == '1') {
					System.out.println("1 gefunden");
					countingOnes += 1;
				}
				else {
					countingZeroes += 1;
				}
			}

			if (countingOnes < countingZeroes) {
				System.out.println("Bit wird 0 mit " + countingZeroes + " Nullen");
				deltaString = deltaString + "0";
				epsilonString = epsilonString + "1";
			}
			else {
				System.out.println("Bit wird 1 mit " + countingOnes + " Einsen");
				deltaString = deltaString + "1";
				epsilonString = epsilonString + "0";
			}
			System.out.println("Delta = " + deltaString);
		}
		System.out.println(deltaString);
		System.out.println(new BigInteger(deltaString,2));
		System.out.println(new BigInteger(epsilonString,2));
		System.out.println(new BigInteger(deltaString,2).multiply(new BigInteger(epsilonString,2)));
	}

}
