package application;


import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PatientData {

	@FXML private Pane patientViewPane;
	@FXML private Label lblName;
	@FXML private Label lblSex;
	@FXML private Label lblAge;
	@FXML private Label lblJob;
	@FXML private Label lblPhone;
	@FXML private Label lblSocial;
	@FXML private Button btnAddRegister;
	
	@FXML private TableView<PatientTableData> patientTable;
	@FXML private TableColumn<PatientTableData, String> dateColumn;
	@FXML private TableColumn<PatientTableData, String> hospitalColumn;
	@FXML private TableColumn<PatientTableData, String> docterColumn;
	@FXML private TableColumn<PatientTableData, String> registerColumn;
	
	ArrayList<PatientTableData> classification;
	
	@FXML
	public void initialize()  { // �ʱ⿡ ȭ���� �����ϸ� ����

		// ���� �޼ҵ带 ����Ͽ� ��ü(������ �޸�)���� ���� ������
		String receivePatientData = PassString.getReceiveData();  

		System.out.println("Receive Server Data : " + receivePatientData);
		String[] patientInfo = receivePatientData.split("#", 7);
		
		int count = 0; // ȯ���� �������� �ִٸ� patientInfo�� ���ڿ��� �� 7��
		for(String i : patientInfo) {
			System.out.print(i + "  ");
			count++;
		}
		
		lblName.setText(patientInfo[0]);
		lblSex.setText(patientInfo[1]);
		lblAge.setText(patientInfo[2]);
		lblSocial.setText(patientInfo[3]);
		lblPhone.setText(patientInfo[4]);
		lblJob.setText(patientInfo[5]);

		String[] patientData = null; // ȯ���� �������� ������ ���ڿ�
	
		if(count >= 7) {
			
			// ȯ���� ���� ����� ��¥ ���� 1�� ����
			if(patientInfo[6].contains("&")) {  
				patientData = patientInfo[6].split("&");
			} else {
				patientData = new String[1];
				patientData[0] = patientInfo[6];
			}
			
			// ������ ȯ���� ���� ����� �з�. ArrayList�� ���� 
			// ȯ���� �������� �� �������� ��ü�� ���� 
			classification = new ArrayList<PatientTableData>();
			for(String array : patientData) {
				String[] register = array.split("#");
				classification.add(
						new PatientTableData(register[0], register[2], register[1], register[3])	
				);
			}
			
			// ������ TableView�� ������ �����ϴ� �ڵ� 
			ObservableList<PatientTableData> patientList = FXCollections.observableArrayList(classification);
			
			dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
			dateColumn.setStyle("-fx-alignment: CENTER;");
			hospitalColumn.setCellValueFactory(cellData -> cellData.getValue().hospitalProperty());
			hospitalColumn.setStyle("-fx-alignment: CENTER;");
			docterColumn.setCellValueFactory(cellData -> cellData.getValue().docterProperty());
			docterColumn.setStyle("-fx-alignment: CENTER;");
			registerColumn.setCellValueFactory(cellData -> cellData.getValue().registerProperty());
			
			patientTable.setItems(patientList);
		}
	}
	
	
	public void AddRegister(ActionEvent event) {
		try {
			System.out.println("Patient Register Additional");
			
			Parent printAgree = FXMLLoader.load(getClass().getResource("/application/PatientRegisterAdd.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(printAgree));
			stage.setTitle("Print Agreement");
			stage.showAndWait();

			String newDiagnosis = PassString.getAddTableData();
			System.out.println(newDiagnosis);
			// ���� ���ο� �������� �����ϴ� String�� ������� �ʴٸ� 
			if(!newDiagnosis.equals("")) {
				String[] newRegister = newDiagnosis.split("#");
				
				for(String i : newRegister)
					System.out.print(newRegister + "*" );
				
				// ���� TableView�� �������� �����Ͽ� �߰� 
				patientTable.getItems().add(
						new PatientTableData(newRegister[0], newRegister[2], newRegister[1], newRegister[3])
				);
				
				// ���� �۾� �غ� ���� �ʱ�ȭ
				PassString.setAddTableData("");
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
}
