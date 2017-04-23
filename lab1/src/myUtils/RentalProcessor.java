import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by teo on 20-Mar-17.
 */
public class RentalProcessor implements IStringProcessor<Rental> {

    @Override
    public Rental parseString(String toParse) {
        String[] splitString = toParse.split(";");
        return new Rental(Integer.parseInt(splitString[0]), Integer.parseInt(splitString[1]), Rental.getGlobalID());
    }

    @Override
    public Rental parseXML(Node toParse) {
        //String rental = ((Element) toParse).getAttribute("rental");
        int movieID = Integer.parseInt(
                ((Element) toParse)
                .getElementsByTagName("movie")
                .item(0)
                .getTextContent()
        );
        int clientID = Integer.parseInt(
                ((Element) toParse)
                .getElementsByTagName("client")
                .item(0)
                .getTextContent()
        );

        return new Rental(movieID, clientID, Rental.getGlobalID());
    }

    @Override
    public Node createNode(Document document, Element root, Rental object) {
        Node rentalNode = document.createElement("rental");

        Node movieId = document.createElement("movie");
        movieId.setTextContent(String.valueOf(object.getMovie()));
        rentalNode.appendChild(movieId);

        Node clientId = document.createElement("client");
        movieId.setTextContent(String.valueOf(object.getClient()));
        rentalNode.appendChild(clientId);
        return rentalNode;
    }
}
