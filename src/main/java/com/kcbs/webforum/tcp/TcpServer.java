package com.kcbs.webforum.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

    public void start(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8089);
            MsgPool.getInstance().start();

            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("ip = "+socket.getInetAddress().getHostAddress()+" ,port = "+socket.getPort()+" is online...");
                ClientTask clientTask = new ClientTask(socket);
                MsgPool.getInstance().addMsgComingListener(clientTask);
                clientTask.start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
