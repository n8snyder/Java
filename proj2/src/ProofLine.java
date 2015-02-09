import java.util.ArrayList;


public class ProofLine {
	
	public ProofLineNode root;
	public static ProofLineNode current; 
	
	
	//Construct an empty ProofLine tree 
		public ProofLine () {
			root = null;
			current = root; 
		}
		
		
		//Construct a one-node ProofLine tree 
		public ProofLine (Object item) {
			root = new ProofLineNode (item); 
			root.myParent = null; 
			root.myPrevSibling = null;
			root.myNextSibling = null;
			root.myLeftChild = null;
			current = root; 
		}
		
		
		public ProofLineNode parent () {
			return current.myParent;
		}
		
		public ProofLineNode prev () {
			return current.myPrevSibling;
		}
		
		public ProofLineNode next () {
			return current.myNextSibling;
		}
		
		public ProofLineNode child () {
			return current.myLeftChild;
		}
		
		private static boolean validateLineReference (String x){
			LineNumber reference = new LineNumber (x); 
			LineNumber current = new LineNumber ("3.1.2");  
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
		
		
		public static void main(String[] args) {
			boolean check = false;
			ProofLine test = new ProofLine ("1");
			ProofLineNode temp = current;
			ProofLineNode prev; 
			System.out.println(temp.myLineNumber.toString());
			current.insertChild("2");			
			temp = current.myLeftChild;
			System.out.println(temp.myLineNumber.toString());
			current.insertChild("3");
			temp = temp.myNextSibling;
			System.out.println(temp.myLineNumber.toString());
			current.insertChild("4");
			temp = temp.myNextSibling;
			System.out.println(temp.myLineNumber.toString());
			temp.insertChild("5");
			temp = temp.myLeftChild;
			System.out.println(temp.myLineNumber.toString());
			
			if (validateLineReference("2.1.1")){
				System.out.println("okay line refernce");
			}
			else{
				System.out.println("Invalid line refernce");
			}
			if (validateLineReference("3.1.1")){
				System.out.println("okay line refernce");
			}
			else{
				System.out.println("Invalid line refernce");
			}
			
		}		
		
}