package day11;

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

public class Day11 {

	public static void main(String[] args) throws IOException {
		Solver solver = new Day11().new Solver();
		solver.teilA();
		solver.teilB();
	}

	public class Solver {
		public void teilA() throws IOException {
			List<String> lines = Files
					.lines(Paths.get(
							"C:\\Users\\00010550\\Documents\\GitHub\\AoC21\\src\\main\\resources\\day11\\input.txt"))
					.collect(Collectors.toList());
			int[][] field = new int[lines.size()][lines.get(0).length()];
			int[][] flashed = new int[lines.size()][lines.get(0).length()];
			boolean[][] flashedThisRound = new boolean[lines.size()][lines.get(0).length()];
			
			initField(lines, field);
			
			int totalFlashesThisRound = 0;
			for (int n = 0; totalFlashesThisRound<100; n++) {
				totalFlashesThisRound = 0;
				
				updateField(lines, field, flashed, flashedThisRound);
				// part A
				if (n==100) {
					int totalFlashes = 0;
					for (int i = 0; i < lines.size(); i++) {
						for (int j = 0; j < lines.get(0).length(); j++) {
							totalFlashes += flashed[i][j];
						}
					}
					System.out.println(totalFlashes);
				}
				// part B
				for (int i = 0; i < lines.size(); i++) {
					for (int j = 0; j < lines.get(0).length(); j++) {
						totalFlashesThisRound += flashedThisRound[i][j] ? 1 : 0;
					}
				}
				System.out.println("Round " + (n + 1) + " total flashes: " + totalFlashesThisRound);
				flashedThisRound = new boolean[lines.size()][lines.get(0).length()];
			}
		}

		private void updateField(List<String> lines, int[][] field, int[][] flashed, boolean[][] flashedThisRound) {
			for (int i = 0; i < lines.size(); i++) {
				for (int j = 0; j < lines.get(0).length(); j++) {
					if (field[i][j] == 9) {
						field[i][j] = 0;
						flashedThisRound[i][j] = true;
						flashed[i][j] += 1;
						updateNeighbours(field, flashed, flashedThisRound, i, j);
					}
					else if (!flashedThisRound[i][j]){
						field[i][j] += 1;
					}
				}
			}
		}

		private void initField(List<String> lines, int[][] field) {
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				String[] digits = line.split("");
				for (int j = 0; j < digits.length; j++) {
					field[i][j] = Integer.parseInt(digits[j]);
				}
			}
		}

		public void updateNeighbours(int[][] field, int[][] flashed, boolean[][] flashedThisRound, int i, int j) {
			for (int a = -1; a <= 1; a++) {
				for (int b = -1; b<=1; b++) {
					if (i+a >= 0 && i+a < field.length && j+b >= 0 && j+b < field[0].length) {
						if (!flashedThisRound[i+a][j+b]){
							field[i+a][j+b] += 1;
						}
						if (field[i+a][j+b] > 9) {
							flashed[i+a][j+b] += 1;
							field[i+a][j+b] = 0;
							flashedThisRound[i+a][j+b] = true;
							updateNeighbours(field, flashed, flashedThisRound, Math.min(Math.max(i+a,0),field.length-1), Math.min(Math.max(j+b,0),field[0].length-1));
						}
					}
				}
			}
		}
		
		
		public void teilB() throws IOException {
			List<String> lines = Files.lines(Paths.get(
					"C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day10\\input.txt"))
					.collect(Collectors.toList());

		}

	}
}
