package com.kcbs.webforum.tcp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient {
    private Scanner mScanner;

    public TcpClient() {
        mScanner = new Scanner(System.in);
        mScanner.useDelimiter("\n");
    }


    public void start() {
        try {
            Socket socket = new Socket("47.111.9.152", 8089);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            // 输出服务端发送的数据
            new Thread() {
                @Override
                public void run() {
                    try {
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                    }

                }
            }.start();

            while (true) {
                String msg = mScanner.next();
                bw.write(msg);
                bw.newLine();
                bw.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TcpClient().start();
    }

}
