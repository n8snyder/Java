import java.util.*;

public class Proof {
	TheoremSet myTheorems;
	
	private static ProofLineNode myRoot;
	public static ProofLineNode currentParent; 

	public Proof (TheoremSet theorems) {
		myRoot = new ProofLineNode (null);
		currentParent = myRoot;
	}
	

	public LineNumber nextLineNumber ( ) {	
		return currentParent.insertChild(null).myLineNumber;
	}

	public void extendProof (String x) throws IllegalLineException, IllegalInferenceException {
		Scanner lineScanner = new Scanner (x);
		String firstWord = lineScanner.next();
		if (firstWord.equals("show")) {
			showHelper(lineScanner);
		}
		if (firstWord.equals("assume")) {
			assumeHelper(lineScanner);
		}
		if (firstWord.equals("mp")) {
			mpHelper(lineScanner);
		}
		if (firstWord.equals("mt")) {
			mtHelper(lineScanner);
		}
		if (firstWord.equals("co")) {
			coHelper(lineScanner);
		}
		if (firstWord.equals("ic")) {
			icHelper(lineScanner);
		}
		if (firstWord.equals("repeat")) {
			repeatHelper(lineScanner);
		}
		if (firstWord.equals("print")) {
			System.out.print(this.toString());
		}
		//if its a theorem
		else {
			Scanner s = new Scanner(x);
			checkTheorems(s);
		}
	}
	
	private void showHelper (Scanner lineScanner) throws IllegalLineException {
		ProofLineNode finder = currentParent.myLeftChild;
		Expression showExpr = new Expression (lineScanner.next());
		if (finder.myNextSibling == null) {
			//throw exception: two "show" statements in a row
		}
		else {
			while (finder.myNextSibling != null) {
				finder = finder.myNextSibling;
			}
			finder.myExprTree = showExpr.myExpression;
			currentParent = finder;
		}
	}
	
	private void assumeHelper (Scanner lineScanner) throws IllegalLineException  {
		//check that its the only sibling
		if (currentParent.myLeftChild.myNextSibling != null) {
			//throw some exception
		}
		Expression assumeExpr = new Expression (lineScanner.next());
		if (currentParent.myExprTree.myRoot.myLeft.equals(assumeExpr.myExpression.myRoot)) {
			currentParent.myLeftChild.myExprTree = assumeExpr.myExpression;
		}
		else if (assumeExpr.myExpression.myRoot.myLeft.equals(currentParent.myExprTree)){
			if (assumeExpr.myExpression.myRoot.myItem.equals("~")) {
				currentParent.myLeftChild.myExprTree = assumeExpr.myExpression;
			}
			else {
				// throw some exception
			}
		}
		else {
			//throw some illegal format exception or something
		}
	}
	
	private void mpHelper (Scanner lineScanner) throws IllegalLineException {
		String lineRef1 = lineScanner.next();
		String lineRef2 = lineScanner.next();
		ProofLineNode nodeRef1 = findNode(lineRef1);
		ProofLineNode nodeRef2 = findNode(lineRef2);
		Expression expr1 = new Expression(nodeRef1.myExprTree.toString());
		Expression expr2 = new Expression(nodeRef2.myExprTree.toString());
		Expression mpExpr = new Expression (lineScanner.next());
		ProofLineNode finder = currentParent.myLeftChild;
		while (finder.myNextSibling != null) {
			finder = finder.myNextSibling;
		}	
		if (!validateLineReference(lineRef1) || !validateLineReference(lineRef2)) {
			//throw new IllegalLineException("reference invalid");
		}
		else {
			if (expr1.myExpression.myRoot.equals(expr2.myExpression.myRoot.myLeft) 
				|| expr2.myExpression.myRoot.equals(expr1.myExpression.myRoot.myLeft) ) {
				finder.myExprTree = mpExpr.myExpression;
			}
		}
	}
	
	private void mtHelper (Scanner lineScanner) throws IllegalLineException {
		String lineRef1 = lineScanner.next();
		String lineRef2 = lineScanner.next();
		ProofLineNode nodeRef1 = findNode(lineRef1);
		ProofLineNode nodeRef2 = findNode(lineRef2);
		Expression expr1 = new Expression(nodeRef1.myExprTree.toString());
		Expression expr2 = new Expression(nodeRef2.myExprTree.toString());
		Expression mtExpr = new Expression (lineScanner.next());
		ProofLineNode finder = currentParent.myLeftChild;
		while (finder.myNextSibling != null) {
			finder = finder.myNextSibling;
		}	
		if (!validateLineReference(lineRef1) || !validateLineReference(lineRef2)) {
			//throw new IllegalLineException("reference invalid");
		}
		else {
			if (expr1.myExpression.myRoot.myLeft.equals(expr2.myExpression.myRoot.myRight) 
				|| expr2.myExpression.myRoot.myRight.equals(expr2.myExpression.myRoot.myLeft)) {
				finder.myExprTree = mtExpr.myExpression;
			}
		}
	}
	
