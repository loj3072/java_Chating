package my.netchat;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class  ChatClient extends Frame implements ActionListener{
	//UI 생성
	private TextArea ta=new TextArea();
	private TextField tf = new TextField();
	private Button b1=new Button("Send");
	private Button b2=new Button("Exit");
	private Panel p=new Panel();
	
	private ChatConnect  cc;

	public ChatClient(String ip, int port, String name){
		createGUI();
		cc=new ChatConnect(this,ip,port, name); //show하기 위해 나this를 넘겨줌
	}
	//GUI 생성하고 Event 등록,처리
	public void createGUI(){
		p.add(b1);
		p.add(b2);
		add( ta,"West" );
		add(p,"Center");		
		add( tf, "South");		
		tf.addActionListener( this );//Event 등록
		setBounds( 200,200,500,400 );
		setVisible( true );
		b1.addActionListener(this);//Event 등록
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
					System.exit(0);
		    }
		});
		
		this.addWindowListener( new WindowAdapter() { //x버튼 처리
				public void windowClosing( WindowEvent we ) {
					System.exit(0);
				}
		});
		tf.requestFocus(); //텍스트필드에 커서가 깜빡이도록
	}
	public void show(String msg){ //메시지를 화면에
		ta.append(msg);
	}
	public void actionPerformed( ActionEvent ae ) { //send버튼을 눌렀을 때. ActionListener의 메서드 actionPerformed
		String msg=tf.getText().trim(); //STring의 양쪽 여백 제거 //OjbectOutputStream이니까 글자가 아니라 파일객체도 보낼 수 있다.
		cc.send(msg); //ChatConnect의 send
	} 
	
	
	
	public static void main(String[] args) 	{
		String ip = "115.145.20.171"; //원격 접속 서버 ip 확인
		int port = 5500;
		String name = "userrrr";
		new ChatClient( ip,port,name );
	}
}//outer










