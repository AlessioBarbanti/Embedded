package inputOutput;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import database.Mysql;

/**
 * 
 * @author Andrea class for the I/O on http
 */
public class DataService extends AbstractVerticle {

	private int port;
	static int currToken = 0;
	static int token;
	static String material = "";
	static int weight = 0;
	static int currentWeight = 0;
	static String msgRcvd = "";

	static String startDate = new String();
	static String endDate = new String();
	static boolean available = true;
	static boolean busyDep = false;

	static boolean reqWeight = false;

	static boolean reqEmpty = false;
	static private List<varToSend> info = new ArrayList<>();

	/**
	 * costruttore.
	 * 
	 * @param prt
	 * @param esp
	 * @param android
	 * @param dashboard
	 */
	public DataService(int prt) {
		this.port = prt;
	}

	@Override
	public void start() throws ClassNotFoundException, SQLException {
		final Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		router.post("/api/*").handler(arg0 -> {
			try {
				handleGetNewData(arg0);
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		});
		router.get("/api/*").handler(arg0 -> {
			try {
				handleGetNewData(arg0);
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		});
		vertx.createHttpServer().requestHandler(router::accept).listen(port);

		log("Service ready.");

	}

	/**
	 * metodo che ascolta quello che arriva sulla porta e gestisce le info tramite
	 * lo switch
	 * 
	 * @param routingContext:
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void handleGetNewData(RoutingContext rc) throws ClassNotFoundException, SQLException {
		JsonObject jso = new JsonObject();
		String path = rc.normalisedPath();

		switch (path) {
		case "/api/token":
			received("TOKEN");
			if (available && busyDep == false) {
				token += 1;
				info.add(new varToSend("token", String.valueOf(token)));
				busyDep = true;
			} else {
				info.add(new varToSend("token", "Il servizio non è al momento disponibile, attendere!"));
			}
			break;
		case "/api/app":
			received("MATERIAL = " + rc.getBodyAsJson().getValue("m").toString() + " & TOKEN = " + rc.getBodyAsJson().getValue("t").toString());
			material = rc.getBodyAsJson().getValue("m").toString();
			currToken = Integer.valueOf(rc.getBodyAsJson().getValue("t").toString()).intValue();
			reqWeight= true;
			token+=1;
			info.add(new varToSend("risposta", "OK"));
			break;
		case "/api/exit":
			received("EXIT FROM APP: DEPOSIT NOT DONE WITH TOKEN " + token);
			info.add(new varToSend("risposta", "OK"));
			busyDep = false;
			break;
//		case "/api/d":
//			reqWeight = true;
//			busyDep = false;
//			received("DEPOSIT DONE");
//			info.add(new varToSend("risposta", "OK"));
//			break;
		case "/api/data":
			info.add(new varToSend("dep", Mysql.getDepInRange(startDate, endDate)));
			info.add(new varToSend("qta", Mysql.getTotalWeight(startDate, endDate)));
			received("DASHBOARD NEEDS STATISTICS");
			break;
		case "/api/full":
			available = false;
			received("fTRASH IS FULL");
			info.add(new varToSend("risposta", "OK"));
			break;
		case "/api/av":
			reqEmpty = true;
			available = true;
			busyDep = false;
			currentWeight = 0;
			received("DUMPSTER AVAILABLE");
			info.add(new varToSend("risposta", "OK"));
			break;
		case "/api/time":
			startDate = rc.getBodyAsJson().getString("inizio");
			endDate = rc.getBodyAsJson().getString("fine");
			info.add(new varToSend("risposta", "OK"));
			break;
		case "/api/statoEdge":
			info.add(new varToSend("disp", String.valueOf(available)));
			info.add(new varToSend("reqWeight", String.valueOf(reqWeight)));
			info.add(new varToSend("reqEmpty", String.valueOf(reqEmpty)));
			reqWeight = false;
			reqEmpty = false;
			break;
		case "/api/unav":
			available = false;
			received("DUMPSTER UNAVAILABLE");
			info.add(new varToSend("risposta", "OK"));
			break;
		case "/api/weight":
			weight = Integer.valueOf(rc.getBodyAsJson().getValue("w").toString()).intValue();
			currentWeight += weight;
			received("WEIGHT = " +String.valueOf(weight));
			Mysql.insertIntoDB(Integer.valueOf(currToken).intValue(), material, weight);
			received("INSERT TOKEN =" + currToken +" & MATERIAL = " + material);
			currToken = 0;
			reqWeight = false;
			info.add(new varToSend("risposta", "OK"));
			break;

		case "/api/stato":
			info.add(new varToSend("disp", String.valueOf(available)));
			info.add(new varToSend("dep", Mysql.getDepToday()));
			info.add(new varToSend("qta", String.valueOf(currentWeight)));
			received("DASHBOARD NEEDS CURRENT DATA");
			break;
		default:
			break;
		}
		sendInfo(rc, jso);
	}

	private void sendInfo(RoutingContext rc, JsonObject jso) {
		String str = new String();
		for (varToSend elem : info) {
			jso.put(elem.getString(), elem.getObject());
			str += elem.getString().toUpperCase() + " : " + elem.getObject().toString() + " & ";
		}
		rc.response().putHeader("content-type", "application/json").end(jso.encodePrettily());
		log("SEND: ---> " + str);
		str = null;
		info.removeAll(info);
	}

	/**
	 * metodo per stampare cosa si riceve
	 * 
	 * @param s stringa ricevuta
	 */
	private void received(String s) {
		log("Received: " + s);
	}

	/**
	 * @param msg: cosa stampare a video per informare l'utente
	 */
	private void log(String msg) {
		System.out.println("[DATA SERVICE] " + msg + "\n");
	}

	public void setToken() throws SQLException {
		token = Mysql.setTokenfromDB();
	}

	private static class varToSend {
		private String str;
		private Object obj;

		varToSend(String str, Object obj) {
			this.str = str;
			this.obj = obj;
		}

		public String getString() {
			return str;
		}

		public Object getObject() {
			return obj;
		}
	}
}