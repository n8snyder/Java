import java.awt.Color;
import java.util.*;

public class Board {
	private Color[][] moves;
	
	public static boolean iAmDebugging = true;
	
	// Initialize an empty board with no colored edges.
	public Board ( ) {
		// You fill this in.
		
		moves = new Color[][] {
				{Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
				{Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
				{Color.WHITE, Color.WHITE, Color.WHITE},
				{Color.WHITE, Color.WHITE},
				{Color.WHITE}
				};
		
	}
	
	// Add the given connector with the given color to the board.
	// Unchecked precondition: the given connector is not already chosen 
	// as RED or BLUE.
	public void add (Connector cnctr, Color c) {
		// You fill this in.
		moves[cnctr.endPt1()-1][cnctr.endPt2() - cnctr.endPt1() - 1] = c;
	}
	
	// Iterator class
	private class ConnectorIterator implements Iterator<Connector> {
		
		private int connIndex[] = new int[2];
		private boolean byColor = false;
		private Color myColor;
		public ConnectorIterator(){
			connIndex[0] = 1;
			connIndex[1] = 2;
		}
		public ConnectorIterator(Color c){
			myColor = c;
			for(int i=1; i<6; i++){
				for(int j=i+1; j<7; j++){
					if(myColor == moves[i - 1][j - i - 1]){
						connIndex[0]=i;
						connIndex[1]=j;
						byColor = true;
						break;
					}
				}
				if(byColor){
					break;
				}
			}
			byColor=true;
		}
		public boolean hasNext(){
			if(byColor){
				for(int k=connIndex[1]; k<7; k++){
					if(myColor == moves[connIndex[0]-1][k-connIndex[0]-1]){
						return true;
					}
				}
				for(int i=connIndex[0]+1; i<6; i++){
					for(int j=i+1; j<7; j++){
						if(myColor == moves[i - 1][j - i - 1]){
							return true;
						}
					}
				}
				return false;
			}
			else{
				if(connIndex[0]==6 && connIndex[1]==7){
					return false;
				}
				return true;
			}
		}
		public Connector next(){
			Connector result = new Connector(connIndex[0], connIndex[1]);
			if(byColor){
				for(int k=connIndex[1]+1; k<7; k++){
					if(myColor == moves[connIndex[0]-1][k-connIndex[0]-1]){
						connIndex[1] = k;
						return result;
					}
				}
				for(int i=connIndex[0]+1; i<6; i++){
					for(int j=i+1; j<7; j++){
						if(myColor == moves[i - 1][j - i - 1]){
							connIndex[0] = i;
							connIndex[1] = j;
							return result;
						}
					}
				}
				connIndex[1]++;
				return result;
			}	
			else{
				if(connIndex[1] == 6){
					connIndex[0]++;
					connIndex[1]=connIndex[0]+1;
				}
				else{
					connIndex[1]++;
				}
			}
			return result;
		}
		public void remove(){
		}
	}
	
	
	
	// Set up an iterator through the connectors of the given color, 
	// which is either RED, BLUE, or WHITE. 
	// If the color is WHITE, iterate through the uncolored connectors.
	// No connector should appear twice in the iteration.  
	public Iterator<Connector> connectors (Color c) {
		// You fill this in.
		return new ConnectorIterator(c);
	}
	
	// Set up an iterator through all the 15 connectors.
	// No connector should appear twice in the iteration.  
	public Iterator<Connector> connectors ( ) {
		// You fill this in.
		return new ConnectorIterator();
	}
	
	// Return the color of the given connector.
	// If the connector is colored, its color will be RED or BLUE;
	// otherwise, its color is WHITE.
	public Color colorOf (Connector e) {
		// You fill this in.
		return moves[e.endPt1()-1][e.endPt2() - e.endPt1() - 1];
	}
	
	// Unchecked prerequisite: cnctr is an initialized uncolored connector.
	// Let its endpoints be p1 and p2.
	// Return true exactly when there is a point p3 such that p1 is adjacent
	// to p3 and p2 is adjacent to p3 and those connectors have color c.
	public boolean formsTriangle (Connector cnctr, Color c) {
		// You fill this in.
		for(int i = 1; i < 7; i++){
			if(cnctr.endPt1() != i && cnctr.endPt2() != i){
				if(i<cnctr.endPt1()){
					if(c == moves[i-1][cnctr.endPt1()-i-1]){
						if(c == moves[i-1][cnctr.endPt2()-i-1]){
							return true;
						}
					}
				}
				else {
					if(i>cnctr.endPt2()){
						if(c == moves[cnctr.endPt2()-1][i - cnctr.endPt2() - 1]){
							if(c == moves[cnctr.endPt1()-1][i - cnctr.endPt1() - 1]){
								return true;
							}
						}
					}
					else{
						if(c == moves[cnctr.endPt1() - 1][i - cnctr.endPt1() - 1]){
							if(c == moves[i - 1][cnctr.endPt2() - i -1]){
								return true;
							}
						}
					}	
				}
			}
		}
		return false;
	}
	
	// The computer (playing BLUE) wants a move to make.
	// The board is assumed to contain an uncolored connector, with no 
	// monochromatic triangles.
	// There must be an uncolored connector, since if all 15 connectors are colored,
	// there must be a monochromatic triangle.
	// Pick the first uncolored connector that doesn't form a BLUE triangle.
	// If each uncolored connector, colored BLUE, would form a BLUE triangle,
	// return any uncolored connector.
	public Connector choice ( ) {
		// You fill this in.
		/* if available connector, if formsTriangle blue, continue
		 * if available connector, if not formsTriangle blue,...
		 * if no available connector (else), ERROR
		 */
		Iterator<Connector> iterWhite = this.connectors (Color.WHITE);
		Connector current, otherOne;
		ArrayList<Connector> good = new ArrayList<Connector>();
		ArrayList<Connector> losingForRed = new ArrayList<Connector>();
		ArrayList<Connector> nonLosingForBoth = new ArrayList<Connector>();
		ArrayList<Connector> lose = new ArrayList<Connector>();
		while(iterWhite.hasNext()){
			current = iterWhite.next();
			Iterator<Connector> iter = this.connectors();
			boolean hasUnusedPoints = true;
			while(iter.hasNext()){
				otherOne = iter.next();
				if(otherOne.endPt1() == current.endPt1() || otherOne.endPt1() == current.endPt2() ||
						otherOne.endPt2() == current.endPt1() || otherOne.endPt2() == current.endPt2()){
					if(this.colorOf(otherOne) != Color.WHITE){
						hasUnusedPoints = false;
					}
					else{
						continue;
					}
				}
			}
			if(hasUnusedPoints){
				return current;
			}
			
			
			
			for(int i = 1; i < 7; i++){
				if(current.endPt1() != i && current.endPt2() != i){
					if(i<current.endPt1()){
						if(Color.WHITE != moves[i-1][current.endPt1()-i-1]){
							if(Color.WHITE != moves[i-1][current.endPt2()-i-1]){
								if(moves[i-1][current.endPt1()-i-1] != moves[i-1][current.endPt2()-i-1]){
									if(!this.formsTriangle(current,Color.BLUE) && !this.formsTriangle(current,Color.RED)){
										good.add(current);
									}
								}
							}
						}
					}
					else {
						if(i>current.endPt2()){
							if(Color.WHITE != moves[current.endPt2()-1][i - current.endPt2() - 1]){
								if(Color.WHITE != moves[current.endPt1()-1][i - current.endPt1() - 1]){
									if(moves[current.endPt2()-1][i - current.endPt2() - 1] != moves[current.endPt1()-1][i - current.endPt1() - 1]){
										if(!this.formsTriangle(current,Color.BLUE) && !this.formsTriangle(current,Color.RED)){
											good.add(current);
										}
									}
								}
							}
						}
						else{
							if(Color.WHITE != moves[current.endPt1() - 1][i - current.endPt1() - 1]){
								if(Color.WHITE != moves[i - 1][current.endPt2() - i -1]){
									if(moves[current.endPt1() - 1][i - current.endPt1() - 1] != moves[i - 1][current.endPt2() - i -1]){
										if(!this.formsTriangle(current,Color.BLUE) && !this.formsTriangle(current,Color.RED)){
											good.add(current);
										}
									}
								}
							}
						}	
					}
				}
			}
			
			
			if(!this.formsTriangle(current,Color.BLUE) && !this.formsTriangle(current,Color.RED)){
				nonLosingForBoth.add(current);
			}
			if(this.formsTriangle(current,Color.RED)){
				losingForRed.add(current);
			}
			if(this.formsTriangle(current,Color.BLUE)){
				lose.add(current);
			}
		}
		if(good.size() != 0){
			return good.get(0);
		}
		if(nonLosingForBoth.size() != 0){
			return nonLosingForBoth.get(0);
		}
		if(losingForRed.size()!=0){
			return losingForRed.get(0);
		}
		return lose.get(0);
	}

	// Return true if the instance variables have correct and internally
	// consistent values.  Return false otherwise.
	// Unchecked prerequisites:
	//	Each connector in the board is properly initialized so that 
	// 	1 <= myPoint1 < myPoint2 <= 6.
	public boolean isOK ( ) {
		// You fill this in.
		int countRed=0, countBlue=0;;
		Connector current;
		Iterator<Connector> iter = this.connectors();
		while(iter.hasNext()){
			current = iter.next();
			if(this.colorOf(current) != Color.RED && this.colorOf(current) != Color.BLUE && 
					this.colorOf(current) != Color.WHITE){
				return false;
			}
			if(this.colorOf(current) == (Color.BLUE)){
				if(this.formsTriangle(current,Color.BLUE)){
					return false;
				}
				countBlue++;
			}
			if(this.colorOf(current) == (Color.RED)){
				if(this.formsTriangle(current,Color.RED)){
					return false;
				}
				countRed++;
			}
		}
		if(countRed -1 > countBlue || countBlue > countRed){
			return false;
		}
		
		
		return true;
	}
}
