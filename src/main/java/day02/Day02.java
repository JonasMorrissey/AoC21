package day02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day02 {

	public static void main(String[] args) throws IOException {
		List<String> lines = Files
				.lines(Paths
						.get("C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day02\\input.txt"))
				.collect(Collectors.toList());
		partA(lines);
		partB(lines);
	}

	private static void partB(List<String> lines) {
		int y = 0; 
		int z = 0;
		int aim = 0;
		
		for (String line : lines) {
			String[] commandArray = line.split(" ");
			if (commandArray[0].contains("up")) {
				aim -= Integer.parseInt(commandArray[1]);
			}
			else if (commandArray[0].contains("forward")) {
				y += Integer.parseInt(commandArray[1]);
				z -= Integer.parseInt(commandArray[1])*aim;
			}
			else if (commandArray[0].contains("down")) {
				aim += Integer.parseInt(commandArray[1]);
			}
		}
		System.out.println(y);
		System.out.println(z);
		System.out.println(-y*z);
	}

	
	private static void partA(List<String> lines) {
		int y = 0; 
		int z = 0;
		
		for (String line : lines) {
			String[] commandArray = line.split(" ");
			if (commandArray[0].contains("up")) {
				z += Integer.parseInt(commandArray[1]);
			}
			else if (commandArray[0].contains("forward")) {
				y += Integer.parseInt(commandArray[1]);
			}
			else if (commandArray[0].contains("down")) {
				z -= Integer.parseInt(commandArray[1]);
			}
		}
		System.out.println(y);
		System.out.println(z);
		System.out.println(-y*z);
	}

}
