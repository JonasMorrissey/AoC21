package day17;

public class Day17 {

	public static void main(String[] args)  {
		int counter = 0;
		for (int x = 10; x < 95; x++) {
			for (int y = -180; y < 180; y++) {
				if(landsInTargetArea(x, y, 60, 94, -136, -171)) {
					counter+=1;
				}
			}
		}
		System.err.println(counter);
	}
	
	public static boolean landsInTargetArea(int xStart, int yStart, int xTargetLeft, int xTargetRight, int yTargetTop, int yTargetBottom) {
		int posX = 0;
		int posY = 0;
		boolean update = true;
		while( update ) {
			posX += xStart;
			posY += yStart;
			if (xStart>0) {
				xStart-=1;
			}
			yStart-=1;
			if ( posX > xTargetRight ) {
				return false;
			}
			if (posY < yTargetBottom) {
				return false;
			}
			if (posX >= xTargetLeft && posX <= xTargetRight && posY >= yTargetBottom && posY<=yTargetTop) {
				return true;
			}
		}
		return false;
	}
}
