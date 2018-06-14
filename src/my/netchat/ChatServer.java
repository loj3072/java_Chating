package my.netchat;
import java.io.*;
import java.net.*;
import java.util.*;

//깔끔한 채팅 프로그램 코드.

public class ChatServer{

	private ArrayList<User> users= new ArrayList<User>(); //유저 여러명

	private int port=5500;

	public void go() {
		
		try{
		//1. ServerSocket 생성
		ServerSocket ss = new ServerSocket( port );
		System.out.println("ServerSocket 생성 성공. port="+port); //에러나면 catch로 가니까. 
		while( true ) {//서버가 죽으면 안되니까 무한루프
			//2. Socket생성: Client 접속시
			Socket s=ss.accept();
			System.out.println("Socket 생성 성공");
			//3. 각각 Client에 I/O Stream
				//서버는 입력-출력 순서, 클라이언트는 출력-입력 순서로 만드는 것이 좋음 + 똑같은 객체
			InputStream is=s.getInputStream();
            OutputStream os=s.getOutputStream();
            ObjectInputStream ois=new ObjectInputStream(is);
			ObjectOutputStream oos=
				new ObjectOutputStream(os);
			//4. Client 정보 저장
             users.add(new  User(s,ois,oos));
			//5. 각각의 Client로 부터 입력을 담당하는 Thread생성
             Thread t=new ChatServerThread(ois);
             t.start();
		}
		}catch(Exception ee ) {
			System.out.println("오류발생 ... : "+ee.getMessage() );
			ee.printStackTrace();
		}
	}

	public void broadcast( String msg ) {
		for( User u :users){
			ObjectOutputStream  oos=u.getObjectOutputStream();
			try {
				oos.writeObject(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public void removeClient( ObjectInputStream ois ) {
		for( User u :users){
			ObjectInputStream us = u.getObjectInputStream();
			if(ois == us){
				try{	
					us.close();
					u.getObjectOutputStream().close();
					u.getSocket().close();
				}catch( Exception e ) {}
				users.remove(u); 
				System.out.println("1 client exited !!");
			}
		}
	}

	public static void main(String[] args) 	{
			// ChatServer cs = new ChatServer(port);
			// cs.go();
			new ChatServer().go();
	}

class ChatServerThread extends Thread {
	private ObjectInputStream ois ;

	public ChatServerThread(  ObjectInputStream ois){
		this.ois=ois;
	}

	public void run(){
		try {
		  while(true){	
			String msg=(String) ois.readObject();
		    broadcast(msg);
		  }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}		
		removeClient(ois);
	}
}
}


class User{
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public User(Socket s, ObjectInputStream ois,ObjectOutputStream oos ){
		this.s = s;
		this.ois=ois;
		this.oos=oos;
	}
	public Socket getSocket(){ return s; }
	public ObjectInputStream getObjectInputStream(){ return ois; }
	public ObjectOutputStream getObjectOutputStream(){ return oos; }
}

