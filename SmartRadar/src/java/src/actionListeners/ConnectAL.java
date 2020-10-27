package actionListeners;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import SerialUtilities.PortUtilities;
import view.Welcome;

public class ConnectAL implements ActionListener {
	
	private Welcome welcome;
	private PortUtilities p;
	
	public ConnectAL(Welcome welcome) {
		this.welcome = welcome;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
			p = new PortUtilities(welcome.getComb().getSelectedItem(), welcome);
			System.out.println(p);
			welcome.setP(p);
				Color cl = p.checkConnection();
				if(cl == Color.GREEN) {
					welcome.getConnect().setEnabled(false);
					welcome.getStart().setBackground(cl);
					welcome.getStart().setForeground(Color.BLACK);
					welcome.getConnect().setBackground(Color.CYAN);
					welcome.getStart().setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(welcome , "unable to connect");
					welcome.getStart().setBackground(cl);
					welcome.getStart().setEnabled(false);
					welcome.getStart().setForeground(Color.WHITE);
				}
	}
}
