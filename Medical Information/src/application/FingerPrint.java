package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FingerPrint {
	
	@FXML private Button searchInfor;
	@FXML private Button exitPage;
	@FXML private TextField inputDate;
	@FXML private Label statusInfor;
	
	public void SearchInfor(ActionEvent evnet) {
		try {
			System.out.println("search information");
			
			String fingerData = inputDate.getText();
			ServerConnect server = new ServerConnect();
			String sendString = "Finger/"  + fingerData + "\0";
			boolean judge;
			// ������ �Է°��� ����
			judge = server.sendData(sendString);
			
			if(judge) System.out.println("���������͸� ������ ������ �����Ͽ����ϴ�");
			else System.out.println("���������� ���ۿ� ������ �߻��Ͽ����ϴ�.");
		
			// �����κ��� �����͸� �ް� �ɶ�(����) 
			// ���� �����͸� �����Ͽ� ������ ������ �㰡�ߴ��� �Ǵ�
			String receiveData = server.receiveData();
			
			// ������ ���� �����͸� �ް�, �ش� �����Ͱ� �´��� ��.
			String[] userData = receiveData.split("/");
			
			boolean dataAccess = false;
			if(userData[0].equals("exist")) {
				dataAccess = true;
				
				System.out.println("Exist Patient Data");
				
				
				SetData serverData = new SetData();
				serverData.setServerData(userData[1]); // �����κ��� ���� ���� ȭ���� ����� ��ü�� ���� 
				
				Parent fingerPage = FXMLLoader.load(getClass().getResource("/application/PatientView.fxml"));
				Stage stage = new Stage();
				stage.setScene(new Scene(fingerPage));
				stage.setTitle("Patient Register");
				stage.show();
			}
			else {
				dataAccess = false;
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("�˸�");
				alert.setHeaderText("��ϵ��� �ʴ� ȯ���Դϴ�.");
				alert.showAndWait();
			}
			
			System.out.println(dataAccess);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void exitButton(ActionEvent evnet) {
		try {
			Stage stage = (Stage) exitPage.getScene().getWindow();
			stage.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}

//https://okky.kr/article/56870


