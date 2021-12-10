package day07;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day07 {
	
	public static void main(String[] args) throws IOException {
		teilA();
		teilB();
	}

	private static void teilB() throws IOException {
		List<String> lines = Files.lines(Paths
				.get("C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day07\\input.txt"))
				.collect(Collectors.toList());
		int[] positions = Arrays.stream(lines.get(0).split(",")).mapToInt(Integer::parseInt).toArray();
		int[] fuelCost = new int[2000];
		for (int i = 0; i<2000; i++) {
			int fuelCostI = 0;
			System.out.println("Cost for " + i);
			for (int position : positions) {
				System.out.println("Cost for position " + position);
				fuelCostI += Math.pow(Math.abs(position-i),2.)/2.+((double) Math.abs(position-i))/2.;
				System.out.println( Math.pow(Math.abs(position-i),2.)/2.+((double) Math.abs(position-i))/2.);
			}
			fuelCost[i] = fuelCostI;
		}
		System.out.println(Arrays.stream(fuelCost).min());
	}

	
	private static void teilA() throws IOException {
		List<String> lines = Files.lines(Paths
				.get("C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day07\\input.txt"))
				.collect(Collectors.toList());
		int[] positions = Arrays.stream(lines.get(0).split(",")).mapToInt(Integer::parseInt).toArray();
		int[] fuelCost = new int[2000];
		for (int i = 0; i<2000; i++) {
			int fuelCostI = 0;
			System.out.println("Cost for " + i);
			for (int position : positions) {
				System.out.println("Cost for position " + position);
				fuelCostI += Math.abs(position-i);
				System.out.println(Math.abs(position-i));
			}
			fuelCost[i] = fuelCostI;
		}
		for (int i = 0; i<20; i++) {
			System.out.println(fuelCost[i]);
		}
		System.out.println(fuelCost);
		System.out.println(Arrays.stream(fuelCost).min());
	}

}
