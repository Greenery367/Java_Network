package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {

	// 메인 함수
	public static void main(String[] args) {

		System.out.println("===== 서버 실행 =====");

		// 서버측 소켓을 만들기 위한 준비물
		// 서버 소켓, 포트 번호

		try (ServerSocket serverSocket = new ServerSocket(5004)) {
			
			
			Socket socket = serverSocket.accept(); // 클라이언트 대기 --> 연결 요청이 오면 --> 소켓 객체 생성 (클라와 연결)
			System.out.println("----- client connected -----");

			// 클라이언트와 통신을 위한 스트림을 설정 (대상 소켓을 얻었다)
			BufferedReader readerStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// 클라이언트에게 보낼 스트림
			PrintWriter writerStream = new PrintWriter(socket.getOutputStream(), true);

			// 키보드 스트림 준비
			BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

			// 스레드를 시작합니다.
			startReadThread(readerStream); // <--- main join() : 대기 상태
			startWriteThread(writerStream, keyboardReader);

			// 쓰지 말기! <--- join() : 안정적인 종료를 위해서 (굳이 join 안 써도 된다.)
			System.out.println("main 스레드 작업 완료 ...");

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end of main

	// -----------------------------------------------

	// 클라이언트로부터 데이터를 읽는 스레드 분리
	// 소켓 <--- 스트림을 얻어야 한다.
	/// 데이터를 읽는 객체는 뭐지??? <--- 문자.
	private static void startReadThread(BufferedReader bufferedReader) {

		Thread readThread = new Thread(() -> {
			try {
				String clientMessage;
				while ((clientMessage = bufferedReader.readLine()) != null) {
					// 서버측 콘솔에 클라이언트가 보낸 문자 데이터 출력
					System.out.println(" 클라이언트에서 온 MSG : " + clientMessage);
				}
			} catch (Exception e) {

			}

		});
		readThread.start(); // 스레드 실행 -> run() 메서드 실행/

		// join() --> readThread 가 종료될 때까지 메인스레드를 대기시킨다.
		// waitForThreadToEnd(readThread);
		// 메인 스레드 대기 처리 --> join() --> 고민 --> 2번의 반복 될 듯

	}

	// 서버 측에서 --> 클라이언트로 데이터를 보내는 기능
	private static void startWriteThread(PrintWriter printWriter, BufferedReader keyboardReader) {

		Thread writeThread = new Thread(() -> {
			try {
				String serverMessage;
				while ((serverMessage = keyboardReader.readLine()) != null) {
					printWriter.println(serverMessage);
					printWriter.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		writeThread.start();
		// 메인 스레드 대기
	}

	// 워커 스레드가 종료 될 때까지 기다리는 메서드
	private static void waitForThreadToEnd(Thread thread) {
		try {
			thread.join();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

} // end of class
