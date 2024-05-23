package ch05.practiceCopyCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {

	public static void main(String[] args) {
		
		// 서버 측 소켓을 만들기 위한 준비물
		// 서버 소켓, 포트 번호
		
		try (ServerSocket serverSocket = new ServerSocket(5003)){
			Socket socket = serverSocket.accept();
			System.out.println("----client connected----");
			
			BufferedReader readerStream = 
					new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter swriterStream = 
					new PrintWriter(socket.getOutputStream(), true);
			BufferedReader keyboardReader =
					new BufferedReader(new InputStreamReader(System.in));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 클라이언트로부터 데이터를 읽는 스레드를 분리하자.
	// 소켓 <--- 스트림을 얻어야 한다.
	// 데이터를 읽는 객체는 뭐지? =문자
	
	private static void startReadThread(BufferedReader bufferedReader) {
		Thread readThread = new Thread(()->{
			String clientMessage;
			try {
				while((clientMessage=bufferedReader.readLine())!=null) {
					System.out.println("클라이언트에서 온 MSG : "+clientMessage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		readThread.start();
	}
	
	// 서버 측에서 클라이언트로 데이터를 보내는 기능
	private static void startWriteThread(PrintWriter printWriter, BufferedReader keyboardReader) {
		Thread writeThread = new Thread(()->{
			try {
				String serverMessage;
				while((serverMessage=keyboardReader.readLine())!=null) {
					printWriter.println(serverMessage);
					printWriter.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		writeThread.start();
	}
	
	private static void waitForThreadToEnd(Thread thread) {
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
			}
	}
	
	
}
