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
			System.out.print(i + " ");// ���� �ƹ��� �����Ͱ� ������� �ʴٸ� 
			if(i .equals("")) {
				judge = false; 
				break;
			}
		}
		
		if(judge) {// ������ �����͸� �������� ����
			ServerConnect server = new ServerConnect();
			String sendServer = "Patient/";
			for(String info : patientInfo) {
				sendServer += info + " ";
			}
			sendServer += '\0';
			System.out.println("������ ������ ȯ�� ������ : " + sendServer);
			server.sendData(sendServer);
			
			// ���� �����͸� �����Ͽ� ������ ������ �㰡�ߴ��� �Ǵ�
			String receiveData = server.receiveData();
			
			// ������ ���� �����͸� �ް�, �ش� �����Ͱ� �´��� ��.
			String[] response = receiveData.split(" ");
			
			// �����κ��� ������ �޾� DB�� �����Ͱ� ���������� ����Ǿ����� Ȯ�� 
			if(response[0].equals("Done")) {
				Alert alert = new Alert(AlertType.INFORMATION);
				
				alert.setTitle("Information");
				alert.setWidth(400);
				alert.setHeaderText(null);
				alert.setContentText("���������� ȯ�ڸ� �߰��Ͽ����ϴ�");
				alert.showAndWait();
				
				txtname.clear();
				txtage.clear();
				txtsex.clear();
				txtphone.clear();
				txtsocial.clear();
				txtjob.clear();
			} else if(response[0].equals("Not")){
				status.setText("�̹� ��ϵ� ȯ���Դϴ�.");
				status.setTextFill(Color.rgb(210,39,30));
			} else {
				status.setText("ȯ�ڸ� �߰����� ���߽��ϴ�.");
				status.setTextFill(Color.rgb(210,39,30));
			}
			
		} else {
			status.setText("��ĭ�� ä���ּ���!");
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
