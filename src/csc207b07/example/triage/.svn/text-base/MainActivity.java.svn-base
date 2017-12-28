package csc207b07.example.triage;


import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {
	
	public static Nurse nurse = new Nurse();
	Intent intent1 = new Intent(this, NewRecord.class);
	Intent intent2 = new Intent(this, PatientList.class);
	Intent intent3 = new Intent(this, SearchRecords.class);
	//Gets the string in the search bar
	//String string = getString(R.string.search_bar);
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    protected void createNewRecord(View view) {
    	startActivity(intent1);
    }
    
    protected void searchPatients(View view) {
    	startActivity(intent2);
    }
    
    protected void searchRecords(View view) {
    	startActivity(intent3);
    }
}