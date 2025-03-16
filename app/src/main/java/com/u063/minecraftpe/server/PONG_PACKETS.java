package com.u063.minecraftpe.server;

import java.nio.ByteBuffer;

public class PONG_PACKETS {
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
}
