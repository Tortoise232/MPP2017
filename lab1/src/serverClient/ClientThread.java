import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * Created by Petean Mihai on 4/2/2017.
 */
public class ClientThread {
    Socket mySocket;
    PrintWriter serverOut;
    BufferedReader serverIn;


    public ClientThread() throws IOException {
        mySocket = new Socket("localhost",6969);
        serverIn = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
        serverOut = new PrintWriter(mySocket.getOutputStream(),true);
        if(mySocket.isConnected())
            System.out.println("CONNECTED TO SERVER");
    }
}
