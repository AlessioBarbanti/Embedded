package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import SerialUtilities.PortUtilities;
import actionListeners.DecreaserGradeAL;
import actionListeners.IncreaserGradeAL;
import actionListeners.SetGradeAL;
import actionListeners.SetNewGradeValueAL;

public class Settings extends JPanel {
	
	public static final long serialVersionUID = -747113765621726874L;
	private static final String delimitator = "-----------------------"
											+ "-----------------------"
											+ "-----------------------";
	
	private JPanel cont = new JPanel();
	private JPanel line1 = new JPanel();
	private JPanel line = new JPanel();
	private JButton left = new JButton(" <--- left ");
	private JButton right = new JButton(" right ---> ");
	private JButton go = new JButton(" set ");
	private JButton send = new JButton(" send to Arduino your grade ");
	private JButton goPot = new JButton(" send to Arduino your scan-time ");
	private JButton pot = new JButton(" set scan-time from potentiometer ");
	private JTextField newValue = new JTextField("0");
	private JTextField val = new JTextField("");
	private JLabel panel = new JLabel("Serial Control Panel");
	private JLabel grade = new JLabel("0");
	private JLabel set = new JLabel("set new value for the grade (0°-180°) ");
	private JLabel d = new JLabel("set new value for the scan-time (2s-10s) ");
	private static JLabel alarmTxt = new JLabel("ALARM!!");
	private static JLabel trackingTxt = new JLabel("");
	private JPanel line2 = new JPanel();
	private static JPanel sentP = new JPanel();
	private static JScrollPane sent = new JScrollPane(sentP);
	private static JPanel rcvdP = new JPanel();
	private static JScrollPane rcvd = new JScrollPane(rcvdP);
	private static JPanel alarm = new JPanel();
	private Color myGrey = new Color(224, 224, 224);
	private Color myRed = new Color(102, 0, 0);
	private static Font myFont = new Font("TimesRoman", Font.BOLD, 16);
	private Font myFont2 = new Font("TimesRoman", Font.PLAIN, 16);
	private static Font myFont3 = new Font("TimesRoman", Font.BOLD, 60);
	private static Font myFont4 = new Font("TimesRoman", Font.BOLD, 45);
	
	
	Settings(PortUtilities p) {
		
		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.NORTH);
		this.add(cont, BorderLayout.CENTER);
		this.setBackground(myGrey);
		this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
		cont.add(line1);
		cont.add(line);
		cont.add(line2);
		cont.setBackground(myGrey);
		
		panel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.setFont(new Font("TimesRoman", Font.BOLD, 26));
		
		left.setBackground(Color.WHITE);
		left.setForeground(myRed);
		left.setBorder(new LineBorder(myRed, 1, true));
		left.setFont(myFont);
		left.addActionListener(new DecreaserGradeAL(this.getGrade()));
		
		right.setBackground(Color.WHITE);
		right.setBorder(new LineBorder(myRed, 1, true));
		right.setForeground(myRed);
		right.setFont(myFont);
		right.addActionListener(new IncreaserGradeAL(this.getGrade()));

		line1.setLayout(new FlowLayout(SwingConstants.CENTER, 38, 20));
		line1.add(set);
		line1.add(newValue);
		line1.add(go);
		line1.add(left);
		line1.add(grade);
		line1.add(right);
		line1.add(send);
		line1.setBackground(myGrey);

		
		line.setLayout(new FlowLayout(SwingConstants.CENTER, 40, 20));
		line.setBackground(myGrey);
		line.add(pot);
		line.add(d);
		line.add(val);
		line.add(goPot);
		line.setVisible(false);
		
		line2.setLayout(new FlowLayout(SwingConstants.CENTER, 20, 5));
		line2.setBackground(myGrey);
		line2.add(sent);
		line2.add(rcvd);
		line2.add(alarm);

		
		sent.setBackground(new Color(255, 255, 255));
		sent.setFont(myFont);
		sent.setPreferredSize(new Dimension(300,150));
		sent.setAutoscrolls(true);
		sent.setBackground(Color.WHITE);
		sent.setForeground(myRed);
		sent.setBorder(new LineBorder(myRed, 1, true));
		sent.setAutoscrolls(true);
		sent.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sent.getVerticalScrollBar().setValue(rcvd.getVerticalScrollBar().getMaximum());
		sentP.setLayout(new BoxLayout(sentP, BoxLayout.Y_AXIS));
		
		
		rcvd.setFont(myFont);
		rcvd.setBackground(new Color(255, 255, 255));
		rcvd.setPreferredSize(new Dimension(300,150));
		rcvd.setBackground(Color.WHITE);
		rcvd.setForeground(myRed);
		rcvd.setBorder(new LineBorder(myRed, 1, true));
		rcvd.setAutoscrolls(true);
		rcvd.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		rcvd.getVerticalScrollBar().setValue(rcvd.getVerticalScrollBar().getMaximum());
		rcvdP.setLayout(new BoxLayout(rcvdP, BoxLayout.Y_AXIS));
		
