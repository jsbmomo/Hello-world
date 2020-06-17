package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddPatient {

	@FXML private TextField txtname;
	@FXML private TextField txtsex;
	@FXML private TextField txtage;
	@FXML private TextField txtphone;
	@FXML private TextField txtsocial;
	@FXML private TextField txtjob;
	@FXML private Label status;
	@FXML private Button btnExit;
	@FXML private Button btnCreate;
	
	public void CreatePatient(ActionEvent event) {
		String[] patientInfo = new String[6];
		
		System.out.println("Send Data");
		
		patientInfo[0] = txtname.getText();
		patientInfo[1] = txtsex.getText();
		patientInfo[2] = txtage.getText();
		patientInfo[3] = txtsocial.getText();
		patientInfo[4] = txtphone.getText();
		patientInfo[5] = txtjob.getText();
		
		
		boolean judge = true;
		for(String i : patientInfo) {
			System.out.print(i + " ");// 만약 아무런 데이터가 들어있지 않다면 
			if(i .equals("")) {
				judge = false; 
				break;
			}
		}
		
		if(judge) {// 서버로 데이터를 전송하지 않음
			ServerConnect server = new ServerConnect();
			String sendServer = "Patient/";
			for(String info : patientInfo) {
				sendServer += info + " ";
			}
			sendServer += '\0';
			System.out.println("서버로 보내는 환자 데이터 : " + sendServer);
			server.sendData(sendServer);
			
			// 받은 데이터를 분할하여 서버가 접근을 허가했는지 판단
			String receiveData = server.receiveData();
			
			// 서버로 부터 데이터를 받고, 해당 데이터가 맞는지 비교.
			String[] response = receiveData.split(" ");
			
			// 서버로부터 응답을 받아 DB에 데이터가 성공적으로 저장되었는지 확인 
			if(response[0].equals("Done")) {
				Alert alert = new Alert(AlertType.INFORMATION);
				
				alert.setTitle("Information");
				alert.setWidth(400);
				alert.setHeaderText(null);
				alert.setContentText("성공적으로 환자를 추가하였습니다");
				alert.showAndWait();
				
				txtname.clear();
				txtage.clear();
				txtsex.clear();
				txtphone.clear();
				txtsocial.clear();
				txtjob.clear();
			} else if(response[0].equals("Not")){
				status.setText("이미 등록된 환자입니다.");
				status.setTextFill(Color.rgb(210,39,30));
			} else {
				status.setText("환자를 추가하지 못했습니다.");
				status.setTextFill(Color.rgb(210,39,30));
			}
			
		} else {
			status.setText("빈칸을 채워주세요!");
			status.setTextFill(Color.rgb(210,39,30));
		}
	}
	
	public void ExitPage(ActionEvent event) {
		try {
			Stage stage = (Stage) btnExit.getScene().getWindow();
			stage.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
