package SerialUtilities;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;

import view.ControlPanel;
import view.Settings;

public class checkOutThread  extends Thread{
	
	PortUtilities p;
	ArrayList<JButton>  b = ControlPanel.getArrayButtons();
	String c = "";
	private String tmpGrade;
	
	public checkOutThread(PortUtilities p) {
		this.p = p;
	}
	
	public void run(){
		while(true) {
			try {
				c = p.input();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Arduino has been disconnected");
				System.exit(0);
				e.printStackTrace();
			  }
			
			if(c.equals("m")) {
				clickAtIndex(0);
			} else if (c.equals("s")){
				clickAtIndex(2);
			} else if(c.equals("a")) {
				clickAtIndex(1);
			} else if(c.contains("a") || c.contains("b"))  {
				Settings.setAlarm(c);
			} else if(c.contains("d")) {
				try {
					p.output(c);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				c = c.replace("d", "");
				Settings.setRcvdText("move servo to " + c + " degree");
			} else if(c.contains("L")) {
				
				c = c.replace("L", "");
				tmpGrade = c;
				
			}else if(c.contains("g")) {
				c = c.replace("g", "");
				Settings.setRcvdText("Object at " + c + "° & " + tmpGrade + "cm");
			} else {
				Settings.setRcvdText("COMMAND NOT FOUND: |"+c+"|");
			}
		}
	}
	
	private void clickAtIndex(final int i) {
		//SwingUtilities.invokeLater(() -> {
			b.get(i).doClick();			
		//});
	}

}
