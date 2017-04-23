
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by teo on 27-Mar-17.
 */
public class XMLRepository<ID, T extends BaseEntity<ID>> implements TRepository<ID, T>{
    private String filename;
    private HashMap<ID, T> data;
    private IStringProcessor<T> stringProcessor;

    public XMLRepository(String filename, IStringProcessor<T> stringProcessor){
        this.filename = filename;
        this.data = new HashMap<ID, T>();
        this.stringProcessor = stringProcessor;
        try {
            grabData();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private void grabData() throws ParserConfigurationException, IOException, SAXException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
        Element root = document.getDocumentElement();
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++){
            Node node = nodes.item(i);
            if(node instanceof Element){
                T object = stringProcessor.parseXML(node);
                data.put(object.getId(), object);
            }
        }
    }

    private void writeData() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = document.createElement("data");
        document.appendChild(root);
        //Element root = document.createElement("stuff");
        data.values().forEach(entity -> {
            Node node = stringProcessor.createNode(document, root, entity);
            root.appendChild(node);
        });
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(document), new StreamResult(new PrintWriter(new File(filename))));
    }

    @Override
    public Optional<T> findOne(ID id) {
        if (id == null){
            throw new IllegalArgumentException("id must not be null");
        }
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Iterable<T> findAll() {
        Set<T> allEntities = data.entrySet().stream()
                .map(entry -> entry.getValue())
                .collect(Collectors.toSet());
        return allEntities;
    }

    @Override
    public Optional<T> save(T entity) {
        //i wanna die
        if (entity == null){
            throw new IllegalArgumentException("entity must not be null");
        }
        T result = data.putIfAbsent(entity.getId(), entity);
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
            Element root = document.getDocumentElement();
            Node node = stringProcessor.createNode(document, root, entity);
            root.appendChild(node);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(new File(filename)));
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<T> delete(ID id) {
        if(id == null)
            throw new IllegalArgumentException("id must not be null");
        T result = data.remove(id);
        try {
            writeData();
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<T> update(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        T result = data.computeIfPresent(entity.getId(), (k, v) -> entity);
        try {
           writeData();
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(result);
    }
}
