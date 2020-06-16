package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class StaffPage implements Initializable{

	@FXML private Pane staffPage;
	@FXML private Button backToLogin;
	@FXML private Button printDocu;
	@FXML private Button printFinger;
	@FXML private Button addToPatient;
	
	public void initialize(URL location, ResourceBundle resources) {
		backToLogin.setOnAction(e->loginOut(e));
	}
	
	
	public void loginOut(ActionEvent event) {
		try {
			StackPane root = (StackPane)backToLogin.getScene().getRoot();
			
			System.out.println("Check  remove Button");
			staffPage.setTranslateX(0); // x축의 시작 위치 지정 
			
			// 스태프만 사용할 수 있는 페이지에서 다시 로그인 페이지로 넘어갈때 모션 등을 설정하는 코드
			Timeline timeline = new Timeline();
			KeyValue keyValue = new KeyValue(staffPage.translateXProperty(), 450);
			KeyFrame keyFrame = new KeyFrame( 
				Duration.millis(400),
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent evnet) {
						root.getChildren().remove(staffPage);
					}
				},
				keyValue
			);
			
			timeline.getKeyFrames().add(keyFrame);
			timeline.play();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 만약 문서 출력 버튼을 눌렀을 경우
	public void printDocument(ActionEvent evnet) {  
		try {
			System.out.println("print document");
			
			Parent printAgree = FXMLLoader.load(getClass().getResource("/application/"));
			Stage stage = new Stage();
			stage.setScene(new Scene(printAgree));
			stage.setTitle("Print Agreement");
			stage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 만약 지문 입력 버튼을 눌렀을 경우
	public void inputFingerprint(ActionEvent event) {  
		try {
			System.out.println("click Input Fingerprint Button");
			
			Parent fingerPage = FXMLLoader.load(getClass().getResource("/application/FingerInput.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(fingerPage));
			stage.setTitle("Search Patient Data");
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// DB에 등록되지 않은 환자의 데이터를 추가로 넣을 경우 실행 
	public void addPatient(ActionEvent event) {  
		try {
			System.out.println("Add patient!");
			
			Parent patient = FXMLLoader.load(getClass().getResource("/application/AddPatientInfo.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(patient));
			stage.setTitle("환자 추가");
			stage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
