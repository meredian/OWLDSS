package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteBackEnd implements SQLBackEnd {

	private static final String DB_PATH = "sql/data.db";
	private Connection conn;

	public SQLiteBackEnd() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
		} catch (Exception e) {
			throw new AssertionError("SQLiteBackEnd : Can't connect DB");
		}
	}

	@Override
	public ResultSet getDataForClass(String className) throws SQLException  {
		Statement stat = conn.createStatement();
		return stat.executeQuery("SELECT * FROM " + normalizeClass(className));
	}

	@Override
	public ResultSet getDataForClassFiltered(String className, String[] attributes, String[] values)
			throws SQLException {
		Statement stat = conn.createStatement();
		String table = normalizeClass(className);
		String query = "SELECT * FROM " + table + collectConditions(table, attributes, values);
		System.out.println("SQLiteBackEnd: Executing query " + query);
		return stat.executeQuery(query);
	}

	public Connection getConnection() {
		return conn;
	}

	private String normalizeClass(String className) {
		return className + "s";
	}

	private String collectConditions(String table, String[] arg, String[] val) {
		String res = null;
		if( arg.length == val.length && arg.length > 0 ) {
			res = " WHERE `"+table+"`.`" + arg[0] + "` = '" + val[0] + "'";
			for( int i = 1; i < arg.length; ++i ) {
				res += " AND `"+table+"`.`" + arg[i] + "` = '" + val[i] + "'";
			}
			res += ";";
		}
		return res;
	}

	@Override
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.err.println("SQLiteBackEnd: Error closing DB connection");
		}
	}
}
