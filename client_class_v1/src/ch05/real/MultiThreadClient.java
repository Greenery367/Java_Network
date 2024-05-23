package ch05.real;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 1단계 - 함수로 분리해서 리팩토링 진행
public class MultiThreadClient {

	public static void main(String[] args) {

		System.out.println("===== 클라이언트 실행 =====");

		try (Socket socket = new Socket("localhost", 5004)) {
			System.out.println("=====Connected to the server=====");

			PrintWriter socketWriter = new PrintWriter(socket.getOutputStream());
			BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

			startWriteThread(socketWriter, keyboardReader);
			startReadThread(socketReader);

			System.out.println("main 스레드 작업 완료 ...");

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end of main

	// 1. 서버로부터 데이터를 읽어들이는 스레드
	private static void startReadThread(BufferedReader socketReader) {
		Thread readThread = new Thread(() -> {
			try {
				String serverMessage;
				while ((serverMessage = socketReader.readLine()) != null) {
					System.out.println("서버에서 온 MSG : " + serverMessage);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		readThread.start();
		waitForThreadToEnd(readThread);
	}

	// 2. 서버에게 데이터를 보내는 스레드
	private static void startWriteThread(PrintWriter sockerWriter, BufferedReader keyboardReader) {
		Thread writeThread = new Thread(() -> {
			try {
				String clientMessage;
				while ((clientMessage = keyboardReader.readLine()) != null) {
					sockerWriter.println(clientMessage);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		writeThread.start();
		waitForThreadToEnd(writeThread);
	}

	// 3. 워커 스레드가 종료될 때까지 기다리는 메서드
	private static void waitForThreadToEnd(Thread thread) {
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

} // end of class
