import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by teo on 16-Mar-17.
 */
public class ClientProcessor implements IStringProcessor<Client> {
    @Override
    public Client parseString(String toParse) {
        String[] splitString = toParse.split(";");
        return new Client(splitString[0], Client.getGlobalID());
    }

    @Override
    public Client parseXML(Node toParse) {
        String clientName = ((Element) toParse).getAttribute("name");
        return new Client(clientName, Client.getGlobalID());
    }

    @Override
    public Node createNode(Document document, Element root, Client object) {
        Node clientNode = document.createElement("client");

        ((Element) clientNode).setAttribute("name", object.getName());
        return clientNode;
    }
}