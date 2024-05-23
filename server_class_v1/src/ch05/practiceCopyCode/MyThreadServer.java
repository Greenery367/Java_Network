package ch05.practiceCopyCode;

import java.io.IOException;
import java.net.ServerSocket;

public class MyThreadServer extends AbstractServer {

	@Override
	protected void setupServer() throws IOException {
		// 추상 클래스: 부모->자식(부모의 기능을 확장, 사용한다.)
		// 서버측 소켓 통신 - 준비물: 서버 소켓, 포트 번호
		
		super.setServerSocket(new ServerSocket(7454));
		System.out.println(">>> Server started on port 5000 <<<");
	}

	@Override
	protected void connection() throws IOException {
		// 서버 소켓.accept(); 호출이다!!
		super.setSocket(super.getServerSocket().accept());
	}

	public static void main(String[] args) {
		MyThreadServer myThreadServer = new MyThreadServer();
		myThreadServer.run();
	}
	
}
