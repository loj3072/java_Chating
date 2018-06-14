package my.netchat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatConnect {
	String ip;
	int port;
	String name;
	private Socket s;
	private Object ta;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ChatClient cl;
	
	public ChatConnect(ChatClient cl, String ip, int port, String name){
		this.cl=cl;
		this.ip=ip;
		this.port=port;
		this.name=name;
		go();
	}
	/** 네트워크 접속 */
	public  void go( ){
		try {
			//1. Socket 생성
             s=new Socket(ip,port);
			 cl.show("서버접속 성공 !!\n");
			//2. I/O stream 생성
			 oos=new ObjectOutputStream(
					 		s.getOutputStream()); //출력스트림
			 ois=new ObjectInputStream(
					 		s.getInputStream()); //입력스트림
			 cl.show("스트림 생성 성공 !!\n"); //에러가 없었다면 스트림생성성공. 에러가 발생한다면 catch로 빠져나감
		//  Thread 만들기...
			//new ChatClientThread(ois).start();
			/** 데이터 수신 */
			//Anonymous Nested class
			 new Thread(){
					public void run(){
						try{
						while( true ) {
							String msg = (String) ois.readObject();
							cl.show( msg +"\n" );
						}
						}catch(Exception e ) {
							cl.show("readObject()시 오류발생 : "+e.getMessage()+"\n");
						}
					}				 
			 }.start(); //New Thread 해서 run() 오버라이딩 하고 start()
			cl.show("쓰레드 생성 성공 !!\n");
		}catch(Exception e ){
			cl.show("서버가 시작중인지,IP와port가 맞는지 확인 바랍니다.");
		}
	}
	/** 데이터 전송 */
	public  void send(String msg){
		try {
			oos.writeObject("["+name+"]"+msg);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	
}
