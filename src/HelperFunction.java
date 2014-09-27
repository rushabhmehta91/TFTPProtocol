/**
 * Created by rushabhmehta on 9/20/14.
 */
public class HelperFunction {
    /**
     *  converts short to byte
     * @param s
     * @return
     */
    public static byte[] shortToByteArray(short s) {
        return new byte[] { (byte) ((s & 0xFF00) >> 8), (byte) (s & 0x00FF) };
    }

    /**
     * converts bytes to shoert
     *
     * @param data
     * @param offset
     * @return
     */
    public static short readShort(byte[] data, int offset) {
        return (short) (((data[offset] << 8)) | ((data[offset + 1] & 0xff)));
    }

    /**
     * converts bytes to string
     *
     * @param bytes
     * @return
     */
    public static String byteToString(byte[] bytes){
        String data = "";
        for(int i = 0; i < bytes.length; i++){
            data = data+(char)bytes[i];
        }
        return data;
    }
}
