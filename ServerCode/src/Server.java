package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.control.SplitPane.Divider;


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
					
					System.out.println("Ŭ���̾�Ʈ�� ������ �õ��մϴ� : "+client.toString()); // Ŭ���̾�Ʈ�� �����
				} catch (IOException e) { e.printStackTrace(); } // ����ó��
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	
	public class ClientProcessor extends Thread { // ������� ��ӹ޾� clientProcessor Ŭ������ ����.
		private final Socket client;

		// ������ DB�� ���� ���� �Է�
		private final String driver = "org.mariadb.jdbc.Driver";
		private final String jdburl = "jdbc:mariadb://127.0.0.1:3306/hospital";
		private final String dbId = "jeon";
		private final String dbPw = "password";
				
		
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
				
				// 1:n ��� �׽�Ʈ�� �ڵ�(Ŭ���̾�Ʈ���� ���)
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
						System.out.println(request);
						String[] ClientData = request.split("/"); 
						
						if(ClientData[0].equals("User")) {
							ReceiveUserData(ClientData[1]); // �α����� ���� ������ �ϰ�� 
						}
						else if(ClientData[0].equals("Finger")) {
							ReceiveFingeData(ClientData[1]); // ������������ ��� 
						}
						else if(ClientData[0].equals("NewAcount")){
							ReceiveNewAccount(ClientData[1]); // ���� ������ �����ϰ��� �ϴ� ��� 
						} 
						else if(ClientData[0].equals("Patient")) {
							NewPatientData(ClientData[1]);
						} 
						else {
							System.out.println("�߸��� ���� �޾ҽ��ϴ�");	
						}
					} else 
						request += read; // '\0'�� ������ ������ read ������ �� ���ھ� ���� �����͸� request�� ����
				}
				System.out.println("Complited");
				//client.close(); // client�� ���� �۾��� �������Ƿ� ������ ����.
			} catch (IOException e) { e.printStackTrace(); }
		}
		
		
		// ================Ŭ���̾�Ʈ�ΰ� �߰��� ȯ�ڸ� ����� ��� ���� 
		public void NewPatientData(String ClientData) {
			String[] patientInformation = ClientData.split(" ");
			
			System.out.print("ȯ�ڸ� �߰��մϴ� : ");
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
				
				// �������� �����Ͽ� �ش� �ֹε�Ϲ�ȣ�� �����Ѵٸ� "True"
				if(rs.next()) { 
					sendResponseToClient("Not making");
				} else {
					pstmt = con.prepareStatement(sql2);
					pstmt.setString(1, patientInformation[0]); // �̸� �Է�
					pstmt.setString(2,  patientInformation[1]); // ���� �Է�
					pstmt.setString(3,  patientInformation[2]); // ���� �Է�
					pstmt.setString(4,  patientInformation[3]); // �ֹε�Ϲ�ȣ �Է�
					pstmt.setString(5,  patientInformation[4]); // �޴���ȭ��ȣ �Է�
					pstmt.setString(6,  patientInformation[5]); // ���� �Է�
					
					pstmt.executeUpdate();
					
					sendResponseToClient("Done adding");
				}
			} catch(ClassNotFoundException e) {
				System.out.println("�ش� Ŭ������ ã�� �� �����ϴ�.");
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
		
		
		// ================Ŭ���̾�Ʈ�κ��� ���� �����͸� �޾��� ��� ����
		public void ReceiveFingeData(String ClientData) {
			 try {
				 
				 Connection con  = null; 
				 ResultSet rs = null; // �ʱ� ȯ���� ���������� �������� ���� ��ü��
				 PreparedStatement pstmt = null;
				 ResultSetMetaData rsmd = null;

				 String sql = "select * from patient_info where id = ?";
				 String sql2 = "select * from patient_data where id = ?";
				 
				 int number = 0; // DB�� ���� ������ ������ ���� 
				 
				 try {
					 Class.forName(driver);
					 
					 con = DriverManager.getConnection(jdburl, dbId, dbPw);
					 
					 // ȯ���� ���������� �������� ���� ���� 
					 pstmt = con.prepareStatement(sql); // ���� spt���� �������� 
					 pstmt.setString(1, ClientData); // ?( = �Ķ����) �� �� �� ����   
					 rs = pstmt.executeQuery(); // sql �� �����ϸ� �� ���� 
					
					 rsmd = rs.getMetaData(); // �÷��� �� ������ ���� �˱� ���� ��� 
					number = rsmd.getColumnCount(); // ������ ���̸�ŭ ������ ���� 
					
					// ���� Ŭ���̾�Ʈ�� ��û�� �����Ͱ� �ִٸ� 
					if(rs.next()) {
						
						String result = "exist/"; // Ŭ���̾�Ʈ���� ������ ���ڿ� ���� 

						 for(int i = 2; i <= number; i++) {
							result += rs.getNString(i); // DB���뿡�� i��° ���� ������ ������ 
							result += "#"; // �� �����͸� �����ϱ����� ������ 
						}
						
						// ȯ���� ���� ����� �������� ���� ���� ���� �ѹ� �� ���� 
						pstmt = con.prepareStatement(sql2);
						pstmt.setString(1, ClientData);
						rs = pstmt.executeQuery();
						
						rsmd = rs.getMetaData();
						number = rsmd.getColumnCount();
						
						while(rs.next()) {
						//if(rs.next()) { // ȯ���� �������� �����Ѵٸ� �����͸� ������ 
							for(int i = 3; i <= number; i++) {
								result += rs.getNString(i);
								result += "#";
							}
							result = result.substring(0, result.length() - 1); // �������� ���� # ���� 
							result += "&";
						}
						result = result.substring(0, result.length() - 1); // �������� ���� & ���� 
						System.out.println(result);
						
						sendResponseToClient(result);
					} else {
						System.out.println("�ش� �����Ͱ� �������� �ʽ��ϴ�.");
						sendResponseToClient("failed");
					}
					 
				 }catch(ClassNotFoundException e) {
					 System.out.println("�ش� Ŭ������ ã�� �� �����ϴ�.");
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
		
		
		// ================ ���� ���������� �������� �Ǵ� 
		public void ReceiveNewAccount(String ClientData) {
			String[] newClient = ClientData.split(" ");
			
			System.out.print("Client�� ���� �����Ϸ��� ���� ���� : ");
			for (String index : newClient) {
				System.out.print(index + "  ");	
			}
			System.out.println();
			
			Connection con = null;

			ResultSet rs = null; // �ش絥���Ͱ� DB���� �����ϴ��� true, false ��ȯ 
			ResultSet rs2 = null;
			PreparedStatement pstmt = null; 
			PreparedStatement pstmt2 = null;
			PreparedStatement pstmt3 = null;

			String sql = ""; // ���̵� �̹� �����ϴ��� �ľ��� SELECT ��
			String sql2 = ""; // �ڰ� ��ȣ�� �̹� �����ϴ��� �ľ��� SELECT ��
			String sql3 = ""; // ��ϵ��� ���� ������� INSERT�� DB�� ��� 
	
			// DB�� �����Ͽ� select ������ �ش� id �Ǵ� �ڰݹ�ȣ�� ��ϵǾ��ִ��� Ȯ��
			try{  
				Class.forName(driver);
				
				con = DriverManager.getConnection(jdburl, dbId, dbPw);
				System.out.println("DB�� �����Ͽ����ϴ�.");
				
				sql = "select user_id from user_info where user_id = ?";
				sql2 = "select user_number from user_info where user_number = ?";
				
				pstmt = con.prepareStatement(sql);
				pstmt2 = con.prepareStatement(sql2);
				
				pstmt.setString(1, newClient[0]);
				System.out.println(pstmt);
				pstmt2.setString(1, newClient[2]);
				
				// �̹� ���⼭ �������� �����Ͽ� �ش� id�� DB���� �����ϴ��� �Ǵ��Ͽ�, true false�� ��ȯ��
				rs = pstmt.executeQuery(); 
				// ���⼭ �������� �����Ͽ� �ش� �ڰݹ�ȣ�� �����ϴ��� �Ǵ��Ͽ� true,false�� ��ȯ 
				rs2 = pstmt2.executeQuery();
	
				// �׳� rs�� ����ϸ� �޸� �ּ�(?)�� ���. .next() ���� true / false �� �ľǰ��� 
				//System.out.println(rs.next() + "     "  + rs2.next());
				try {
					System.out.println("DB ����� ����...");
					if(rs.next()) {
						System.out.println("DB : �̹� �����ϴ� ���̵�");
						sendResponseToClient("ID_Error");
					}else if(rs2.next()) {
						System.out.println("DB : �̹� �����ϴ� �ڰ� ��ȣ");
						sendResponseToClient("Qualification_Error");
					} else {
						System.out.println("DB : ���� �߰��� ������!");
						sql3 = "insert into user_info values(?,?,?,?,?)";
						pstmt3 = con.prepareStatement(sql3);
						pstmt3.setString(1,  newClient[0]); // ID �Է�
						pstmt3.setString(2,  newClient[1]); // PW �Է�
						pstmt3.setString(3,  newClient[4]); // ��ȭ��ȣ �Է�
						pstmt3.setString(4,  newClient[3]); // �̸� �Է�
						pstmt3.setString(5,  newClient[2]); // �ڰݹ�ȣ �Է� 
						
						pstmt3.executeUpdate();
						
						sendResponseToClient("Created");
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}catch(ClassNotFoundException e) {
				System.out.println("�ش� Ŭ������ ã�� �� �����ϴ�.");
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
		
		
		// ��ϵ� ��������� Ȯ���ϴ� �κ�( ��ϵ� ����ڸ� ����ڿ��� ���� )
		// ================ Ŭ���̾�Ʈ�ʿ��� �����κ��� ������ �޾� ����� ����		
		public void ReceiveUserData(String ClientData) {
			String[] userData = ClientData.split(" ", 2);
			boolean loginDB = false;
			System.out.println("������ �õ��ϴ� Client�� ID�� PW : " + userData[0]+ " | " + userData[1]);
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			
			// DB�� �����Ͽ� �ش� ID�� ��й�ȣ�� �ִ� �� Ȯ�� �� �̻��� ������ access(���� �㰡)
			try {  
				
				// DB�� �����ϴ� �ڵ�. ���������� ����ڰ� ��ϵǾ� �ִ��� Ȯ��  -----------------------------
				try{ 
					Class.forName(driver);
					
					con = DriverManager.getConnection(jdburl, dbId, dbPw);
					System.out.println("DB�� �����Ͽ����ϴ�.");
					stmt = con.createStatement(); // ���� ������ ���� statement ��ü ���� 
					
					// user_info ���̺��� user_id / user_pw�� �������� sql�� 
					String sql = "select * from USER_INFO"; 
					
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
					System.out.println("�ش� Ŭ������ ã�� �� �����ϴ�.");
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
				// DB �ڵ尡 ������ �κ� -----------------------------
				
				if(loginDB) {
					sendResponseToClient("access"); // client���� �޼����� ���� �޼ҵ� ȣ�� 
				} else {
					sendResponseToClient("failed"); // client���� �޼����� ���� �޼ҵ� ȣ��
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		//================�����͸� Ŭ���̾�Ʈ���� �ٽ� ������ ���� �ڵ� �ۼ� 
		public void sendResponseToClient(String request) throws IOException {
			// �����͸� client�� ������ ���� OutputStream ����
			// OutputStream�� ����ص� ����� ����������, �ش� �Լ��� ������ byte�� ����մϴ�
			OutputStream baseOutputStream = client.getOutputStream(); 
			// �׷��� byte�� String���� ��ȯ�Ͽ� ����� �� �ְ��ϴ� OutputStreamWriter�� ���������,
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(baseOutputStream);
			// ���ۼӵ��� ����� ���ؼ� BufferedWriter�� ����߽��ϴ�. ��������� OutputStream�� ����� �Ͱ� �����ϴ�.
			BufferedWriter prettyOutput = new BufferedWriter(outputStreamWriter);
			
			prettyOutput.write(request + '\0'); // �ش� ���ڿ��� client���� ���ڿ� ��� 
			prettyOutput.flush(); // ���۸� ���鼭 client���� ������ ���� 

			/*
			 * // client�� ���� String�� " "�� �������� ���� 
			String[] splited = request.split(" ");
			if (splited[0].equals("user")) { // ���� ������ ù ��° ���ڿ��� "id"�̸�
				if (splited[1].equals("pass")) { // ���� ������ �� ��° ���ڿ��� "kim_chulsu"�̸�(����� ����)
					prettyOutput.write("access server "); // �ش� ���ڿ��� client���� ���ڿ� ��� 
				} else {
					prettyOutput.write("��ϵ��� ���� ���̵��Դϴ�!\n"); // �ش� ���ڿ���  client���� ���ڿ� ���
				}
				prettyOutput.flush(); // ���۸� ���鼭 client���� ������ ���� 
			}
			 */
			
			// ������ �α� �ۼ� 
			// System.out.print("������ ���� ��û�� �޾ҽ��ϴ�: "+splited[0]+"\n����û�� Ŭ���̾�Ʈ: "+client.toString());
		}
	}
}