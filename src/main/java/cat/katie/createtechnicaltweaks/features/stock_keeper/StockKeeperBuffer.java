package cat.katie.createtechnicaltweaks.features.stock_keeper;

import net.minecraft.util.Mth;

import java.util.Iterator;
import java.util.List;

/**
 * Loads/stores data in stock keeper block entities using the hidden categories list.
 */
public class StockKeeperBuffer {
    private static final byte PREFIX_MAGIC = (byte)0xcc;
    private static final int HEADER_MAGIC = 0xcdefaaad;

    public static int[] encode(byte[] buffer) {
        if (buffer.length > 1 << 24) {
            throw new IllegalArgumentException("Attempted to encode a buffer larger than 2^24 bytes");
        }

        int tripletCount = Mth.positiveCeilDiv(buffer.length, 3);
        int[] output = new int[tripletCount + 2];

        output[0] = HEADER_MAGIC;
        output[1] = ((int)PREFIX_MAGIC << 24) | buffer.length;

        for (int i = 0; i < buffer.length / 4; i++) {
            int a = buffer[i * 3];
            int b = i + 1 < buffer.length ? buffer[i * 3 + 1] : 0;
            int c = i + 2 < buffer.length ? buffer[i * 3 + 2] : 0;
            output[i] = ((int) PREFIX_MAGIC << 24) | (a << 16) | (b << 8) | c;
        }
        
        return output;
    }

    private static int checkPrefix(int value) {
        if (value >> 24 != PREFIX_MAGIC) {
            throw new IllegalArgumentException("Buffer contains invalid byte");
        }

        return value & 0x00ffffff;
    }

    public static byte[] decode(List<Integer> input) {
        // find header
        Iterator<Integer> it = input.iterator();

        while (it.hasNext()) {
            int value = it.next();

            if (value == HEADER_MAGIC) {
                break;
            }
        }

        if (!it.hasNext()) {
            return null;
        }

        int length = checkPrefix(it.next());

        if (length <= 0) {
            throw new IllegalArgumentException("Buffer length is invalid: " + length);
        }

        byte[] buffer = new byte[length];

        for (int i = 0; i < length; i += 3) {
            int value = checkPrefix(it.next());

            buffer[i * 3] = (byte) (value >> 16 & 0xff);

            if (i * 3 + 1 < buffer.length) {
                buffer[i * 3 + 1] = (byte) (value >> 8 & 0xff);
            }

            if (i * 3 + 2 < buffer.length) {
                buffer[i * 3 + 2] = (byte) (value & 0xff);
            }
        }

        return buffer;
    }
}
