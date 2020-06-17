package application;

public class PassString {
	
	private static String serverDataPass = "";
	private static String patientData = "";
	
	public static void setReceiveData(String serverData) {
		serverDataPass = serverData;
		System.out.println("RECEIVE : " + serverData);
	}
	
	public static void setFingerData(String data) {
		patientData = data;
		System.out.println("현재 인증한 지문 데이터는 : " + data);
	}
	
	public static String getReceiveData() {
		return serverDataPass;
	}
	
	public static String getFingerData() {
		return patientData;
	}
}
