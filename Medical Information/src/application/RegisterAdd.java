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
			if(data == null || data.trim().isEmpty()) { // ���� null�̰ų� ����ų�, Whitespace�� ��츦 �Ǵ�
				judgeRecordNull = false;
				break;
			}
		}
		
		if(judgeRecordNull) {
			ServerConnect server = new ServerConnect();
			String sendString = "Diagnosis/" + PassString.getFingerData() + '#' + record[0] + '#' + record[1] + '#' + record[2] + '\0';
			
			System.out.println(sendString);
			server.sendData(sendString);
			
			// �����κ��� �����͸� �޾ƿ� 
			String[] receiveToServer = server.receiveData().split("/",2);
			
			if(receiveToServer[0].equals("NewData")) {
				new AlertPage("���������� ���� ����� �߰��Ͽ����ϴ�.", 2, null);
				
				// TableView�� ���� �߰��� �������� �ٷ� ���ε�
				System.out.println("������ " + receiveToServer[1] + "�� TableView�� �߰��մϴ�.");
				PassString.setAddTableData(receiveToServer[1]);
				
				Stage stage = (Stage) btnSendData.getScene().getWindow();
				stage.close();
				
			} else {
				new AlertPage("�������� �߰����� ���߽��ϴ�.", 2, "��� ������ �Է��ϼ̴��� Ȯ�����ּ���.");
			}
			
		} else {
			new AlertPage("�����͸� ��� �Է��ؾ� �մϴ�.", 1, null);
		}
		
	}
}
