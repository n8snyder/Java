import junit.framework.TestCase;

import org.junit.Test;


public class ExpressionTest extends TestCase {
	public void testSyntax() {
		assertTrue(Expression.checkExpression("((x&y)=>x)"));
		assertTrue(!Expression.checkExpression("((x&y)=>x))"));
		assertTrue(!Expression.checkExpression("()"));
		assertTrue(!Expression.checkExpression("(x=>y=>z)"));
		
		assertTrue(Expression.checkExpression("~x"));
		assertTrue(!Expression.checkExpression("(z)"));
		assertTrue(Expression.checkExpression("((((r|s)=>(x|~y))&(~(r|s)=>(x|~y)))=>(x|~y))"));
		assertTrue(!Expression.checkExpression(""));
		
		assertTrue(Expression.checkExpression("x"));
		assertTrue(!Expression.checkExpression("xy"));
		assertTrue(!Expression.checkExpression("A"));
		assertTrue(!Expression.checkExpression("5"));
		
		assertTrue(!Expression.checkExpression("(a=>"));
		assertTrue(!Expression.checkExpression("(a&|b)"));
		assertTrue(!Expression.checkExpression("~x "));
		assertTrue(!Expression.checkExpression("((((r|s)=>(x|~y))&(~(r| s)=>(x|~y)))=>(x|~y))"));
		
		assertTrue(!Expression.checkExpression(" "));
		assertTrue(!Expression.checkExpression("x)"));
		assertTrue(!Expression.checkExpression("(x&y"));
		assertTrue(Expression.checkExpression("(((a=>b)&(c=>d))=>((~a=>~b)&(~c=>~d)))"));
	}
	
	public void testMakingIntoTree() {
		try {
			Expression expr = new Expression("((x&y)=>x)");
			assertTrue(expr.myExpression.myRoot.myItem.equals("=>"));
			assertTrue(expr.myExpression.myRoot.myLeft.myItem.equals("&"));
			assertTrue(expr.myExpression.myRoot.myLeft.myLeft.myItem.equals("x"));
			assertTrue(expr.myExpression.myRoot.myLeft.myRight.myItem.equals("y"));
			assertTrue(expr.myExpression.myRoot.myRight.myItem.equals("x"));
			
			expr = new Expression("~((x&y)=>x)");
			assertTrue(expr.myExpression.myRoot.myItem.equals("~"));
			assertTrue(expr.myExpression.myRoot.myLeft.myItem.equals("=>"));
			assertTrue(expr.myExpression.myRoot.myRight == null);
			
			
		} catch (IllegalLineException e) {
			fail();
			e.printStackTrace();
		}
		
	}
	
	public void testToString() {
		Expression expr;
		try {
			expr = new Expression("((x&y)=>x)");
			assertTrue(expr.toString().equals("((x&y)=>x)"));
			
			expr = new Expression("(~~x=>x)");
			assertTrue(expr.toString().equals("(~~x=>x)"));
			
			expr = new Expression("(((a=>b)&(c=>d))=>((~a=>~b)&(~c=>~d)))");
			assertTrue(expr.toString().equals("(((a=>b)&(c=>d))=>((~a=>~b)&(~c=>~d)))"));
			
			expr = new Expression("(~~y=>~~y)");
			assertTrue(expr.toString().equals("(~~y=>~~y)"));
			
			expr = new Expression("((((r|s)=>(x|~y))&(~(r|s)=>(x|~y)))=>(x|~y))");
			assertTrue(expr.toString().equals("((((r|s)=>(x|~y))&(~(r|s)=>(x|~y)))=>(x|~y))"));
			
			expr = new Expression("~x");
			assertTrue(expr.toString().equals("~x"));
			
			expr = new Expression("x");
			assertTrue(expr.toString().equals("x"));
			
			expr = new Expression("(((a=>b)&(c=>d))=>((~a=>~b)&(~c=>~d)))");
			assertTrue(expr.toString().equals("(((a=>b)&(c=>d))=>((~a=>~b)&(~c=>~d)))"));
			
		} catch (IllegalLineException e) {
			fail();
			e.printStackTrace();
		}
		
	}
	public void testEquals() { // INCOMPLETE
		Expression expr1, expr2;
		
	}

}
