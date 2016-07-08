package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import engine.Centrifuge;
import javax.swing.JSlider;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JMain extends JFrame {

	private JPanel contentPane;
	private JCentrifuge jC;
	private Centrifuge c;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JMain frame = new JMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Locale.setDefault(Locale.ENGLISH);
		setSize(650, 700);
		setResizable(false);
		setVisible(true);
		setTitle("Donna CentriFugata DOCG");
		contentPane = new JPanel();
		contentPane.setBackground(new Color(243,255,255));
		contentPane.setOpaque(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		c = new Centrifuge(10, 20);
		contentPane.setLayout(null);
		setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-666/2,
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2)-700/2,
				666, 700);
		

		JSlider SliderPits = new JSlider();
		int sliderSize = 300;
		
		JLabel lblCopyright = new JLabel("Copyright 2016 - Francesco \"Ubbi\" Massimino");
		lblCopyright.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblCopyright.setBounds(10, 646, 234, 14);
		contentPane.add(lblCopyright);
		SliderPits.setOpaque(false);
		SliderPits.setBounds(getWidth() / 2 - sliderSize/2, getHeight() / 2, sliderSize, 50);
		contentPane.add(SliderPits);
		SliderPits.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				c.setPits((int) ((JSlider) e.getSource()).getValue());
				jC.remake();
				repaint();
			}
		});
		SliderPits.setValue(c.getPits());
		SliderPits.setSnapToTicks(true);
		SliderPits.setPaintTicks(true);
		SliderPits.setPaintLabels(true);
		SliderPits.setMinorTickSpacing(1);
		SliderPits.setMinimum(4);
		
		JButton btnOptimize = new JButton("Balance");
		btnOptimize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.optimize();
				jC.refresh();
				repaint();
			}
		});
		btnOptimize.setFont(new Font("Tahoma", Font.PLAIN, 24));
		int btnSize=40;
		btnOptimize.setBounds(getWidth()/2-btnSize*3/2, getHeight()/2-btnSize/2-150, btnSize*3, btnSize);
		contentPane.add(btnOptimize);

		JButton btnDummy = new JButton("Dummy");
		btnDummy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.dummyBalance();
				jC.refresh();
				repaint();
			}
		});
		btnDummy.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnDummy.setBounds(getWidth()/2-btnSize*3/2, getHeight()/2-btnSize/2-100, btnSize*3, btnSize);
		contentPane.add(btnDummy);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.clearMasses();
				jC.refresh();
				repaint();
			}
		});
		btnClear.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnClear.setBounds(getWidth()/2-btnSize*3/2, getHeight()/2-btnSize/2+200, btnSize*3, btnSize);
		contentPane.add(btnClear);
		
		jC = new JCentrifuge(c);
		getContentPane().add(jC);
	}
}
