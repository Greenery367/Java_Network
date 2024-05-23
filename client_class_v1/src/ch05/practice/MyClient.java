package ch05.practice;

import java.io.IOException;
import java.net.Socket;

public class MyClient extends AbstractClient {

	@Override
	protected void setupClient() throws IOException {
		// 추상 클래스: 자식 클래스는 부모 클래스에게 기능을 상속받아,
		// 그 기능을 확장하거나 사용한다.
		// 클라이언트 측 소켓 통신 --- 준비물: 소켓
		super.setSocket(new Socket("localhost",7674));
		System.out.println(">>> Client was connected on port 5000 <<<");
	}
	
	public static void main(String[] args) {
		MyClient myClient=new MyClient();
		myClient.run();
	}

}
