package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import SerialUtilities.PortUtilities;
import actionListeners.ConnectAL;
import actionListeners.RefreshPortsAL;
import actionListeners.StartAL;
import jssc.SerialPortList;


public class Welcome extends JFrame{
	

	public static final long serialVersionUID = 1L;
	public Color myRed = new Color(102, 0, 0);
	
	private JPanel cont = new JPanel();
	private JPanel center2 = new JPanel();
	private JPanel center = new JPanel();
	private JLabel title = new JLabel();
	private JLabel frase = new JLabel(" Choose the number of the segments (1-180): ");
	private JTextField numberOfSegments = new JTextField();
	private JButton start = new JButton("START");
	private JButton connect = new JButton("CONNECT TO SERIAL");
	private JButton refreshPorts = new JButton("Refresh Ports");
	private JComboBox<String> comb =new JComboBox<String>(SerialPortList.getPortNames());
	private PortUtilities p;
	private static Font myFont = new Font("TimesRoman", Font.BOLD, 16);

	public Welcome() {

		this.setSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().width / 3),
				                   (int)(Toolkit.getDefaultToolkit().getScreenSize().height / 3)));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(cont);
		this.setVisible(true);
		this.setTitle("Smart Radar Controller for Arduino");
		this.setResizable(false);	
		
		title.setText("SMART RADAR");
		title.setFont(new Font("TimesRoman", Font.BOLD, 60));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setForeground(Color.WHITE);
		
		
		cont.setLayout(new FlowLayout(BoxLayout.Y_AXIS));
		cont.setBackground(myRed);
		cont.add(title, BorderLayout.NORTH);
		cont.add(center, BorderLayout.CENTER);
		cont.add(center2, BorderLayout.CENTER);

		center.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
		
		frase.setForeground(Color.WHITE);
		
		center.add(refreshPorts);
		center.add(comb);
		center.add(connect);
		center.add(start);	
		center.setBackground(myRed);
		
		numberOfSegments.setPreferredSize(new Dimension(50, 25));
		numberOfSegments.setText("16");
		
		center2.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
		center2.setBackground(myRed);
		center2.add(frase);
		center2.add(numberOfSegments);
	
		refreshPorts.addActionListener(new RefreshPortsAL(this));
		
		connect.setBackground(Color.WHITE);
		connect.setForeground(myRed);
		connect.addActionListener(new ConnectAL(this));
		start.setBackground(myRed);
		start.setForeground(Color.WHITE);
		start.setEnabled(false);
		start.addActionListener(new StartAL(this));	
	}
	
	public PortUtilities getP() {
		return p;
	}
	
	public void setP(PortUtilities p1) {
		p = p1;
	}
	
	public JButton getConnect() {
		return connect;
	}
	
	public JButton getStart() {
		return start;
	}
	
	public JComboBox<String> getComb() {
		return comb;
	}
	
	public int getNumOfSeg() {
		int tmp = 0;
		try{
			tmp = Integer.parseInt(numberOfSegments.getText());
		} catch(NumberFormatException e1) {
			numberOfSegments.setText("16");
			tmp = Integer.parseInt(numberOfSegments.getText());
		}
		if(tmp<1) {
			numberOfSegments.setText("16");
			tmp = Integer.parseInt(numberOfSegments.getText());
		}
		if(tmp>180) {
			numberOfSegments.setText("16");
			tmp = Integer.parseInt(numberOfSegments.getText());
		}
		return tmp;
	}
	
}
