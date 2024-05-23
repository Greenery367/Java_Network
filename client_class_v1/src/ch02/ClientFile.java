package ch02;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFile {
	
	public static void main(String[] args) {
		
		// 클라이언트 측 준비물
		// 1. 서버 측 IP 주소와 포트 번호가 필요하다.
		// 2. 서버 측 소켓과 연결될 소켓이 필요하다.
		Socket socket = null;
		
		try {
			socket = new Socket("localhost",5001);
			PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
			writer.println("안녕! 반가워~");
			
			socket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
