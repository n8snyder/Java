

public class ProofLineNode {
	
	
	protected Object myItem;
	protected Object myLineNumber;
	protected ProofLineNode myParent;
	protected ProofLineNode myPrevSibling;
	protected ProofLineNode myNextSibling;
	protected ProofLineNode myLeftChild;
	
	
//Constructor for ProofLineNode with item reference
	ProofLineNode (Object item){
		this.myItem = item;
		myLineNumber = this.setLineNumber(); 
		myParent = null;
		myPrevSibling = null;
		myNextSibling = null;
		myLeftChild = null; 
		
	}
	
	
	
	//Constructor for ProofLineNode with item, parent and prevSib reference 
	ProofLineNode (Object item, ProofLineNode prevSib){
		this.myItem = item;
		myLineNumber = this.setLineNumber();
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
	
	
	//Creates a newChild as the last sibling of previous children.  Should current be reset to newChild? 
	public void insertChild (Object item){
		ProofLineNode temp = this;
		if (temp.myLeftChild != null){
			temp = temp.myLeftChild;
			while (temp.myNextSibling != null){
				temp = temp.myNextSibling;
			}
			ProofLineNode newChild = new ProofLineNode (item, temp);
			temp.myNextSibling = newChild; 
			newChild.myLineNumber = newChild.setLineNumber(); 	

			
		}
		else{
			ProofLineNode newChild = new ProofLineNode(item);
			newChild.myParent = temp;
			temp.myLeftChild = newChild; 
			newChild.myLineNumber = newChild.setLineNumber(); 	

			
		}
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
