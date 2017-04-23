import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.awt.print.Book;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by teo on 20-Mar-17.
 */
public class MovieProcessor implements IStringProcessor<Movie> {

    @Override
    public Movie parseString(String toParse) {
        //Movies are held in file as such: name;rating;year
        String[] splitString = toParse.split(";");
        return new Movie(splitString[0], Integer.parseInt(splitString[1]), Integer.parseInt(splitString[2]), Movie.getGlobalID());
    }

    @Override
    public Movie parseXML(Node toParse) {
        String movieName = ((Element) toParse).getAttribute("name");
        int movieRating = Integer.parseInt(((Element) toParse)
                .getElementsByTagName("rating")
                .item(0)
                .getTextContent());
        int movieReleaseDate = Integer.parseInt(((Element) toParse)
                .getElementsByTagName("yearofrelease")
                .item(0)
                .getTextContent());

        return new Movie(movieName, movieRating, movieReleaseDate, Movie.getGlobalID());
    }

    @Override
    public Node createNode(Document document, Element root, Movie object) {
        Node movieNode = document.createElement("movie");

        ((Element) movieNode).setAttribute("name", object.getName());

        Node ratingNode = document.createElement("rating");
        ratingNode.setTextContent(String.valueOf(object.getRating()));
        movieNode.appendChild(ratingNode);

        Node yearNode = document.createElement("yearofrelease");
        yearNode.setTextContent(String.valueOf(object.getYearOfRelease()));
        movieNode.appendChild(yearNode);
        return movieNode;
    }
}
