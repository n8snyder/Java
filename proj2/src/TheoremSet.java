import java.util.*;
public class TheoremSet {
	private HashMap<String, Expression> myTheorems = new HashMap();
	public HashMap<String, ExpressionTree.ExpressionNode> variables; // checkTheorems should be the only method that uses this.
	public TheoremSet ( ) {
	}

	public Expression put (String key, Expression e) {
		myTheorems.put(key, e);
		return e;
	}
	
	public Expression get (String key){
		return myTheorems.get(key);
	}
	public boolean contains(String key){
		return myTheorems.containsKey(key);
	}
	
	public void resetVariables() {
		variables = new HashMap<String, ExpressionTree.ExpressionNode>();
	}
}
