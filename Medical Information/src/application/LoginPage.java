package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginPage implements Initializable {

	@FXML private Label lblStatus;
	@FXML private TextField txtUserName;
	@FXML private PasswordField  txtPassword;
	@FXML private Button btnLogin;
	@FXML private Button btnNewAccount;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnLogin.setOnAction(e->{
			try {
				Login(e);
			} catch (Exception e1) {
				e1.printStackTrace();	
			}
		});
	}
	
	
	public void Login(ActionEvent event) throws Exception {

		// 사용자에게 입력 ID, PW를 입력 받음
		String idStr = txtUserName.getText();
		String pwStr = txtPassword.getText();
		ServerConnect server = new ServerConnect();
		boolean connectionCheck;
		
		String sendData = "User/" + idStr + ' ' +  pwStr + "\0"; // 보낼 데이터 입력
		
		// ServerConnect에 기록된 서버로 데이터를 보내고 
		// 만약 문제가 있을 경우 false를 문제가 없을 경우 true를 반환
		connectionCheck = server.sendData(sendData); 
		
		// 문제가 없다면 실행 = 데이터를 정상적으로 보냈다면 실행
		if(connectionCheck) { 
			System.out.println("로그인 데이터를 정상적으로 서버로 전송했습니다.");
		} else {
			System.out.println("네트워크 연결을 확인해주세요.");
			lblStatus.setText("check your internet connection");
			lblStatus.setTextFill(Color.rgb(210,39,30));
		}
		
		// 받은 데이터를 분할하여 서버가 접근을 허가했는지 판단
		String receiveData = server.receiveData();
		boolean loginJudge = false;
		
		// 서버로 부터 접근이 허용된다는 데이터를 받고, 해당 데이터가 맞는지 비교.
		// 맞으면 로그인 성공
		String[] userData = receiveData.split(" ");
		if(userData[0].equals("access")) {
			loginJudge = true;
		}  else {
			loginJudge = false;
		}

		System.out.println(loginJudge);
		// 로그인을 성공하였을 경우 실행 
		if(loginJudge)  {
			System.out.println("Login Success");
			lblStatus.setText("Login Success");
			lblStatus.setTextFill(Color.rgb(21,117,84));
			
			// 로그인 페이지에서 로그인 성공시 화면 넘김 
			try {
				Parent  staffPage = FXMLLoader.load(getClass().getResource("/application/HospitalStaff.fxml"));
				StackPane moveStaffPage = (StackPane)  btnLogin.getScene().getRoot();
				moveStaffPage.getChildren().add(staffPage);
				
				// javafx에서 Staff 페이지로 화면을 넘길 때 x축 시작 위치 지정
				staffPage.setTranslateX(450);  
				Timeline timeline = new Timeline();
				KeyValue keyValue = new KeyValue(staffPage.translateXProperty(), 0);
				KeyFrame keyFrame = new KeyFrame(Duration.millis(400), keyValue);
				timeline.getKeyFrames().add(keyFrame);
				timeline.play();
				
				txtPassword.clear();
				txtUserName .clear();
				lblStatus.setText(null);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			lblStatus.setText("Check your ID or password");
			lblStatus.setTextFill(Color.rgb(210,39,30));
		}
	}
	
	
	public void NewAccount(ActionEvent event) throws Exception {
		try {
			System.out.println("Click Create New Account ");
			
			Parent newAcount = FXMLLoader.load(getClass().getResource("/application/NewCreateAccount.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(newAcount));
			stage.setTitle("Create new account");
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
