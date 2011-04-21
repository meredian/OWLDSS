package data;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLBackEnd {

	public ResultSet getDataForClass( String className ) throws SQLException;
	public ResultSet getDataForClassFiltered( String className, String[] attributes, String[] values )  throws SQLException;

	public void close();
}
