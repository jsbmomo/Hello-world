package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class RegisterAdd  {
	@FXML private TextField txtHospital;
	@FXML private TextField txtDocter;
	@FXML private TextField txtRegister;
	@FXML private Button btnSendData;
	
	public void  newRegisterData(ActionEvent event) {
		
		String[] record = new String[3];
		record[0] = txtHospital.getText();
		record[1] = txtDocter.getText();
		record[2] = txtRegister.getText();
		
		boolean judgeRecordNull = true;
		for(String data : record) {
			if(data == null || data.trim().isEmpty()) { // 값이 null이거나 비었거나, Whitespace일 경우를 판단
				judgeRecordNull = false;
				break;
			}
		}
		
		if(judgeRecordNull) {
			ServerConnect server = new ServerConnect();
			String sendString = "Diagnosis/" + PassString.getFingerData() + '#' + record[0] + '#' + record[1] + '#' + record[2] + '\0';
			
			System.out.println(sendString);
			server.sendData(sendString);
			
			// 서버로부터 데이터를 받아옴 
			String[] receiveToServer = server.receiveData().split("/",2);
			
			if(receiveToServer[0].equals("NewData")) {
				new AlertPage("성공적으로 진료 기록을 추가하였습니다.", 2, null);
				
				// TableView에 새로 추가한 진료기록을 바로 업로드
				System.out.println("진료기록 " + receiveToServer[1] + "을 TableView에 추가합니다.");
				PassString.setAddTableData(receiveToServer[1]);
				
				Stage stage = (Stage) btnSendData.getScene().getWindow();
				stage.close();
				
			} else {
				new AlertPage("진료기록을 추가하지 못했습니다.", 2, "모든 정보를 입력하셨는지 확인해주세요.");
			}
			
		} else {
			new AlertPage("데이터를 모두 입력해야 합니다.", 1, null);
		}
		
	}
}
