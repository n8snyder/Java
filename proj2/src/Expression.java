

public class Expression {
	
	ExpressionTree myExpression; 
	public Expression (String s) throws IllegalLineException {
		if(!checkExpression(s)){
			throw new IllegalLineException(s + " is not a valid expression.");
		}
		else{
			myExpression = makeExpressionTree(s);
		}
		
		
	}
	public static ExpressionTree makeExpressionTree(String s){
		ExpressionTree result = new ExpressionTree();
		result.myRoot = makeExpressionTreeHelper(s);
		return result;
	}
	private static ExpressionTree.ExpressionNode makeExpressionTreeHelper(String s){
		if(Character.isLetter(s.charAt(0)) && Character.isLowerCase(s.charAt(0)) && s.length() == 1){
			return new ExpressionTree.ExpressionNode(s);
		}
		if(s.charAt(0) == '~'){
			return new ExpressionTree.ExpressionNode("" + s.charAt(0), makeExpressionTreeHelper(s.substring(1, s.length())), null);
		}
		int depth = 0;
		for(int i = 1; i < s.length(); i++){
			if(s.charAt(i) == '('){
				depth++;
			}
			if(s.charAt(i) == ')'){
				depth--;
			}
			if((s.charAt(i) == '&' || s.charAt(i) == '|') && depth == 0){
				return new ExpressionTree.ExpressionNode("" + s.charAt(i), makeExpressionTreeHelper(s.substring(1, i)), makeExpressionTreeHelper(s.substring(i+1, s.length()-1)));
			}
			if((s.charAt(i) == '=' && s.charAt(i+1) == '>')  && depth == 0){
				return new ExpressionTree.ExpressionNode(s.substring(i,i+2), makeExpressionTreeHelper(s.substring(1, i)), makeExpressionTreeHelper(s.substring(i+2, s.length()-1)));
			}
		}
		return null;
	}
	
	public static boolean checkExpression(String s){
		if(s.isEmpty()){
			return false;
		}
		if(Character.isLetter(s.charAt(0)) && Character.isLowerCase(s.charAt(0)) && s.length() == 1){
			return true;
		}
		if(s.charAt(0) == '~'){
			if(!checkExpression(s.substring(1,s.length()))){
				return false;
			}
			return true;
		}
		if(s.charAt(0) != '('){
			return false;
		}
		if(s.charAt(s.length()-1) != ')'){
			return false;
		}
		// s.charAt(0) == '('
		else{
			int depth = 0;
			for(int k=1; k<s.length();k++){
				if(Character.isWhitespace(s.charAt(k))){
					return false;
				}
				if(s.charAt(k) == '('){
					depth++;
				}
				if(s.charAt(k) == ')'){
					depth--;
				}
				if((s.charAt(k) == '&' || s.charAt(k) == '|') && depth == 0){
					if(!checkExpression(s.substring(1,k)) || !checkExpression(s.substring(k+1,s.length()-1))){
						return false;
					}
					return true;
				}
				
				if((s.charAt(k) == '=' && s.charAt(k+1) == '>')  && depth == 0){
					if(!checkExpression(s.substring(1,k)) || !checkExpression(s.substring(k+2,s.length()-1))){
						return false;
					}
					return true;
				}
			}
			return false;
		}
	}
	
	public String toString(){
		return myExpression.toString();
	}
	public boolean equals(Expression otherExpr) {
		return myExpression.equals(otherExpr.myExpression);
	}
}
