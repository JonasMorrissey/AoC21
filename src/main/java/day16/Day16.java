package day16;

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
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day16 {

	static Map<String, String> replacements = new HashMap<>();

	public static void main(String[] args) throws IOException {
		Solver solverA = new Day16().new SolverA();
		solverA.solve();
		Solver solverB = new Day16().new SolverB();
		solverB.solve();
	}

	public class SolverA implements Solver {
		@Override
		public void printAnswer(List<Long> versions, List<Long> values) {
			int sum = 0;
			for (Long version : versions) {
				sum += version;
			}
			System.out.println(sum);
		}
	}

	public class SolverB implements Solver {
		@Override
		public void printAnswer(List<Long> versions, List<Long> values) {
			System.out.println(values.get(0));
		}
	}

	public interface Solver {

		static final String INPUT_PATH = "C:\\Users\\00010550\\Documents\\GitHub\\AoC21\\src\\main\\resources\\day16\\input.txt";

		default void solve() throws IOException {
			String input = getInput();

			List<Long> versions = new ArrayList<>();
	
			String packageString = input;
			System.out.println(packageString);
			
			List<Long> values = new ArrayList<Long>();
			
			handlePackage(versions, values, packageString);
			printAnswer(versions, values);
		}
		
		
		/**
		 * Handles one package at a time
		 * @param versions All versions of subpackages are added to this list 
		 * @param values ALl values of subpackages are added to this list
		 * @param packagesString The string of which to process the first package
		 * @return  the part of the input string that was not processed
		 */
		default String handlePackage(List<Long> versions, List<Long> values, String packagesString) {
			long value = 0;
			// list containing the values of packages that are contained in this one
			List<Long> newValues = new ArrayList<>();
			// the packages that remain to be processed
			String unprocessedPackage = "";
			
			String version = packagesString.substring(0, 3);
			versions.add(Long.parseLong(version, 2));
			
			String typeId = packagesString.substring(3, 6);
			// literal package
			if (typeId.equals("100")) {
				// substring that contains the codified number
				packagesString = packagesString.substring(6);
				// first codified block, always exists
				String number = packagesString.substring(1, 5);
				// are there more blocks?
				boolean continueProcessing = packagesString.substring(0, 1).equals("1");
				while (continueProcessing) {
					// move to the next block
					packagesString = packagesString.substring(5);
					number += packagesString.substring(1, 5);
					continueProcessing = packagesString.substring(0, 1).equals("1");
				}
				value = Long.parseLong(number, 2);
				System.out.println("Raw value: " + number + ": " + value);
				// move position once more 
				packagesString = packagesString.substring(5);
				// this part of the string has not been processed
				unprocessedPackage = packagesString;
			} else {
				String lengthTypeId = packagesString.substring(6, 7);
				switch (lengthTypeId) {
					case "0":
						Integer totalLength = Integer.parseInt(packagesString.substring(7, 7 + 15), 2);
						// this is the current operator packet
						String subpackagesToParse = packagesString.substring(7 + 15, 7 + 15 + totalLength);
						// everything after the length of this operator packet
						String unparsedPackages = packagesString.substring(7 + 15 + totalLength);
						unprocessedPackage = handlePackages(subpackagesToParse, versions,newValues) + unparsedPackages;
						break;
					case "1":
						Integer totalPackages = Integer.parseInt(packagesString.substring(7, 7 + 11), 2);
						unprocessedPackage = handlePackages(packagesString.substring(7 + 11), versions, newValues, totalPackages);
						break;
				}
				switch (typeId) {
					default:
						break;
					case "000":
						value = newValues.stream().mapToLong(x -> x).sum();
						System.out.println("Sum of " + newValues + " is " + value);
						break;
					case "001":
						value = newValues.stream().mapToLong(x -> x).reduce(1l, (a, b) -> {return a * b;});
						System.out.println("Product of " + newValues + " is " + value);
						break;
					case "010":
						value = newValues.stream().mapToLong(x -> x).min().getAsLong();
						System.out.println("Minimum of " + newValues + " is " + value);
						break;
					case "011":
						value = newValues.stream().mapToLong(x -> x).max().getAsLong();
						System.out.println("Maximum of " + newValues + " is " + value);
						break;
					case "101":
						value = newValues.get(0) > newValues.get(1) ? 1l : 0l;
						System.out.println("bigger of " + newValues + " is " + value);
						break;
					case "110":
						value = newValues.get(0) < newValues.get(1) ? 1l : 0l;
						System.out.println("smaller of " + newValues + " is " + value);
						break;
					case "111":
						value = newValues.get(0).equals(newValues.get(1)) ? 1l : 0l;
						System.out.println("equals of " + newValues + " is " + value);
						break;
				}
			}
			values.add(value);
			// if the unprocessed part has length <6 it contains only trailing zeroes
			return unprocessedPackage.length() < 6 ? "" : unprocessedPackage;
		}

		default String handlePackages(String packageString, List<Long> versions, List<Long> values) {
			while (!packageString.isEmpty()) {
				packageString = handlePackage(versions, values, packageString);
			}
			return packageString.length() < 6 ? "" : packageString;
		}

		default String handlePackages(String packageString, List<Long> versions, List<Long> values,
				int numberOfPackages) {
			for (int i = 0; i < numberOfPackages; i++) {
				packageString = handlePackage(versions, values, packageString);
			}
			return packageString.length() < 6 ? "" : packageString;
		}
		
		
		void printAnswer(List<Long> versions, List<Long> values);

		default String getInput() throws IOException {
			String input = Files.lines(Paths.get(INPUT_PATH)).collect(Collectors.toList()).get(0);
			input = input.replaceAll("0", "0000");
			input = input.replaceAll("1", "0001");
			input = input.replaceAll("2", "0010");
			input = input.replaceAll("3", "0011");
			input = input.replaceAll("4", "0100");
			input = input.replaceAll("5", "0101");
			input = input.replaceAll("6", "0110");
			input = input.replaceAll("7", "0111");
			input = input.replaceAll("8", "1000");
			input = input.replaceAll("9", "1001");
			input = input.replaceAll("A", "1010");
			input = input.replaceAll("B", "1011");
			input = input.replaceAll("C", "1100");
			input = input.replaceAll("D", "1101");
			input = input.replaceAll("E", "1110");
			input = input.replaceAll("F", "1111");
			return input;
		}

	}
}
