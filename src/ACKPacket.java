/**this class is use to handle and create ACKpacket
 *
 * Created by rushabhmehta on 9/20/14.
 */
public class ACKPacket {
    short opCode;
    short block;
    byte packet[];

    /**
     * constructor
     *
     * @param block
     */
    ACKPacket(short block){
        this.opCode=4;
        this.block=block;
        try {
            this.packet=getByteACK();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method converts ack to bytes for sending
     * @return
     */
    public byte[] getByteACK() {
        byte opcodeByte[]=HelperFunction.shortToByteArray(this.opCode);
        byte block[]=HelperFunction.shortToByteArray(this.block);
        byte ack[]=new byte[4];
        ack[0]=opcodeByte[0];
        ack[1]=opcodeByte[1];
        ack[2]=block[0];
        ack[3]=block[1];
        return ack;
    }
}
