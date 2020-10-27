package actionListeners;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import view.ControlPanel;
import view.Welcome;

public class StartAL implements ActionListener {
	
	private Welcome welcome;
	
	public StartAL(Welcome welcome) {
		this.welcome = welcome;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
			if(welcome.getP().open()) {
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					System.out.println("errore");
				}
				String n = "k";
				n += welcome.getNumOfSeg();
				try {
					welcome.getP().output(n);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				new ControlPanel(welcome);
				welcome.dispose();
			} else {
				JOptionPane.showMessageDialog(welcome , "unable to connect");
				welcome.getStart().setBackground(Color.RED);
				welcome.getStart().setForeground(Color.WHITE);
				welcome.getStart().setEnabled(false);
				welcome.getConnect().setEnabled(true);
				welcome.getConnect().setBackground(Color.WHITE);
				welcome.getConnect().setForeground(welcome.myRed);
			}
	}
}