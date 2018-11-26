package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
/**
 * 基于socket编程的多人聊天室客户端
 * @author LIUFAN
 *
 */
public class ManyPeopleClient {
	private static final String IP = "127.0.0.1";
	private static final int PORT = 6666;
	private static String Name = "匿名用户";
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("请输入你的用户名:");
		Name = s.next();
		try {
			Socket socket = new Socket(IP,PORT);
			System.out.println(Name+"连接服务器成功！");
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
			
			Thread readThread = new Thread(){
				public void run(){
					String msg = "";
					while(true){
						try {
							msg = reader.readLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.println("与服务器断开连接");
							//e.printStackTrace();
							break;
						}
						if(null !=msg&&msg!=""){
							System.out.println(msg);
						}
					}
				}
			};		
			Thread writeThread = new Thread(){
				public void run(){
					while(true){
						String msg = s.next();
						writer.println(Name+":"+msg);		
					}
				}
			};
			readThread.start();
			writeThread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("找不到服务器:"+IP);
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("与服务器断开连接");
			//e.printStackTrace();
			
		}
		
	}
	
}
