package com.my.rpc.server.bio;

import com.my.rpc.entity.RpcRequest;

import java.io.*;
import java.net.Socket;

/**
 * @Description 接收端
 * @Date 2019/7/3 12:53
 * @Created by rogan.luo
 */
public class RpcClient {

    private Integer port = 8080;
    private String  ip = "127.0.0.1";

    public Object sendRequest(RpcRequest rpcRequest){
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            socket = new Socket(ip, port);
            outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(rpcRequest);
            outputStream.flush();
            inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return objectInputStream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(socket != null)
            {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null)
            {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
