package day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day22 {
	public static final String INPUT_PATH = "C:\\Users\\00010550\\Documents\\GitHub\\AoC21\\src\\main\\resources\\day22\\input.txt";

	static String input;

	public static void main(String[] args) throws IOException {
		new Day22().new SolverA().solve();
		new Day22().new SolverB().solve();
	}

	public class SolverA {

		public void solve() throws IOException {
			List<String> lines = Files.lines(Paths.get(INPUT_PATH)).collect(Collectors.toList());
			int offset = 50;
			int[][][] reactorField = new int[101][101][101];
			for (int n = 0; n < lines.size(); n++) {
				String line = lines.get(n);
				Pattern pattern = Pattern
						.compile("(on|off) x=(-?\\d*)\\.\\.(-?\\d*),y=(-?\\d*)\\.\\.(-?\\d*),z=(-?\\d*)\\.\\.(-?\\d*)");
				Matcher matcher = pattern.matcher(line);
				matcher.matches();
//				System.out.println(matcher.group(2));
				int xLeft = Integer.parseInt(matcher.group(2));
				int xRight = Integer.parseInt(matcher.group(3));
				int yLeft = Integer.parseInt(matcher.group(4));
				int yRight = Integer.parseInt(matcher.group(5));
				int zLeft = Integer.parseInt(matcher.group(6));
				int zRight = Integer.parseInt(matcher.group(7));
				if (xLeft >= -50 && xRight <= 50 && yLeft >= -50 && yRight <= 50 && zLeft >= -50 && zRight <= 50) {
					for (int i = xLeft + offset; i <= xRight + offset; i++) {
						for (int j = yLeft + offset; j <= yRight + offset; j++) {
							for (int k = zLeft + offset; k <= zRight + offset; k++) {
								reactorField[i][j][k] = matcher.group(1).equals("on") ? 1 : 0;
							}
						}
					}
				}
			}

			int number = 0;
			for (int i = 0; i < reactorField.length; i++) {
				for (int j = 0; j < reactorField[0].length; j++) {
					for (int k = 0; k < reactorField.length; k++) {
						number += reactorField[i][j][k];
					}
				}
			}
			System.out.println(number);
		}

	}

	public class SolverB {

		public void solve() throws IOException {

			List<String> lines = Files.lines(Paths.get(INPUT_PATH)).collect(Collectors.toList());

			// list of disjoint cubes that are turned on
			List<Cube> onCubes = new ArrayList<>();
			for (int n = 0; n < lines.size(); n++) {
				String line = lines.get(n);
				Pattern pattern = Pattern
						.compile("(on|off) x=(-?\\d*)\\.\\.(-?\\d*),y=(-?\\d*)\\.\\.(-?\\d*),z=(-?\\d*)\\.\\.(-?\\d*)");
				Matcher matcher = pattern.matcher(line);
				matcher.matches();

				Long xLeft = Long.parseLong(matcher.group(2));
				Long xRight = Long.parseLong(matcher.group(3));
				Long yLeft = Long.parseLong(matcher.group(4));
				Long yRight = Long.parseLong(matcher.group(5));
				Long zLeft = Long.parseLong(matcher.group(6));
				Long zRight = Long.parseLong(matcher.group(7));

				Cube newCube = new Cube(new Edge(xLeft, xRight), new Edge(yLeft, yRight), new Edge(zLeft, zRight));

				// create a copy of the list that we can modify
				List<Cube> onCubesAfterSplit = new ArrayList<Cube>();
				onCubesAfterSplit.addAll(onCubes);

				Iterator<Cube> existingCubeIterator = onCubes.iterator();
				while (existingCubeIterator.hasNext()) {
					Cube existingCube = existingCubeIterator.next();

					if (newCube.intersects(existingCube)) {
						// the existing cube is replaced by its subdivions except for the intersection
						// with the newCube which, later, is either added to the list (if the new cube is an on
						// cube) or not (if the new cube is an off cube)
						onCubesAfterSplit.addAll(existingCube.split(newCube));
						onCubesAfterSplit.remove(existingCube);
					}
				}
				onCubes = onCubesAfterSplit;
				if (matcher.group(1).equals("on")) {
					System.out.println("Turned on " + newCube);
					onCubes.add(newCube);
				} else {
					System.out.println("Turned off " + newCube);
				}
				Long number = 0L;
				for (Cube cube : onCubes) {
					number += cube.getVolume();
				}
				System.out.println(number);
			}

			Long number = 0L;
			for (Cube cube : onCubes) {
				number += cube.getVolume();
			}
			System.out.println(number);
		}

	}

	public class Edge {
		Long start;
		Long end;

		public boolean intersects(Edge otherEdge) {
			if (otherEdge.start >= this.start) {
				return otherEdge.start <= this.end;
			} else {
				return otherEdge.end >= this.start;
			}
		}

		public Edge getIntersection(Edge otherEdge) {
			Long intersectionStart = Math.max(this.start, otherEdge.start);
			Long intersectionEnd = Math.min(this.end, otherEdge.end);
			return new Edge(intersectionStart, intersectionEnd);
		}

		public Edge(Long start, Long end) {
			super();
			this.start = start;
			this.end = end;
		}

		public boolean isValid() {
			return start <= end;
		}

		public Long getLength() {
			return (end - start) + 1;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Edge) {
				return ((Edge) o).start.equals(this.start) && ((Edge) o).end.equals(this.end);
			}
			return super.equals(o);
		}

		@Override
		public String toString() {
			return "[" + start + "," + end + "]";
		}
	}

	public class Cube {
		Edge xEdge;
		Edge yEdge;
		Edge zEdge;

		public Long getVolume() {
			return xEdge.getLength() * yEdge.getLength() * zEdge.getLength();
		}

		public Cube(Edge xEdge, Edge yEdge, Edge zEdge) {
			super();
			this.xEdge = xEdge;
			this.yEdge = yEdge;
			this.zEdge = zEdge;
		}

		public boolean intersects(Cube otherCube) {
			return xEdge.intersects(otherCube.xEdge) && yEdge.intersects(otherCube.yEdge)
					&& zEdge.intersects(otherCube.zEdge);
		}

		public Cube getIntersection(Cube otherCube) {
			return new Cube(xEdge.getIntersection(otherCube.xEdge), yEdge.getIntersection(otherCube.yEdge),
					zEdge.getIntersection(otherCube.zEdge));
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Cube) {
				return this.xEdge.equals(((Cube) o).xEdge) && this.yEdge.equals(((Cube) o).yEdge)
						&& this.zEdge.equals(((Cube) o).zEdge);
			}
			return super.equals(o);
		}

		/**
		 * Creates a subdivison of this cube based on the intersection with another
		 * cube. Returns all fragments except for the intersection.
		 */
		public List<Cube> split(Cube otherCube) {
			List<Cube> cubeFragments = new ArrayList<>();
			List<Edge> xEdges = new ArrayList<>();
			xEdges.add(new Edge(this.xEdge.start, otherCube.xEdge.getIntersection(this.xEdge).start - 1));
			xEdges.add(otherCube.xEdge.getIntersection(this.xEdge));
			xEdges.add(new Edge(otherCube.xEdge.getIntersection(this.xEdge).end + 1, this.xEdge.end));

			List<Edge> yEdges = new ArrayList<>();
			yEdges.add(new Edge(this.yEdge.start, otherCube.yEdge.getIntersection(this.yEdge).start - 1));
			yEdges.add(otherCube.yEdge.getIntersection(this.yEdge));
			yEdges.add(new Edge(otherCube.yEdge.getIntersection(this.yEdge).end + 1, this.yEdge.end));

			List<Edge> zEdges = new ArrayList<>();
			zEdges.add(new Edge(this.zEdge.start, otherCube.zEdge.getIntersection(this.zEdge).start - 1));
			zEdges.add(otherCube.zEdge.getIntersection(this.zEdge));
			zEdges.add(new Edge(otherCube.zEdge.getIntersection(this.zEdge).end + 1, this.zEdge.end));

			for (Edge xEdge : xEdges) {
				if (xEdge.isValid()) {
					for (Edge yEdge : yEdges) {
						if (yEdge.isValid()) {
							for (Edge zEdge : zEdges) {
								if (zEdge.isValid()) {
									Cube cubeFragment = new Cube(xEdge, yEdge, zEdge);
									if (!cubeFragment.equals(this.getIntersection(otherCube))) {
										cubeFragments.add(cubeFragment);
									}
								}
							}
						}
					}
				}
			}
			return cubeFragments;
		}

		@Override
		public String toString() {
			return "x:" + xEdge.toString() + ", y:" + yEdge.toString() + ", z:" + zEdge.toString();
		}
	}

}
