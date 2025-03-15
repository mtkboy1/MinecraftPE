package com.u063.minecraftpe.server;

import android.util.Log;

import java.nio.ByteBuffer;

public class Num2Byte {
    public static byte[] toByte(long num){
        ByteBuffer b = ByteBuffer.allocate(8);
        //b.order(ByteOrder.BIG_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
        b.putLong(num);

        byte[] result = b.array();
        return result;
    }
    public static byte[] toByte(short num){
        ByteBuffer b = ByteBuffer.allocate(2);
        //b.order(ByteOrder.BIG_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
        b.putShort(num);

        byte[] result = b.array();
        return result;
    }
}
