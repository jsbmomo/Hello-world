package application;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class NewAccount {
	
	@FXML private Pane createAccountPane; // ���� �ǳ�
	@FXML private TextField txtName; // ������� �����͸� �Է¹޴� ������ 6��
	@FXML private TextField txtID;
	@FXML private TextField txtQualification;
	@FXML private TextField txtPhone;
	@FXML private PasswordField txtPassword;
	@FXML private PasswordField txtPassCheck;
	@FXML private Label lblStatus;  // ���� �߻��� ���� �޼��� ��� 
	@FXML private Button btnReturn; // ��ư ���� 
	@FXML private Button btnNewAccount;
	/*
	private static final int LIMIT_ID = 30;
	private static final int LIMIT_PW = 50;
	private static final int LIMIT_Qu = 7;
	private static final int LIMIT_NAME = 10;
	private static final int LIMIT_Phone = 11;
	*/
	
	public void createNewAccount(ActionEvent event) {
		try {
			String newNameStr = txtName.getText();
			String newIDStr = txtID.getText();
			String newQualification = txtQualification.getText();
			String newPhoneNum = txtPhone.getText();
			String newPassStr = txtPassword.getText();
			String checkPassStr = txtPassCheck.getText();
			
			ServerConnect server = new ServerConnect();
			boolean connectionCheck = false;
			String createJudge; // ������ ���������� �������� �Ǵ�(���̵�, ��� �� ��ġ�� ���� �Ǵ�) 
			
			if(newPassStr.equals(checkPassStr)) {	// ��й�ȣ�� ��ġ�ϴ��� Ȯ�� 
				// ��� ����� �����͸� ������ �� �� ���̵�, �Ƿ��ڰݹ�ȣ�� ������ �����Ϳ� ��ġ���� Ȯ�� 
				String sendNewData = "NewAcount/" + newIDStr + ' ' + newPassStr + ' ' + newQualification + ' '
						+ newNameStr + ' ' + newPhoneNum + '\0';
				
				connectionCheck = server.sendData(sendNewData);
				
				if(connectionCheck) { 
					System.out.println("Client :  �����͸� ���������� ������ �����߽��ϴ�.");
				} else {
					System.out.println("��Ʈ��ũ ������ Ȯ�����ּ���.");
					lblStatus.setText("check your internet connection");
					lblStatus.setTextFill(Color.rgb(210,39,30));
				}
			} else {
				lblStatus.setText("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
				lblStatus.setTextFill(Color.rgb(210,39,30));
			}
			
			// ȸ�������� �����ϴ� �κ� 
			createJudge = server.receiveData();
			System.out.println(createJudge);
			String[] serverData = createJudge.split(" ");
			
			
			if(serverData[0].equals("ID_Error")) { // ���� DB���� �����ϴ� ���̵���
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Information");
				
				alert.setContentText("�̹� �����ϴ� ID�Դϴ�.");
				alert.setHeaderText(null);
				alert.showAndWait();

				lblStatus.setText("�̹� �����ϴ� ID�Դϴ�.");
				lblStatus.setTextFill(Color.rgb(210,39,30));
			} 
			else if(serverData[0].equals("Qualification_Error")) { // ���� DB���� �����ϴ� �ڰݹ�ȣ���
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Information");
				
				alert.setContentText("�̹� ��ϵ� �ڰݹ�ȣ �Դϴ�.");
				alert.setHeaderText(null);
				alert.showAndWait();
				
				lblStatus.setText("�̹� ��ϵ� �Ƿ��ڰ� ��ȣ�Դϴ�.");
				lblStatus.setTextFill(Color.rgb(210,39,30));				
			}
			else if(serverData[0].equals("Created")) { // ���� ���������� DB�� �߰��Ǿ��ٸ�
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				
				alert.setContentText("������ �����Ͽ����ϴ�.");
				alert.setHeaderText(null);
				alert.showAndWait();
				
				lblStatus.setText("���������� ������ �����Ͽ����ϴ�!");
				lblStatus.setTextFill(Color.rgb(21,117,84));
				
				// �۾��� ���� �� �ؽ�Ʈ�ڽ� �� ���¸޼��� �ʱ�ȭ
				txtName.clear();
				txtID.clear();
				txtQualification.clear();
				txtPhone.clear();
				txtPassword.clear();
				txtPassCheck .clear();
			} 
			else {
				System.out.println("�߸��� ���� ���Ź޾ҽ��ϴ�.");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// ���� ���� â �ݱ� 
	public void returnButton(ActionEvent evnet) {
		try {
			Stage stage = (Stage) btnReturn.getScene().getWindow();
			stage.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
