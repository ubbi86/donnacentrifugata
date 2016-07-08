package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import engine.Centrifuge;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class JPit extends JPanel {

	private JFormattedTextField mass;
	private JLabel lbl;
	private int pitNumber;
	private Centrifuge c;
	private JCentrifuge jC;
	private final int MAXSIZE = 80;

	/**
	 * Create the panel.
	 */

	public JPit(int pitNumber, Centrifuge c, JCentrifuge jC) {
		super();
		setOpaque(false);
		this.pitNumber = pitNumber;
		this.c = c;
		this.jC = jC;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(3);
		setLayout(null);
		final int pitSize = Math.min(MAXSIZE, Math.max(MAXSIZE - c.getPits(), 15));
		setSize(pitSize * 2, pitSize);

		lbl = new JLabel(Integer.toString(pitNumber));
		lbl.setFont(new Font("Tahoma", Font.PLAIN, Math.max(10, pitSize / 4)));
		lbl.setBounds(pitSize, 0, pitSize, pitSize);
		add(lbl);
		mass = new JFormattedTextField();
		mass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mass.setSize(Math.min(MAXSIZE, mass.getWidth() * 2), Math.min(MAXSIZE, mass.getHeight() * 2));
				setSize(mass.getWidth() * 2, mass.getHeight());
				mass.setFont(new Font("Tahoma", Font.PLAIN, mass.getHeight()/2));
				lbl.setBounds(mass.getHeight(), 0, mass.getHeight(), mass.getHeight());
				mass.grabFocus();
			}

			public void mouseExited(MouseEvent e) {
				mass.setSize(pitSize, pitSize);
				setSize(mass.getWidth() * 2, mass.getHeight());
				mass.setFont(new Font("Tahoma", Font.PLAIN, mass.getHeight()/2));
				lbl.setBounds(pitSize, 0, pitSize, pitSize);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				mass.selectAll();

			}
		});
		add(mass);
		mass.setBounds(0, 0, pitSize, pitSize);
		mass.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				double value = 0;
				try {
					value = Double.parseDouble(mass.getText());
				} catch (Exception e2) {
				}
				setMass(value);
				refresh();
			}
		});
		mass.setFont(new Font("Tahoma", Font.PLAIN, Math.max(10, pitSize / 2)));
		mass.setHorizontalAlignment(SwingConstants.TRAILING);
		refresh();
	}

	private void setMass(double mass) {
		c.setMass(mass, this.pitNumber);
	}

	public void refresh() {
		mass.setBackground(
				c.isDummy(pitNumber) ? Color.ORANGE : (c.getMass(pitNumber) == 0 ? Color.WHITE : Color.CYAN));
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		mass.setText(nf.format(c.getMass(pitNumber)));
		jC.calcScore();
	}
}
