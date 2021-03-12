package com.kcbs.webforum.tcp;

import java.io.*;
import java.net.Socket;

public class ClientTask extends Thread implements MsgPool.MsgComingListener{
    private Socket msocket;
    private InputStream is;
    private OutputStream os;

    public ClientTask(Socket socket){
        try {
            msocket = socket;
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
            try {
                while ((line = br.readLine())!=null){
                    System.out.println("read = "+ line);
                    //转发消息到其他socket
                    MsgPool.getsInstance().sendMsg(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onMsgComing(String msg) {
        try {
            os.write(msg.getBytes());
            os.write("\n".getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
