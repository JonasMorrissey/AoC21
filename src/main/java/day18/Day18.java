package day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day18 {
	public static final String INPUT_PATH = "C:\\Users\\00010550\\Documents\\GitHub\\AoC21\\src\\main\\resources\\day18\\input.txt";
	
	public static void main(String[] args) throws IOException {
		new Day18().new Solver().solve();
	}

	public class Solver {
		
		public void solve() throws IOException {

			List<String> input = Files.lines(Paths.get(INPUT_PATH)).collect(Collectors.toList());

			String number = input.get(0);
			
			Pair pair = constructPairFromStringAtDepth(number, 0);
			
			for (int i = 1; i<input.size();i++) {
				pair = pair.add(constructPairFromStringAtDepth(input.get(i),0));
//				System.out.println(pair);
			}
			
			System.out.println(pair.getMagnitude());
			
			List<Integer> magnitudes = new ArrayList<>();
			for (int i = 0; i < input.size(); i++) {
				for (int j = 0; j<input.size(); j++) {
					if (i!=j) {
						magnitudes.add(constructPairFromStringAtDepth(input.get(i), 0).add(constructPairFromStringAtDepth(input.get(j), 0)).getMagnitude());
					}
				}
			}
			
			System.out.println(magnitudes.stream().mapToInt(x->x).max().getAsInt());			

		}

		/**
		 * Constructs a snailfish number at a certain depth from a given string, including all sub-numbers at correspondingly higher depths.

		 * @return the snailfish number represented as a {@link Pair}
		 */
		private Pair constructPairFromStringAtDepth(String number, int depth) {
			// remove the outermost brackets 
			number = number.replaceAll("^\\[", "").replaceAll("\\]$", "");
			Pair leftPair = null;
			Pair rightPair = null;
			// The sub-numbers can be constructed directly
			if (!number.contains("[")) {
				// This number is a regular number itself
				if (!number.contains(",")) {
					return new Pair(Integer.parseInt(number), depth);
				}
				// Both sub-numbers are regular numbers
				leftPair = new Pair(Integer.parseInt(number.split(",")[0]), depth + 1);
				rightPair = new Pair(Integer.parseInt(number.split(",")[1]), depth + 1);
			} else {
				int openParenthesesCounter = 0;
				String leftNumber = "";
				String rightNumber = "";
				boolean constructingRightPair = false;
				for (String character : number.split("")) {
					// in this case the left sub-pair has been parsed as a string and can be converted to a pair (recursively)
					if (character.equals(",") && openParenthesesCounter == 0) {
						// recurse, increasing the depth
						leftPair = constructPairFromStringAtDepth(leftNumber, depth + 1);
						// we have now moved on to the right pair
						constructingRightPair = true;
					} else {
						if (constructingRightPair) {
							rightNumber += character;
						} else {
							leftNumber += character;
						}
						if (character.equals("[")) {
							openParenthesesCounter += 1;
						}
						if (character.equals("]")) {
							openParenthesesCounter -= 1;
						}
					}
				}
				// we have reached the end of the string, the right pair can now be constructed
				rightPair = constructPairFromStringAtDepth(rightNumber, depth + 1);
			}
			return new Pair(leftPair, rightPair, depth);
		}

	}

	/**
	 * Class representing a number. Either left != null and right != null or value != null. 
	 * I.e. an object of this type represents either a pair of numbers or a regular number.
	 */
	public class Pair {
		
		Pair left;
		Pair right;
		Integer value;
		int depth;

		public Pair(Pair left, Pair right, int depth) {
			super();
			this.left = left;
			this.right = right;
			this.depth = depth;
		}

		public Pair(Integer value, int depth) {
			this.value = value;
			this.depth = depth;
		}
		// for debugging purposes only
		public String toString() {
			if (value != null) {
				return value.toString();
			} else {
				return "[" + left.toString() + "," + right.toString() + "]";
			}
		}

		/**
		 * Adds +1 to the depth of all sub-pairs of this pair object. 
		 */
		public void increaseDepth() {
			this.depth = this.depth + 1;
			if (left != null) {
				left.increaseDepth();
			}
			if (right != null) {
				right.increaseDepth();
			}
		}

		/**
		 * returns an identical copy of this pair. 
		 */
		public Pair copy() {
			if (value != null) {
				return new Pair(this.value, this.depth);
			} else {
				return new Pair(this.left.copy(), this.right.copy(), this.depth);
			}
		}

		/**
		 * returns the reduced sum of this pair and another one.
		 */
		public Pair add(Pair pair) {
			Pair sum = new Pair(this.copy(), pair.copy(), 0);
			sum.left.increaseDepth();
			sum.right.increaseDepth();
			sum.reduce();
			return sum;
		}

		public void reduce() {
			explode();
			while (split()) {
				explode();
			}
		}

		public void explode() {
			// repeat until no more explosions occur
			while (getExplosion() != null) {
				getExplosion();
			}
		}

		/**
		 * Splits the first offending regular number that is too high into a pair of smaller regular numbers.
		 * 
		 * @return true if a split has occured
		 */
		public boolean split() {
			if (this.value != null) {
				if (this.value >= 10) {
					Pair leftNumber = new Pair((int) Math.floor(this.value / 2.), this.depth + 1);
					Pair rightNumber = new Pair((int) Math.ceil(this.value / 2.), this.depth + 1);
					this.left = leftNumber;
					this.right = rightNumber;
					// this pair no longer represents a regular number hence its value must be null. 
					this.value = null;
					return true;
				} else {
					return false;
				}
			} else {
				// taking advantage of operator short-circuiting: as soon as one split has occured the return statement will be executed
				return left.split() || right.split();
			}
		}

		/**
		 * Returns null if no sub-number of this number has a depth of 5 or more. 
		 * Otherwise returns an {@link ExplosionContainer} containing two {@link Explosion} objects.
		 * One to propagate the value to the left, one to propagate to the right.
		 * 
		 * @return an {@link ExplosionContainer} 
		 */
		public ExplosionContainer getExplosion() {
			// In this case the currenct object needs to be exploded: it becomes the pair representing the regular number 0
			// further, its previously stored left and right sub-numbers (both guaranteed to be regular numbers) 
			// are propagated to the left and right respectively 
			if (left != null && left.depth == 5) {
				Integer leftValue = left.value;
				Integer rightValue = right.value;
				left = null;
				right = null;
				this.value = 0;
				return new ExplosionContainer(leftValue, rightValue);
			}
			// a single regular number never needs to be exploded
			if (value != null) {
				return null;
			}
			
			// check left sub-number first
			ExplosionContainer explosion = left.getExplosion();
			// if the left sub-pair of this number was exploded we try to propagate the corresponding value to the right sub-pair
			if (explosion != null) {
				Explosion propagatedExplosion = explosion.explosionToTheRight;
				right.explode(propagatedExplosion);
				// The explosion is passed on higher along the call stack.
				return explosion;
			}
			explosion = right.getExplosion();
			// Conversely, if the left sub-pair of this number was exploded we try to propagate the corresponding value to the left sub-pair
			if (explosion != null) {
				Explosion propagatedExplosion = explosion.explosionToTheLeft;
				left.explode(propagatedExplosion);
				return explosion;
			}

			return explosion;
		}

		/**
		 * Increases the value of this number by the amount of an explosion that occured elsewhere, if appropriate 
		 * 
		 * @param toLeft specifies whether the explosion propagates to the left or to the right
		 * @return the explosion - if it has affected this pair it 
		 */
		public Explosion explode(Explosion explosion) {
			// This explosion has not yet affected any other number
			if (explosion.toHandle) {
				// regular numbers can directly be affected
				if (this.value != null) {
					value += explosion.value;
					// explosion has been handled and won't need to be handled again in the future
					explosion.toHandle = false;
					return explosion;
				} else {
					// the explosion propagates from right to left, hence we need to pass it to the right sub-number first
					// or vice-versa 
					if (explosion.propagatesToLeft) {
						return this.right.explode(explosion);
					} else {
						return this.left.explode(explosion);
					}
				}
			}
			return explosion;
		}

		public Integer getMagnitude() {
			if (value != null) {
				return value;
			}
			else {
				return 3*left.getMagnitude() + 2*right.getMagnitude();
			}
		}
		
	}

	public class Explosion {
		Boolean toHandle;
		Integer value;
		Boolean propagatesToLeft;

		public Explosion(Boolean toHandle, Integer value, Boolean propagatesToLeft) {
			super();
			this.toHandle = toHandle;
			this.value = value;
			this.propagatesToLeft = propagatesToLeft;
		}
	}

	public class ExplosionContainer {
		Explosion explosionToTheLeft;
		Explosion explosionToTheRight;

		public ExplosionContainer(Integer valueLeft, Integer valueRight) {
			super();
			this.explosionToTheLeft = new Explosion(true, valueLeft, true);
			this.explosionToTheRight = new Explosion(true, valueRight, false);
		}
	}
}
