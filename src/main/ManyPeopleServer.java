package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 * 基于socket编程的多人聊天室服务器端
 * @author LIUFAN
 *
 */
public class ManyPeopleServer {
	private static List<Socket> socketList = new ArrayList<>();
	private static final int port = 6666;
	private static int count = 0;
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("服务器启动成功。。。。");
			while(true){
				//服务器端循环接受客户端发来的连接请求，然后接受连接
				Socket socket = serverSocket.accept();
				//接受连接后加入list
				socketList.add(socket);
				String msg = "欢迎用户"+(++count)+"号"+socket.getInetAddress()
								.getLocalHost().toString().split("/")[1]+"加入聊天室";		
				writeToClient(msg);
				readFromClient();
				
			}
		} catch (Exception e) {
			System.out.println("【系统提醒】：服务器好像出了点问题");
			//e.printStackTrace();
		}
		
	}
	public static synchronized void writeToClient(String msg)throws Exception{
		for(Socket socket:socketList){
//			DataOutputStream dataOs = new DataOutputStream(socket.getOutputStream());
			PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
			writer.println(msg);
			//writer.close();
		}
	}
	public static void readFromClient()throws Exception{
		for(Socket socket:socketList){
			Thread writeThread = new Thread(){
				public void run(){
					//开辟一个写的进程来广播数据
					try {
//						DataInputStream dataIs = new DataInputStream(socket.getInputStream());
						BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						
						String msg = reader.readLine();
						//读取到了就来广播
						writeToClient(msg);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("【系统提醒】：一个用户退出了聊天室");
						//e.printStackTrace();
					}
				}
			};
			writeThread.start();
		}
	}
}
