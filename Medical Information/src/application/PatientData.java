package application;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

public class PatientData  {

	@FXML private Pane patientViewPane;
	@FXML private Label lblName;
	@FXML private Label lblSex;
	@FXML private Label lblAge;
	@FXML private Label lblJob;
	@FXML private Label lblPhone;
	@FXML private Label lblSocial;
	
	@FXML private TableView<PatientTableData> patientTable;
	
	/*ObservableList<PatientTableData> patientList = FXCollections.observableArrayList(
		new PatientTableData()	
	);*/
	
	@FXML
	public void initialize() {

		SetData receiveData = new SetData();
		
		String patientData = receiveData.getServerData();

		System.out.println("Receive Server Data : " + patientData);
		String[] patientRegister = patientData.split("#");
		
		for(String i : patientRegister) {
			System.out.print(i + "  ");
		}
		
		lblName.setText(patientRegister[0]);
		lblSex.setText(patientRegister[1]);
		lblAge.setText(patientRegister[2]);
		lblSocial.setText(patientRegister[3]);
		lblPhone.setText(patientRegister[4]);
		lblJob.setText(patientRegister[5]);
		
	}
	
	/*
	@FXML
	public Pane patientViewPane(ActionEvent evnet) {
		Pane patientPane = new Pane();
		
		SetData receiveData = new SetData();
		
		String patientData = receiveData.getServerData();
		
		System.out.println("Receive Server Data : " + patientData);
		String[] patientRegister = patientData.split("#");
		
		for(String i : patientRegister) {
			System.out.print(i + "  ");
		}
		
		lblName.setText(patientRegister[0]);
		lblSex.setText(patientRegister[1]);
		lblAge.setText(patientRegister[2]);
		lblSocial.setText(patientRegister[3]);
		lblPhone.setText(patientRegister[4]);
		lblJob.setText(patientRegister[5]);
		
		return patientPane;
	}*/
	
	
}
