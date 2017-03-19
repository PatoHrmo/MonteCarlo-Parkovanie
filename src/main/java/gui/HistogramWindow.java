package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JDialog;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import core.MCParkovanie;

public class HistogramWindow extends JDialog {
	private DefaultCategoryDataset data;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HistogramWindow dialog = new HistogramWindow();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public HistogramWindow() {
		setTitle("Histogramy");
		setBounds(100, 100, 1000, 600);
		data = new DefaultCategoryDataset();
		JFreeChart barChart = ChartFactory.createBarChart("PoËetnosti ˙speönosti", "⁄speönosù", "PoËet", data,
				PlotOrientation.VERTICAL, true, true, false);
		((BarRenderer)barChart.getCategoryPlot().getRenderer()).setItemMargin(0.01);
		ChartPanel chartPanel = new ChartPanel(barChart);
		setContentPane(chartPanel);
		chartPanel.setLayout(new BorderLayout(0, 0));
		data.addValue( 50 , "prv˝" , 1+"" );  
	}
	public void nastavHistogram(MCParkovanie prvy, MCParkovanie dveTretiny) {
		data.clear();
		for(int i = 0; i<prvy.getVelkostParkoviska();i++) {
			data.addValue(prvy.getUspesnosti()[i+1], "stratÈgia prvÈho voænÈho miesta", (i+1)+"");
			data.addValue(dveTretiny.getUspesnosti()[i+1], "stratÈgia preskoËenia dvoch tretÌn parkovacÌch miest", (i+1)+"");
		}
		data.addValue(prvy.getUspesnosti()[prvy.getVelkostParkoviska()*2-1], "stratÈgia prvÈho voænÈho miesta", (prvy.getVelkostParkoviska()*2)+"");
		data.addValue(dveTretiny.getUspesnosti()[dveTretiny.getVelkostParkoviska()*2-1], "stratÈgia preskoËenia dvoch tretÌn parkovacÌch miest", (dveTretiny.getVelkostParkoviska()*2)+"");
	}

}
