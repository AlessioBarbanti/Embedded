package actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;


public class IncreaserGradeAL implements ActionListener {

	private JLabel grade;
	
	public IncreaserGradeAL(JLabel grade) {
		this.grade = grade;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int temp = Integer.parseInt(grade.getText());
		if (temp ==  180) {
			grade.setText(Integer.toString(180));
		}
		else {
			grade.setText(Integer.toString(temp +1));
		}
	}
}
