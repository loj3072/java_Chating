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
	/** ��Ʈ��ũ ���� */
	public  void go( ){
		try {
			//1. Socket ����
             s=new Socket(ip,port);
			 cl.show("�������� ���� !!\n");
			//2. I/O stream ����
			 oos=new ObjectOutputStream(
					 		s.getOutputStream()); //��½�Ʈ��
			 ois=new ObjectInputStream(
					 		s.getInputStream()); //�Է½�Ʈ��
			 cl.show("��Ʈ�� ���� ���� !!\n"); //������ �����ٸ� ��Ʈ����������. ������ �߻��Ѵٸ� catch�� ��������
		//  Thread �����...
			//new ChatClientThread(ois).start();
			/** ������ ���� */
			//Anonymous Nested class
			 new Thread(){
					public void run(){
						try{
						while( true ) {
							String msg = (String) ois.readObject();
							cl.show( msg +"\n" );
						}
						}catch(Exception e ) {
							cl.show("readObject()�� �����߻� : "+e.getMessage()+"\n");
						}
					}				 
			 }.start(); //New Thread �ؼ� run() �������̵� �ϰ� start()
			cl.show("������ ���� ���� !!\n");
		}catch(Exception e ){
			cl.show("������ ����������,IP��port�� �´��� Ȯ�� �ٶ��ϴ�.");
		}
	}
	/** ������ ���� */
	public  void send(String msg){
		try {
			oos.writeObject("["+name+"]"+msg);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	
}
