package graphing;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import parser.DataSource;

public class Grapher implements GraphTool, ImageGenerator {
	private static Grapher instance = null;
	private TimeSeriesCollection occupancy = null;
	private TimeSeriesCollection power = null;
	private DataSource dataStore;
	private String axisLabel;
	private Grapher() {

	}

	public static Grapher getInstance() {
		if (instance == null) {
			instance = new Grapher();
		}
		return instance;
	}
	
	public void setDataSource(DataSource dataSource){
		dataStore = dataSource;
	}

	private JFreeChart createChart(XYDataset occupancy, XYDataset power) {

		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Lab Usage Data", // title
				"Date", // x-axis label
				"Occupants", // y-axis label
				occupancy, // data
				true, // create legend?
				true, // generate tooltips?
				false // generate URLs?
				);

		chart.setBackgroundPaint(Color.white);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		
        final NumberAxis powerAxis = new NumberAxis(axisLabel);
        powerAxis.setAutoRangeIncludesZero(false);
        plot.setRangeAxis(1, powerAxis);
        plot.setDataset(1, power);
        plot.mapDatasetToRangeAxis(1, 1);
        
        final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setSeriesPaint(0, Color.black);
        renderer2.setBaseShapesVisible(false);
        plot.setRenderer(1, renderer2);
        
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("HH:mm dd/MM/yy"));
		axis.setVerticalTickLabels(true);
		return chart;
	}

	private void createDataset(String labName, Date start, Date end,
			Map<Date, Double> labData, TimeSeriesCollection dataset, String namePostfix, double multiplier) {
		
		TimeSeries s1 = new TimeSeries(labName+namePostfix);
		// Ensure dates are in order
		TreeSet<Date> keys = new TreeSet<Date>(labData.keySet());
		for (Date key : keys) {
			if (key.compareTo(start) >= 0 && key.compareTo(end) <= 0)
				s1.add(new Minute(key), labData.get(key)*multiplier);
		}
		
		dataset.addSeries(s1);

	}
	
	private void createMultiDataset(String labName, Date start, Date end,
			Map<Date, List<Double>> labData, TimeSeriesCollection dataset, String namePostfix, double multiplier) {
		//Using this rather than calling create dataset 3X means it can be done in 1 pass
		
		
		// Ensure dates are in order
		TreeSet<Date> keys = new TreeSet<Date>(labData.keySet());
		int count = labData.get(keys.first()).size();
		for (int i = 0; i < count; i++) {
			TimeSeries s1 = new TimeSeries(labName+namePostfix+" unit "+(i+1));
			for (Date key : keys) {
				if (key.compareTo(start) >= 0 && key.compareTo(end) <= 0)
					s1.add(new Minute(key), labData.get(key).get(i)*multiplier);
			}
			dataset.addSeries(s1);
		}
	}

	@Override
	public JPanel getGraph(int xSize, int ySize) {
		// TODO make proper exception
		if (occupancy != null&&power!=null) {
			JFreeChart chart = createChart(occupancy, power);
			ChartPanel chartPanel = new ChartPanel(chart, false);
			chartPanel.setPreferredSize(new java.awt.Dimension(xSize, ySize));
			return chartPanel;
		}
		System.out.println("no data");
		return null;
	}

	@Override
	public void setRequestedData(List<String> labs, Date start, Date end, int axisType) {
		
		boolean splitPower = axisType==2;
		power = new TimeSeriesCollection();
		occupancy = new TimeSeriesCollection();
		for (String labName : labs) {
			Map<Date, Double> labData = dataStore.getAbsoluteOccupancy(labName, start, end);
			createDataset(labName, start, end, labData, occupancy, "", 1);
			
			if(splitPower){
				Map<Date,List <Double>> labPower=dataStore.getPower(labName, start,end);
				if(labPower !=null)
				createMultiDataset(labName, start, end, labPower, power, " Power", 0.001);
			}else{
				Map<Date, Double> labPower;
				String annotation; 
				if(axisType==1){
					labPower = dataStore.getTotalPower(labName, start,end );
					annotation = " Power";
					axisLabel = "Power (kW)";
					
				}else{
					labPower = dataStore.getCO2(labName, start,end);
					annotation = " CO2";
					axisLabel = "CO2 (ppm)";
				}
				if (labPower != null)
					createDataset(labName, start, end, labPower, power, annotation, 0.001);
			}
		}
	}

}