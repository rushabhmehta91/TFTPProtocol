import java.io.*;

/**this class is use to handle and create RRQpacket/WRQpackets
 *
 * Created by Rushabh on 9/14/2014.
 */
public class ReadWritePacket implements Serializable{
    short opcode;
    String filename;
    byte fileIndentification;
    String mode;
    byte modeIndetification;
    byte packet[];

    /**
     * constructor
     *
     * @param opcodeNumber
     * @param file
     * @param mode
     */
    ReadWritePacket(short opcodeNumber,String file, String mode){

        this.opcode=opcodeNumber;
        this.filename=file;
        this.fileIndentification=0;
        this.mode=mode;
        this.modeIndetification=0;
        try {
            this.packet=this.getByte();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts to data bytes
     *
     * @return
     * @throws Exception
     */
   public byte[] getByte()throws Exception{
       byte opcodeByte[]=HelperFunction.shortToByteArray(opcode);
       byte filenameByte[]=filename.getBytes();
       byte fileIdenByte=0;
       byte modeByte[]=mode.getBytes();
       byte modeIdenByte=0;
       int i=0;
       byte bytes[]=new byte[4+filename.length()+modeByte.length];
       for(int j=0;j<opcodeByte.length;j++){
           bytes[i++]=opcodeByte[j];
       }
       for(int j=0;j<filenameByte.length;j++){
           bytes[i++]=filenameByte[j];
       }
       bytes[i++]=fileIdenByte;
       for(int j=0;j<modeByte.length;j++) {
           bytes[i++] = modeByte[j];
       }
       bytes[i++]=modeIdenByte;

        return bytes;
    }





}
