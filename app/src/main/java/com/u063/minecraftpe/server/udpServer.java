package com.u063.minecraftpe.server;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class udpServer {
    private DatagramSocket datagramSocket;
    private long MAGIC = 0x00ffff00fefefefefdfdfdfd12345678;
    public udpServer(int port){
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
    public void start() throws IOException {
        byte[] data = new byte[2000];
        while (true){
            DatagramPacket datagramPacket = new DatagramPacket(data,data.length);
            datagramSocket.receive(datagramPacket);

            Log.e("Client", ""+datagramPacket.getLength());
            if(datagramPacket.getData()[0]==0x05){
                byte[] serverData = new byte[28]; //ID_OPEN_CONNECTION_REPLY_1

                serverData[0] = 0x06;
                serverData

                DatagramPacket reply = new DatagramPacket(serverData,
                        serverData.length, datagramPacket.getAddress(), datagramPacket.getPort());
                datagramSocket.send(reply);
            }
        }
    }
}
