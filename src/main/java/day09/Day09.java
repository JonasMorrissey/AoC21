package day09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day09 {

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.lines(Paths
				.get("C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day09\\input.txt"))
				.collect(Collectors.toList());

		int[][] field = new int[lines.size()][lines.get(0).length()];

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			field[i] = Stream.of(line.split("")).mapToInt(Integer::parseInt).toArray();
		}
		int riskLevel = 0;
		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(0).length(); j++) {
				int position = field[i][j];
				boolean lowPoint = compare(field, i - 1, j, position) 
						&& compare(field, i + 1, j, position)
						&& compare(field, i, j - 1, position)
						&& compare(field, i, j + 1, position);
				if (lowPoint) {
					riskLevel += position + 1;
				}
			}
		}
		System.out.println("Risklevel: " + riskLevel);

		Integer[][][] sinkMap = new Integer[lines.size()][lines.get(0).length()][2];

		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(0).length(); j++) {
				if (field[i][j] == 9 || sinkMap[i][j][0] != null) {
					continue;
				}
				setLowPoint(field, sinkMap, i, j);
			}
		}		
		List<Integer> sinkPositions = Stream.of(sinkMap).flatMap(array -> Arrays.stream(array))	// sinkhole positions
					.filter(array -> array[0] != null).map(array -> array[0] + array[1])// uniquely identified since the first coordinate is 
																						// scaled way beyond the range of the second coordinate
					.collect(Collectors.toList());
		long partB = sinkPositions.stream().distinct() 									// the sinkholes
					.mapToLong(sink -> sinkPositions.stream().filter(position -> position.equals(sink)).count()) // mapped to their frequencies
					.boxed().sorted(Collections.reverseOrder()).limit(3) 				// only the 3 most common
					.reduce(1L, (a, b) -> a*b);
		System.out.println(partB);
	}
	

	public static void setLowPoint(int[][] field, Integer[][][] sinkMap, int i, int j) {
		int[] lowPointCoordinates = findLowestNeighbour(field, i, j);
		// stable point, i.e. a sinkhole was reached
		if (lowPointCoordinates[0] == i && lowPointCoordinates[1] == j ) {
			sinkMap[i][j] = new Integer[] {i % 100000 == 0? i : i*100000,j}; 			// scale first coordinate to avoid collisions when summing later on
		}
		else {
			setLowPoint(field, sinkMap, lowPointCoordinates[0], lowPointCoordinates[1]); // go to the nearest low point and recurse until a sinkhole is found, filling the sinkMap on the way for performance reasons.
			sinkMap[i][j] = sinkMap[lowPointCoordinates[0]][lowPointCoordinates[1]];
		}
	}
	
	public static int[] findLowestNeighbour(int[][] field, int i, int j) {
		int[][] array = { {Math.max(i-1,0), j}, {Math.min(i+1,field.length-1),j}, {i,Math.max(j-1,0)}, {i,Math.min(j+1,field[0].length-1)}, {i,j}}; // candidates for minimal neighbours
		Comparator<int[]> comparingInt = Comparator.comparingInt(coordinates -> field[coordinates[0]][coordinates[1]]);
		int[] minimalPosition = Stream.of(array).max(Collections.reverseOrder(comparingInt)).get();
//		System.out.println("Minimal neighbour for (" + i + "," + j + ") with value " + field[i][j] + " is " + "(" + minimalPosition[0] + "," + minimalPosition[1] + ") with value " + field[minimalPosition[0]][minimalPosition[1]]);
		return minimalPosition;
	}
	
	public static boolean compare(int[][] field, int i, int j, int position) {
		try {
			return field[i][j] > position;
		} catch (ArrayIndexOutOfBoundsException e) { return true;}
	}
}
