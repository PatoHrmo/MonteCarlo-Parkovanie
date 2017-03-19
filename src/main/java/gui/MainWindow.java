package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import core.MCParkovanie;
import core.MCParkovaniePoslednaTretina;
import core.MCParkovaniePrvyVolny;
import core.RozmiestnovacParkoviska;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.SystemColor;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JPanel panelGraf23;
	private JPanel panelGrafPrvy;
	private JTextField txtFieldPocetRepl;
	private JTextField txtFieldVelkostParkoviska;
	private JTextField txtPercenta;
	private JLabel lblPrvy;
	private JLabel lbl23;
	private JLabel lblPoetPercentZahodench;
	private JButton btnUkHistogram;
	private HistogramWindow histogramWindow;
	
	private XYSeries dataPrvyVolny;
	private XYSeries data23;
	
	
	private SwingWorker<Void, Double[]> worker;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setLocationRelativeTo(null);
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
	public MainWindow() {
		setTitle("Pozorovanie parkovania");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 733);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.control);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPoetReplikci = new JLabel("Po\u010Det replik\u00E1ci\u00ED:");
		lblPoetReplikci.setBounds(10, 11, 127, 14);
		contentPane.add(lblPoetReplikci);

		txtFieldPocetRepl = new JTextField();
		txtFieldPocetRepl.setBounds(204, 8, 140, 20);
		contentPane.add(txtFieldPocetRepl);
		txtFieldPocetRepl.setColumns(10);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(worker!=null){
					worker.cancel(false);
				}
				
			}
		});
		btnStop.setBounds(382, 32, 89, 23);
		btnStop.setEnabled(false);
		contentPane.add(btnStop);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnUkHistogram.setEnabled(false);
				btnStop.setEnabled(true);
				btnStart.setEnabled(false);
				data23.clear();
				dataPrvyVolny.clear();
				int pocetReplikacii = Integer.parseInt(txtFieldPocetRepl.getText());
				int velkostParkoviska = Integer.parseInt(txtFieldVelkostParkoviska.getText());
				double pocetPercent = Double.parseDouble(txtPercenta.getText())/2;
				
				worker = new SwingWorker<Void, Double[]>() {
					double pocetZapisanychDatPredMazanim = 100d/pocetPercent;
					double pocetVlozenych=0;
					MCParkovanie prvyVolny;
					MCParkovanie dveTretiny;
					@Override
					protected Void doInBackground() throws Exception {
						RozmiestnovacParkoviska rz = new RozmiestnovacParkoviska(velkostParkoviska);
						prvyVolny = new MCParkovaniePrvyVolny(velkostParkoviska);
						dveTretiny = new MCParkovaniePoslednaTretina(velkostParkoviska);
						rz.pridajMonteCarlo(dveTretiny);
						rz.pridajMonteCarlo(prvyVolny);
						while(pocetReplikacii>rz.getPocetHotovychReplikacii() && !isCancelled()) {
							if(pocetReplikacii>=1000000000)
								rz.vykonajReplik·cie(pocetReplikacii/10000);
							else {
								rz.vykonajReplik·cie(pocetReplikacii/1000);
							}
							publish(new Double[]{prvyVolny.getCelkovuUspesnostParkovania(), 
									(double)prvyVolny.getPocetIteracii(), dveTretiny.getCelkovuUspesnostParkovania(),
									(double)dveTretiny.getPocetIteracii()});
						}
						
						return null;
					}
					@Override
					protected void process(List<Double[]> chunks) {
						for(Double[] poleDat : chunks) {
							dataPrvyVolny.add(poleDat[1], poleDat[0]);
							data23.add(poleDat[3], poleDat[2]);
							lbl23.setText(poleDat[2]+"");
							lblPrvy.setText(poleDat[0]+"");
							pocetVlozenych+=1;
							if(pocetVlozenych>=pocetZapisanychDatPredMazanim){
								pocetVlozenych-=pocetZapisanychDatPredMazanim;
								data23.delete(0, 1);
								dataPrvyVolny.delete(0, 1);
							} 
						}
					}
					@Override
					protected void done() {
						histogramWindow.nastavHistogram(prvyVolny, dveTretiny);
						btnUkHistogram.setEnabled(true);
						btnStart.setEnabled(true);
						btnStop.setEnabled(false);
					}
				};
				worker.execute();
			}
		});
		btnStart.setBounds(382, 7, 89, 23);
		contentPane.add(btnStart);

		JLabel lblPriemernspenosPri = new JLabel(
				"Priemern\u00E1 \u00FAspe\u0161nos\u0165 pri strat\u00E9gi\u00ED prv\u00E9ho vo\u013En\u00E9ho miesta:");
		lblPriemernspenosPri.setBounds(10, 94, 350, 14);
		contentPane.add(lblPriemernspenosPri);

		JLabel lblPriemernspenosPri_1 = new JLabel(
				"Priemern\u00E1 \u00FAspe\u0161nos\u0165 pri strat\u00E9gi\u00ED presko\u010Denia dvoch tret\u00EDn miest:");
		lblPriemernspenosPri_1.setBounds(358, 94, 416, 14);
		contentPane.add(lblPriemernspenosPri_1);

		lblPrvy = new JLabel("");
		lblPrvy.setBounds(10, 119, 209, 14);
		contentPane.add(lblPrvy);

		lbl23 = new JLabel("");
		lbl23.setBounds(358, 119, 263, 14);
		contentPane.add(lbl23);

		panelGrafPrvy = new JPanel();
		panelGrafPrvy.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		panelGrafPrvy.setBounds(10, 160, 764, 253);
		contentPane.add(panelGrafPrvy);

		JLabel lblVekosParkoviska = new JLabel("Ve\u013Ekos\u0165 parkoviska:");
		lblVekosParkoviska.setBounds(10, 36, 148, 14);
		contentPane.add(lblVekosParkoviska);

		txtFieldVelkostParkoviska = new JTextField();
		txtFieldVelkostParkoviska.setBounds(204, 33, 140, 20);
		contentPane.add(txtFieldVelkostParkoviska);
		txtFieldVelkostParkoviska.setColumns(10);

		panelGraf23 = new JPanel();
		panelGraf23.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		panelGraf23.setBounds(10, 424, 764, 253);
		contentPane.add(panelGraf23);
		
		lblPoetPercentZahodench = new JLabel("Po\u010Det percent zahoden\u00FDch d\u00E1t: ");
		lblPoetPercentZahodench.setBounds(10, 61, 209, 14);
		contentPane.add(lblPoetPercentZahodench);
		
		txtPercenta = new JTextField();
		txtPercenta.setBounds(204, 58, 140, 20);
		contentPane.add(txtPercenta);
		txtPercenta.setColumns(10);
		
		btnUkHistogram = new JButton("Uk\u00E1\u017E histogram");
		btnUkHistogram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				histogramWindow.setVisible(true);
			}
		});
		btnUkHistogram.setEnabled(false);
		btnUkHistogram.setBounds(481, 7, 140, 23);
		contentPane.add(btnUkHistogram);
		histogramWindow = new HistogramWindow();
		histogramWindow.setLocationRelativeTo(this);
		nastavGrafy();
	}

	private void nastavGrafy() {
		data23 = new XYSeries("˙speönosù pri parkovanÌ poslednej tretiny");
		dataPrvyVolny = new XYSeries("˙speönosù parkovania prv˝ voæn˝");

		// Nastavovanie ciaroveho grafu pre strategiu prejdenia dvoch tretin
		final JFreeChart graf23 = ChartFactory.createXYLineChart("StratÈgia preskoËenia dvoch tretÌn",
				"PoËet replik·ciÌ", "Priemern· ˙speönosù", new XYSeriesCollection(data23), PlotOrientation.VERTICAL,
				false, true, false);
		graf23.setBorderPaint(Color.black);
		XYPlot plot23 = (XYPlot) graf23.getPlot();
		((NumberAxis) plot23.getDomainAxis()).setAutoTickUnitSelection(true);
		plot23.getRangeAxis().setAutoRange(true);
		((NumberAxis) plot23.getRangeAxis()).setAutoRangeIncludesZero(false);
		plot23.getRenderer().setSeriesPaint(0, Color.blue);
		panelGraf23.setLayout(new BorderLayout(0, 0));
		panelGraf23.add(new ChartPanel(graf23));
		
		// Nastavovanie ciaroveho grafu pre strategiu prejdenia dvoch tretin
		final JFreeChart grafPrvyVolny = ChartFactory.createXYLineChart("StratÈgia prvÈho voænÈho miesta",
				"PoËet replik·ciÌ", "Priemern· ˙speönosù", new XYSeriesCollection(dataPrvyVolny), PlotOrientation.VERTICAL,
				false, true, false);
		XYPlot plotPrvyVolny = (XYPlot) grafPrvyVolny.getPlot();
		((NumberAxis) plotPrvyVolny.getDomainAxis()).setAutoTickUnitSelection(true);
		plotPrvyVolny.getRangeAxis().setAutoRange(true);
		((NumberAxis) plotPrvyVolny.getRangeAxis()).setAutoRangeIncludesZero(false);
		panelGrafPrvy.setLayout(new BorderLayout(0, 0));
		ChartPanel chartPanel = new ChartPanel(grafPrvyVolny);
		chartPanel.setBorder(null);
		chartPanel.setBackground(Color.WHITE);
		panelGrafPrvy.add(chartPanel);
		
		

	}
}
