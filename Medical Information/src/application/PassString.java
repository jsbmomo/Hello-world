package application;

// Ŭ���� ���� �����͸� �����ϱ� ���� �ۼ��� Ŭ���� 
public class PassString {
	
	private static String serverDataPass = "";
	private static String patientData = "";
	private static String addRegister = "";
	
	// ������ ���� ���� ������(=id)�� ����Ͽ� ���� ȯ���� �������� 
	// �߰��� ��, DB������ ��ġ�ϴ� id�� �����ϱ� ����(RegisterAdd���� ��Ȱ��)
	public static String getFingerData() {
		return patientData;
	}
	
	public static void setFingerData(String data) {
		patientData = data;
		System.out.println("���� ������ ���� �����ʹ� : " + data);
	}
	
	
	// �����κ��� ���� ȯ���� �������� �� ���� ����� 
	// FingerPrint���� PatientData�� �ű�
	public static void setReceiveData(String serverData) {
		serverDataPass = serverData;
		System.out.println("RECEIVE : " + serverData);
	}
	
	public static String getReceiveData() {
		return serverDataPass;
	}
	
	
	// ���� �߰��� �������� TableView�� �ٷ� �߰����� ���� ���
	public static String getAddTableData() {
		return addRegister;
	}
	
	public static void  setAddTableData(String addDiagnosis) {
		addRegister = addDiagnosis;
		System.out.println("���̺� �߰��� �����ʹ� : " + addRegister);
	}
}
