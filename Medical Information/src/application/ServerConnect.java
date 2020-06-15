package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerConnect {
	
	private Socket server;
	
	
	// Ŭ���̾�Ʈ�� ������ �����͸� ������ ��� 
	public boolean sendData(String sendToServer) {
		try {
			Socket server = new Socket("127.0.0.1", 3000);
			
			this.server = server;
			
			// ������ �����͸� ������ ���� ����(���ڿ�)�� �غ�
			// OutputStream�� ����ص� ����� ����������, �ش� �Լ��� ������ byte�� ����մϴ�
			OutputStream baseOutputStream = server.getOutputStream(); 
			// �׷��� byte�� String���� ��ȯ�Ͽ� ����� �� �ְ��ϴ� OutputStreamWriter�� ���������,
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(baseOutputStream);
			// ���ۼӵ��� ����� ���ؼ� BufferedWriter�� ����߽��ϴ�. ��������� OutputStream�� ����� �Ͱ� �����ϴ�.
			BufferedWriter prettyOutput = new BufferedWriter(outputStreamWriter);
			
			System.out.println(sendToServer);
			
			prettyOutput.write(sendToServer); // ���ۿ� ������ �ۼ�
			prettyOutput.flush(); // ������ ������ ���� 
			
			return true;
		} catch(IOException e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	
	// Ŭ���̾�Ʈ�� �����κ��� �����͸� ������ ���
	public String receiveData() {
		try {
			// �����κ��� �����͸� �ް� �ɶ�(����) 
			InputStream baseInputStream = server.getInputStream();
			// InputStreamReader�� ����Ͽ� ���ڿ� ������ ���ϰ� ó���� �� �ְ� �ϸ�
			InputStreamReader inputStreamReader = new InputStreamReader(baseInputStream);
			// BufferedReader�� �̿��� ���� ������ �Ѳ����� �о� �� �� �ְ� �մϴ�.
			BufferedReader clientReceive = new BufferedReader(inputStreamReader);
			
			String returnData = "";
			char dataRead;
			
			// �� ���ھ� �о ��ȯ�� String�� �߰�. ���������� �����κ��� ���� ���ڿ��� ��ȯ 
			while((dataRead = (char)clientReceive.read())!= -1){
				if(dataRead == '\0') {
					break;
				} else {
					returnData += dataRead;
				} 
			}		
			server.close();
			
			System.out.println("Send to server : " + returnData);
			return returnData; // ��ȯ 
		
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null; // ��� �������� �׳� ���� 
	}
}
