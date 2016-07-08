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

public class JPit extends JFormattedTextField {

	private int pitNumber;
	private Centrifuge c;
	private JCentrifuge jC;
	private final int MAXSIZE = 85;

	/**
	 * Create the panel.
	 */

	public JPit(int pitNumber, Centrifuge c, JCentrifuge jC) {
		super();
		this.pitNumber = pitNumber;
		this.c = c;
		this.jC = jC;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(3);
		setLayout(null);
		final int pitSize = Math.min(MAXSIZE, Math.max(MAXSIZE - c.getPits(), 15));
		setSize(pitSize, pitSize);


		setToolTipText(Integer.toString(pitNumber+1));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				int size=Math.min(MAXSIZE, getWidth() * 2);
				setSize(size, size);
				setFont(new Font("Tahoma", Font.PLAIN, getHeight()/2));
				grabFocus();
			}

			public void mouseExited(MouseEvent e) {
				setSize(pitSize, pitSize);
				setFont(new Font("Tahoma", Font.PLAIN, getHeight()/2));
				}

			@Override
			public void mouseClicked(MouseEvent e) {
				selectAll();

			}
		});
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				double value = 0;
				try {
					value = Double.parseDouble(getText());
				} catch (Exception e2) {
				}
				setMass(value);
				refresh();
			}
		});
		setFont(new Font("Tahoma", Font.PLAIN, Math.max(10, pitSize / 2)));
		setHorizontalAlignment(SwingConstants.TRAILING);
		refresh();
	}

	private void setMass(double mass) {
		c.setMass(mass, this.pitNumber);
	}

	public void refresh() {
		setBackground(
				c.isDummy(pitNumber) ? Color.ORANGE : (c.getMass(pitNumber) == 0 ? Color.WHITE : Color.CYAN));
		NumberFormat nf = NumberFormat.getInstance();
		double mass=c.getMass(pitNumber);
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits((mass>=10?1:2));
		setText(nf.format(mass));
		jC.calcScore();
	}
}
