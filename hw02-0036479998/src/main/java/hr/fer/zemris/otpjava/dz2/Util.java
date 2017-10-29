package hr.fer.zemris.otpjava.dz2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import Jama.Matrix;

/**
 * Utility class.
 * 
 * @author lmark
 *
 */
public class Util {

	/**
	 * Convert matrix to string.
	 * 
	 * @param mat
	 * @return String representation of matrix.
	 */
	public static String MatrixToString(Matrix mat) {
		StringBuffer b = new StringBuffer();
		for (int i = 0, iMax = mat.getRowDimension(); i < iMax; i++) {
			b.append("[");
			for (int j = 0, jMax = mat.getColumnDimension(); j < jMax; j++) {
				b.append(String.format("%.2f", mat.get(i, j)));
				
				if (j+1 < jMax) {
					b.append(", ");
				}
			}
			b.append("]");
			if (i+1 < iMax) {
				b.append("\n");
			}
		}
		
		return b.toString();
	}
	
	/**
	 * Check if all elements in matrix are close to zero.
	 * 
	 * @param mat
	 * @param toll
	 * @return
	 */
	public static boolean isZero(Matrix mat, double toll) {
		for (int i = 0, iMax = mat.getRowDimension(); i < iMax; i++) {
			for (int j = 0, jMax = mat.getColumnDimension(); j < jMax; j++) {
				if (Math.abs(mat.get(i, j))>=toll) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Generate line graph based on given data.
	 * 
	 * @param vecList List of vector points.
	 * @param chartTitle Graph title.
	 * @param path Path to save the graph.
	 */
	public static void generateGraph(List<Matrix> vecList, String chartTitle, Path path) {
		JFreeChart xylineChart = ChartFactory.createXYLineChart(
		         chartTitle, 
		         "X1 value",
		         "X2 value", 
		         createDataset(vecList),
		         PlotOrientation.VERTICAL, 
		         true, true, false);
		      
		int width = 640;   	
		int height = 480;  
		final XYPlot plot = xylineChart.getXYPlot( );
  
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    renderer.setSeriesPaint( 0 , Color.RED );
	    renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
	    plot.setRenderer(renderer); 
	     
		try {
			ChartUtilities.saveChartAsJPEG(path.toFile(), xylineChart, width, height);
		} catch (IOException e) {
			System.out.println("Not able to generate image.");
			e.printStackTrace();
			return;
		}
		
		System.out.println("Graph successfully generated.");
	}

	/**
	 * Create a dataset used for graphing based on given Matrix list.
	 * Supports only 2D data.
	 * 
	 * @param vecList
	 * @return
	 */
	private static XYDataset createDataset(List<Matrix> vecList) {
		
		if (vecList.get(0).getRowDimension() > 2) {
			throw new UnsupportedOperationException("Graphing only suppoerts 2x1 vectors");
		}
		
		final XYSeries trajectory = new XYSeries( "Trajectory" );          
		for (Matrix vector : vecList) {
			trajectory.add(vector.get(0, 0), vector.get(1, 0));
		}
		
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(trajectory);

	    return dataset;  
	}
	
	
	/**
	 * Parse line of the following type:
	 * [double1, double2, ..., doublen]
	 * 
	 * @param line
	 * @return Return a list of doubles.
	 */
	public static List<Double> parseLine(String line) {
		String procLine = line.trim().substring(1, line.length()-1);
		String[] sNumbers = procLine.split(",");
		List<Double> doubles = new ArrayList<>();
		
		// Extract numbers
		for (int i = 0, len = sNumbers.length; i < len; i++) {
			double number = Double.valueOf(sNumbers[i].trim());
			
			doubles.add(number);
		}
		
		return doubles;
	}
}
