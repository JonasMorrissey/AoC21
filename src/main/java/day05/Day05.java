package day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day05 {

	public class StraightLine extends Line {
		public StraightLine(String xStart, String yStart, String xEnd, String yEnd) {
			super(xStart, yStart, xEnd, yEnd);
		}
		@Override
		public boolean isPointInLine(int xPoint, int yPoint) {
			if (isDiagonal()) {
				return false;
			}
			return super.isPointInLine(xPoint, yPoint);
		}
		public boolean isDiagonal() {
			int deltaX = (xEnd-xStart);
			int deltaY = (yEnd - yStart);
			return deltaX*deltaY!=0;
		}
	}
	public class Line {
		int xStart;
		int yStart;
		int xEnd;
		int yEnd;
		
		public Line(int xStart, int yStart, int xEnd, int yEnd) {
			this.xStart = xStart;
			this.yStart = yStart;
			this.xEnd = xEnd;
			this.yEnd = yEnd;
		}

		public Line(String xStart, String yStart, String xEnd, String yEnd) {
			this.xStart = Integer.parseInt(xStart);
			this.yStart = Integer.parseInt(yStart);
			this.xEnd = Integer.parseInt(xEnd);
			this.yEnd = Integer.parseInt(yEnd);
		}
		
		public boolean isPointInLine(int xPoint, int yPoint) {
			int deltaX = (xEnd-xStart);
			int deltaY = yEnd - yStart;
			int deltaXNorm = (int) deltaX/Math.max(Math.abs(deltaX), Math.abs(deltaY));
			int deltaYNorm = (int) deltaY/Math.max(Math.abs(deltaX), Math.abs(deltaY));
			
			int steps = deltaXNorm != 0? deltaX / deltaXNorm : deltaY / deltaYNorm ;
			
			for (int i = 0; i<=steps; i++) {
				if (xPoint == xStart + i*deltaXNorm && yPoint == yStart + i*deltaYNorm) {
					return true;
				}
			}
			return false;
		}
		
		public String toString() {
			return xStart +"," + yStart +" -> " + xEnd + "," + yEnd;
		}
	}
	
	public static void main(String[] args) throws IOException {
		List<String> lines = Files
				.lines(Paths
						.get("C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day05\\input.txt"))
				.collect(Collectors.toList());
		List<Line> linesToAvoid = new ArrayList<>();
		List<Line> linesToAvoidB = new ArrayList<>();
		for (String line : lines) {
			String[] coordinates = line.split("->");
			String[] startCoordinates = coordinates[0].trim().split(",");
			String[] endCoordinates = coordinates[1].trim().split(",");
			teilA(linesToAvoid, startCoordinates, endCoordinates);
			teilB(linesToAvoidB, startCoordinates, endCoordinates);
		}
		
		int numberOfOverlappingPointsA = 0;
		int numberOfOverlappingPointsB = 0;
		for (int i = 0; i<1200; i++) {
			for (int j =0; j<1200; j++) {
				int intersectingLines = 0;
				int intersectingLinesB = 0;

				for (Line line : linesToAvoid) {
					if (line.isPointInLine(i, j)) {
//						System.out.println("Point (" + i + "," + j + ") is contained in ");
//						System.out.println(line);
						intersectingLines += 1;
					}
					if (intersectingLines>=2) {
//						System.out.println("WATCH OUT");
						numberOfOverlappingPointsA += 1;
						break;
					}
				}
				for (Line line : linesToAvoidB) {
					if (line.isPointInLine(i, j)) {
//						System.out.println("Point (" + i + "," + j + ") is contained in ");
//						System.out.println(line);
						intersectingLinesB += 1;
					}
					if (intersectingLinesB>=2) {
//						System.out.println("WATCH OUT");
						numberOfOverlappingPointsB += 1;
						break;
					}
				}
			}
		}
		System.out.println(numberOfOverlappingPointsA);
		System.out.println(numberOfOverlappingPointsB);
		
	}

	private static void teilA(List<Line> linesToAvoid, String[] startCoordinates, String[] endCoordinates) {
		linesToAvoid.add(new Day05().new StraightLine(startCoordinates[0], startCoordinates[1], endCoordinates[0], endCoordinates[1]));
	}

	private static void teilB(List<Line> linesToAvoid, String[] startCoordinates, String[] endCoordinates) {
		linesToAvoid.add(new Day05().new Line(startCoordinates[0], startCoordinates[1], endCoordinates[0], endCoordinates[1]));
	}
}
