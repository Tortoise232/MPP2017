import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by teo on 16-Mar-17.
 */
public interface IStringProcessor<T> {
    T parseString(String toParse);
    T parseXML(Node node);
    Node createNode(Document document, Element root, T object);
}
