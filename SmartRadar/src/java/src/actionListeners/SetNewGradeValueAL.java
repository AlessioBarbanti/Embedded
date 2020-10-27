package actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.Settings;

public class SetNewGradeValueAL implements ActionListener {

	private Settings settings;
	
	public SetNewGradeValueAL(Settings settings) {
		this.settings = settings;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
			int tmpS = 0;
			try{
				tmpS = Integer.parseInt(settings.getNewValue().getText());
			} catch (NumberFormatException e1) {
				settings.getNewValue().setText("0");
			}
			
			if(tmpS < 0) {
				tmpS = 0;
				settings.getNewValue().setText("0");
			} else if(tmpS > 180) {
				tmpS = 180;
				settings.getNewValue().setText("180");
			}
			
			settings.getGrade().setText(""+tmpS);
	}
}
