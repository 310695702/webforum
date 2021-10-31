package com.kcbs.webforum.tcp;

import java.io.*;
import java.net.Socket;

public class ClientTask extends Thread implements MsgPool.MsgComingListener{
    private Socket mSocket;
    private InputStream mIs;
    private OutputStream mOs;

    public ClientTask(Socket socket) {

        try {
            mSocket = socket;
            mIs = socket.getInputStream();
            mOs = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        BufferedReader br = new BufferedReader(new InputStreamReader(mIs));
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                System.out.println("read  = " + line);
                // 转发消息至其他Socket
                MsgPool.getInstance().sendMsg(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMsgComing(String msg) {
        try {
            mOs.write(msg.getBytes());
            mOs.write("\n".getBytes());
            mOs.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
