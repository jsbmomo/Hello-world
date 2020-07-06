package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class AlertPage {
	
	// 프로그램 내에서 각종 알림창을 출력하는 클래스 입니다.
	// 사용자에게 보여줄 안내 문구 and 경고, 정보, 에러 창중 어떤 창을 띄울 것인지 선택
	// selectPage 1 = 경고창 / 2 = 정보창 / 3 = 에러창 
	public AlertPage(String message, int selectPage, String header) {
		Alert alert = new Alert(AlertType.NONE);
		
		switch(selectPage) {
		case 1:
			alert.setAlertType(AlertType.WARNING);
			alert.setTitle("Warning");
			break;
		case 2:
			alert.setAlertType(AlertType.INFORMATION);
			alert.setTitle("Information");
			break;
		case 3:
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("Error");
			break;
		default :
			System.out.println("해당 안내창은 존재하지 않습니다.");
			break;
		}
		
		alert.setContentText(message);
		
		if(header == null) alert.setHeaderText(null);
		else alert.setHeaderText(header);
		
		alert.showAndWait();
	}
	
}
