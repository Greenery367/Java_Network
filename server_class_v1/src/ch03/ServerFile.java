package ch03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFile {

	public static void main(String[] args) {

		// 준비물
		// 1. 서버 소켓이 필요하다.
		// 2. 포트 번호가 필요하다. (0~65535까지 존재한다.)
		// 2-1. 잘 알려진 포트 번호: 주로 시스템 레벨에서 먼저 선점 중 (0-1023)
		// 2-2. 등록 가능한 포트 번호: 1024-49151
		// 2-3. 동적/사설 포트 번호: 그 외 임시 사용을 위해 할당된다.

		// 지역 변수 -->
		ServerSocket serverSocket = null;
		Socket socket=null;
		
		try {
			serverSocket = new ServerSocket(5001);
			System.out.println(" 서버를 시작합니다. 포트 번호 : 5001 ");
			socket = serverSocket.accept(); 
			System.out.println(">>> 클라이언트가 연결하였습니다. <<<");
			
			// 대상은 소켓이다. (input Stream) 작업
			InputStream inputStream = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			// 1. 클라이언트에서 먼저 보낸 데이터를 읽는다.
			// 실제 데이터를 읽는 행위가 필요하다.
			String message = reader.readLine();
			System.out.println("클라이언트 측 메세지 전달 받음 : " + message);
			
			// 대상은 소켓이다. (output Stream) 작업
			PrintWriter writer = new PrintWriter(socket.getOutputStream(),true); // auto flush
			writer.println("난 서버야, 클라이언트 반가워!"); // 줄바꿈 포함 메서드 ("안녕") ---> ("안녕\n")

			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(socket!=null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
