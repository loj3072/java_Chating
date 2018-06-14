package my.netchat;
import java.io.*;
import java.net.*;
import java.util.*;

//����� ä�� ���α׷� �ڵ�.

public class ChatServer{

	private ArrayList<User> users= new ArrayList<User>(); //���� ������

	private int port=5500;

	public void go() {
		
		try{
		//1. ServerSocket ����
		ServerSocket ss = new ServerSocket( port );
		System.out.println("ServerSocket ���� ����. port="+port); //�������� catch�� ���ϱ�. 
		while( true ) {//������ ������ �ȵǴϱ� ���ѷ���
			//2. Socket����: Client ���ӽ�
			Socket s=ss.accept();
			System.out.println("Socket ���� ����");
			//3. ���� Client�� I/O Stream
				//������ �Է�-��� ����, Ŭ���̾�Ʈ�� ���-�Է� ������ ����� ���� ���� + �Ȱ��� ��ü
			InputStream is=s.getInputStream();
            OutputStream os=s.getOutputStream();
            ObjectInputStream ois=new ObjectInputStream(is);
			ObjectOutputStream oos=
				new ObjectOutputStream(os);
			//4. Client ���� ����
             users.add(new  User(s,ois,oos));
			//5. ������ Client�� ���� �Է��� ����ϴ� Thread����
             Thread t=new ChatServerThread(ois);
             t.start();
		}
		}catch(Exception ee ) {
			System.out.println("�����߻� ... : "+ee.getMessage() );
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

