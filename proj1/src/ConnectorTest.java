import junit.framework.TestCase;

public class ConnectorTest extends TestCase {
	public void testToConnector() {
		Connector.toConnector("  26 "); //should work
		//assertTrue("Should be 2, 6, is " + Connector.toConnector("  26 ").toString() + '.', 
				//Connector.toConnector("  26 ").toString().equals("26"));
		Connector.toConnector(""); // should not work
		Connector.toConnector("124"); //should not work
		Connector.toConnector("5 3"); // should not work
		Connector.toConnector("	"); // should not work
		Connector.toConnector("6 0"); // should not work
		Connector.toConnector("11"); // should not work
	}
}


/*
 * toConnector tests:
 * null set
 * more than two numbers
 * space between two numbers
 * tab used as space
 * number out of range (not 1-6)
 * white space after two numbers
 * initial white space
 * repeated numbers
 * 



*/
