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
		new ServerProcessor(); // ServerProcessor 객체 생성
	}
}


class ServerProcessor {
	public ServerProcessor() { // ServerProcessro 객체가 생성되자마자 바로 실행
		try {
			ServerSocket server = new ServerSocket(3000); // 소캣 서버 포트 지정 + 서버 소켓(객체) 생성
			System.out.println("3000번 포트에서 클라이언트의 접속을 기다립니다...");
			
			while (true) { // client의 접속을 계속 받으면서 서버를 계속 실행하는 구간
				try {
					// client를 받았더라도, 스레드를 사용했기 때문에, 추가로 client를 받을 수 있다.
					Socket client = server.accept(); // 소켓 객체에 client 접속을 받기를 대기 
					
					ClientProcessor processor = new ClientProcessor(client); // 접속한 client를 ClientProcessor의 메소드의 인자로
					processor.start(); // ClientProcessor의 스레드가 실행되도록 함.(스레드는 start를 해야 시작된다.)
					
					System.out.println("클라이언트가 접속했습니다: "+client.toString()); // 클라이언트가 연결됨
				} catch (IOException e) { e.printStackTrace(); } // 예외처리
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	
	public class ClientProcessor extends Thread { // 쓰레드로 상속받아 clientProcessor 클래스를 생성.
		private final Socket client;
		
		public ClientProcessor(Socket client) {
			this.client = client; // client를 ClientProcessor 클래스의 Socket에 저장  
		}
		
		@Override
		public void run() { // 쓰레드가 실행되는 부븐
			try {
				// client에서 데이터를 받아오기 위한 InputStream 세팅\
				// InputStream만 이용해도 client가 보낸 데이터를 받을 수 있지만 byte로 받아오기 때문에 
				InputStream baseInputStream = client.getInputStream();
				// InputStreamReader를 사용하여 문자열 값으로 편리하게 처리할 수 있게 하며
				InputStreamReader inputStreamReader = new InputStreamReader(baseInputStream);
				// BufferedReader를 이용해 한줄 단위로 한꺼번에 읽어 올 수 있게 합니다.
				BufferedReader prettyInput = new BufferedReader(inputStreamReader);
				
				// 1:n 통신 테스트용 코드
				/*for (int sec = 0; sec <= 30; sec++) {
					try {
						prettyOutput.write("Seconds: "+sec+"\n"); // client에게 출력할 문자열
						prettyOutput.flush(); // 버퍼를 비우면서 client에게 문자열 전송 
						
						System.out.println("Sent message to "+client.toString()+": Seconds: "+sec);
						
						Thread.sleep(1000); // 스레드가 1초 멈춤.
						
					// 스레드가 멈췄다가 다시 실행할때 Interrupt를 발생시키기에
					// 인터럽트 발생 시 생기는 예외인 InturrptedException을 처리해주기 위해 사용
					} catch (InterruptedException e) {} 
				}*/
				
				
				String request = ""; // client로 부터 받을 문자(data)를 저장할 문자열
				char read; // client가 보낸 데이터를 한 글자씩 읽음
				
				// client와 연결되어 있을동안 반복
				// client와 연결이 끊어지면 반복문 종료 
				while ((read = (char)prettyInput.read()) != -1) { // read()가 연결이 끊어지면 -1를 반환   
					if (read == '\0') { // 한 글자씩 읽었을 때 client가 보낸 데이터가 '\0'이면   
						sendResponseToClient(request); // client에게 메세지를 보낼 메소드 호출 
						
						request = ""; // 다음 request를 위해 초기화 
					} else request += read; // '\0'을 만나기 전까진 read 변수로 한 문자씩 읽은 데이터를 request에 저장
				}
				
				client.close(); // client에 대한 작업이 끝낫으므로 소켓을 닫음.
			} catch (IOException e) { e.printStackTrace(); }
		}
		
		public void sendResponseToClient(String request) throws IOException {
			// 데이터를 client로 보내기 위한 OutputStream 세팅
			// OutputStream만 사용해도 통신이 가능하지만, 해당 함수는 무조건 byte로 통신합니다
			OutputStream baseOutputStream = client.getOutputStream(); 
			// 그래서 byte를 String으로 변환하여 사용할 수 있게하는 OutputStreamWriter를 사용했으며,
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(baseOutputStream);
			// 전송속도의 향상을 위해서 BufferedWriter를 사용했습니다. 결과적으론 OutputStream를 사용한 것과 같습니다.
			BufferedWriter prettyOutput = new BufferedWriter(outputStreamWriter);
			
			// client가 보낸 String을 " "를 기준으로 분할 
			String[] splited = request.split(" ");
			if (splited[0].equals("id")) { // 만약 분할한 첫 번째 문자열이 "id"이면
				if (splited[1].equals("kim_chulsu")) { // 만약 분할한 두 번째 문자열이 "kim_chulsu"이면(사용자 구분)
					prettyOutput.write("안녕하세요, 김철수님!\n"); // 해당 문자열을 client에게 문자열 출력 
				} else {
					prettyOutput.write("등록되지 않은 아이디입니다!\n"); // 해당 문자열을  client에게 문자열 출력
				}

				prettyOutput.flush(); // 버퍼를 비우면서 client에게 데이터 전송 
			}
			
			// 서버에 로그 작성 
			System.out.print("다음과 같은 요청을 받았습니다: "+request+"\n└요청한 클라이언트: "+client.toString());
		}
	}
}