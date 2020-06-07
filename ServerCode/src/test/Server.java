package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	public static void main(String[] args) {
		new ServerProcessor(); // ServerProcessor ��ü ����
	}
}


class ServerProcessor {
	public ServerProcessor() { // ServerProcessro ��ü�� �������ڸ��� �ٷ� ����
		try {
			ServerSocket server = new ServerSocket(3000); // ��Ĺ ���� ��Ʈ ���� + ���� ����(��ü) ����
			System.out.println("3000�� ��Ʈ���� Ŭ���̾�Ʈ�� ������ ��ٸ��ϴ�...");
			
			while (true) { // client�� ������ ��� �����鼭 ������ ��� �����ϴ� ����
				try {
					// client�� �޾Ҵ���, �����带 ����߱� ������, �߰��� client�� ���� �� �ִ�.
					Socket client = server.accept(); // ���� ��ü�� client ������ �ޱ⸦ ��� 
					
					ClientProcessor processor = new ClientProcessor(client); // ������ client�� ClientProcessor�� �޼ҵ��� ���ڷ�
					processor.start(); // ClientProcessor�� �����尡 ����ǵ��� ��.(������� start�� �ؾ� ���۵ȴ�.)
					
					System.out.println("Ŭ���̾�Ʈ�� �����߽��ϴ�: "+client.toString()); // Ŭ���̾�Ʈ�� �����
				} catch (IOException e) { e.printStackTrace(); } // ����ó��
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	
	public class ClientProcessor extends Thread { // ������� ��ӹ޾� clientProcessor Ŭ������ ����.
		private final Socket client;
		
		public ClientProcessor(Socket client) {
			this.client = client; // client�� ClientProcessor Ŭ������ Socket�� ����  
		}
		
		@Override
		public void run() { // �����尡 ����Ǵ� �κ�
			try {
				// client���� �����͸� �޾ƿ��� ���� InputStream ����\
				// InputStream�� �̿��ص� client�� ���� �����͸� ���� �� ������ byte�� �޾ƿ��� ������ 
				InputStream baseInputStream = client.getInputStream();
				// InputStreamReader�� ����Ͽ� ���ڿ� ������ ���ϰ� ó���� �� �ְ� �ϸ�
				InputStreamReader inputStreamReader = new InputStreamReader(baseInputStream);
				// BufferedReader�� �̿��� ���� ������ �Ѳ����� �о� �� �� �ְ� �մϴ�.
				BufferedReader prettyInput = new BufferedReader(inputStreamReader);
				
				// 1:n ��� �׽�Ʈ�� �ڵ�
				/*for (int sec = 0; sec <= 30; sec++) {
					try {
						prettyOutput.write("Seconds: "+sec+"\n"); // client���� ����� ���ڿ�
						prettyOutput.flush(); // ���۸� ���鼭 client���� ���ڿ� ���� 
						
						System.out.println("Sent message to "+client.toString()+": Seconds: "+sec);
						
						Thread.sleep(1000); // �����尡 1�� ����.
						
					// �����尡 ����ٰ� �ٽ� �����Ҷ� Interrupt�� �߻���Ű�⿡
					// ���ͷ�Ʈ �߻� �� ����� ������ InturrptedException�� ó�����ֱ� ���� ���
					} catch (InterruptedException e) {} 
				}*/
				
				
				String request = ""; // client�� ���� ���� ����(data)�� ������ ���ڿ�
				char read; // client�� ���� �����͸� �� ���ھ� ����
				
				// client�� ����Ǿ� �������� �ݺ�
				// client�� ������ �������� �ݺ��� ���� 
				while ((read = (char)prettyInput.read()) != -1) { // read()�� ������ �������� -1�� ��ȯ   
					if (read == '\0') { // �� ���ھ� �о��� �� client�� ���� �����Ͱ� '\0'�̸�   
						sendResponseToClient(request); // client���� �޼����� ���� �޼ҵ� ȣ�� 
						
						request = ""; // ���� request�� ���� �ʱ�ȭ 
					} else request += read; // '\0'�� ������ ������ read ������ �� ���ھ� ���� �����͸� request�� ����
				}
				
				client.close(); // client�� ���� �۾��� �������Ƿ� ������ ����.
			} catch (IOException e) { e.printStackTrace(); }
		}
		
		public void sendResponseToClient(String request) throws IOException {
			// �����͸� client�� ������ ���� OutputStream ����
			// OutputStream�� ����ص� ����� ����������, �ش� �Լ��� ������ byte�� ����մϴ�
			OutputStream baseOutputStream = client.getOutputStream(); 
			// �׷��� byte�� String���� ��ȯ�Ͽ� ����� �� �ְ��ϴ� OutputStreamWriter�� ���������,
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(baseOutputStream);
			// ���ۼӵ��� ����� ���ؼ� BufferedWriter�� ����߽��ϴ�. ��������� OutputStream�� ����� �Ͱ� �����ϴ�.
			BufferedWriter prettyOutput = new BufferedWriter(outputStreamWriter);
			
			// client�� ���� String�� " "�� �������� ���� 
			String[] splited = request.split(" ");
			if (splited[0].equals("id")) { // ���� ������ ù ��° ���ڿ��� "id"�̸�
				if (splited[1].equals("kim_chulsu")) { // ���� ������ �� ��° ���ڿ��� "kim_chulsu"�̸�(����� ����)
					prettyOutput.write("�ȳ��ϼ���, ��ö����!\n"); // �ش� ���ڿ��� client���� ���ڿ� ��� 
				} else {
					prettyOutput.write("��ϵ��� ���� ���̵��Դϴ�!\n"); // �ش� ���ڿ���  client���� ���ڿ� ���
				}

				prettyOutput.flush(); // ���۸� ���鼭 client���� ������ ���� 
			}
			
			// ������ �α� �ۼ� 
			System.out.print("������ ���� ��û�� �޾ҽ��ϴ�: "+request+"\n����û�� Ŭ���̾�Ʈ: "+client.toString());
		}
	}
}