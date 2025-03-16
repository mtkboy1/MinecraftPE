package com.u063.minecraftpe.server;

import java.nio.ByteBuffer;

public class LOGIN_PACKETS {
    static byte []MAGIC = new byte[16];

    public static byte[] CONNECTION_REPLY_1(int len){
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
    public static byte[] CONNECTION_REPLY_2(int port){
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
