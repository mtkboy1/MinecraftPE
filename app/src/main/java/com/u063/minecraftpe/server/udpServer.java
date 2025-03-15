package com.u063.minecraftpe.server;

import com.u063.minecraftpe.server.Num2Byte;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class udpServer {
    private DatagramSocket datagramSocket;
    private byte []MAGIC = new byte[16];
    public udpServer(int port){
        MAGIC[0] = 0x00;
        MAGIC[1] = (byte) 0xff;
        MAGIC[2] = (byte) 0xff;
        MAGIC[3] = 0x00;
        MAGIC[4] = (byte) 0xfe;
        MAGIC[5] = (byte) 0xfe;
        MAGIC[6] = (byte) 0xfe;
        MAGIC[7] = (byte) 0xfe;
        MAGIC[8] = (byte) 0xfd;
        MAGIC[9] = (byte) 0xfd;
        MAGIC[10] = (byte) 0xfd;
        MAGIC[11] = (byte) 0xfd;
        MAGIC[12] = 0x12;
        MAGIC[13] = 0x34;
        MAGIC[14] = 0x56;
        MAGIC[15] = 0x78;
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
                for(int i = 0; i<MAGIC.length; i++){
                    serverData[i+1] = MAGIC[i];
                }
                for(int i = 0; i<Num2Byte.toByte(0x00000000372cdc9e).length; i++){
                    serverData[i+16] = Num2Byte.toByte(0x00000000372cdc9e)[i];
                }
                serverData[25] = 0x00;
                for(int i = 0; i<Num2Byte.toByte((short) 1447).length; i++){
                    serverData[i+26] = Num2Byte.toByte((short) 1447)[i];
                }
                DatagramPacket reply = new DatagramPacket(serverData,
                        serverData.length, datagramPacket.getAddress(), datagramPacket.getPort());
                datagramSocket.send(reply);
            }
        }
    }
}
