package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 {

	static Map<Integer,Set<String>> pathsToEnd = new HashMap<>();
	static Set<Cave> caves = new HashSet<>();

	public static void main(String[] args) throws IOException {
		Solver solver = new Day12().new Solver();
		solver.teilA();
	}
	
	public class Cave {
		Set<Cave> neighbours;
		String name;
		
		public Cave(String name) {
			this.name = name;
			this.neighbours = new HashSet<>();
		}
		
		public void addNeighbour(Cave newNeighbour) {
			neighbours.add(newNeighbour);
		}
		
		/**
		 * Get a list of possible paths from this cave to the cave with name "end"
		 * @param forbiddenCaves caves that must not occur in the path
		 * @param visitOnceMore small caves (with lowercase names) that have been visited exactly once "in the past" (higher up in the recursion). 
		 * @param visitedTwice boolean flag indicating whether or not any small cave is already occuring twice. If this is true then no other caves may occur twice.
		 */
		public Set<String> getPathsToEnd(Set<Cave> forbiddenCaves, Set<Cave> visitOnceMore, boolean visitedTwice) {
//			System.out.println("Visiting " + this + " but excluding " + forbiddenCaves + " visiting " + visitOnceMore + " again.");
			
			// creating a hash of the input parameters and the cave itself (this)
			Integer[] hashedInputs = new Integer[] {this.hashCode(), forbiddenCaves.hashCode(), visitOnceMore.hashCode(), visitedTwice ? 1 : 0};
			int hash = Arrays.hashCode(hashedInputs);
			
			boolean doubleVisit = visitedTwice;
			
			// need to create copies because the original sets are still used in the previous recursion steps and must not be changed 
			Set<Cave> forbiddenCavesDownstream = new HashSet<>();
			Set<Cave> visitOnceMoreDownstream = new HashSet<>();
			visitOnceMoreDownstream.addAll(visitOnceMore);
			forbiddenCavesDownstream.addAll(forbiddenCaves);
			
			// special rules for small caves
			if (!this.name.equals(this.name.toUpperCase())) {
				// all small caves except for 'start' and 'end' may occur twice, but only if another cave hasn't already been visited twice
				if (!name.equals("start") && !name.equals("end") && !visitedTwice) {
					//Is this the second visit?
					if (visitOnceMoreDownstream.contains(this)) {
						doubleVisit = true;
						forbiddenCavesDownstream.add(this);
						// now all other small caves that have already been visited are forbidden, too
						forbiddenCavesDownstream.addAll(visitOnceMoreDownstream);
					}
					else {
						visitOnceMoreDownstream.add(this);
					}
				}
				else {
					forbiddenCavesDownstream.add(this);
				}
			}
			// get cached result if it has already been computed
			if (pathsToEnd.get(hash)!=null) {
//				System.out.println("Cache hit");
//				System.out.println(hash);
				return pathsToEnd.get(hash);
			}
			else {
				// these are the paths from here to the end
				Set<String> paths = new HashSet<>();
				if(name.equals("end")) {
					paths.add("end");
				}
				// 
				for (Cave neighbour : neighbours) {
					if (!forbiddenCavesDownstream.contains(neighbour)) {
						paths.addAll(neighbour.getPathsToEnd(forbiddenCavesDownstream, visitOnceMoreDownstream, doubleVisit)	// paths from neighbour to the end
								.stream().map(path -> this.name+","+path).collect(Collectors.toList()));  						// this cave is prepended to each path
					}
				}
				pathsToEnd.put(hash, paths);
				return paths;
			}
		}
		
		@Override
		public boolean equals(Object o) {
			return (o instanceof Cave)? false : ((Cave) o).name.equals(this.name);
		}
		@Override
		public int hashCode() {
			return this.name.hashCode();
		}
		@Override
		public String toString() {
			return "Cave: " + this.name + (neighbours.isEmpty()? "" : ", neighbours: " + neighbours.stream().map(cave -> cave.name).reduce((a,b) -> a + ", " + b).get());
		}
	}

	public class Solver {
		public void teilA() throws IOException {
			List<String> lines = Files
					.lines(Paths.get(
							"C:\\Users\\00010550\\Documents\\GitHub\\AoC21\\src\\main\\resources\\day12\\input.txt"))
					.collect(Collectors.toList());
			for (String line : lines) {
				String start = line.split("-")[0];
				String end = line.split("-")[1];
				Cave startCave = caves.stream().filter(cave -> cave.name.equals(start)).findFirst().orElse(new Cave(start));
				caves.add(startCave);
				Cave endCave = caves.stream().filter(cave -> cave.name.equals(end)).findFirst().orElse(new Cave(end));
				caves.add(endCave);
				startCave.addNeighbour(endCave);
				endCave.addNeighbour(startCave);
			}
			
			Set<Cave> startingCaveSet = new HashSet<>();
			Cave startingCave = caves.stream().filter(cave -> cave.name.equals("start")).findFirst().orElse(null);
			startingCaveSet.add(startingCave);
			
			Set<String> pathsToEnd = startingCave.getPathsToEnd(startingCaveSet, new HashSet<>(), false);
			System.out.println(pathsToEnd.size());
		}
	}

}
