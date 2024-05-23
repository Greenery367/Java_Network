package ch05;

import java.io.IOException;
import java.net.ServerSocket;

public class MyThreadServer extends AbstractServer{

	@Override
	protected void setupServer() throws IOException {
		// 추상 클래스 --> 부모 --> 자식 (부모 기능을 확장, 또는 사용)
		// 서버측 소켓 통신 -- 준비물 : 서버 소켓
		super.setServerSocket(new ServerSocket(5000));
		System.out.println(">>> Server started on port 5000 <<<");
	}

	@Override
	protected void connection() throws IOException {
		// 서버 소켓.accpet(); 호출이다!!!
		super.setSocket(super.getServerSocket().accept());
		
	}
	
	public static void main(String[] args) {
		MyThreadServer myThreadServer = new MyThreadServer();
		myThreadServer.run();
	}

}
