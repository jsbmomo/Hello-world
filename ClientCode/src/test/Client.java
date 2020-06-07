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
			
			// 데이터를 server로 보내기 위한 OutputStream 세팅
			// OutputStream만 사용해도 통신이 가능하지만, 해당 함수는 무조건 byte로 통신합니다
			OutputStream baseOutputStream = server.getOutputStream(); 
			// 그래서 byte를 String으로 변환하여 사용할 수 있게하는 OutputStreamWriter를 사용했으며,
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(baseOutputStream);
			// 전송속도의 향상을 위해서 BufferedWriter를 사용했습니다. 결과적으론 OutputStream를 사용한 것과 같습니다.
			BufferedWriter prettyOutput = new BufferedWriter(outputStreamWriter);
			
			prettyOutput.write("id kim_chulsuaaa\0");
			prettyOutput.flush();
			//asdfasdfas
			// server에서 데이터를 받아오기 위한 InputStream 세팅
			// InputStream만 이용해도 client가 보낸 데이터를 받을 수 있지만 byte로 받아오기 때문에 
			InputStream baseInputStream = server.getInputStream();
			// InputStreamReader를 사용하여 문자열 값을 편리하게 처리할 수 있게 하며
			InputStreamReader inputStreamReader = new InputStreamReader(baseInputStream);
			// BufferedReader를 이용해 한줄 단위로 한꺼번에 읽어 올 수 있게 합니다.
			BufferedReader prettyInput = new BufferedReader(inputStreamReader);
			
			char read;// server에서 한글자씩 받아오기 위한 변수 선언 
			
			// client와 연결되어 있을동안 반복
			// client와 연결이 끊어지면 반복문 종료 
			while ((read = (char)prettyInput.read()) != -1) // read()가 연결이 끊어지면 -1를 반환 
				System.out.print(read); // 읽어온 값을 출력(줄바꿈 없음) 
			
			server.close(); // 소켓 연결 끊음 
		} catch(IOException e){
			e.printStackTrace();
		}
	
	}
}
