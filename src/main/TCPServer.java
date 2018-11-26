package main;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class TCPServer {
	//尝试维护一个list保存所有的连接socket
	private static List<Socket> socketList = new ArrayList<>();
	public static void main(String[] args) throws Exception{
		ServerSocket serverSocket = new ServerSocket(6667);
		//建立后就在这儿等着，是阻塞式的，等待client来进行连接，client连接上来后才会往下执行
		//我们往往会给它指定一个端口号，来监听客户端的连接
		System.out.println("listen on port:6667");
		while(true){
			//client申请连接，server端应该接收连接
			Socket socket = serverSocket.accept();
			//接受到申请以后就把socket加入到list
			//socketList.add(socket);
			System.out.println("客户端:"+socket.getInetAddress().getLocalHost()+"已经连接到服务器");
			//然而接受一个连接就结束了，所以一般服务端都是死循环
			DataInputStream dataIs = new DataInputStream(socket.getInputStream());
			DataOutputStream dataos = new DataOutputStream(socket.getOutputStream());
			Scanner scanner = new Scanner(System.in);
			Thread readThread = new Thread(){
				public void run(){
					while(true){
						String msg = null;
						try{
							msg = dataIs.readUTF();
						}catch(IOException e){
							System.out.println("error:"+e.getStackTrace());
							break;
						}
						try {
							System.out.println("读取来自"+socket.getInetAddress().getLocalHost().toString().split("/")[1]+"的消息:"+msg);
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
//			//readUTF也是阻塞式的，它也会在这儿等，如果client端不写，它还是在这儿等着些东西
//			System.out.println(dataIs.readUTF());
//			dataIs.close();
//			socket.close();
		}
		//问题，readUTF是阻塞式的，那么另一个客户端就无法连接上来。因为连接上是需要server端执行accept方法的
		//但是server这时候被阻塞在了readUTF这个方法上
		
	}

}
