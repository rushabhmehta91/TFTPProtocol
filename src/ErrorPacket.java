/**this class is use to handle and create Errorpacket
 *
 * Created by rushabhmehta on 9/20/14.
 */
public class ErrorPacket {
    short opCode;
    short error;
    String errorStrng;
    byte errorStringIdentifier;

    /**
     * constructor
     *
     * @param receivebuffer
     */
    public ErrorPacket(byte[] receivebuffer) {
        this.opCode=HelperFunction.readShort(receivebuffer,0);
        this.error=HelperFunction.readShort(receivebuffer,2);
        errorStringIdentifier=0;
        byte[] errorStrngByte=new byte[(receivebuffer.length-5)];
        System.arraycopy(receivebuffer,4,errorStrngByte,0,receivebuffer.length-5);
        errorStrng=HelperFunction.byteToString(errorStrngByte);
    }

    /**
     * get error text message
     * @return
     */
    String geterrorStrng(){
        return errorStrng;
    }
}