	private void coHelper (Scanner lineScanner) throws IllegalLineException {
		String lineRef1 = lineScanner.next();
		String lineRef2 = lineScanner.next();
		ProofLineNode nodeRef1 = findNode(lineRef1);
		ProofLineNode nodeRef2 = findNode(lineRef2);
		Expression expr1 = new Expression(nodeRef1.myExprTree.toString());
		Expression expr2 = new Expression(nodeRef2.myExprTree.toString());
		Expression coExpr = new Expression (lineScanner.next());
		ProofLineNode finder = currentParent.myLeftChild;
		while (finder.myNextSibling != null) {
			finder = finder.myNextSibling;
		}
		if (!validateLineReference(lineRef1) || !validateLineReference(lineRef2)) {
			//throw new IllegalLineException("reference invalid");
		}
		else {
			if (expr1.myExpression.myRoot.myItem.equals("~")) {
				if (!expr1.myExpression.myRoot.myLeft.equals(expr2.myExpression.myRoot)) {
					if (expr2.myExpression.myRoot.myItem.equals("~")) {
						if (!expr2.myExpression.myRoot.myLeft.equals(expr1.myExpression.myRoot)) {
							//throw some exception
						} 
						else {
							finder.myExprTree = coExpr.myExpression;
						}
					}
					else {
						//throw some exception
					}
					
				}
				else {
					finder.myExprTree = coExpr.myExpression;
				}
			}
			else if (expr2.myExpression.myRoot.myItem.equals("~")) {
				if (!expr2.myExpression.myRoot.myLeft.equals(expr1.myExpression.myRoot)) {
					//throw some exception
				}
				else {
					finder.myExprTree = coExpr.myExpression;
				}
			}
		}
	}
	
	private void icHelper (Scanner lineScanner) throws IllegalLineException {
		String lineRef = lineScanner.next();
		ProofLineNode nodeRef = findNode(lineRef);
		Expression expr = new Expression(nodeRef.myExprTree.toString());
		ProofLineNode finder = currentParent.myLeftChild;
		Expression icExpr = new Expression (lineScanner.next());
		while (finder.myNextSibling != null) {
			finder = finder.myNextSibling;
		}
		if (!validateLineReference(lineRef)) {
			//throw new IllegalLineException("reference invalid");
		}
		else {
			if (expr.myExpression.myRoot.equals(icExpr.myExpression.myRoot.myRight)) {
				finder.myExprTree = icExpr.myExpression;
			}
		}
	}
	
	private void repeatHelper (Scanner lineScanner) throws IllegalLineException {
		String lineRef = lineScanner.next();
		ProofLineNode nodeRef = findNode(lineRef);
		Expression expr = new Expression(nodeRef.myExprTree.toString());
		ProofLineNode finder = currentParent.myLeftChild;
		Expression repeatExpr = new Expression (lineScanner.next());
		while (finder.myNextSibling != null) {
			finder = finder.myNextSibling;
		}
		if (!validateLineReference(lineRef)) {
			//throw new IllegalLineException("reference invalid");
		}
		else {
			if (expr.myExpression.myRoot.equals(repeatExpr.myExpression.myRoot)) {
				finder.myExprTree = repeatExpr.myExpression;
			}
		}
	}

	public String toString ( ) {
		return "";
	}

	public boolean isComplete ( ) {
		return true;
	}
	
	//checks that a line reference is a valid reference that refers to a line
	//that can be accessed from the current line
	private static boolean validateLineReference (String x){
		LineNumber reference = new LineNumber (x); 
		ProofLineNode finder = currentParent.myLeftChild;
		while (finder.myNextSibling != null) {
			finder = finder.myNextSibling;
		}
		LineNumber current = finder.myLineNumber;  
		if (reference.Line.size() > current.Line.size()){
			return false;
		}
		else if (reference.Line.size() > 1 && reference.Line.get(0) != current.Line.get(0)){
			return false;
		}
		else{
			for (int i = 0; i < reference.Line.size(); i++){
				if (reference.Line.get(i) != current.Line.get(i) && i != reference.Line.size() - 1){
					return false;
				}
				else if (reference.Line.get(i) > current.Line.get(i) && i == reference.Line.size()-1){
					return false; 
				}
			}
		}
		return true;
		
	}
	
	private static ProofLineNode findNode (String x){
		LineNumber toFind = new LineNumber(x); 
		ProofLineNode temp = myRoot; 
		for (int i = 0; i < toFind.Line.size(); i++){
			if (toFind.Line.get(i) > 0){
				int count = toFind.Line.get(i);
				while (count > 1){
					temp = temp.myNextSibling; 
					count--; 
				}
			}
			if (i % 2 == 0 && i < toFind.Line.size() -1){
				temp = temp.myLeftChild;
			}
		}
	return temp;	
	}
	
