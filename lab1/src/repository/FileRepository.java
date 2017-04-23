import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by teo on 16-Mar-17.
 */
public class FileRepository<ID, T extends BaseEntity<ID>> implements TRepository<ID, T> {
    private String filename;
    private IStringProcessor<T> stringProcessor;
    private HashMap<ID, T> data;

    public FileRepository(String fileName, IStringProcessor<T> stringProcessor) throws IOException {
        this.filename = fileName;
        this.stringProcessor = stringProcessor;
        data = new HashMap<ID, T>();
        grabData();
    }

    private void grabData() throws IOException {
        File testFile = new File(filename);
        if(!testFile.isFile())
            testFile.createNewFile();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line = bufferedReader.readLine();
        while(line != null){
            T objectToAdd = stringProcessor.parseString(line);
            save(objectToAdd);
            line = bufferedReader.readLine();
        }
    }

    private void writeToFile() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename));
        PrintWriter printWriter = new PrintWriter(bufferedWriter);
        data.values().forEach(entity -> {printWriter.write(entity.toString() + "\n");});
        printWriter.close();
    }

    @Override
    public Optional<T> findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Iterable<T> findAll() {
        Set<T> allEntities = data.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toSet());
        return allEntities;
    }

    @Override
    public Optional<T> save(T entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null");
        T result = data.putIfAbsent(entity.getId(), entity);
        try {
            writeToFile();
        } catch (IOException e) {
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
            writeToFile();
        } catch (IOException e) {
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
        return Optional.ofNullable(result);
    }
}






