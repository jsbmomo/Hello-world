package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class RegisterAdd {
	@FXML private TextField txtHospital;
	@FXML private TextField txtDocter;
	@FXML private TextField txtRegister;
	@FXML private Button btnSendData;
	
	public void newRegisterData(ActionEvent event) {
		
		String[] record = new String[3];
		record[0] = txtHospital.getText();
		record[1] = txtDocter.getText();
		record[2] = txtRegister.getText();
		
		boolean judgeNull = true;
		for(String data : record) {
			if(data.equals(" ")) {
				judgeNull = false;
				break;
			}
		}
		
		if(judgeNull) {
			ServerConnect server = new ServerConnect();
			String sendString = "Diagnosis/" + PassString.getFingerData() + '#' + record[0] + '#' + record[1] + '#' + record[2] + '\0';
			 
			System.out.println(sendString);
			server.sendData(sendString);
			
			String receiveToServer = server.receiveData();
			if(receiveToServer.equals("NewData")) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setContentText("성공적으로 진료 기록을 추가하였습니다.");
				alert.setHeaderText(null);
				alert.showAndWait();
				
				Stage stage = (Stage) btnSendData.getScene().getWindow();
				stage.close();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Information");
				alert.setContentText("진료기록을 추가하지 못했습니다.");
				alert.setHeaderText("모든 정보를 입력하셨는지 확인해주세요.");
				alert.setHeaderText(null);
				alert.showAndWait();
			}
			
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Information");
			alert.setContentText("데이터를 모두 입력해야 합니다.");
			alert.setHeaderText(null);
			alert.showAndWait();
		}
		
	}
}
