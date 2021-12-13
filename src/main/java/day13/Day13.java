package day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {

	public static void main(String[] args) throws IOException {
		Solver solverA = new Day13().new SolverA();
		solverA.solve();
		Solver solverB = new Day13().new SolverB();
		solverB.solve();
	}

	public interface Solver {
		public void solve() throws IOException;

		default int[][] setupFoldedPaper(List<String> foldsInput, List<Integer[]> folds) {
			int[][] foldedPaper;
			int xMax = 2000;
			int yMax = 2000;
			for (String fold : foldsInput) {
				fold = fold.replaceAll("fold along ", "");
				if (fold.split("=")[0].equals("x")) {
					xMax = Math.min(xMax, Integer.parseInt(fold.split("=")[1]));
					folds.add(new Integer[] { 1, Integer.parseInt(fold.split("=")[1]) });
				} else if (fold.split("=")[0].equals("y")) {
					yMax = Math.min(yMax, Integer.parseInt(fold.split("=")[1]));
					folds.add(new Integer[] { -1, Integer.parseInt(fold.split("=")[1]) });
				} else {
					System.out.println("BUG!");
				}
			}

			foldedPaper = new int[yMax][xMax];
			return foldedPaper;
		}

		default void populatePaperWithPoints(List<Integer[]> folds, int[][] foldedPaper, List<String> lines) {
			for (String line : lines) {
				Integer x = Integer.parseInt(line.split(",")[0]);
				Integer y = Integer.parseInt(line.split(",")[1]);
				// point needs to be folded
				if (x > foldedPaper[0].length || y > foldedPaper.length) {
					for (Integer[] fold : folds) {
						// x-axis fold
						if (fold[0] > 0) {
							if (x > fold[1]) {
								x = x - 2 * (x - fold[1]);
							}
						} else if (fold[0] < 0) {
							if (y > fold[1]) {
								y = y - 2 * (y - fold[1]);
							}
						}
					}
				}
				foldedPaper[y][x] = 1;
			}
		}
	}

	public class SolverA implements Solver {
		public void solve() throws IOException {

			List<String> foldsInput = Files
					.lines(Paths.get(
							"C:\\Users\\00010550\\Documents\\GitHub\\AoC21\\src\\main\\resources\\day13\\folds.txt"))
					.collect(Collectors.toList());

			List<Integer[]> folds = new ArrayList<>();
			int[][] foldedPaper;

			foldedPaper = setupFoldedPaper(foldsInput, folds);

			List<String> lines = Files
					.lines(Paths.get(
							"C:\\Users\\00010550\\Documents\\GitHub\\AoC21\\src\\main\\resources\\day13\\input.txt"))
					.collect(Collectors.toList());

			populatePaperWithPoints(folds, foldedPaper, lines);

			printResponse(foldedPaper);
		}

		private void printResponse(int[][] foldedPaper) {
			int numberOfDots = 0;
			for (int i = 0; i < foldedPaper[0].length; i++) {
				for (int j = 0; j < foldedPaper.length; j++) {
					numberOfDots += foldedPaper[j][i];
				}
			}
			System.out.println(numberOfDots);
		}

	}

	public class SolverB implements Solver {
		public void solve() throws IOException {
			List<String> foldsInput = Files
					.lines(Paths.get(
							"C:\\Users\\00010550\\Documents\\GitHub\\AoC21\\src\\main\\resources\\day13\\foldsB.txt"))
					.collect(Collectors.toList());

			List<Integer[]> folds = new ArrayList<>();
			int[][] foldedPaper;

			foldedPaper = setupFoldedPaper(foldsInput, folds);
			List<String> lines = Files
					.lines(Paths.get(
							"C:\\Users\\00010550\\Documents\\GitHub\\AoC21\\src\\main\\resources\\day13\\input.txt"))
					.collect(Collectors.toList());

			populatePaperWithPoints(folds, foldedPaper, lines);

			printResponse(foldedPaper);
		}

		private void printResponse(int[][] foldedPaper) {
			int divider = foldedPaper[0].length / 8;
			for (int j = 0; j < foldedPaper.length; j++) {
				for (int i = 0; i < foldedPaper[0].length; i++) {
					System.out.print(foldedPaper[j][i]);
					if ((i + 1) % divider == 0) {
						System.out.print("  ");
					}
				}
				System.out.println("");
			}
		}

	}
}
