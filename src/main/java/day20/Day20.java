package day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day20 {
	public static final String INPUT_PATH = "C:\\Users\\00010550\\Documents\\GitHub\\AoC21\\src\\main\\resources\\day20\\input.txt";

	static String input;

	public static void main(String[] args) throws IOException {
		new Day20().new Solver().solve();
	}

	public class Solver {

		public void solve() throws IOException {
			List<String> lines = Files.lines(Paths.get(INPUT_PATH)).collect(Collectors.toList());
			input = lines.get(0);
			int offset = 2;
			int[][] inputImage = new int[lines.size() - offset][lines.get(offset).length()];
			for (int i = 0; i < lines.size() - offset; i++) {
				String line = lines.get(i + offset);
				for (int j = 0; j < lines.get(offset).length(); j++) {
					inputImage[i][j] = line.toCharArray()[j] == '#' ? 1 : 0;
				}
			}

			for (int i = 1; i < 51; i++) {
				inputImage = transformImage(inputImage, i % 2 == 1);
			}
			int number = 0;
			for (int i = 0; i < inputImage.length; i++) {
				for (int j = 0; j < inputImage[0].length; j++) {
					number += inputImage[i][j];
				}
			}
			System.out.println(number);

		}

		public int[][] transformImage(int[][] inputImage, boolean odd) throws IOException {

			int padding = 1;
			int[][] paddedInputImage = new int[inputImage.length + padding * 2][inputImage[0].length + padding * 2];
			for (int i = 0; i < inputImage.length; i++) {
				for (int j = 0; j < inputImage[0].length; j++) {
					paddedInputImage[i + padding][j + padding] = inputImage[i][j];
				}
			}

			if (!odd) {
				for (int j = 0; j < paddedInputImage[0].length; j++) {
					paddedInputImage[0][j] = 1;
					paddedInputImage[paddedInputImage.length - 1][j] = 1;
				}
				for (int i = 0; i < paddedInputImage.length; i++) {
					paddedInputImage[i][0] = 1;
					paddedInputImage[i][paddedInputImage[0].length - 1] = 1;
				}
			}
//			System.err.println("Vor trafo");
//			for (int i = 0; i < paddedInputImage.length; i++) {
//				for (int j = 0; j < paddedInputImage[0].length; j++) {
//					System.out.print(paddedInputImage[i][j] == 1 ? '#' : '.');
//				}
//				System.out.println();
//			}
			int[][] outputImage = new int[inputImage.length + padding * 2][inputImage[0].length + padding * 2];
			for (int i = 0; i < outputImage.length; i++) {
				for (int j = 0; j < outputImage[0].length; j++) {
					Integer parsedInteger = getParsedInteger(paddedInputImage, i, j, odd);
					outputImage[i][j] = input.charAt(parsedInteger) == '#' ? 1 : 0;
				}
			}

			return outputImage;
		}

		private Integer getParsedInteger(int[][] paddedInputImage, int iCoordinate, int jCoordinate, boolean odd) {
			String binaryString = "";
			for (int a = -1; a <= 1; a++) {
				for (int b = -1; b <= 1; b++) {
					if (iCoordinate + a >= 0 && jCoordinate + b >= 0 && iCoordinate + a < paddedInputImage.length
							&& jCoordinate + b < paddedInputImage[0].length) {
						binaryString += paddedInputImage[iCoordinate + a][jCoordinate + b];
					} else {
						binaryString += odd ? "0" : "1";
					}
				}
			}
			Integer parsedNumber = Integer.parseInt(binaryString, 2);
			return parsedNumber;
		}

	}

}
