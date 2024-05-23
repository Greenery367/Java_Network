package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 2단계 - 상속 활용 리팩토링 단계
public abstract class AbstractClient {
	
	private Socket socket;
	private BufferedReader socketReader;
	private PrintWriter socketWriter;
	private BufferedReader keyboardReader;
	
	
	// set 메서드
	// 메서드 의존 주입 (멤버 변수에 참조 변수 할당)
	protected void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	// get 메서드
	protected Socket getSocket() {
		return this.socket;
	}
	
	// 실행의 흐름이 필요하다.
	// final - 상속 받은 자식 클래스에서 수정이 불가
	public final void run() {
		try {
			setupClient();
			setupStream();
			startService();
		} catch (Exception e) {
			
		} finally {
			cleanup();
		}
	}
	
	// 1. 포트 번호 할당
	protected abstract void setupClient() throws IOException;
	
	// 2. 스트림 초기화
	private void setupStream() throws IOException{
		socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		socketWriter = new PrintWriter(socket.getOutputStream(),true);
		keyboardReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	// 3. 서비스 시작
	private void startService() {
		Thread writeThread = createWriteThread();
		Thread readThread = createReadThread();
		
		writeThread.start();
		readThread.start();
		
		try {
			readThread.join();
			writeThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// 4. 캡슐화
	private Thread createWriteThread(){
		return new Thread(()->{
			try {
				String msg;
				while((msg=keyboardReader.readLine())!=null) {
					socketWriter.println(msg);
					socketWriter.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	private Thread createReadThread() {
		return new Thread(()->{
			try {
				String msg;
				while((msg=socketReader.readLine())!=null) {
					System.out.println("client 측 msg : "+msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	// 캡슐화 - 소켓 자원 종료
	private void cleanup() {
		try {
			if(socket!=null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
