package actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;


public class DecreaserGradeAL implements ActionListener {
	
	private JLabel grade;
	
	public DecreaserGradeAL(JLabel grade) {
		this.grade = grade;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int temp = Integer.parseInt(grade.getText());
		if (temp ==  0) {
			grade.setText(Integer.toString(0));
		}
		else {
			grade.setText(Integer.toString(temp -1));
		}
	}
}
