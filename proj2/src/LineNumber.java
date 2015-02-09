import java.util.ArrayList;


public class LineNumber {
	protected ArrayList<Integer> Line; 
	
	public LineNumber ( ){
		Line = new ArrayList<Integer> (0); 
		Line.add(0, -2); 
		
	}
	
	public LineNumber (String x){
		Line = new ArrayList<Integer> (0);
		convertLine(x); 
	}
	
	private void convertLine(String x){
		if (x.contains(".")){
			int location  = x.indexOf(".");
			String curr = x.substring(0, location);
			Line.add(Integer.parseInt(curr)); 
			Line.add(-1); 
			convertLine(x.substring(location+1)); 
		}
		else{
			Line.add(Integer.parseInt(x));
		}
		
			
	}
	
	public String toString( ){
		String line = " "; 
		for (int i = 0; i < Line.size(); i++){
			if (Line.get(i) == -2){
				return line;
			}
			else if (Line.get(i) != -1){
				line = line + Line.get(i);
			}
			else{
				line = line + ".";
			}
		}
		return line; 
	}
	
	public static void main(String [] args) {
		LineNumber test = new LineNumber ();
		System.out.println(test.toString());
		LineNumber test1 = new LineNumber("3.1.2");
		System.out.println(test1.toString());
		LineNumber test2 = new LineNumber("3.11.2.44");
		System.out.println(test2.toString());
		LineNumber test3 = new LineNumber("12");
		System.out.println(test3.toString());
	}
	
}