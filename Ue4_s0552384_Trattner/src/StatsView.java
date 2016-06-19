// BV Ue04 WS2015/16 Vorgabe Hilfsklasse StatsView
//
// Copyright (C) 2014 by Klaus Jung

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


public class StatsView extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String[] names = { "Minimum:", "Maximum:", "Mean:", "Median:", "Varianz:", "Entropy:" }; // TODO: enter proper names
	private static final int rows = names.length;
	private static final int border = 2;
	private static final int columns = 2;
	private static final int graySteps = 256;
	
	private JLabel[] infoLabel = new JLabel[rows];
	private JLabel[] valueLabel = new JLabel[rows];
	
	public int[] histogram = null;
	public int[] greyPix = null;
	
	public StatsView() {
		super(new GridLayout(rows, columns, border, border));
		TitledBorder titBorder = BorderFactory.createTitledBorder("Statistics");
		titBorder.setTitleColor(Color.GRAY);
		setBorder(titBorder);
		for(int i = 0; i < rows; i++) {
			infoLabel[i] = new JLabel(names[i]);
			valueLabel[i] = new JLabel("-----");
			add(infoLabel[i]);
			add(valueLabel[i]);
		}
	}
	
	private void setValue(int column, int value) {
		valueLabel[column].setText("" + value);
	}
	
	private void setValue(int column, double value) {
		valueLabel[column].setText(String.format(Locale.US, "%.2f", value));
	}
	
	public boolean setHistogram(int[] histogram) {
		if(histogram == null || histogram.length != graySteps) {
			return false;
		}
		this.histogram = histogram;
		update();
		return true;
	}
	
	public boolean update() {
		if(histogram == null) {
			return false;
		}
		int min = 255;;
		int max = 0;
		double mean = 0;
		int median = 6;
		double var = 0;
		float entrop = 0;
		// TODO: calculate and display statistic values
		//calculation for min
		for(int i = 0; i < greyPix.length; i++){
			if(min > greyPix[i]){
				min = greyPix[i]; 
			}
		}
		
		//Calculation for max
		for(int i = 0; i < greyPix.length; i++){
			if(max < greyPix[i]){
				max = greyPix[i]; 
			}
		}
		
		//Calculation for middle value
		for(int i = 0; i < greyPix.length; i++){
				mean += greyPix[i]; 
		}	
		mean = mean/greyPix.length;
		
		//Calculation for variance
		for(int i = 0; i < greyPix.length; i++){
			var += ((mean - greyPix[i]) * (mean - greyPix[i]));
		}
		var = var/greyPix.length;
		
		//calculation for entropy
		int pixelAm = 0;
		for (int i = 0; i < histogram.length; i++) {
			pixelAm = pixelAm + histogram[i];
		}
		
		for (int i = 1; i < histogram.length; i++) {

			if (histogram[i] != 0) {
				float probability = histogram[i] / (float) pixelAm;
				float negLog2OfProb = (float) -(Math.log10(probability) / Math.log10(2));
				entrop = entrop + probability * negLog2OfProb;
			}
		}
		
		//Calculation for median
		Arrays.sort(greyPix);
		median = greyPix[greyPix.length / 2];
		
		setValue(0, min);
		setValue(1, max);
		setValue(2, mean);
		setValue(3, median);
		setValue(4, var);
		setValue(5, entrop);

		return true;
	}
	

}
