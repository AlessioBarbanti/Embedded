package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * 
 * @author Andrea
 *class for I/O with db
 */
public class Mysql {
	/*
	 * fields
	 */
	private final static String URL = "jdbc:mysql://localhost:3306/dumpster?serverTimezone=UTC";
    private final static String USER = "root";
    private final static String PASSWORD = "";
    private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS deposits (\n" +
	"     token MEDIUMINT NOT NULL,\n" +
	"     material CHAR(30) NOT NULL,\n" +
	"     weight MEDIUMINT NOT NULL,\n" +
	"     datetime DATETIME NOT NULL,\n" +
	"     PRIMARY KEY (token)\n" +
	");";
    private final static String INSERT_NEW = "INSERT INTO deposits " + "(token, material, weight, datetime) values (?, ?, ?, ?)";
    private final static String SELECT_ALL = "SELECT * from deposits";
    private final static String COUNT_DEP_BETWEEN_DATE= "SELECT COUNT(*) AS totale FROM deposits WHERE datetime BETWEEN ? AND ?";
    private final static String COUNT_DEP_TODAY= "SELECT COUNT(*) AS totale FROM deposits WHERE datetime LIKE ?";
    private final static String TOTAL_WEIGHT= "SELECT SUM(weight) AS totale FROM deposits WHERE datetime BETWEEN ? AND ?";
    private final static String GET_MAX_TOKEN= "SELECT MAX(token) AS massimo FROM deposits";
	/**
	 * method unused to create the main table in the db.
	 */
	public void createTable() {
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);				
			       Statement stmt = conn.createStatement();) {									
				   		Class.forName(DRIVER);
				   		stmt.executeUpdate(CREATE_TABLE);										
			  } catch (ClassNotFoundException | SQLException ex) {
			            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
			}
	}
	/**
	 * method to save in the db each deposit done by the user.
	 * @param token
	 * @param material
	 * @param weight: 
	 */
	public static void insertIntoDB(int token, String material, int weight) {
		
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);		
				   Statement stmt = conn.createStatement();
			       PreparedStatement pstmt = conn.prepareStatement(INSERT_NEW);) {		
				   		Class.forName(DRIVER);
				   		pstmt.setInt(1, token);
				   		pstmt.setString(2, material);
				   		pstmt.setInt(3, weight);
				   		pstmt.setString(4, LocalDateTime.now().toString());				
				   		pstmt.execute();												
			  } catch (ClassNotFoundException | SQLException ex) {
			            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
			}
	}
	/**
	 * method used to get all data from db.
	 * @return a set 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static ResultSet selectAllFromDB() throws ClassNotFoundException, SQLException {
			ResultSet rs = null;
			try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);				
								   Statement stmt = conn.createStatement();) {								
					   					Class.forName(DRIVER);
					   					try (ResultSet temp = stmt.executeQuery(SELECT_ALL);) {
					   						rs = temp;
					   					} catch (SQLException ex) {
					   						Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
					   					  }
		   }
		return rs;
	}
	/**
	 * method to get number of deposits done in a range of date
	 * @param d1
	 * @param d2
	 * @throws SQLException 
	 */
	public static int getDepInRange(String d1, String d2) throws SQLException {
		int i = 0;
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);		
				   Statement stmt = conn.createStatement();
			       PreparedStatement pstmt = conn.prepareStatement(COUNT_DEP_BETWEEN_DATE);) {		
				   		Class.forName(DRIVER);
				   		pstmt.setString(1, d1);
				   		pstmt.setString(2, d2);			
				   		ResultSet temp = pstmt.executeQuery();
				   		while (temp.next()) {
					   		i = (temp.getInt(1));		
						}
			  } catch (ClassNotFoundException | SQLException ex) {
				  	i = 0;
			            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
			}
		if (i > 0) {
			return i;
		}
		else {
			return 0;
		}
	}
	
	/**
	 * method to get number of deposits done in a range of date
	 * @param token
	 * @param material
	 * @param weight: 
	 * @throws SQLException 
	*/
	public static int getTotalWeight(String d1, String d2) throws SQLException {
		int i = 0;
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);		
				   Statement stmt = conn.createStatement();
			       PreparedStatement pstmt = conn.prepareStatement(TOTAL_WEIGHT);) {		
				   		Class.forName(DRIVER);
				   		pstmt.setString(1, d1);
				   		pstmt.setString(2, d2);			
				   		ResultSet temp = pstmt.executeQuery();
				   		while (temp.next()) {
					   		i = (temp.getInt(1));		
						}
			  } catch (ClassNotFoundException | SQLException ex) {
				  	i = 0;
			            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
			}
		if (i > 0) {
			return i;
		}
		else {
			return 0;
		}
	}
	
	public static int getDepToday() throws SQLException {
		int i = 0;
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);		
				   Statement stmt = conn.createStatement();
			       PreparedStatement pstmt = conn.prepareStatement(COUNT_DEP_TODAY);) {		
				   		Class.forName(DRIVER);
				   		pstmt.setString(1, "%" +LocalDate.now().toString()+ "%");		
				   		ResultSet temp = pstmt.executeQuery();
				   		while (temp.next()) {
					   		i = (temp.getInt(1));		
						}
			  } catch (ClassNotFoundException | SQLException ex) {
				  	i = 0;
			            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
			}
		if (i > 0) {
			return i;
		}
		else {
			return 0;
		}
	}
	
	private static int getMaxToken() throws SQLException {
		int i = 0;
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);		
				   Statement stmt = conn.createStatement();
			       PreparedStatement pstmt = conn.prepareStatement(GET_MAX_TOKEN);) {		
				   		Class.forName(DRIVER);
				   		ResultSet temp = pstmt.executeQuery();
				   		while (temp.next()) {
					   		i = (temp.getInt(1));		
						}
			  } catch (ClassNotFoundException | SQLException ex) {
				  	i = 0;
			            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
			}	
			return i;
	}

	public static int setTokenfromDB() throws SQLException {
		return getMaxToken();
	}
}
