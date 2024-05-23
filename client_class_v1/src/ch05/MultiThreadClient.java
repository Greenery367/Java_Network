package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 1단계 - 함수로 분리해서 리팩토링 진행
public class MultiThreadClient {

	// 메인 함수
	public static void main(String[] args) {
		System.out.println("### Client 실행 ###");
		
		try(Socket socket = new Socket("localhost",5000);) {
			System.out.println("~~~Connected to the server~~~");
			
			// 서버와 통신을 위한 스트림 초기화 
			BufferedReader bufferedReader = 
					new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter printWriter = 
					new PrintWriter(socket.getOutputStream());
			BufferedReader keyboardReader = 
					new BufferedReader(new InputStreamReader(System.in));
			
			startReadThread(bufferedReader);
			startWriteThread(printWriter, keyboardReader);
			// 메인 Thread야 기다려<는 어디있지?
			// 가독성이 나쁘다.
			// startWriteThread() <--- 내부에 있음.
			
		} catch (Exception e) {
			
		}
	} // end of main
	
	// 1. 클라이언트로부터 데이터를 읽는 스레드 시작 메서드 생성
	private static void startReadThread(BufferedReader reader) {
		Thread writeThread = new Thread(()->{
			try {
				String msg;
				while((msg=reader.readLine())!=null) {
					System.out.println("client에서 온 msg : "+msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		writeThread.start();
		try {
			// 메인 스레드야, 기다려!
			writeThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// 2. 키보드에서 입력을 받아, 클라이언트 측으로 데이터를 전송하는 스레드
	private static void startWriteThread(PrintWriter writer,BufferedReader keyboardReader) {
		Thread readThread = new Thread(()->{
			try {
				String msg;
				while((msg=keyboardReader.readLine())!=null) {
					// 전송
					writer.println();
					writer.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		readThread.start();
		
	}

} // end of class
