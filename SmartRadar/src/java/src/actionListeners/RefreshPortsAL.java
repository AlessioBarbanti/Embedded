package actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jssc.SerialPortList;
import view.Welcome;

public class RefreshPortsAL implements ActionListener {
	
	private Welcome welcome;
	
	public RefreshPortsAL(Welcome welcome) {
		this.welcome = welcome;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		welcome.getComb().removeAllItems();
		String[] tmp = SerialPortList.getPortNames();
		for(String s : tmp) {
		welcome.getComb().addItem(s);
		}
	}
}