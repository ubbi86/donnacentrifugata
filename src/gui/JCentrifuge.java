package gui;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import engine.Centrifuge;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class JCentrifuge extends JPanel {

	private Centrifuge c;
	private ArrayList<JPit> pits;
	private JLabel lblPit;
	private JLabel lblScore;
	private JFormattedTextField armLength;
	private JFormattedTextField speed;
	private JFormattedTextField gForce;
	private JLabel lblArmLength;
	private JLabel lblRpm;
	private JLabel lblG;

	/**
	 * Create the panel.
	 */
	public JCentrifuge(final Centrifuge c) {
		this.c = c;
		setLayout(null);
		setOpaque(false);
		setBounds(0, 0, 650, 700);
		lblPit = new JLabel(Integer.toString(c.getPits()) + " pits");
		lblPit.setBounds(getWidth() / 2 - 50, getHeight() / 2 - 80, 100, 100);
		lblPit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(lblPit);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(false);
		lblScore = new JLabel("Score: " + nf.format(c.calcScore()));
		lblScore.setBounds(getWidth() / 2, getHeight() / 2 + 25, 200, 100);
		lblScore.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(lblScore);

		int fieldSize = 70;

		armLength = new JFormattedTextField();
		armLength.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				newArm();
			}
		});

		armLength.setHorizontalAlignment(SwingConstants.TRAILING);
		armLength.setBounds(

				getWidth() / 2 - 2 * fieldSize, getHeight() / 2 + 100, fieldSize, 50);
		armLength.setText("10.0");
		armLength.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(armLength);

		speed = new JFormattedTextField();
		speed.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				newSpeed();
			}
		});
		speed.setHorizontalAlignment(SwingConstants.TRAILING);
		speed.setBounds(getWidth() / 2 - fieldSize / 2, getHeight() / 2 + 100, fieldSize, 50);
		speed.setValue("1000");
		speed.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(speed);

		gForce = new JFormattedTextField();
		gForce.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				newGForce();
			}
		});
		gForce.setHorizontalAlignment(SwingConstants.TRAILING);
		gForce.setBounds(getWidth() / 2 + fieldSize, getHeight() / 2 + 100, fieldSize, 50);
		gForce.setValue("11");
		gForce.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(gForce);
		
		lblArmLength = new JLabel("arm length [cm]");
		lblArmLength.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblArmLength.setHorizontalAlignment(SwingConstants.TRAILING);
		lblArmLength.setBounds(155, 499, 100, 14);
		add(lblArmLength);
		
		lblRpm = new JLabel("RPM");
		lblRpm.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRpm.setHorizontalAlignment(SwingConstants.TRAILING);
		lblRpm.setBounds(325, 499, 35, 14);
		add(lblRpm);
		
		lblG = new JLabel("g");
		lblG.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblG.setHorizontalAlignment(SwingConstants.TRAILING);
		lblG.setBounds(450, 499, 15, 14);
		add(lblG);
		remake();
	}

	public void remake() {
		if (pits != null)
			for (JPit p : pits)
				remove(p);
		pits = new ArrayList<JPit>();
		JPit dummyP = new JPit(0, c, this);
		int pitSize = dummyP.getHeight();
		Point center = new Point(getWidth() / 2 - pitSize / 2, getHeight() / 2 - pitSize / 2);
		int radius = Math.min(getWidth() / 2, getHeight() / 2) - 50;
		for (int i = 0; i < c.getPits(); i++) {
			JPit pit = new JPit(i, c, this);
			pits.add(pit);
			add(pit);
			pit.setBounds((int) (c.getPosition(i).getX() * radius + center.getX()),
					(int) (c.getPosition(i).getY() * radius + center.getY()), pit.getWidth(), pit.getHeight());
			lblPit.setText(Integer.toString(c.getPits()) + " pits");
		}
	}

	public void refresh() {
		for (int i = 0; i < pits.size(); i++)
			pits.get(i).refresh();
		calcScore();
	}

	public void calcScore() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		lblScore.setText("Score: " + nf.format(c.calcScore()));
	}

	public void newGForce() {
		double value = 0;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		nf.setMinimumFractionDigits(0);
		nf.setGroupingUsed(false);

		try {
			value = Double.parseDouble(gForce.getText());
		} catch (Exception e) {
		}
		gForce.setValue(nf.format(value));
		speed.setValue(nf.format(c.rpmCalc(value)));
	}

	public void newSpeed() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		nf.setMinimumFractionDigits(0);
		nf.setGroupingUsed(false);
		int value = 0;
		try {
			value = Integer.parseInt(speed.getText());
		} catch (Exception e) {
		}
		speed.setValue(nf.format(value / 100 * 100));
		gForce.setValue(nf.format(c.gCalc(value)));
	}

	public void newArm() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		nf.setMinimumFractionDigits(1);
		nf.setGroupingUsed(false);

		double value = 0;
		try {
			value = Double.parseDouble(armLength.getText());
		} catch (Exception e) {
		}
		c.setArm(value);
		armLength.setValue(nf.format(c.getArm()));
		int rpm = 0;
		try {
			rpm = Integer.parseInt(speed.getText());
		} catch (Exception e) {
		}

		nf.setMaximumFractionDigits(0);
		nf.setMinimumFractionDigits(0);
		gForce.setValue(nf.format(c.gCalc(rpm)));
	}
}
