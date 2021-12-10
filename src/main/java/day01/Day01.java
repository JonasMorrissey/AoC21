package day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day01 {

	public static void main(String[] args) throws IOException {
		List<String> lines = Files
				.lines(Paths
						.get("C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day01\\input.txt"))
				.collect(Collectors.toList());
		Integer value = null;
		Integer oldValue = null;
		int increases = 0;
		for (String line : lines) {
			value = Integer.parseInt(line);
			if (oldValue != null ) {
				if (value > oldValue) {
					increases += 1;
				}
			}
			oldValue = value;
		}
		System.out.println(increases);
		Integer slidingValue = null;
		Integer oldSlidingValue = null;
		int slidingIncreases = 0;
		for (int i = 0; i < lines.size()-2;i++) {
			slidingValue = Integer.parseInt(lines.get(i)) + Integer.parseInt(lines.get(i+1)) + Integer.parseInt(lines.get(i+2));
			if (oldSlidingValue != null ) {
				if (slidingValue > oldSlidingValue) {
					slidingIncreases += 1;
				}
			}
			oldSlidingValue = slidingValue;
		}
		System.out.println(slidingIncreases);
	}

}
