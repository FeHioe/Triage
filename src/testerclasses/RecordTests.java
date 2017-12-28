package testerclasses;

import org.junit.Test;

import junit.framework.TestCase;
import csc207b07.example.triage.DefaultValues;
import csc207b07.example.triage.Record;

public class RecordTests extends TestCase {

	@Test
	public void testRecordData() {

		Record r1 = new Record("2007-02-05", "1100", 50, 30, 37, "Patient test");
		assertEquals(30, r1.getBp());

	}
	@Test
	public void testRecordEqualsTwoSimmilarRecords() {
		Record r1 = new Record("2007-02-05", "1100", 50, 30, 37, "Patient test");
		Record r2 = new Record("2007-02-09", "1158", 50, 30, 37, "Patient test");
		
		assertTrue(r1.equals(r2));
		
	}
	
	@Test
	public void testRecordEqualsTwoDifferentRecords() {
		Record r1 = new Record("2007-02-05", "1100", 50, 30, 37, "Patient test");
		Record r2 = new Record("2007-02-09", "1158", 35, 80, 38, "Patient is dead");
		assertFalse(r1.equals(r2));				
		
	}
	
	@Test
	public void testRecordFormatConsistency() {
		Record r1 = new Record("2007-02-05", "1100", 30, 50, 37.0, "Patient test");
		
		assertEquals("1100" + DefaultValues.param_sep + "30"
				+ DefaultValues.param_sep + "50" + DefaultValues.param_sep
				+ "37.0" + DefaultValues.param_sep + "Patient test",  r1.toString());		
	}

}
