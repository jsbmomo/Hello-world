package application;

// 클래스 간의 데이터를 공유하기 위해 작성한 클래스 
public class PassString {
	
	private static String serverDataPass = "";
	private static String patientData = "";
	private static String addRegister = "";
	
	// 서버에 보낸 지문 데이터(=id)를 기억하여 추후 환자의 진료기록을 
	// 추가할 때, DB내에서 일치하는 id에 저장하기 위함(RegisterAdd에서 재활용)
	public static String getFingerData() {
		return patientData;
	}
	
	public static void setFingerData(String data) {
		patientData = data;
		System.out.println("현재 인증한 지문 데이터는 : " + data);
	}
	
	
	// 서버로부터 받은 환자의 개인정보 및 진료 기록을 
	// FingerPrint에서 PatientData로 옮김
	public static void setReceiveData(String serverData) {
		serverDataPass = serverData;
		System.out.println("RECEIVE : " + serverData);
	}
	
	public static String getReceiveData() {
		return serverDataPass;
	}
	
	
	// 새로 추가한 진료기록을 TableView에 바로 추가히기 위해 사용
	public static String getAddTableData() {
		return addRegister;
	}
	
	public static void  setAddTableData(String addDiagnosis) {
		addRegister = addDiagnosis;
		System.out.println("테이블에 추가된 데이터는 : " + addRegister);
	}
}
