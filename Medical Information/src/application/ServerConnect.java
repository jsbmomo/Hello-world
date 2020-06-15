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
	
	
	// 클라이언트가 서버로 데이터를 보낼때 사용 
	public boolean sendData(String sendToServer) {
		try {
			Socket server = new Socket("127.0.0.1", 3000);
			
			this.server = server;
			
			// 서버에 데이터를 보내기 위한 변수(문자열)를 준비
			// OutputStream만 사용해도 통신이 가능하지만, 해당 함수는 무조건 byte로 통신합니다
			OutputStream baseOutputStream = server.getOutputStream(); 
			// 그래서 byte를 String으로 변환하여 사용할 수 있게하는 OutputStreamWriter를 사용했으며,
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(baseOutputStream);
			// 전송속도의 향상을 위해서 BufferedWriter를 사용했습니다. 결과적으론 OutputStream를 사용한 것과 같습니다.
			BufferedWriter prettyOutput = new BufferedWriter(outputStreamWriter);
			
			System.out.println(sendToServer);
			
			prettyOutput.write(sendToServer); // 버퍼에 데이터 작성
			prettyOutput.flush(); // 서버에 데이터 전송 
			
			return true;
		} catch(IOException e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	
	// 클라이언트가 서버로부터 데이터를 받을때 사용
	public String receiveData() {
		try {
			// 서버로부터 데이터를 받게 될때(응답) 
			InputStream baseInputStream = server.getInputStream();
			// InputStreamReader를 사용하여 문자열 값으로 편리하게 처리할 수 있게 하며
			InputStreamReader inputStreamReader = new InputStreamReader(baseInputStream);
			// BufferedReader를 이용해 한줄 단위로 한꺼번에 읽어 올 수 있게 합니다.
			BufferedReader clientReceive = new BufferedReader(inputStreamReader);
			
			String returnData = "";
			char dataRead;
			
			// 한 글자씩 읽어서 반환할 String에 추가. 최종적으로 서버로부터 받은 문자열을 반환 
			while((dataRead = (char)clientReceive.read())!= -1){
				if(dataRead == '\0') {
					break;
				} else {
					returnData += dataRead;
				} 
			}		
			server.close();
			
			System.out.println("Send to server : " + returnData);
			return returnData; // 반환 
		
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null; // 계속 에러떠서 그냥 넣음 
	}
}
