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
			
			// �����κ��� �����͸� �޾ƿ� 
			String[] receiveToServer = server.receiveData().split("/",2);
			
			if(receiveToServer[0].equals("NewData")) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setContentText("���������� ���� ����� �߰��Ͽ����ϴ�.");
				alert.setHeaderText(null);
				alert.showAndWait();
				
				// TableView�� ���� �߰��� �������� �ٷ� ���ε�
				System.out.println("������ " + receiveToServer[1] + "�� TableView�� �߰��մϴ�.");
				PassString.setAddTableData(receiveToServer[1]);
				
				Stage stage = (Stage) btnSendData.getScene().getWindow();
				stage.close();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Information");
				alert.setContentText("�������� �߰����� ���߽��ϴ�.");
				alert.setHeaderText("��� ������ �Է��ϼ̴��� Ȯ�����ּ���.");
				alert.setHeaderText(null);
				alert.showAndWait();
			}
			
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Information");
			alert.setContentText("�����͸� ��� �Է��ؾ� �մϴ�.");
			alert.setHeaderText(null);
			alert.showAndWait();
		}
		
	}
}
