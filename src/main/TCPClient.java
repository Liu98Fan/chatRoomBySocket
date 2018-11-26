package main;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.Scanner;
public class TCPClient {
	public static void main(String[] args)throws Exception {
		Socket socket = new Socket("127.0.0.1", 6666);
		//和server端连接完成，client端的端口是系统随机选择的
		//一旦连上以后，就可以通过流这个管道来相互交流
//		OutputStream os = s.getOutputStream();
//		DataOutputStream dataOs = new DataOutputStream(os);
//		//UTF8的方式省空间
//		dataOs.writeUTF("hello server");
//		dataOs.close();
//		os.close();
//		s.close();
		DataInputStream dataIs = new DataInputStream(socket.getInputStream());
		DataOutputStream dataos = new DataOutputStream(socket.getOutputStream());
		Scanner scanner = new Scanner(System.in);
		System.out.println("成功建立连接。。。。");
		Thread readThread = new Thread(){
			public void run(){
				while(true){
					String msg = null;
					try{
						msg = dataIs.readUTF();
						System.out.println("读取来自"+socket.getInetAddress().getLocalHost().toString().split("/")[1]+"的消息:"+msg);
					}catch(IOException e){
						e.printStackTrace();
						break;
					}
					
				}
			}
		};
		Thread writeThread = new Thread(){
			public void run(){
				while(true){
					try {
						dataos.writeUTF(scanner.next());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		readThread.start();
		writeThread.start();
	}
}
