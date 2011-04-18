package implementation.importers;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySqlBaseImporter {

	public static void connect() {
		try {
			String myDriver = "com.mysql.jdbc.Driver";
			String myUrl = "jdbc:mysql://localhost/test";
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, "root", "LocalSQLPass");

			conn.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			e.printStackTrace();
		}
	}
}
