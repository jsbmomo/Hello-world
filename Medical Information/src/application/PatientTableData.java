package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PatientTableData {

	private StringProperty date;
	private StringProperty hospital;
	private StringProperty docter;
	private StringProperty register;
	
	public  PatientTableData(String date, String hospital, String doctor, String register) {
		this.date = new SimpleStringProperty(date);
		this.docter = new SimpleStringProperty(hospital);
		this.hospital = new SimpleStringProperty(doctor);
		this.register = new SimpleStringProperty(register);
	}
	
	public StringProperty dateProperty() {
		return date;
	}	
	
	public StringProperty hospitalProperty() {
		return hospital;
	}
	
	public StringProperty docterProperty() {
		return docter;
	}
	
	public StringProperty registerProperty() {
		return register;
	}
	
}
