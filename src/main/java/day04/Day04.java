package day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day04 {

	public class Board {
		private List<List<Integer>> field;
		
		Board(List<List<Integer>> field) {
			this.field = field;
		}
		
		public List<List<Integer>> getRowsAndColumns() {
			List<List<Integer>> rowsAndColumns = new ArrayList<>();
			rowsAndColumns.addAll(field);
			List<List<Integer>> columns = new ArrayList<>();
			for (int i = 0; i<field.size();i++) {
				List<Integer> ithRow = field.get(i);
				for (int j = 0; j < ithRow.size(); j++) {
					if (columns.size()<=j) {
						List<Integer> jthColumn = new ArrayList<>();
						columns.add(jthColumn);
					}
					columns.get(j).add(ithRow.get(j));
				}
			}
			rowsAndColumns.addAll(columns);
			return rowsAndColumns;
		}
		
		public String toString() {
			return field.toString();
		}
		
		public int getScore(List<Integer> numbersDrawn) {
			int winningIndex = getWinningIndex(numbersDrawn);
			List<Integer> previouslyDrawnNumbers = numbersDrawn.subList(0, winningIndex+1);
			int sumOfUnmarkedNumbers = 0;
			for (int i = 0; i < field.size(); i++) {
				List<Integer> ithRow = field.get(i);
				for (int j = 0; j< ithRow.size(); j++) {
					Integer positionij = ithRow.get(j);
					if (!previouslyDrawnNumbers.contains(positionij)) {
						sumOfUnmarkedNumbers+=positionij;
					}
				}
			}
			return sumOfUnmarkedNumbers*numbersDrawn.get(winningIndex);
					
		}

		public int getWinningIndex(List<Integer> numbersDrawn) {
			int winningIndex = numbersDrawn.size()+1;
//			System.out.println("Numbers drawn: " + numbersDrawn);
			for (List<Integer> rowOrColumn : getRowsAndColumns()) {
//				System.out.println(rowOrColumn);
				int winningIndexOfRow = -1;
				for (Integer entry : rowOrColumn) {
					if (!numbersDrawn.contains(entry)) {
//						System.out.println(entry + " is never drawn");
						winningIndexOfRow = numbersDrawn.size()+1;
						break;
					}
					else {
//						System.out.println(entry + " is drawn at position " + numbersDrawn.indexOf(entry));
						winningIndexOfRow = Math.max(winningIndexOfRow, numbersDrawn.indexOf(entry));
//						System.out.println("New winning index of row is " + winningIndexOfRow);
					}
				}
				if (winningIndexOfRow != -1) {
					winningIndex = Math.min(winningIndex, winningIndexOfRow);
//					System.out.println("New winning index of board  is " + winningIndex);
				}
			}
			return winningIndex;
		}
		
	}
	
	
	public static void main(String[] args) throws IOException {
		List<String> lines = Files
				.lines(Paths
						.get("C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day04\\input.txt"))
				.collect(Collectors.toList());
		List<Integer> numbersToDraw = Arrays.stream(lines.get(0).split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
		List<Board> listOfBoards = new ArrayList<>();
		List<List<Integer>> field = null;
		for (int i = 1; i<lines.size();i++) {
			String ithRow = lines.get(i);
			if (ithRow.trim().isEmpty()) {
				if (field != null) {
					listOfBoards.add(new Day04().new Board(field));
				}
				field = new ArrayList<>();
			}
			else {
				field.add(Arrays.stream(ithRow.split("\\s+")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
			}
		}
		if (field != null) {
			listOfBoards.add(new Day04().new Board(field));
		}

		int fastestBoardWin = numbersToDraw.size()+1;
		for (Board board : listOfBoards) {
			if (board.getWinningIndex(numbersToDraw) < fastestBoardWin) {
				fastestBoardWin = board.getWinningIndex(numbersToDraw);
				System.out.println("Winning board has score " + board.getScore(numbersToDraw));
			}
		}

		int slowestBoardWin = -1;
		for (Board board : listOfBoards) {
			if (board.getWinningIndex(numbersToDraw) > slowestBoardWin) {
				slowestBoardWin = board.getWinningIndex(numbersToDraw);
				System.out.println("Losing board has score " + board.getScore(numbersToDraw));
			}
		}

	}
	

}