		alarm.setFont(myFont);
		alarm.setBackground(new Color(255, 255, 255));
		alarm.setPreferredSize(new Dimension(300,150));
		alarm.setBackground(Color.WHITE);
		alarm.setForeground(myRed);
		alarm.setBorder(new LineBorder(myRed, 1, true));
		alarm.add(alarmTxt);
		
		alarmTxt.setFont(myFont3);
		alarmTxt.setForeground(Color.WHITE);

		
		newValue.setPreferredSize(new Dimension(50, 25));
		
		grade.setFont(myFont2);
		
		set.setFont(myFont);
		
		d.setFont(myFont);
		
		val.setPreferredSize(new Dimension(50, 25));
		
		pot.setBackground(Color.WHITE);
		pot.setBorder(new LineBorder(myRed, 1, true));
		pot.setForeground(myRed);
		pot.setFont(myFont);
		pot.addActionListener(e -> {
			try {
				p.output("tp");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		go.setBackground(Color.WHITE);
		go.setForeground(myRed);
		go.setBorder(new LineBorder(myRed, 1, true));
		go.setFont(myFont);
		go.addActionListener(new SetNewGradeValueAL(this));
		
		send.setBackground(Color.WHITE);
		send.setForeground(myRed);
		send.setBorder(new LineBorder(myRed, 1, true));
		send.setFont(myFont);
		send.addActionListener(new SetGradeAL(p, this.getGrade()));
		
		goPot.setBackground(Color.WHITE);
		goPot.setForeground(myRed);
		goPot.setBorder(new LineBorder(myRed, 1, true));
		goPot.setFont(myFont);
		goPot.addActionListener(e -> {
			try {
				int tmpS = 0;
				try{
					tmpS = Integer.parseInt(val.getText());
				} catch (NumberFormatException e1) {
					val.setText("2");
				}
				
				if(tmpS < 2) {
					tmpS = 2;
					val.setText("2");
				} else if(tmpS > 10) {
					tmpS = 10;
					val.setText("10");
				}
				p.output("t"+val.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

	}
	
	public JPanel getLine1() {
		return line1;
	}
	
	public JPanel getLine() {
		return line;
	}
	
	public JLabel getGrade() {
		return grade;
	}
	
	public JTextField getNewValue() {
		return newValue;
	}
	
	static public void setSentText(String s) {
		JLabel l = new JLabel("SENT: |"+s+"|");
		l.setFont(myFont);
		sentP.add(l);
		sentP.add(new JLabel(delimitator));
		sentP.setVisible(false);
		sentP.setVisible(true);
		sent.getVerticalScrollBar().setValue(sent.getVerticalScrollBar().getMaximum());
		return;
	}
	
	static public void setRcvdText(String s) {
		JLabel l = new JLabel("RECEIVED: |"+s+"|");
		l.setFont(myFont);
		rcvdP.add(l);
		rcvdP.add(new JLabel(delimitator));
		rcvdP.setVisible(false);
		rcvdP.setVisible(true);
		rcvd.getVerticalScrollBar().setValue(rcvd.getVerticalScrollBar().getMaximum());
	}
	
	public static void setAlarm(String s) {
		if(s.equals("a1")) {
			alarmTxt.setText("ALARM!!");
			alarmTxt.setFont(myFont3);
			alarm.setBackground(Color.RED);
			alarmTxt.setBackground(Color.RED);
		} else if(s.equals("a0")) {
			alarmTxt.setText("ALARM!!");
			alarmTxt.setFont(myFont3);
			alarm.setBackground(Color.WHITE);
			alarmTxt.setBackground(Color.WHITE);
		} else if(s.equals("a2")) {
			alarmTxt.setText("TRACKING!!");
			alarmTxt.setFont(myFont4);
			alarm.setBackground(Color.RED);
			alarmTxt.setBackground(Color.RED);
		}  else if(s.equals("b1")) {
			alarmTxt.setText("DETECTED");
			alarmTxt.setFont(myFont4);
			alarm.setBackground(Color.GREEN);
			alarmTxt.setBackground(Color.GREEN);
		} else if(s.equals("b0")) {
			alarmTxt.setText("DETECTED!!");
			alarmTxt.setFont(myFont4);
			alarm.setBackground(Color.WHITE);
			alarmTxt.setBackground(Color.WHITE);
		}
	}
}
