package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class AlertPage {
	
	// ���α׷� ������ ���� �˸�â�� ����ϴ� Ŭ���� �Դϴ�.
	// ����ڿ��� ������ �ȳ� ���� and ���, ����, ���� â�� � â�� ��� ������ ����
	// selectPage 1 = ���â / 2 = ����â / 3 = ����â 
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
			System.out.println("�ش� �ȳ�â�� �������� �ʽ��ϴ�.");
			break;
		}
		
		alert.setContentText(message);
		
		if(header == null) alert.setHeaderText(null);
		else alert.setHeaderText(header);
		
		alert.showAndWait();
	}
	
}
