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
	public void initialize()  { // 초기에 화면이 시작하면 실행

		// 정적 메소드를 사용하여 객체(고정된 메모리)에서 값을 가져옴
		String receivePatientData = PassString.getReceiveData();  

		System.out.println("Receive Server Data : " + receivePatientData);
		String[] patientInfo = receivePatientData.split("#", 7);
		
		int count = 0; // 환자의 진료기록이 있다면 patientInfo의 문자열은 총 7개
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

		String[] patientData = null; // 환자의 진료기록을 저장할 문자열
	
		if(count >= 7) {
			
			// 환자의 진단 기록을 날짜 별로 1차 분할
			if(patientInfo[6].contains("&")) {  
				patientData = patientInfo[6].split("&");
			} else {
				patientData = new String[1];
				patientData[0] = patientInfo[6];
			}
			
			// 분할한 환자의 진단 기록을 분류. ArrayList를 통해 
			// 환자의 개인정보 및 진료기록을 객체로 저장 
			classification = new ArrayList<PatientTableData>();
			for(String array : patientData) {
				String[] register = array.split("#");
				classification.add(
						new PatientTableData(register[0], register[2], register[1], register[3])	
				);
			}
			
			// 실제로 TableView의 내용을 생성하는 코드 
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
			// 만약 새로운 진료기록을 저장하는 String이 비어있지 않다면 
			if(!newDiagnosis.equals("")) {
				String[] newRegister = newDiagnosis.split("#");
				
				for(String i : newRegister)
					System.out.print(newRegister + "*" );
				
				// 기존 TableView에 보여지는 진료기록에 추가 
				patientTable.getItems().add(
						new PatientTableData(newRegister[0], newRegister[2], newRegister[1], newRegister[3])
				);
				
				// 다음 작업 준비를 위한 초기화
				PassString.setAddTableData("");
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
}
