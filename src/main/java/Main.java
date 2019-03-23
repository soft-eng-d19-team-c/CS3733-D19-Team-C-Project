import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;

/**
 * @author Ryan LaMarche.
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("-------Embedded Java DB Connection Testing --------");
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Java DB Driver not found. Add the classpath to your module.");
            System.out.println("For IntelliJ do the following:");
            System.out.println("File | Project Structure, Modules, Dependency tab");
            System.out.println("Add by clicking on the green plus icon on the right of the window");
            System.out.println("Select JARs or directories. Go to the folder where the Java JDK is installed");
            System.out.println("Select the folder java/jdk1.8.xxx/db/lib where xxx is the version.");
            System.out.println("Click OK, compile the code and run it.");
            e.printStackTrace();
            return;
        }

        System.out.println("Java DB driver registered!");
        Connection connection = null;

        try {
            // substitute your database name for myDB
            connection = DriverManager.getConnection("jdbc:derby:TeamC;create=true");
        } catch (SQLException e) {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
            return;
        }
        System.out.println("Java DB connection established!");

        // creates new MainFXML object that loads main.fxml on a new thread, starting at its main method.
        MainFXML app = new MainFXML();
        app.main(args);

    }
}
