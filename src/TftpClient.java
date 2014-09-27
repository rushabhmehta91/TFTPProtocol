import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.*;
import java.util.Scanner;

/** This Program is TFTP client. It can connect to TFTP server
 *
 * Created by Rushabh on 9/14/2014.
 */
public class TftpClient {
    int port;
    DatagramPacket receivePacket;
    DatagramPacket sendPacket;
    DatagramSocket socket;
    byte buffer[];
    boolean connected;
    String hostName = "glados.cs.rit.edu";
    InetAddress hostAddress;

    /**
     * Default construction
     *
     */
    TftpClient() {
        port = 69;
        connected = false;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Error in creating socket");
        }

    }

    /**
     * Main method
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        TftpClient tc = new TftpClient();
        Scanner sc = new Scanner(System.in);

        boolean flag = true;

        while (flag) {
            int x = 0;
            System.out.print("myTftp>");
            String inputLine = sc.nextLine();
            String input[] = inputLine.split(" ");
            String command = input[0];
            if (command.equals("connect")) {
                x = 1;
            }
            if (command.equals("get")) {
                x = 2;
            }
            if (command.equals("?")) {
                x = 3;
            }
            if (command.equals("quit")) {
                x = 4;
            }
            switch (x) {
                case 1:
                    String parameter = (input.length == 1) ? "glados.cs.rit.edu" : input[1];
                    tc.connect(parameter, 69);
                    break;
                case 2:
                    String parameterFile = input[1];
                    tc.get(parameterFile);
                    break;
                case 3:
                    System.out.println("connect \tconnect to remote tftp\n" +
                            "get     \treceive file\n" +
                            "quit    \texit tftp\n" +
                            "?       \tprint help information\n");

                    break;
                case 4:
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }

    /**
     * connect method. This sets hostname and port for connection
     *
     * @param host
     * @param port
     */
    void connect(String host, int port) {
        hostName = host;
        this.port = port;

        InetAddress aInetAddress = null;
        try {
            hostAddress = InetAddress.getByName(hostName);
            System.out.println("connected to " + host);
            connected = true;
            } catch (UnknownHostException e) {
            System.out.println("host not found");
            connected = false;
        }

    }

    /**
     * this is a get method which downloads the file from server
     *
     * @param fileName
     */
    void get(String fileName) {
        if (connected) {
            ReadWritePacket rw = new ReadWritePacket((short) 1, fileName, "octal");
            byte sendbuffer[] = rw.packet;
            sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, hostAddress, 69);
            byte receivebuffer[];
            buffer = new byte[516];
            receivePacket = new DatagramPacket(buffer, buffer.length);
            int i = 0;
            try {
                socket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            do {

                try {
                    socket.setSoTimeout(5000);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                while (true) {
                    if (i == 5) {
                        System.out.println("TimeOut");
                        return;
                    } else {
                        try {

                            socket.receive(receivePacket);
                            break;
                        } catch (SocketTimeoutException e) {
                            i++;
                            try {
                                System.out.println("Retransmitting packet");
                                socket.send(sendPacket);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            continue;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


                port = receivePacket.getPort();
                int recLength = receivePacket.getLength();
                receivebuffer = new byte[recLength];
                byte temp[] = receivePacket.getData();
                System.arraycopy(temp, 0, receivebuffer, 0, recLength);
                packetHandler(receivebuffer, fileName);
            } while (receivePacket.getLength() == 516);

        } else {
            System.out.println("Please Connect to host");
        }

    }

    /**
     * this method handles the different kinds of packet
     *
     * @param receivebuffer
     * @param fileName
     */
    private void packetHandler(byte[] receivebuffer, String fileName) {
        short opCode = HelperFunction.readShort(receivebuffer, 0);
        switch (opCode) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                dataPacket(receivebuffer, fileName);
                break;
            case 4:
                ackPacket(receivebuffer);
                break;
            case 5:
                errorPacket(receivebuffer);
                break;
            default:
        }
    }

    /**error packet handling
     *
     * @param receivebuffer
     */
    private void errorPacket(byte[] receivebuffer) {
        ErrorPacket error = new ErrorPacket(receivebuffer);
        System.out.println(error.geterrorStrng());
    }

    /**
     * ackPacket handling during put operation
     *
     * @param receivebuffer
     */
    private void ackPacket(byte[] receivebuffer) {

    }

    /**data packet handling
     *
     * @param receivebuffer
     * @param fileName
     */
    private void dataPacket(byte[] receivebuffer, String fileName) {
        DataPacket data = new DataPacket(receivebuffer);
        ACKPacket returnAckPacket = new ACKPacket(data.block);
        byte sendbufferAck[] = returnAckPacket.packet;
        sendPacket = new DatagramPacket(sendbufferAck, sendbufferAck.length, hostAddress, port);
        try {
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        data.writeFile(fileName);

    }

}






