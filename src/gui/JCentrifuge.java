package gui;

import java.awt.Font;
import java.awt.Point;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import engine.Centrifuge;

public class JCentrifuge extends JPanel {

	private Centrifuge c;
	private ArrayList<JPit> pits;
	private JLabel lblPit;
	private JLabel lblScore;
	
	/**
	 * Create the panel.
	 */
	public JCentrifuge(Centrifuge c) {
		this.c = c;
		setLayout(null);
		setOpaque(false);
		setBounds(0, 0, 650, 700);
		lblPit = new JLabel(Integer.toString(c.getPits()) + " pits");
		lblPit.setBounds(getWidth() / 2-50, getHeight() / 2-80, 100, 100);
		lblPit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(lblPit);
		NumberFormat nf=NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		lblScore = new JLabel("Score: "+nf.format(c.calcScore()));
		lblScore.setBounds(getWidth() / 2, getHeight() / 2+25, 200, 100);
		lblScore.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(lblScore);
		remake();
	}

	public void remake() {
		if (pits != null)
			for (JPit p : pits)
				remove(p);
		pits = new ArrayList<JPit>();
		JPit dummyP=new JPit(0, c,this);
		int pitSize=dummyP.getHeight();
		Point center=new Point(getWidth()/2-pitSize/2, getHeight()/2-pitSize/2);
		int radius=Math.min(getWidth()/2, getHeight()/2)-50;
		for (int i = 0; i < c.getPits(); i++) {
			JPit pit = new JPit(i, c,this);
			pits.add(pit);
			add(pit);
			pit.setBounds((int) (c.getPosition(i).getX() * radius+center.getX()),
					(int) (c.getPosition(i).getY() * radius+center.getY()),
					pit.getWidth(),
					pit.getHeight());
		lblPit.setText(Integer.toString(c.getPits()) + " pits");
		}
	}

	public void refresh() {
		for (int i = 0; i < pits.size(); i++)
			pits.get(i).refresh();
		calcScore();
	}

	public void calcScore() {
		NumberFormat nf=NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		lblScore.setText("Score: "+nf.format(c.calcScore()));
	}

}