	public class ProofLineNode {
		
		
		protected ExpressionTree myExprTree;
		protected LineNumber myLineNumber;
		protected ProofLineNode myParent;
		protected ProofLineNode myPrevSibling;
		protected ProofLineNode myNextSibling;
		protected ProofLineNode myLeftChild;
		
		
	    //Constructor for ProofLineNode with item reference
		public ProofLineNode (ExpressionTree item){
			this.myExprTree = item;
			myLineNumber = this.setLineNumber(); 
			myParent = null;
			myPrevSibling = null;
			myNextSibling = null;
			myLeftChild = null; 
			
		}
		
		
		
		//Constructor for ProofLineNode with item, parent and prevSib reference 
		public ProofLineNode (ExpressionTree item, ProofLineNode prevSib){
			this.myExprTree = item;
			myLineNumber = null;
			myParent = prevSib.myParent;
			myPrevSibling = prevSib;
			myNextSibling = null;
			myLeftChild = null; 
		}
		
		
		
		
		private  LineNumber setLineNumber( ){
			ProofLineNode current = this; 
			String line = ""; 
			int count = 1;
			while (current.myParent != null){
				while(current.myPrevSibling  != null){
					count++; 
					current = current.myPrevSibling; 
				}
				line = count + line;
				current = current.myParent;
				line = "." + line; 
				count = 1;
			}
			if (current.myParent == null){
				while(current.myPrevSibling != null){
					count++;
				}
				line = count + line; 
			}
			LineNumber x = new LineNumber (line);
			return x;
		}
		
		
		// Creates a newChild as the last sibling of previous children.  
		// Should current be reset to newChild? 
		public ProofLineNode insertChild (ExpressionTree item){
			ProofLineNode temp = this;
			ProofLineNode newChild = new ProofLineNode (item, temp);
			if (temp.myLeftChild != null){
				temp = temp.myLeftChild;
				while (temp.myNextSibling != null){
					temp = temp.myNextSibling;
				}
				temp.myNextSibling = newChild; 
				newChild.myLineNumber = newChild.setLineNumber(); 	

				
			}
			else{
				newChild.myParent = temp;
				temp.myLeftChild = newChild; 
				newChild.myLineNumber = newChild.setLineNumber(); 	

				
			}
			return newChild;
		}


	}
	
	
	private void checkTheorems (Scanner s) throws IllegalLineException, IllegalInferenceException {
		String thmName = s.next();
		Expression expr;
		try{
			expr = new Expression(s.next());
			myTheorems.resetVariables();
			
			if (myTheorems.contains(thmName)) {
				Expression theorem = myTheorems.get(thmName);
				try{
					checkTheoremsHelper(theorem.myExpression.myRoot, expr.myExpression.myRoot);
				} catch(IllegalInferenceException e){
					if(e.getMessage().equals("Structure is not correct")){
						throw new IllegalInferenceException("The expression " + expr + " does not have the same structure as the theorem " + myTheorems.get(thmName) + ".");
					}
					throw e;
				}
				
			}
			else {
				throw new IllegalLineException("Theorem " + thmName + " not in theorem set.");
			}
			
			
		}catch(IllegalLineException e){
			throw e;
		}
		
		
	}
	
	private void checkTheoremsHelper (ExpressionTree.ExpressionNode theorem, ExpressionTree.ExpressionNode expr)  throws IllegalInferenceException {
		// all the code is in the if, if they are both not null. it is ok for them to both be null.
		if(theorem != null && expr != null){
			// if the item in the theorem is a variable, then
			if(!theorem.myItem.equals("~") && !theorem.myItem.equals("&") && !theorem.myItem.equals("|") && !theorem.myItem.equals("=>")){
				// check to see if it is in the HashMap of <variable, Expression>. If not in there, add it.
				if(!myTheorems.variables.containsKey(theorem.myItem)){
					myTheorems.variables.put(theorem.myItem, expr);
				}
				// Then we already have an Expression assigned to the variable so check if the value will match.
				else if(!myTheorems.variables.get(theorem.myItem).equals(expr)){
					// If it doesn't match, error.
					throw new IllegalInferenceException("Bad theorem application: " + theorem.myItem + "=" + myTheorems.variables.get(theorem.myItem)  + ", " + theorem.myItem + "=" + expr + "."); 
				}
				// keep going after adding it or confirming that it is the same expression.
			}
			// if the two expressions have different myItems, which should be operators, error.
			else if(!theorem.myItem.equals(expr.myItem)){
				if(!expr.myItem.equals("~") && !expr.myItem.equals("&") && !expr.myItem.equals("|") && !expr.myItem.equals("=>")){
					throw new IllegalInferenceException("Structure is not correct");
				}
				throw new IllegalInferenceException("Operator in theorem does not agree with the expression. Expected " + theorem.myItem + ", got " + expr.myItem + ".");
			}
			// check to see if they both have lefts or rights that are valid.
			try{
				if(!myTheorems.variables.containsKey(theorem.myItem)){
					checkTheoremsHelper(theorem.myLeft, expr.myLeft);
					checkTheoremsHelper(theorem.myRight, expr.myRight);
				}
			}catch(IllegalInferenceException e){
				throw e;
			}
		}	
	}	
}	
