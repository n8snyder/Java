public class Connector {
	
	// Implement an immutable connector that connects two points on the game board.
	// Invariant: 1 <= myPoint1 < myPoint2 <= 6.

	private int myPoint1, myPoint2;
	
	public Connector (int p1, int p2) {
		if (p1 < p2) {
			myPoint1 = p1;
			myPoint2 = p2;
		} else {
			myPoint1 = p2;
			myPoint2 = p1;
		}
	}
	
	public int endPt1 ( ) {
		return myPoint1;
	}
	
	public int endPt2 ( ) {
		return myPoint2;
	}
	
	public boolean equals (Object obj) {
		Connector e = (Connector) obj;
		return (e.myPoint1 == myPoint1 && e.myPoint2 == myPoint2);
	}
	
	public String toString ( ) {
		return "" + myPoint1 + myPoint2;
	}
	
	// Format of a connector is endPoint1 + endPoint2 (+ means string concatenation),
	// possibly surrounded by white space. Each endpoint is a digit between
	// 1 and 6, inclusive; moreover, the endpoints aren't identical.
	// If the contents of the given string is correctly formatted,
	// return the corresponding connector.  Otherwise, throw IllegalFormatException.
	public static Connector toConnector (String s) throws IllegalFormatException {
		// You fill this in.
		String numbers = "";
		for (int k = 0; k < s.length (); k++) {
			if (Character.isWhitespace(s.charAt(k))) {
				continue;
			}
			if (s.charAt(k) == '1' || s.charAt(k) == '2' || 
					s.charAt(k) == '3' || s.charAt(k) == '4' || 
					s.charAt(k) == '5' || s.charAt(k) == '6') { 
				numbers = numbers + s.charAt(k);
				if (numbers.length() > 2) {
					// ERROR
					System.out.println("Yo mama so fat, her string length goes on for daaays.");
				}
				if (k == s.length() - 1) {
					continue;
				}
				if (numbers.length() == 2) {
					if(Character.isWhitespace(s.charAt(k+1))) {
						continue;
					}
					else {
						// ERROR
						System.out.println(numbers);
						System.out.println("Yo mama so fat, she takes more space than just two characters' worth.");
					}
				}
				if (s.charAt(k+1) != '1' && s.charAt(k+1) != '2' && 
						s.charAt(k+1) != '3' && s.charAt(k+1) != '4' && 
						s.charAt(k+1) != '5' && s.charAt(k+1) != '6') {
					// ERROR
					System.out.println("Yo mama so dumb, she can't remember the first 6 numbers.");
				}
			}
			else {
				// ERROR
				System.out.println("Yo mama so dumb, she can't even start a string with a number.");
			}	
		}
		if (numbers.length() != 2) {
			// ERROR
			Connector result = new Connector (0, 0);
			return result;
		}
		if(numbers.charAt(0) == numbers.charAt(1)) {
			// ERROR
			System.out.println("Yo mama so drunk, she's seeing double.");
		}
		Connector result = new Connector (Character.getNumericValue(numbers.charAt(0)),
				Character.getNumericValue(numbers.charAt(1)));
		return result;
		
	}
}