package engine;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Centrifuge {
	private int pits = 4;
	private double arm;
	private ArrayList<Point2D> positions;
	private ArrayList<Double> masses;
	private ArrayList<Boolean> dummies;

	public static void main(String[] args) {
		Centrifuge c1 = new Centrifuge(2, 50);
		c1.setMass(10, 0);
		c1.setMass(10, 2);
		c1.setMass(10, 4);
		double score = c1.optimize();
		System.out.println(score);
		System.out.println(c1.masses);

		c1.dummyBalance();
		score = c1.calcScore(c1.masses);
		System.out.println(score);
		System.out.println(c1.masses);

		c1.dummyBalance();
		score = c1.calcScore(c1.masses);
		System.out.println(score);
		System.out.println(c1.masses);
	}

	public Centrifuge(int arm, int pits) {
		this.arm = arm;
		this.pits = pits;
		positions = new ArrayList<Point2D>();
		masses = new ArrayList<Double>();
		dummies = new ArrayList<Boolean>();
		calcPositions();
	}

	public double gCalc(int rpm) {
		rpm=rpm/100*100;
		return 1.12 * arm * (rpm * rpm / 1000000.);
	}
	
	public int rpmCalc(double g){
		return 100*(int) Math.round(10*Math.sqrt(g/(1.12*arm)));
	}

	private void calcPositions() {
		double angle = 2 * Math.PI / pits;
		removeDummies();
		positions.clear();
		dummies.clear();
		for (int i = 0; i < pits; i++) {
			positions.add(new Point2D(Math.cos(angle * i), Math.sin(angle * i)));
		}
		while (masses.size() < pits)
			masses.add(0d);

		while (dummies.size() < pits)
			dummies.add(false);

	}

	public int getPits() {
		return pits;
	}

	public double getArm() {
		return arm;
	}

	public Point2D getPosition(int pit) {
		if (pit > pits)
			return null;
		return positions.get(pit);
	}

	public void setPits(int pits) {
		this.pits = pits;
		calcPositions();
	}

	public void setArm(double arm) {
		this.arm = Math.min(999, arm);
		calcPositions();
	}

	public void setMass(double m, int position) {
		m=Math.min(99.9, m);
		masses.set(position, m);
	}

	public double calcScore(ArrayList<Double> masses) {
		Point2D centerMass = centerMass(masses);
		return Math.pow(centerMass.getX(), 2) + Math.pow(centerMass.getY(), 2);
	}

	private Point2D centerMass(ArrayList<Double> masses) {
		Point2D massCenter = new Point2D(0, 0);
		for (Point2D p : positions) {
			int i = positions.indexOf(p);
			massCenter = massCenter.add(p.multiply(masses.get(i)));
		}
		return massCenter;
	}

	public void removeDummies() {
		for (int i = dummies.size() - 1; i >= 0; i--)
			if (dummies != null && masses != null && dummies.get(i) && masses.size() > i) {
				masses.set(i, 0d);
				dummies.set(i, false);
			}
	}

	private boolean placeNextMass(ArrayList<Double> massList) {
		if (massList.size() == 0 || massList.get(massList.size() - 1) == 0)
			return false;
		int bestPos = findBestFreePit();
		if (bestPos<0) return false;
		setMass(massList.remove(massList.size() - 1), bestPos);
		return true;
	}

	private int findBestFreePit() {
		return findBestFreePit(masses);
	}

	public double optimize() {
		removeDummies();
		int NMasses = 0;
		for (double mass : masses)
			if (mass > 0)
				NMasses++;
		if (NMasses % 2 == 0)
			return optimizeEven();
		return optimizeOdd();
	}

	public double optimizeEven() {
		removeDummies();
		ArrayList<Double> massList = (ArrayList<Double>) masses.clone();
		masses.clear();
		calcPositions();
		Collections.sort(massList);
		while (placeNextMass(massList)) {
		}
		return optimizeOdd();
//		return calcScore(masses);
	}

	public double optimizeOdd() {
		double score = calcScore();
		boolean opt = true;
		while (opt) {
			score = calcScore();
			ArrayList<Double> massesClone = (ArrayList<Double>) masses.clone();
			for (int i = 0; i < massesClone.size(); i++) {
				double massToOptimize = massesClone.get(i);
				if (massToOptimize > 0) {
					masses.set(i, 0d);
					int pit=findBestFreePit();
					if (pit<0)
						break;
					masses.set(pit, massToOptimize);
					masses = massesClone;
				}
			}
			opt = score - calcScore() > 0.01;
		}
		return score;
	}

	private int findBestFreePit(ArrayList<Double> masses) {
		Point2D massCenter = centerMass(masses);
		double min = Double.MAX_VALUE;
		int bestPos = -1;
		for (Point2D p : positions)
			if (masses.get(positions.indexOf(p)) == 0) {
				double dotProd = massCenter.dotProduct(p);
				if (dotProd < min) {
					min = dotProd;
					bestPos = positions.indexOf(p);
				}
			}
		return bestPos;
	}

	public void dummyBalance() {
		if (calcScore()<0.01) return;
		int pit = findBestFreePit();
		if (pit<0) return;
		double centerWeight = Math.sqrt(calcScore(masses));
		if (centerWeight<0.01) return;
		double distance = centerMass(masses).normalize().magnitude();
		setMass(centerWeight * distance, pit);
		dummies.set(pit, true);
	}

	public double getMass(int pitNumber) {
		if (pitNumber >= masses.size() || pitNumber < 0)
			return 0;
		return masses.get(pitNumber);
	}

	public boolean isDummy(int pitNumber) {
		if (pitNumber >= dummies.size() || pitNumber < 0)
			return false;
		return dummies.get(pitNumber);
	}

	public double calcScore() {
		return calcScore(masses);
	}
	public void clearMasses(){
		masses.clear();
		dummies.clear();
		calcPositions();
	}

}
