import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCDemo {
    public static void main(String[] args) {
        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://sst-stuproj.city.ac.uk:3306/in2033t17", "in2033t17_a", "3cL1h1l3tY8");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from test");

            while(resultSet.next()) {
                System.out.println(resultSet.getString(1) + " " + resultSet.getInt(2));
            }
            connection.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
