package actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JLabel;

import SerialUtilities.PortUtilities;

public class SetGradeAL implements ActionListener {

	PortUtilities p;
	JLabel grade;
	
	public SetGradeAL(PortUtilities p, JLabel grade) {
		this.p = p;
		this.grade = grade;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			p.output("d"+grade.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
