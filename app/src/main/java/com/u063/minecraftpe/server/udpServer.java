package com.u063.minecraftpe.server;

import com.u063.minecraftpe.server.Num2Byte;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

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

            Log.e("Client", ""+datagramPacket.getData()[0]);
            if(datagramPacket.getData()[0]==0x05){
                byte[] serverData = new byte[28]; //ID_OPEN_CONNECTION_REPLY_1

                serverData = CONNECTION_REPLY_1(datagramPacket.getLength());
                DatagramPacket reply = new DatagramPacket(serverData,
                        serverData.length, datagramPacket.getAddress(), datagramPacket.getPort());
                datagramSocket.send(reply);
            }
            if(datagramPacket.getData()[0]==0x07){
                byte[] serverData = new byte[30]; ////ID_OPEN_CONNECTION_REPLY_2

                serverData = CONNECTION_REPLY_2(datagramPacket.getPort());
                DatagramPacket reply = new DatagramPacket(serverData,
                        serverData.length, datagramPacket.getAddress(), datagramPacket.getPort());
                datagramSocket.send(reply);
            }
        }
    }
    private byte[] CONNECTION_REPLY_1(int len){
        byte[] serverData = new byte[28];
        ByteBuffer b = ByteBuffer.allocate(28);
        b.put((byte) 0x06);
        b.put(MAGIC);
        b.putLong(0x00000000372cdc9e);
        b.put((byte) 0);
        b.putShort((short) (len-18+1));
        serverData = b.array();
        return serverData;
    }
    private byte[] CONNECTION_REPLY_2(int port){
        byte[] serverData = new byte[30];
        ByteBuffer b = ByteBuffer.allocate(30);
        b.put((byte) 0x08);
        b.put(MAGIC);
        b.putLong(0x00000000372cdc9e);
        b.putShort((short) port);
        b.putShort((short) 1464);
        b.put((byte) 0);

        serverData = b.array();
        return serverData;
    }
}
