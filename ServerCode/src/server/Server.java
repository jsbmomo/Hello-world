package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class Server {
	public static void main(String[] args) {
		new ServerProcessor(); // ServerProcessor 객체 생성
	}
}


class ServerProcessor {
	public ServerProcessor() { // ServerProcessro 객체가 생성되자마자 바로 실행
		try {
			// SSLSocket 통신을 위해 사용하는 부분. 같은 프로젝트 안에 java key store가 있어야함 
			//System.setProperty("javax.net.ssl.keyStore", "./server.jks");
			//System.setProperty("javax.net.ssl.keyStorePassword", "123456");
			//System.setProperty("javax.net.debug","ssl");

			//SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			//SSLServerSocket server = (SSLServerSocket) sslServerSocketFactory.createServerSocket(8080); // 소캣 서버 포트 지정 + 서버 소켓(객체) 생성
			ServerSocket server = new ServerSocket(3000);
			
			System.out.println("8080번 포트에서 클라이언트의 접속을 기다립니다...");
			
			while (true) { // client의 접속을 계속 받으면서 서버를 계속 실행하는 구간
				try {
					// client를 받았더라도, 스레드를 사용했기 때문에, 추가로 client를 받을 수 있다.
					//SSLSocket client = (SSLSocket) server.accept(); // 소켓 객체에 client 접속을 받기를 대기
					
					Socket client = server.accept();
					
					ClientProcessor processor = new ClientProcessor(client); // 접속한 client를 ClientProcessor의 메소드의 인자로
					processor.start(); // ClientProcessor의 스레드가 실행되도록 함.(스레드는 start를 해야 시작된다.)
					
					System.out.println("클라이언트가 접근을 시도합니다 : "+client.toString()); // 클라이언트가 연결됨
				} catch (IOException e) { e.printStackTrace(); } // 예외처리
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	
	public class ClientProcessor extends Thread { // 쓰레드로 상속받아 clientProcessor 클래스를 생성.
		//private final SSLSocket client;
		private final Socket client;

		// 연결할 DB에 대한 정보 입력
		private final String driver = "org.mariadb.jdbc.Driver";
		private final String jdburl = "jdbc:mariadb://127.0.0.1:3306/hospital?useUnicode=true&characterEncoding=utf8";
		private final String dbId = "jeon";
		private final String dbPw = "password";
				
		
		public ClientProcessor(Socket client) { // <== SSLSocket 사용시 매개변수룰 바꾸어 주어야함 
			this.client = client; // client를 ClientProcessor 클래스의 Socket에 저장  
		}
		
	
		@Override
		public void run() { // 쓰레드가 실행되는 부븐
			try {
				// client에서 데이터를 받아오기 위한 InputStream 세팅\
				// InputStream만 이용해도 client가 보낸 데이터를 받을 수 있지만 byte로 받아오기 때문에 
				InputStream baseInputStream = client.getInputStream();
				// InputStreamReader를 사용하여 문자열 값으로 편리하게 처리할 수 있게 하며
				InputStreamReader inputStreamReader = new InputStreamReader(baseInputStream, "UTF-8");
				// BufferedReader를 이용해 한줄 단위로 한꺼번에 읽어 올 수 있게 합니다.
				BufferedReader prettyInput = new BufferedReader(inputStreamReader);
				
				// 1:n 통신 테스트용 코드(클라이언트에게 출력)
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
						System.out.println(request);
						String[] ClientData = request.split("/"); 
						
						if(ClientData[0].equals("User")) {
							ReceiveUserData(ClientData[1]); // 로그인을 위한 데이터 일경우 
						}
						else if(ClientData[0].equals("Finger")) {
							ReceiveFingeData(ClientData[1]); // 지문데이터일 경우 
						}
						else if(ClientData[0].equals("NewAcount")){
							ReceiveNewAccount(ClientData[1]); // 새로 계정을 생성하고자 하는 경우 
						} 
						else if(ClientData[0].equals("Patient")) {
							NewPatientData(ClientData[1]);
						}
						else if(ClientData[0].equals("Diagnosis")) {
							NewDiagnosis(ClientData[1]);
						} 
						else {
							System.out.println("잘못된 값을 받았습니다");	
						}
					} else 
						request += read; // '\0'을 만나기 전까진 read 변수로 한 문자씩 읽은 데이터를 request에 저장
				}
				System.out.println("Complited");
				//client.close(); // client에 대한 작업이 끝낫으므로 소켓을 닫음.
			} catch (IOException e) { 
				e.printStackTrace();
			}
		}
		
		
		
		// ================새로운 진료기록을 DB에 추가 
		public void NewDiagnosis(String ClientData) {
			String[] newInformation = ClientData.split("#");
			
			System.out.print("데이터 추가 : ");
			for(String i : newInformation) {
				System.out.print(i + " ");
			}
			System.out.println("------------------");
			
			Connection con = null; // 진료기록을 추가하기위해 사용
			Connection con2 = null; //수정된 진료기록을 클라이언트로 다시 전송 
			PreparedStatement pstmt = null;
			PreparedStatement pstmt2 = null;
			ResultSet rs = null;
			ResultSetMetaData rsmd = null;
			
			String sql = "insert into patient_data (id, hospital, docter, record) values (?,?,?,?)";
			String returnPatientData = "select * from patient_data order by number desc limit 1";
			
			try {
				Class.forName(driver);
				
				con = DriverManager.getConnection(jdburl, dbId, dbPw);

				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, Integer.valueOf(newInformation[0]));
				pstmt.setString(2, newInformation[1]);
				pstmt.setString(3, newInformation[2]);
				pstmt.setString(4, newInformation[3]);
				pstmt.executeUpdate();
			
				
				// 가장 최근에 추가된 데이터를 다시 클라이언트에게 전송 
				String addPatientData = "NewData/";
				
				con2 = DriverManager.getConnection(jdburl, dbId, dbPw);
				
				pstmt2 = con2.prepareStatement(returnPatientData);
				rs = pstmt2.executeQuery();
				rsmd = rs.getMetaData();
				int count = rsmd.getColumnCount();
				
				rs.next();
				for(int i = 3; i <= count; i++ ){
					addPatientData += rs.getNString(i);
					addPatientData += "#";
				}
				addPatientData = addPatientData.substring(0, addPatientData.length() - 1);
				
				System.out.println("Return to add patient data : " + addPatientData);
				
				sendResponseToClient(addPatientData);
				
			}catch(ClassNotFoundException e) {
				System.out.println("해당 테이블을 찾을 수 없습니다.");
			}catch(SQLException e) {
				e.printStackTrace();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(con != null && !con.isClosed()) {
						con.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		// ================클라이언트로가 추가로 환자를 등록할 경우 실행 
		public void NewPatientData(String ClientData) {
			String[] patientInformation = ClientData.split(" ");
			
			System.out.print("환자를 추가합니다 : ");
			for(String i : patientInformation) {
				System.out.print(i + " ");
			}
			
			Connection con = null;
			ResultSet rs = null;
			PreparedStatement pstmt = null;
			
			String sql = "select * from patient_info where social = ?";
			String sql2 = "insert into patient_info (name, sex, age, social, phone, job) values (?,?,?,?,?,?)";
			
			try {
				Class.forName(driver);
				
				con = DriverManager.getConnection(jdburl, dbId, dbPw);
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, patientInformation[3]);
				rs = pstmt.executeQuery();
				
				// 쿼리문을 실행하여 해당 주민등록번호가 존재한다면 "True"
				if(rs.next()) { 
					sendResponseToClient("Not making");
				} else {
					pstmt = con.prepareStatement(sql2);
					pstmt.setString(1, patientInformation[0]); // 이름 입력
					pstmt.setString(2, patientInformation[1]); // 성별 입력
					pstmt.setString(3, patientInformation[2]); // 나이 입력
					pstmt.setString(4, patientInformation[3]); // 주민등록번호 입력
					pstmt.setString(5, patientInformation[4]); // 휴대전화번호 입력
					pstmt.setString(6, patientInformation[5]); // 직업 입력
					
					pstmt.executeUpdate();
					
					sendResponseToClient("Done adding");
				}
			} catch(ClassNotFoundException e) {
				System.out.println("해당 클래스를 찾을 수 없습니다.");
				e.printStackTrace();
			} catch(SQLException e) {
				e.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(con != null && !con.isClosed()) {
						con.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		// ================클라이언트로부터 지문 데이터를 받았을 경우 실행
		public void ReceiveFingeData(String ClientData) {
			try {
				 
				Connection con  = null; 
				ResultSet rs = null; // 초기 환자의 개인정보를 가져오기 위한 객체들
				PreparedStatement pstmt = null;
				ResultSetMetaData rsmd = null;

				String sql = "select * from patient_info where id = ?";
				String sql2 = "select * from patient_data where id = ?";
				
				int number = 0; // DB의 열의 개수를 저장할 변수 
				 
				try {
					Class.forName(driver);
					 
					con = DriverManager.getConnection(jdburl, dbId, dbPw);
					 
					 // 환자의 개인정보를 가져오기 위해 수행 
					pstmt = con.prepareStatement(sql); // 먼저 spt문을 가져오고 
					pstmt.setString(1, ClientData); // ?( = 파라미터) 에 들어갈 값 지정   
					rs = pstmt.executeQuery(); // sql 문 실행하며 값 저장 
					
					rsmd = rs.getMetaData(); // 컬럼의 총 데이터 수를 알기 위해 사용 
					number = rsmd.getColumnCount(); // 데이터 길이만큼 가져와 저장 
					
					// 만약 클라이언트가 요청한 데이터가 있다면 
					if(rs.next()) {
						
						String result = "exist/"; // 클라이언트에게 전송할 문자열 저장 

						for(int i = 2; i <= number; i++) {
							result += rs.getNString(i); // DB내용에서 i번째 행의 내용을 가져옴 
							result += "#"; // 각 데이터를 구분하기위한 구분자 
						}
						
						// 환자의 진료 기록을 가져오기 위해 위의 과정 한번 더 수행 
						pstmt = con.prepareStatement(sql2);
						pstmt.setString(1, ClientData);
						rs = pstmt.executeQuery();
						
						rsmd = rs.getMetaData();
						number = rsmd.getColumnCount();
						
						while(rs.next()) {
						//if(rs.next()) { // 환자의 진료기록이 존재한다면 데이터를 가져옴 
							for(int i = 3; i <= number; i++) {
								result += rs.getNString(i);
								result += "#";
							}
							result = result.substring(0, result.length() - 1); // 마지막에 붙은 # 제거 
							result += "&";
						}
						result = result.substring(0, result.length() - 1); // 마지막에 붙은 & 제거 
						System.out.println(result);
						
						sendResponseToClient(result);
					} else {
						System.out.println("해당 데이터가 존재하지 않습니다.");
						sendResponseToClient("failed");
					}
					 
				 }catch(ClassNotFoundException e) {
					 System.out.println("해당 클래스를 찾을 수 없습니다.");
				 }catch(SQLException e){
					 e.printStackTrace();
				 }finally {
					 try {
							if(con != null && !con.isClosed()) {
								con.close();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
				 }

			 } catch(IOException e) {
				 e.printStackTrace();
			 }
		}
		
		
		// ================ 새로 생성가능한 계정인지 판단 
		public void ReceiveNewAccount(String ClientData) {
			String[] newClient = ClientData.split(" ");
			
			System.out.print("Client가 새로 생성하려는 계정 정보 : ");
			for (String index : newClient) {
				System.out.print(index + "  ");	
			}
			System.out.println();
			
			Connection con = null;

			ResultSet rs = null; // 해당데이터가 DB내에 존재하는지 true, false 반환 
			ResultSet rs2 = null;
			PreparedStatement pstmt = null; 
			PreparedStatement pstmt2 = null;
			PreparedStatement pstmt3 = null;

			String sql = ""; // 아이디가 이미 존재하는지 파악할 SELECT 문
			String sql2 = ""; // 자격 번호가 이미 존재하는지 파악할 SELECT 문
			String sql3 = ""; // 등록되지 않은 정보라면 INSERT로 DB에 등록 
	
			// DB와 연결하여 select 문으로 해당 id 또는 자격번호가 등록되어있는지 확인
			try{  
				Class.forName(driver);
				
				con = DriverManager.getConnection(jdburl, dbId, dbPw);
				System.out.println("DB에 접속하였습니다.");
				
				sql = "select user_id from user_info where user_id = ?";
				sql2 = "select user_number from user_info where user_number = ?";
				
				pstmt = con.prepareStatement(sql);
				pstmt2 = con.prepareStatement(sql2);
				
				pstmt.setString(1, newClient[0]);
				System.out.println(pstmt);
				pstmt2.setString(1, newClient[2]);
				
				// 이미 여기서 쿼리문을 실행하여 해당 id가 DB내에 존재하는지 판단하여, true false를 반환함
				rs = pstmt.executeQuery(); 
				// 여기서 쿼리문을 실행하여 해당 자격번호가 존재하는지 판단하여 true,false를 반환 
				rs2 = pstmt2.executeQuery();
	
				// 그냥 rs를 출력하면 메모리 주소(?)가 뜬다. .next() 통해 true / false 값 파악가능 
				//System.out.println(rs.next() + "     "  + rs2.next());
				try {
					System.out.println("DB 내용과 비교중...");
					if(rs.next()) {
						System.out.println("DB : 이미 존재하는 아이디");
						sendResponseToClient("ID_Error");
					}else if(rs2.next()) {
						System.out.println("DB : 이미 존재하는 자격 번호");
						sendResponseToClient("Qualification_Error");
					} else {
						System.out.println("DB : 새로 추가할 데이터!");
						sql3 = "insert into user_info values(?,?,?,?,?)";
						pstmt3 = con.prepareStatement(sql3);
						pstmt3.setString(1,  newClient[0]); // ID 입력
						pstmt3.setString(2,  newClient[1]); // PW 입력
						pstmt3.setString(3,  newClient[4]); // 전화번호 입력
						pstmt3.setString(4,  newClient[3]); // 이름 입력
						pstmt3.setString(5,  newClient[2]); // 자격번호 입력 
						
						pstmt3.executeUpdate();
						
						sendResponseToClient("Created");
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("해당 클래스를 찾을 수 없습니다.");
			}catch(SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					if(con != null && !con.isClosed()) {
						con.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		// 등록된 사용자인지 확인하는 부분( 등록된 사용자면 사용자에게 반응 )
		// ================ 클라이언트쪽에서 서버로부터 응답을 받아 사용자 구분		
		public void ReceiveUserData(String ClientData) {
			String[] userData = ClientData.split(" ", 2);
			boolean loginDB = false;
			System.out.println("접속을 시도하는 Client의 ID : " + userData[0]);
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			
			// DB에 접속하여 해당 ID와 비밀번호가 있는 지 확인 후 이상이 없으면 access(접근 허가)
			try {  
				
				// DB에 접속하는 코드. 실질적으로 사용자가 등록되어 있는지 확인  -----------------------------
				try{ 
					Class.forName(driver);
					
					con = DriverManager.getConnection(jdburl, dbId, dbPw);
					System.out.println("DB에 접속하였습니다.");
					stmt = con.createStatement(); // 쿼리 수행을 위한 statement 객체 생성 
					
					// user_info 테이블의 user_id / user_pw만 가져오는 sql문 
					String sql = "select * from user_info"; 
					
					rs = stmt.executeQuery(sql);
					
					while(rs.next()) {
						if(rs.getString("user_id").equals(userData[0]) && rs.getString("user_pw").equals(userData[1])) {
							loginDB = true;
							break;
						} 
						System.out.println();
					}
					
					con.close();
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
					System.out.println("해당 클래스를 찾을 수 없습니다.");
				} catch (SQLException e) {
					e.printStackTrace();
				} finally{
					try {
						if(con != null && !con.isClosed()) {
							con.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				// DB 코드가 끝나는 부분 -----------------------------
				
				if(loginDB) {
					sendResponseToClient("access"); // client에게 메세지를 보낼 메소드 호출 
				} else {
					sendResponseToClient("failed"); // client에게 메세지를 보낼 메소드 호출
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		//================데이터를 클라이언트에게 다시 보내기 위한 코드 작성 
		public void sendResponseToClient(String request) throws IOException {
			// 데이터를 client로 보내기 위한 OutputStream 세팅
			// OutputStream만 사용해도 통신이 가능하지만, 해당 함수는 무조건 byte로 통신합니다
			OutputStream baseOutputStream = client.getOutputStream(); 
			// 그래서 byte를 String으로 변환하여 사용할 수 있게하는 OutputStreamWriter를 사용했으며,
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(baseOutputStream, "UTF-8");
			// 전송속도의 향상을 위해서 BufferedWriter를 사용했습니다. 결과적으론 OutputStream를 사용한 것과 같습니다.
			BufferedWriter prettyOutput = new BufferedWriter(outputStreamWriter);
			
			prettyOutput.write(request + '\0'); // 해당 문자열을 client에게 문자열 출력 
			prettyOutput.flush(); // 버퍼를 비우면서 client에게 데이터 전송 

			/*
			 * // client가 보낸 String을 " "를 기준으로 분할 
			String[] splited = request.split(" ");
			if (splited[0].equals("user")) { // 만약 분할한 첫 번째 문자열이 "id"이면
				if (splited[1].equals("pass")) { // 만약 분할한 두 번째 문자열이 "kim_chulsu"이면(사용자 구분)
					prettyOutput.write("access server "); // 해당 문자열을 client에게 문자열 출력 
				} else {
					prettyOutput.write("등록되지 않은 아이디입니다!\n"); // 해당 문자열을  client에게 문자열 출력
				}
				prettyOutput.flush(); // 버퍼를 비우면서 client에게 데이터 전송 
			}
			 */
			
			// 서버에 로그 작성 
			// System.out.print("다음과 같은 요청을 받았습니다: "+splited[0]+"\n└요청한 클라이언트: "+client.toString());
		}
	}
}
