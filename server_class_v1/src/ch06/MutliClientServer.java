package ch06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
public class MutliClientServer {
	
	private static final int PORT = 5000;
	// 하나의 변수에 자원을 통으로 관리하기 위한 기법 --> 자료구조
	// 자료 구조??? ---> 코드 단일, 멀티 ---> 멀티 스레드 ---> 자료구조??
	// 객체 배열 : ArrayList(멀티스레드에서 안정적이지 않음), Vector<>(멀티스레드에서 안정적이다.)
	// 브로드캐스트 기법
	private static Vector<PrintWriter> clientWriters = new Vector<>();
	public static void main(String[] args) {
		System.out.println("Server started ...");
		
		try(ServerSocket serverSocket = new ServerSocket(PORT)) {
			while(true) {
				// 1. ServerSocket.accept()를 호출하면 블로킹 상태가 된다. (멈춤)
				// 2. 클라이언트가 연결 요청을 하면, 새로운 소켓 객체가 생성된다. (accept)
				// 3. 새로운 스레드를 만들어서 처리...(클라이언드가 데이터를 주고 받기 위한 스레드)
				// 4. 새로운 클라이언트가 접속 하기까지 다시 대기 유지(반복)
				serverSocket.accept();
			}
			
		} catch (Exception e) {
			
		}
		
	} // end of main

	// 정적 내부 클래스 설계 
	private static class ClientHandler extends Thread{
		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;
		// 멤버 필드
		
		public ClientHandler(Socket socket) {
			this.socket=socket;
		}
		
		// Thread.start() 호출 시 동작되는 메서드 = 약속
		@Override
		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(),true);
				// 여기서 중요! - 서버가 관리하는 자료구조에 자원 저장(=클라이언트와 연결된 소켓=ouputStream)
				clientWriters.add(out);
				
				String message;
				while((message=in.readLine())!=null) {
					System.out.println("Received : "+message);
				}
				// 받은 데이터를 서버측과 연결된 클라이언트에게 전달하자.
				for (PrintWriter writer : clientWriters) {
					// 스트림을 통해 데이터 전달
					writer.println(message); // 모든 클라이언트에게 메세지 전송
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
					System.out.println("...... 클라이언트 연결 해제 ......");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	
} // end of class
