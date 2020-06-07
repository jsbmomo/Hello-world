package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
	public static void main(String[] args) {
		try {
			Socket server = new Socket("ec2.firstfloor.pe.kr", 3000);
			
			// �����͸� server�� ������ ���� OutputStream ����
			// OutputStream�� ����ص� ����� ����������, �ش� �Լ��� ������ byte�� ����մϴ�
			OutputStream baseOutputStream = server.getOutputStream(); 
			// �׷��� byte�� String���� ��ȯ�Ͽ� ����� �� �ְ��ϴ� OutputStreamWriter�� ���������,
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(baseOutputStream);
			// ���ۼӵ��� ����� ���ؼ� BufferedWriter�� ����߽��ϴ�. ��������� OutputStream�� ����� �Ͱ� �����ϴ�.
			BufferedWriter prettyOutput = new BufferedWriter(outputStreamWriter);
			
			prettyOutput.write("id kim_chulsuaaa\0");
			prettyOutput.flush();
			//asdfasdfas
			// server���� �����͸� �޾ƿ��� ���� InputStream ����
			// InputStream�� �̿��ص� client�� ���� �����͸� ���� �� ������ byte�� �޾ƿ��� ������ 
			InputStream baseInputStream = server.getInputStream();
			// InputStreamReader�� ����Ͽ� ���ڿ� ���� ���ϰ� ó���� �� �ְ� �ϸ�
			InputStreamReader inputStreamReader = new InputStreamReader(baseInputStream);
			// BufferedReader�� �̿��� ���� ������ �Ѳ����� �о� �� �� �ְ� �մϴ�.
			BufferedReader prettyInput = new BufferedReader(inputStreamReader);
			
			char read;// server���� �ѱ��ھ� �޾ƿ��� ���� ���� ���� 
			
			// client�� ����Ǿ� �������� �ݺ�
			// client�� ������ �������� �ݺ��� ���� 
			while ((read = (char)prettyInput.read()) != -1) // read()�� ������ �������� -1�� ��ȯ 
				System.out.print(read); // �о�� ���� ���(�ٹٲ� ����) 
			
			server.close(); // ���� ���� ���� 
		} catch(IOException e){
			e.printStackTrace();
		}
	
	}
}
