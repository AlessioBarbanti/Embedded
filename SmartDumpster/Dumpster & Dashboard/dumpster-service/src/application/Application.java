package application;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import database.Mysql;
import inputOutput.DataService;
import io.vertx.core.Vertx;

/**
 * 
 * @author Andrea main class
 */
public class Application {
	/**
	 * main method used to start the program
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */

	public static void main(String[] args) throws IOException, InterruptedException, SQLException {
		/*
		 * String espIp, int espPort, String androidIp, int androidPort, String
		 * dashboardIp, int dashboardPort
		 */
		Vertx vertx = Vertx.vertx();
		DataService service = new DataService(5656);
		service.setToken();
		vertx.deployVerticle(service);
		Mysql mySql = new Mysql();
		mySql.createTable();
	}
}
