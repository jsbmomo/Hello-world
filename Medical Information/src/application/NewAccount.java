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
	
	@FXML private Pane createAccountPane; // 메인 판넬
	@FXML private TextField txtName; // 사용자의 데이터를 입력받는 데이터 6개
	@FXML private TextField txtID;
	@FXML private TextField txtQualification;
	@FXML private TextField txtPhone;
	@FXML private PasswordField txtPassword;
	@FXML private PasswordField txtPassCheck;
	@FXML private Label lblStatus;  // 문제 발생시 상태 메세지 출력 
	@FXML private Button btnReturn; // 버튼 생성 
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
			String createJudge; // 계정을 최종적으로 생성할지 판단(아이디, 비번 등 겹치는 지도 판단) 
			
			if(newPassStr.equals(checkPassStr)) {	// 비밀번호가 일치하는지 확인 
				// 모든 사용자 데이터를 보내고 그 중 아이디, 의료자격번호가 기존의 데이터와 겹치는지 확인 
				String sendNewData = "NewAcount/" + newIDStr + ' ' + newPassStr + ' ' + newQualification + ' '
						+ newNameStr + ' ' + newPhoneNum + '\0';
				
				connectionCheck = server.sendData(sendNewData);
				
				if(connectionCheck) { 
					System.out.println("Client :  데이터를 정상적으로 서버로 전송했습니다.");
				} else {
					System.out.println("네트워크 연결을 확인해주세요.");
					lblStatus.setText("check your internet connection");
					lblStatus.setTextFill(Color.rgb(210,39,30));
				}
			} else {
				lblStatus.setText("비밀번호가 일치하지 않습니다.");
				lblStatus.setTextFill(Color.rgb(210,39,30));
			}
			
			// 회원가입을 결정하는 부분 
			createJudge = server.receiveData();
			System.out.println(createJudge);
			String[] serverData = createJudge.split(" ");
			
			
			if(serverData[0].equals("ID_Error")) { // 만약 DB내에 존재하는 아이디라면
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Information");
				
				alert.setContentText("이미 존재하는 ID입니다.");
				alert.setHeaderText(null);
				alert.showAndWait();

				lblStatus.setText("이미 존재하는 ID입니다.");
				lblStatus.setTextFill(Color.rgb(210,39,30));
			} 
			else if(serverData[0].equals("Qualification_Error")) { // 만약 DB내에 존재하는 자격번호라면
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Information");
				
				alert.setContentText("이미 등록된 자격번호 입니다.");
				alert.setHeaderText(null);
				alert.showAndWait();
				
				lblStatus.setText("이미 등록된 의료자격 번호입니다.");
				lblStatus.setTextFill(Color.rgb(210,39,30));				
			}
			else if(serverData[0].equals("Created")) { // 만약 정상적으로 DB에 추가되었다면
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				
				alert.setContentText("계정을 생성하였습니다.");
				alert.setHeaderText(null);
				alert.showAndWait();
				
				lblStatus.setText("성공적으로 계정을 생성하였습니다!");
				lblStatus.setTextFill(Color.rgb(21,117,84));
				
				// 작업이 끝난 후 텍스트박스 및 상태메세지 초기화
				txtName.clear();
				txtID.clear();
				txtQualification.clear();
				txtPhone.clear();
				txtPassword.clear();
				txtPassCheck .clear();
			} 
			else {
				System.out.println("잘못된 값을 수신받았습니다.");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 계정 생성 창 닫기 
	public void returnButton(ActionEvent evnet) {
		try {
			Stage stage = (Stage) btnReturn.getScene().getWindow();
			stage.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
