

public class ExpressionTree {
	public ExpressionNode myRoot;
	
	ExpressionTree(){
		myRoot = null;
	}
	
	public ExpressionTree (ExpressionTree.ExpressionNode t) {
		myRoot = t;
	}
	public String toString(){
		return myRoot.toString();
	}
	public boolean equals(ExpressionTree otherTree){
		return this.myRoot.equals(otherTree.myRoot);
	}
	
	public static class ExpressionNode {
		
		public String myItem;
		public ExpressionNode myLeft;
		public ExpressionNode myRight;
		
		public ExpressionNode (String str) {
			myItem = str;
			myLeft = myRight = null;
		}
		
		public ExpressionNode (String obj, ExpressionNode left, ExpressionNode right) {
			myItem = obj;
			myLeft = left;
			myRight = right;
		}
		
		public boolean equals (ExpressionNode node) {
			if (!this.myItem.equals(node.myItem)) {
				return false;
			}
			if (this.myLeft != null && node.myLeft != null) {
				if (!this.myLeft.equals(node.myLeft)) {
					return false;
				}
			}
			if (this.myRight != null && node.myRight != null) {
				if (!this.myRight.equals(node.myRight)) {
					return false;
				}
			}
			if ((this.myLeft != null && node.myLeft == null) || (this.myLeft == null && node.myLeft != null)) {
				return false;
			}
			if ((this.myRight != null && node.myRight == null) || (this.myRight == null && node.myRight != null)) {
				return false;
			}
			return true;
		}
		public String toString(){
			if(Character.isLetter(this.myItem.charAt(0)) && Character.isLowerCase(this.myItem.charAt(0)) && this.myItem.length() == 1){
				return this.myItem;
			}
			if(this.myItem.equals("&") || this.myItem.equals("|") || this.myItem.equals("=>")){
				return "(" + this.myLeft.toString() + this.myItem + this.myRight.toString() + ")";
			}
			if(this.myItem.equals("~")){
				return "~" + this.myLeft.toString();
			}
			return "";
		}
	}
	
}
