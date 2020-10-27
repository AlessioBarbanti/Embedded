package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import SerialUtilities.PortUtilities;
import SerialUtilities.checkOutThread;
import actionListeners.ChangeModAL;

public class ControlPanel extends JFrame {
	
	private static final long serialVersionUID = -8807677682756492855L;
	
	JButton manual = new JButton("MANUAL MODE");
	JButton auto = new JButton("AUTO MODE");
	JButton single = new JButton("SINGLE MODE");
	JPanel cont = new JPanel();
	JPanel north = new JPanel();
	Settings pan;
	static Color myRed = new Color(102, 0, 0);
	static ArrayList<JButton> mod = new ArrayList<JButton>(3);
	
	public ControlPanel(Welcome welcome) {
		
		PortUtilities p = welcome.getP();
		pan = new Settings(p);
		
		
		this.add(cont);
		this.setSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().width / 1.5),
                (int)(Toolkit.getDefaultToolkit().getScreenSize().height / 2)));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setTitle("Smart Radar Controller for Arduino on the port: " + welcome.getComb().getSelectedItem());
		this.setResizable(false);
		
		cont.setLayout(new BorderLayout());
		cont.add(pan, BorderLayout.CENTER);
		cont.add(north, BorderLayout.NORTH);
		cont.setBackground(myRed);
		
		north.setBackground(myRed);
		north.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
		north.add(manual);
		north.add(auto);
		north.add(single);
		
		mod.add(manual);
		mod.add(auto);
		mod.add(single);
		
		for(JButton elem : mod) {
			if (elem.getText() == mod.get(0).getText()) {
				elem.setBackground(Color.WHITE);
				elem.setForeground(myRed);
				pan.getLine1().setVisible(true);
			} else {
				elem.setBackground(myRed);
				elem.setForeground(Color.WHITE);
			}
			elem.addActionListener(new ChangeModAL(p, pan.getLine1(), pan.getLine()));
		};
		
		checkOutThread t = new checkOutThread(p);
		t.start();
		this.pack();
	}
	
	public static ArrayList<JButton> getArrayButtons() {
		return mod;
	}
	
	public static Color getMyRed() {
		return myRed;
	}
	
}
