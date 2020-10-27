package SerialUtilities;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.fazecast.jSerialComm.SerialPort;

import view.Settings;
import view.Welcome;

public class PortUtilities {

	private Object obj = new Object();
	private SerialPort sp;
	
	
	public PortUtilities(Object obj, Welcome welcome) {
		this.obj = obj;
		try {
			this.sp = SerialPort.getCommPort(obj.toString());
		} catch(NullPointerException e) {
			JOptionPane.showMessageDialog(welcome , "Please make sure to select a Serial port (ex: 'COM6') \nTry to unplug and plug your arduino and click the 'refresh' button");
		}
		
	    sp.setComPortParameters(115200, 8, 1, 0); // default connection settings for Arduino
	    sp.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER , 0, 0); // block until bytes can be written
	}
	
	public boolean open(){
	    if (sp.openPort()) {
	      System.out.println("Port "+obj.toString()+" is open :)");
	      return true;
	    } else {
	      System.out.println("Failed to open port :(");
	      return false;
	    }    
	}
	
	public boolean close(){
	    if (sp.closePort()) {
	      System.out.println("Port is closed :)");
	      return true;
	    } else {
	      System.out.println("Failed to close port :(");
	      return false;
	    }
	}
	
	public Color checkConnection(){
	    if (open() && close()) {
	    	return Color.GREEN;
	    } else return Color.RED;
	}
	
	public void output(String s) throws IOException {
		s = s + "\n";
		sp.getOutputStream().write(s.getBytes());
		System.out.println(s);
		if(s.equals("m\n")) {
			s = "change mode to : MANUAL";
		} else if(s.equals("s\n")) {
			s = "change mode to : SINGLE";
		} else if(s.equals("a\n")) {
			s = "change mode to : AUTO";
		} else if(s.contains("d")) {
			s = s.replace("d", "");
			s = ("move servo to " + s + " degree");
		} else if(s.contains("tp")) {
			s = s.replace("tp", "");
			s = ("the scan time is set from the potentiometer");
		} else if(s.contains("t") && !s.contains("tp")) {
			s = s.replace("t", "");
			s = ("the scan time is set at: "+s+" seconds");
		} else if(s.contains("k")) {
			s = s.replace("k", "");
			s = ("Number of segments set at: "+s);
		}
		Settings.setSentText(s);
	}
	
	public String input() throws IOException {
		
		String s = "";
		int c = 0;
		System.out.println((char)c);
		while(c != 13){
		c =  sp.getInputStream().read();
		if(c != 10 && c != 13) {
		s+=(char)c;
		}
		}
		return s;
	}
	
}