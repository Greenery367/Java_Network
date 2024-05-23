package ch05.practice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 리팩토링 1단계 - 함수로 분리해서 리팩토링 진행
public class MultiThreadClient {
	
	// 메인 함수
	public static void main(String[] args) {
	
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
			// 메인 스레드를 해당 스레드가 끝날 때 까지 기다리게 만듦
			writeThread.join();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	// 2. 키보드에서 입력을 받아, 클라이언트 측으로 데이터를 전송하는 스레드
	private static void startWriteThread(PrintWriter writer, BufferedReader keyboardReader) {
		Thread readThread = new Thread(()->{
			try {
				String msg;
				while((msg=keyboardReader.readLine())!=null) {
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
