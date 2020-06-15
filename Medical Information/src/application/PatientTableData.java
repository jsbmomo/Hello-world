package application;

import javafx.beans.property.StringProperty;

public class PatientTableData {

	private StringProperty date;
	private StringProperty hospital;
	private StringProperty docter;
	private StringProperty register;
	
	public void TableRowData(StringProperty date, StringProperty hospital, StringProperty doctor, StringProperty register) {
		this.date = date;
		this.docter = doctor;
		this.hospital = hospital;
		this.register = register;
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
