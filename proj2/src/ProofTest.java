import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;


public class ProofTest extends TestCase {

	public void testExtendProofTheorems() {
		TheoremSet myTheorems = new TheoremSet();
		try{
			myTheorems.put("demorgan1", new Expression("((~a|~b)=>~(a&b))"));
			myTheorems.put("demorgan2", new Expression("((~a&~b)=>~(a|b))"));
			Proof myProof = new Proof(myTheorems);
			myProof.extendProof("demorgan1 ((~(z|y)|~(z&y))=>~((z|y)&(z&y)))");
			
		}catch (IllegalLineException e){
			fail(e.getMessage());
		}catch (IllegalInferenceException e){
			fail(e.getMessage());
		}
		try{
			myTheorems.put("demorgan1", new Expression("((~a|~b)=>~(a&b))"));
			Proof myProof = new Proof(myTheorems);
			myProof.extendProof("demorgan1 (((z|y)|~(z&y))=>~((z|y)&(z&y)))");
			
		}catch (IllegalLineException e){
			fail(e.getMessage());
		}catch (IllegalInferenceException e){
			assertTrue(e.getMessage().equals("Operator in theorem does not agree with the expression. Expected ~, got |."));
		}
		try{
			myTheorems.put("demorgan1", new Expression("((~a|~b)=>~(a&b))"));
			Proof myProof = new Proof(myTheorems);
			myProof.extendProof("demorgan1 ((~(z|y)|~(z&y))=>~((z|y)&z))");
			
		}catch (IllegalLineException e){
			fail(e.getMessage());
		}catch (IllegalInferenceException e){
			assertTrue(e.getMessage().equals("Bad theorem application: b=(z&y), b=z."));
		}
		try{
			myTheorems.put("demorgan1", new Expression("((~a|~b)=>~(a&b))"));
			Proof myProof = new Proof(myTheorems);
			myProof.extendProof("demorgan ((~(z|y)|~(z&y))=>~((z|y)&(z&y)))");
			
		}catch (IllegalLineException e){
			assertTrue(e.getMessage().equals("Theorem demorgan not in theorem set."));
		}catch (IllegalInferenceException e){
			fail(e.getMessage());
		}
		try{
			myTheorems.put("demorgan1", new Expression("((~a|~b)=>~(a&b))"));
			Proof myProof = new Proof(myTheorems);
			myProof.extendProof("demorgan1 (a=>b)");
			
		}catch (IllegalLineException e){
			fail(e.getMessage());
		}catch (IllegalInferenceException e){
			assertTrue(e.getMessage().equals("The expression (a=>b) does not have the same structure as the theorem ((~a|~b)=>~(a&b))."));
		}
		try{
			myTheorems.put("dn", new Expression("(~~a=>a)"));
			Proof myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (a=>~~a)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume a");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show ~~a");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume ~~~a");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("dn (~~~a=>~a)");
			myProof.nextLineNumber(); //3.3
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("3.3"));	
			myProof.extendProof("show (~~z=>z)");
			myProof.nextLineNumber(); //3.3.1
			myProof.extendProof("dn (~~z=>z)");
			myProof.nextLineNumber(); //3.4
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("3.4"));
			
		} catch (IllegalLineException e){
			fail(e.getMessage());
		}catch (IllegalInferenceException e){
			fail(e.getMessage());
		}
	}
	
	public void testExtendProofShow (){
		TheoremSet myTheorems = new TheoremSet();
		Proof myProof = new Proof(myTheorems);
		try{
			myProof.nextLineNumber();
			myProof.extendProof("show (a=>b)");
			assertTrue(myProof.lastNode().myExprTree.equals((new Expression("(a=>b)")).myExpression));
			myProof.nextLineNumber();
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("2"));
			myProof.extendProof("show a");
			assertTrue(myProof.lastNode().myExprTree.equals((new Expression("a")).myExpression));
			myProof.nextLineNumber();
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("2.1"));
			myProof.extendProof("show a");
			assertTrue(myProof.lastNode().myExprTree.equals((new Expression("a")).myExpression));
			myProof.nextLineNumber();
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("2.1.1"));	
		} catch(IllegalLineException e){
			fail(e.getMessage());
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		
		
		try{
			myTheorems.put("dn", new Expression ("(~~p=>p)"));
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); // lastNode is 1
			myProof.extendProof("show (~~~a=>~a)");
			myProof.nextLineNumber(); // lastNode is 2
			myProof.extendProof("show (~~a=>a)");
			myProof.nextLineNumber(); // lastNode is 2.1
			myProof.extendProof("dn (~~a=>a)");
			myProof.nextLineNumber(); // lastNode is 3
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("3"));
			myProof.extendProof("show (~~a=>a)");
			myProof.nextLineNumber(); // lastNode is 3.1
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("3.1"));
			
			
		} catch(IllegalLineException e){
			fail(e.getMessage());
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		myProof = new Proof(myTheorems);
		try{
			myProof.nextLineNumber(); // lastNode is 1
			myProof.extendProof("show a=>b");
			fail("An exception should have been thrown");
			
		} catch(IllegalLineException e){
			assertTrue(e.getMessage().equals("a=>b is not a valid expression."));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		myProof = new Proof(myTheorems);
		try{
			myProof.nextLineNumber(); // lastNode is 1
			myProof.extendProof("show ");
			fail("An exception should have been thrown");
			
		} catch(IllegalLineException e){
			assertTrue(e.getMessage().equals("No expression given"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
	}
	
	public void testExtendProofAssume (){
		TheoremSet myTheorems = new TheoremSet();
		Proof myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (a=>b)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume a");
			myProof.nextLineNumber(); //3
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("3"));
			myProof.extendProof("show a=>b");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume ~(a=>b)");	
			myProof.nextLineNumber(); //3.2
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("3.2"));
			
		} catch(IllegalLineException e){
			fail(e.getMessage());
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber();
			myProof.extendProof("show (a=>b)");
			myProof.nextLineNumber();
			myProof.extendProof("assume b");
			
		} catch(IllegalLineException e){
			fail(e.getMessage());
		} catch(IllegalInferenceException e){
			assertTrue(e.getMessage().equals("Invalid assumption"));
		}
		
		myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber();
			myProof.extendProof("show (a=>b)");
			myProof.nextLineNumber();
			myProof.extendProof("assume a");
			myProof.nextLineNumber();
			myProof.extendProof("assume a");
			
		} catch(IllegalLineException e){
			assertTrue(e.getMessage().equals("Assume must come after a show"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber();
			myProof.extendProof("show (a=>b)");
			myProof.nextLineNumber();
			myProof.extendProof("assume ");
				
		} catch(IllegalLineException e){
			assertTrue(e.getMessage().equals("No expression given"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber();
			myProof.extendProof("show (a&b)");
			myProof.nextLineNumber();
			myProof.extendProof("assume a");
			
		} catch(IllegalLineException e){
			fail(e.getMessage());
		} catch(IllegalInferenceException e){
			assertTrue(e.getMessage().equals("Invalid assumption"));
		}
	}
	
	public void testExtendProofMP (){
		TheoremSet myTheorems = new TheoremSet();
		Proof myProof;
		try {
			myProof = new Proof(myTheorems);
			myTheorems.put("dn", new Expression("~~a=>a)"));
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (p=>((p=>q)=>q))");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show ((p=>q)=>q)");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume (p=>q)");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("show q");
			myProof.nextLineNumber(); //3.2.1
			myProof.extendProof("mp 2 3.1 q");
			myProof.nextLineNumber(); //3.3
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("3.3"));
			myProof.extendProof("show q");
			myProof.nextLineNumber(); //3.3.1
			myProof.extendProof("mp 3.1 2 q");
			myProof.nextLineNumber(); //3.4
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("3.4"));
			
		} catch(IllegalLineException e){
			fail(e.getMessage());
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber();
			myProof.extendProof("show (a=>b)");
			myProof.nextLineNumber();
			myProof.extendProof("mp "); // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
				
		} catch(IllegalLineException e){
			assertTrue(e.getMessage().equals("No expression given"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (p=>((p=>q)=>q))");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show ((p=>q)=>q)");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume (p=>q)");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("show q");
			myProof.nextLineNumber(); //3.2.1
			myProof.extendProof("mp 3.1 q"); // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
				
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (p=>((p=>q)=>q))");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show ((p=>q)=>q)");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume (p=>q)");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("show q");
			myProof.nextLineNumber(); //3.2.1
			myProof.extendProof("mp q"); // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
				
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (p=>((p=>q)=>q))");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show ((p=>q)=>q)");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume (p=>q)");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("show q");
			myProof.nextLineNumber(); //3.2.1
			myProof.extendProof("mp 3.1"); // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
				
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (p=>((p=>q)=>q))");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show ((p=>q)=>q)");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume (p=>q)");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("show q");
			myProof.nextLineNumber(); //3.2.1
			myProof.extendProof("mp 3 2 q");  // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
				
		} catch(IllegalLineException e){
			fail(e.getMessage());
		} catch(IllegalInferenceException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		}
	}
	
	public void testExtendProofRepeat (){
		TheoremSet myTheorems = new TheoremSet();
		Proof myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (a=>b)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("repeat 1 (a=>b)"); // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
			
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}

		myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (a=>b)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume a");
			myProof.nextLineNumber(); //2.1
			myProof.extendProof("repeat 2"); // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
			
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (a=>b)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("repeat (a=>b)"); // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
			
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (a=>b)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("repeat"); // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
			
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		myProof = new Proof(myTheorems);
		try {
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (a=>b)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("show (a=>b)");
			myProof.nextLineNumber(); //2.1
			myProof.extendProof("repeat 2 (a=>b)"); // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
			
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
	}
	public void testExtendProofIC(){
		TheoremSet myTheorems = new TheoremSet();
		
		Proof myProof;
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (p=>((p=>q)=>q))");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show ((p=>q)=>q)");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume (p=>q)");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("show q");
			myProof.nextLineNumber(); //3.2.1
			myProof.extendProof("mp 2 3.1 q");
			myProof.nextLineNumber(); //3.3
			myProof.extendProof("ic 3.1 (z=>(p=>q))");
			myProof.nextLineNumber(); //3.4
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("3.4"));
			myProof.extendProof("ic 3.2 ((p=>q)=>q)");
			myProof.nextLineNumber(); //4
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("4"));
			myProof.extendProof("ic 3 (p=>((p=>q)=>q))");
			assertTrue(myProof.isComplete());
			
		} catch(IllegalLineException e){
			fail(e.getMessage());
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (p=>((p=>q)=>q))");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show ((p=>q)=>q)");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume (p=>q)");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("show q");
			myProof.nextLineNumber(); //3.2.1
			myProof.extendProof("mp 2 3.1 q");
			myProof.nextLineNumber(); //3.3
			myProof.extendProof("ic 3.2 ((p=>q)=>q)");
			myProof.nextLineNumber(); //4
			myProof.extendProof("ic 3.2 ((p=>q)=>q)"); // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (p=>((p=>q)=>q))");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show ((p=>q)=>q)");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume (p=>q)");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("show q");
			myProof.nextLineNumber(); //3.2.1
			myProof.extendProof("mp 2 3.1 q");
			myProof.nextLineNumber(); //3.3
			myProof.extendProof("ic q"); // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (p=>((p=>q)=>q))");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show ((p=>q)=>q)");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume (p=>q)");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("show q");
			myProof.nextLineNumber(); //3.2.1
			myProof.extendProof("mp 2 3.1 q");
			myProof.nextLineNumber(); //3.3
			myProof.extendProof("ic 3.1 "); // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (p=>((p=>q)=>q))");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show ((p=>q)=>q)");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume (p=>q)");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("show q");
			myProof.nextLineNumber(); //3.2.1
			myProof.extendProof("mp 2 3.1 q");
			myProof.nextLineNumber(); //3.3
			myProof.extendProof("ic "); // THIS SHOULD THROW AN EXCEPTION
			fail("You should not get to this point in the code.");
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
	}
	public void testIsComplete(){
		TheoremSet myTheorems = new TheoremSet();
		Proof myProof;
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (p=>((p=>q)=>q))");
			assertTrue(!myProof.isComplete());
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume p");
			assertTrue(!myProof.isComplete());
			myProof.nextLineNumber(); //3
			myProof.extendProof("show ((p=>q)=>q)");
			assertTrue(!myProof.isComplete());
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume (p=>q)");
			assertTrue(!myProof.isComplete());
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("show q");
			assertTrue(!myProof.isComplete());
			myProof.nextLineNumber(); //3.2.1
			myProof.extendProof("mp 2 3.1 q");
			assertTrue(!myProof.isComplete());
			myProof.nextLineNumber(); //3.3
			myProof.extendProof("ic 3.2 ((p=>q)=>q)");
			assertTrue(!myProof.isComplete());
			myProof.nextLineNumber(); //4
			myProof.extendProof("ic 3 (p=>((p=>q)=>q))");
			assertTrue(myProof.isComplete());
			
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (p=>p)");
			assertTrue(!myProof.isComplete());
			myProof.nextLineNumber(); //2
			myProof.extendProof("show (p=>p)");
			assertTrue(!myProof.isComplete());
			myProof.nextLineNumber(); //2.1
			myProof.extendProof("assume p");
			assertTrue(!myProof.isComplete());
			myProof.nextLineNumber(); //2.2
			myProof.extendProof("ic 2.1 (p=>p)");
			assertTrue(!myProof.isComplete());
			myProof.nextLineNumber(); //3
			myProof.extendProof("repeat 2 (p=>p)");
			assertTrue(myProof.isComplete());
			
		} catch(IllegalLineException e){
			fail(e.getMessage());
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		
	}
	
	
	public void testExtendProofCO () {
		TheoremSet myTheorems = new TheoremSet();
		Proof myProof;
		
		// test that it should work
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (~~p=>p)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume ~~p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show p");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume ~p");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("co 2 3.1 p");
			myProof.nextLineNumber(); //4
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("4"));
			
			
		} catch(IllegalLineException e){
			fail(e.getMessage());
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		// same as last test, but switch the two line numbers
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (~~p=>p)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume ~~p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show p");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume ~p");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("co 3.1 2 p"); //line numbers are in different order
			myProof.nextLineNumber(); //4
			assertTrue(myProof.lastNode().myLineNumber.toString().equals("4"));
			
		} catch(IllegalLineException e){
			fail(e.getMessage());
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (~~p=>p)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume ~~p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show p");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume ~p");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("co 3.1 p"); //wrong number of line numbers
			fail("You should not get to this point in the code.");
			
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (~~p=>p)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume ~~p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show p");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume ~p");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("co p"); //wrong number of line numbers
			fail("You should not get to this point in the code.");
			
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (~~p=>p)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume ~~p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show p");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume ~p");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("co 3.1 2"); //missing expression
			fail("You should not get to this point in the code.");
			
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (~~p=>p)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume ~~p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show p");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume ~p");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("co 3.1 p 2"); //"arguments" are in the incorrect order
			fail("You should not get to this point in the code.");
			
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (~~p=>p)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume ~~p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show p");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume ~p");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("co "); //no "arguments"
			fail("You should not get to this point in the code.");
			
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			fail(e.getMessage());
		}
		try{
			myProof = new Proof(myTheorems);
			myProof.nextLineNumber(); //1
			myProof.extendProof("show (~~p=>p)");
			myProof.nextLineNumber(); //2
			myProof.extendProof("assume ~~p");
			myProof.nextLineNumber(); //3
			myProof.extendProof("show ~~~p");
			myProof.nextLineNumber(); //3.1
			myProof.extendProof("assume ~~~~p");
			myProof.nextLineNumber(); //3.2
			myProof.extendProof("co 2 3.1 ~~~p"); //should not work
			
			
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		} catch(IllegalInferenceException e){
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("CHANGE THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		}
	}
}
























