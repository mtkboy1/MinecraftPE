package com.u063.minecraftpe.server;

import java.nio.ByteBuffer;

public class PONG_PACKETS {
    static byte []MAGIC = new byte[16];
    static long pongTime = 0x00000000003c6d0d;
    public static byte[] PONG(byte[] bytes){
        byte[] serverData = new byte[200];
        ByteBuffer b = ByteBuffer.allocate(16);
        b.put((byte) 0x84);
        b.put(new byte[]{bytes[1], bytes[2], bytes[3]});
        b.put((byte) 0x00);
        b.putShort((short) (48));

        b.put((byte) 0x03);
        for(int i = 11; i<19; i++){
            b.put(bytes[i]);
        }
        //b.putLong(0x0000000000000b3d);
        serverData = b.array();
        return serverData;
    }
    public static byte[] ID_UNCONNECTED_PING_OPEN_CONNECTIONS(){
        byte[] serverData = new byte[200];
        byte[] name = "MCPE;Steve;2 7;0.11.0;0;20".getBytes();
        ByteBuffer b = ByteBuffer.allocate(35+name.length);
        b.put((byte) 0x1C);
        b.putLong(pongTime);
        pongTime+=1;
        b.putLong(0x00000000372cdc9e);
        b.put("MCPE;Steve;2 7;0.11.0;0;20".getBytes());

        b.put(MAGIC);

        serverData = b.array();
        return serverData;
    }
}
