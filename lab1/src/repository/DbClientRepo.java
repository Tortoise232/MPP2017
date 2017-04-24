import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Petean Mihai on 3/27/2017.
 */
public class DbClientRepo implements TRepository<Integer, Client>{
    Connection connection;
    private static int maxID = 0;
    public DbClientRepo() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sdiProject",
                "postgres",
                "teslaCoil");
        PreparedStatement myState = this.connection.prepareStatement("SELECT MAX(id) FROM client");
        ResultSet myResult = myState.executeQuery();
        myResult.next();
        this.maxID = myResult.getInt(1);
    }
    @Override
    public Optional<Client> findOne(Integer integer) {
        try {
            PreparedStatement myState = this.connection.prepareStatement("SELECT * FROM client WHERE id = " + integer);
            ResultSet myResult = myState.executeQuery();
            myResult.next();
            return Optional.of(new Client(myResult.getString("name"), myResult.getInt("id")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Client> findAll(){
        ArrayList<Client> clients = new ArrayList<>();
        try {
            PreparedStatement myState = this.connection.prepareStatement("SELECT * FROM client");
            ResultSet myResult = myState.executeQuery();
            while(myResult.next())
            {
                clients.add(new Client(myResult.getString("name"), myResult.getInt("id")));
            }
        } catch (SQLException e) {
        e.printStackTrace();
    }
        return clients;
    }

    @Override
    public Optional<Client> save(Client entity) {
        try {
            PreparedStatement myState = this.connection.prepareStatement("INSERT INTO client VALUES(" + ++this.maxID + ",'" + entity.getName() + "')");
            myState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Optional<Client> delete(Integer integer) {
        try {
            PreparedStatement myState = this.connection.prepareStatement("DELETE FROM client WHERE id = " + integer);
            myState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Client> update(Client entity) {
        try {
            PreparedStatement myState = this.connection.prepareStatement("UPDATE client SET name = " + entity.getName() + "WHERE id = " + entity.getId());
            myState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
