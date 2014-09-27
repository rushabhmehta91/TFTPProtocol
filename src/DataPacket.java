import java.io.*;


/**this class is use to handle and create datapacket
 *
 * Created by rushabhmehta on 9/20/14.
 */
public class DataPacket {
    short opCode;
    short block;
    byte[] data;


    DataPacket(short opCode, short block, byte[] data ){
        this.opCode=opCode;
        this.block=block;
        this.data=data;
    }

    DataPacket(byte receivebuffer[]){
        this.opCode=HelperFunction.readShort(receivebuffer,0);
        this.block=HelperFunction.readShort(receivebuffer,2);
        data=new byte[(receivebuffer.length-4)];
        System.arraycopy(receivebuffer,4,data,0,receivebuffer.length-4);
    }
        public void writeFile(String fileName) {
            FileOutputStream fos=null;
            boolean appendFlag=true;
            if(block==1)
                appendFlag=false;
            try {
            fos = new FileOutputStream(fileName,appendFlag);
                fos.write(data);
                fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        }
}
