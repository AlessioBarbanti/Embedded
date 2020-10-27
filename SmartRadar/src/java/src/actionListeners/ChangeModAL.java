package actionListeners;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;

import SerialUtilities.PortUtilities;
import view.ControlPanel;

public class ChangeModAL implements ActionListener {

	private PortUtilities p;
	private JPanel line1;
	private JPanel line;
	
	public ChangeModAL(PortUtilities p, JPanel line1, JPanel line) {
		this.p = p;
		this.line = line;
		this.line1 = line1;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton temp = (JButton) e.getSource();
		if(temp.getText() == "MANUAL MODE") {
			try {
				p.output("m");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (temp.getText() == "SINGLE MODE") {
			try {
				p.output("s");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			try {
				p.output("a");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		for(JButton elem2 : ControlPanel.getArrayButtons()) {
			if(temp == elem2) {
				elem2.setBackground(Color.WHITE);
				elem2.setForeground(ControlPanel.getMyRed());
				if (temp.getText() == ControlPanel.getArrayButtons().get(0).getText()) {
					line1.setVisible(true);
					line.setVisible(false);
				}
				else {
					line1.setVisible(false);
					line.setVisible(true);
				}
				
			} else {
				elem2.setBackground(ControlPanel.getMyRed());
				elem2.setForeground(Color.WHITE);
			}
		}

	}

}
