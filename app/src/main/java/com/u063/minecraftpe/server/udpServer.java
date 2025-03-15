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
        byte[] data = new byte[1500];
        while (true){
            DatagramPacket datagramPacket = new DatagramPacket(data,data.length);
            datagramSocket.receive(datagramPacket);

            //Log.e("Client", "ID: "+String.format("%02X ", datagramPacket.getData()[0])+", len: "+datagramPacket.getLength());
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
            if(datagramPacket.getData()[0]==-124){
                data = new byte[200];
                byte[] serverData;
                DatagramPacket reply;
                /*byte[] serverData = datagramPacket.getData(); //0X84 BACK
                DatagramPacket reply = new DatagramPacket(serverData,
                        serverData.length, datagramPacket.getAddress(), datagramPacket.getPort());
                datagramSocket.send(reply);*/
                data = ENCAPSULATION(datagramPacket.getData());
                if(data[0]==0x09){
                    serverData = SERVER_HANDSHAKE(data); //0X84 BACK

                    reply = new DatagramPacket(serverData,
                            serverData.length, datagramPacket.getAddress(), datagramPacket.getPort());
                    datagramSocket.send(reply);
                }
                if(data[0]==0x00){
                    serverData = PONG(data); //0X84 BACK

                    reply = new DatagramPacket(serverData,
                            serverData.length, datagramPacket.getAddress(), datagramPacket.getPort());
                    datagramSocket.send(reply);
                }
                /////////////////////////SUCK CODE////////////////////

                serverData = new byte[7]; ////ACK
                byte[] count = new byte[3];
                for(int i = 1; i<4; i++){
                    count[i-1]=datagramPacket.getData()[i];
                   // Log.e("",""+count[i-1]);
                }
                serverData = ACK(count);
                reply = new DatagramPacket(serverData,
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
    private byte[] ACK(byte[] packet){
        byte[] serverData = new byte[7];
        ByteBuffer b = ByteBuffer.allocate(7);
        b.put((byte) 0xC0);
        b.put((byte) 1);
        b.put((byte) 1); //true or false
        b.put(packet);

        serverData = b.array();
        return serverData;
    }
    private byte[] ENCAPSULATION(byte[] bytes){
        byte[] b = new byte[bytes.length];
        String s="";
        if(bytes[4]==0x40||bytes[4]==0x00) {
            for (int i = 10; i < bytes.length; i++) {
                b[i - 10] = bytes[i];
                //s += String.format("%02X ", bytes[i]);
            }
        } else {
            for (int i = 0; i < bytes.length; i++) {
                b[i] = bytes[i];
                //s += String.format("%02X ", bytes[i]);
            }
        }
        for (int i = 0; i < bytes.length; i++) {
            //b[i] = bytes[i];
            s += String.format("%02X ", bytes[i]);
        }
        Log.e("","len: "+bytes.length+", "+s);
        return b;
    }
    private byte[] putDataArray(){
        byte[] bb = new byte[70];
        ByteBuffer b = ByteBuffer.allocate(70);
        byte[] unknown1 = new byte[] {(byte) 0xf5, (byte) 0xff, (byte) 0xff, (byte) 0xf5};
        byte[] unknown2 = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        b.put((byte) 0x00);
        b.put((byte) 0x00);
        b.put((byte) 0x04);
        b.put(unknown1);
        for (int i = 0; i < 9; i++)
        {
            b.put((byte) 0x00);
            b.put((byte) 0x00);
            b.put((byte) 0x04);
            b.put(unknown2);
        }
        return bb;
    }
    private byte[] SERVER_HANDSHAKE(byte[] bytes){
        byte[] serverData = new byte[2000];
        ByteBuffer b = ByteBuffer.allocate(106);
        b.put((byte) 0x84);
        b.put(new byte[]{bytes[1], bytes[2], bytes[3]});
        b.put((byte) 0x40);
        b.putShort((short) (96*8));
        b.put(new byte[]{0, 0, 0});

        b.put((byte) 0x10);
        b.putInt(0x043f57fe);
        b.put((byte) 0xcd);
        b.putShort((short) 19132);
        b.put(putDataArray());
        b.put((byte) 0x00);
        b.put((byte) 0x00);
        for(int i=8; i<16; i++){
            b.put(bytes[i]);
        }
        byte[] unknown = new byte[] {0x00, 0x00, 0x00, 0x00, 0x04, 0x44, 0x0b, (byte) 0xa9};
        b.put(unknown);
        serverData = b.array();
        return serverData ;
    }
    private byte[] PONG(byte[] bytes){
        byte[] serverData = new byte[200];
        ByteBuffer b = ByteBuffer.allocate(16);
        b.put((byte) 0x84);
        b.put(new byte[]{bytes[1], bytes[2], bytes[3]});
        b.put((byte) 0x00);
        b.putShort((short) (48));

        b.put((byte) 0x03);
        b.putLong(0x0000000000000b3d);
        serverData = b.array();
        return serverData;
    }
}
