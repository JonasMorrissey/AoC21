package day06;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import day06.Day06.Fish;

public class Day06 {

	public class Fish {
		int state;

		public Fish() {
			state = 8;
		}
		public Fish(int state ) {
			this.state = state;
		}

		public Fish update() {

			if (state == 0) {
				state = 6;
				return new Fish();
			} else {
				state -= 1;
				return null;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		teilA();
		teilB();
	}

	private static void teilB() throws IOException {
		List<String> lines = Files.lines(Paths
				.get("C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day06\\input.txt"))
				.collect(Collectors.toList());
		
		Map<Integer,BigInteger> fishDic = new HashMap<>();
		
		for (String fishState : lines.get(0).split(",")) {
			if(fishDic.containsKey(Integer.parseInt(fishState))) {
				fishDic.put(Integer.parseInt(fishState), fishDic.get(Integer.parseInt(fishState)).add(new BigInteger("1")));
			}
			else {
				fishDic.put(Integer.parseInt(fishState), new BigInteger("1"));
			}
		}
		for (int i = 0; i<256; i++) {
			Map<Integer,BigInteger> fishDicAfterUpdate = new HashMap<>();
			for (int state = 8; state>=0; state--) {
				BigInteger numberOfFishInState = fishDic.get(state);
				if (state == 0 && numberOfFishInState!=null) {
					fishDicAfterUpdate.put(8, numberOfFishInState);
					BigInteger fishInStateSix = fishDicAfterUpdate.get(6) != null? fishDicAfterUpdate.get(6) : new BigInteger("0");
					fishDicAfterUpdate.put(6, fishInStateSix.add(numberOfFishInState));
				}
				else {
					fishDicAfterUpdate.put(state-1, numberOfFishInState);
				}
			}
			fishDic = fishDicAfterUpdate;
		}
		BigInteger totalCount = new BigInteger("0");
		for (Entry<Integer, BigInteger> entry : fishDic.entrySet()) {
			totalCount = totalCount.add(entry.getValue());
		}
		System.out.println(totalCount);
	}

	private static void teilA() throws IOException {
		List<String> lines = Files.lines(Paths
				.get("C:\\Users\\00010550\\Documents\\AoC\\workspace\\aoc2021\\src\\main\\resources\\day06\\input.txt"))
				.collect(Collectors.toList());
		
		List<Fish> initialState = new ArrayList<>();
		for (String fishState : lines.get(0).split(",")) {
			initialState.add(new Day06().new Fish(Integer.parseInt(fishState)));
		}
		
		for (int i = 0; i<80; i++) {
			List<Fish> newFishList = new ArrayList<>();
			for (Fish fish : initialState) {
				Fish newFish = fish.update();
				if(newFish != null) {newFishList.add(newFish);}
			}
			initialState.addAll(newFishList);
		}
		System.out.println(initialState.size());
	}

}
