import java.sql.Connection;
import java.sql.DriverManager;

class Database {
    public static Connection connection;


        public static void connect() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost/Dictionary?serverTimezone=EST", "root", "database28");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }



















































/*import java.sql.Connection;
import java.sql.DriverManager;

class Database {
	public static Connection connection;
	
	public static void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String databaseName = "doctors_office"; //String userName = "root"; String password = "minecraft242";
			String userName = "Reader"; String password = "read123";
			connection = DriverManager.getConnection("jdbc:mysql://localhost/" + databaseName + "?serverTimezone=EST", userName, password);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}*/