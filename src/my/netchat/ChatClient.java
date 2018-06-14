package my.netchat;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class  ChatClient extends Frame implements ActionListener{
	//UI ����
	private TextArea ta=new TextArea();
	private TextField tf = new TextField();
	private Button b1=new Button("Send");
	private Button b2=new Button("Exit");
	private Panel p=new Panel();
	
	private ChatConnect  cc;

	public ChatClient(String ip, int port, String name){
		createGUI();
		cc=new ChatConnect(this,ip,port, name); //show�ϱ� ���� ��this�� �Ѱ���
	}
	//GUI �����ϰ� Event ���,ó��
	public void createGUI(){
		p.add(b1);
		p.add(b2);
		add( ta,"West" );
		add(p,"Center");		
		add( tf, "South");		
		tf.addActionListener( this );//Event ���
		setBounds( 200,200,500,400 );
		setVisible( true );
		b1.addActionListener(this);//Event ���
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
					System.exit(0);
		    }
		});
		
		this.addWindowListener( new WindowAdapter() { //x��ư ó��
				public void windowClosing( WindowEvent we ) {
					System.exit(0);
				}
		});
		tf.requestFocus(); //�ؽ�Ʈ�ʵ忡 Ŀ���� �����̵���
	}
	public void show(String msg){ //�޽����� ȭ�鿡
		ta.append(msg);
	}
	public void actionPerformed( ActionEvent ae ) { //send��ư�� ������ ��. ActionListener�� �޼��� actionPerformed
		String msg=tf.getText().trim(); //STring�� ���� ���� ���� //OjbectOutputStream�̴ϱ� ���ڰ� �ƴ϶� ���ϰ�ü�� ���� �� �ִ�.
		cc.send(msg); //ChatConnect�� send
	} 
	
	
	
	public static void main(String[] args) 	{
		String ip = "115.145.20.171"; //���� ���� ���� ip Ȯ��
		int port = 5500;
		String name = "userrrr";
		new ChatClient( ip,port,name );
	}
}//outer










