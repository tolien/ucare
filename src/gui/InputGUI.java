package gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


import parser.DSParserFactory;
import parser.DataSource;
import parser.Parser;
import parser.ParserFactory;
import ucare.Utility;

import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;

public class InputGUI implements InputAnalyser {

	private static final int MINUTE_INTERVAL = 4;

	private JFrame frame = new JFrame("Lab Occupancy Analyser");

	private JPanel mainPanel = new JPanel(new GridBagLayout());

	private JLabel selectLabLabel = new JLabel("Select lab:");
	private JComboBox labComboBox;

	private JLabel startDateLabel = new JLabel("Select start date:");
	private DateField startDateField = CalendarFactory.createDateField();
	private JComboBox startHourComboBox;
	private JComboBox startMinuteComboBox;

	private JLabel endDateLabel = new JLabel("Select end date:");
	private DateField endDateField = CalendarFactory.createDateField();
	private JComboBox endHourComboBox;
	private JComboBox endMinuteComboBox;
	
	private JLabel dataTypeLabel = new JLabel("Select output type:");
	private ButtonGroup graphType = new ButtonGroup();
	private JRadioButton rawButton = new JRadioButton("Raw");
	private JRadioButton analysedButton = new JRadioButton("Analysed");
	private JRadioButton predictButton = new JRadioButton("Prediction");
	
	private JLabel analysisDataLabel = new JLabel("Select data type:");
	private ButtonGroup analysisData = new ButtonGroup();
	private JRadioButton aPowerButton = new JRadioButton("Power");
	private JRadioButton aOccupancyButton = new JRadioButton("Occupancy");
	
	private JLabel seriesLabel = new JLabel("Select second axis:");
	private ButtonGroup seriesType = new ButtonGroup();
	private JRadioButton cOButton = new JRadioButton("CO2");
	private JRadioButton splitButton = new JRadioButton("Split Power");
	private JRadioButton powerButton = new JRadioButton("Total Power");
	
	private JLabel selectDurationLabel = new JLabel("Select Analysis Granularity:");
	private JComboBox durationComboBox;
	private JCheckBox dropHoursCheck = new JCheckBox("Only Use 8-6");
	
	private JButton goButton = new JButton("Go");

	private List<ParserFactory> factories = new ArrayList<ParserFactory>();
	private ParserFactory selectedFactory;
	private Parser parser = new Parser();

	public void addParser(ParserFactory pf) {
		factories.add(pf);
	}

	public void setup() {
		JOptionPane.showMessageDialog(frame,
			    "Please select the location of the occupancy data.");
		Container contentPane = frame.getContentPane();
		contentPane.add(mainPanel);

		selectFileType();

		if (selectedFactory != null) {
			boolean folderSelected = setFileLocation();

			if (folderSelected) {
				setupGUI();
				
				frame.pack();
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			} else {
				frame.dispose();
			}
		} else {
			frame.dispose();
		}
	}

	private void setupGUI() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		mainPanel.add(selectLabLabel, c);
		selectLabLabel.setLabelFor(labComboBox);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		labComboBox = new JComboBox(getLabNames());
		mainPanel.add(labComboBox, c);

		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_START;
		mainPanel.add(startDateLabel, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.ipadx = 10;
		c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(startDateField, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		startHourComboBox = new JComboBox(getHours());
		mainPanel.add(startHourComboBox, c);
		
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 1;
		startMinuteComboBox = new JComboBox(getMinutes());
		mainPanel.add(startMinuteComboBox, c);

		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_START;
		mainPanel.add(endDateLabel, c);
		
		c = new GridBagConstraints();
		c.gridy = 2;
		c.gridx = 1;
		c.ipadx = 10;
		mainPanel.add(endDateField, c);
		
		c.ipadx = 0;
		c.gridx = 2;
		endHourComboBox = new JComboBox(getHours());
		mainPanel.add(endHourComboBox, c);
		
		c.gridx = 3;
		endMinuteComboBox = new JComboBox(getMinutes());
		mainPanel.add(endMinuteComboBox, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_START;
		mainPanel.add(dataTypeLabel, c);
		
		graphType.add(rawButton);
		graphType.add(analysedButton);
		graphType.add(predictButton);
		rawButton.setSelected(true);

		rawButton.addActionListener(new ModeListener(this));
		analysedButton.addActionListener(new ModeListener(this));
		predictButton.addActionListener(new ModeListener(this));
		
		c.gridx = 1;
		mainPanel.add(rawButton, c);
		
		c.gridx = 2;
		mainPanel.add(analysedButton, c);
		
		c.gridx = 3;
		mainPanel.add(predictButton, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.LINE_START;
		mainPanel.add(seriesLabel, c);
		seriesType.add(cOButton);
		seriesType.add(splitButton);
		seriesType.add(powerButton);
		powerButton.setSelected(true);
		
		c.gridx = 1;
		mainPanel.add(cOButton, c);
		c.gridx = 2;
		mainPanel.add(splitButton, c);
		c.gridx = 3;
		mainPanel.add(powerButton, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 5;
		c.anchor = GridBagConstraints.LINE_START;

		mainPanel.add(analysisDataLabel, c);
		analysisData.add(aOccupancyButton);
		analysisData.add(aPowerButton);
		aOccupancyButton.setSelected(true);
		
		c.gridx = 1;
		mainPanel.add(aOccupancyButton, c);
		c.gridx = 2;
		mainPanel.add(aPowerButton, c);
		c.gridx = 3;
		mainPanel.add(dropHoursCheck, c);
		
		
		c = new GridBagConstraints();
		c.gridy = 6;
		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_START;
		mainPanel.add(selectDurationLabel, c);
		final String[] periods = {"Hours", "Half-Day", "Day", "Week", "Month", "Quarter", "Year"};
		durationComboBox = new JComboBox(periods);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		mainPanel.add(durationComboBox, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 9;	
		mainPanel.add(goButton, c);
		goButton.addActionListener(new GoListener(this));
		
		showAnalyser();
	}

	private String[] getLabNames() {
		final List<String> names = Utility.asSortedList(parser.getLabList());
		
		return names.toArray(new String[0]);
	}

	private String[] getHours() {
		List<String> hours = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			if (i < 10) {
				hours.add("0" + Integer.toString(i));
			} else {
				hours.add(Integer.toString(i));
			}
		}
		return hours.toArray(new String[hours.size()]);
	}

	private String[] getMinutes() {
		List<String> minutes = new ArrayList<String>();
		for (int i = 0; i < MINUTE_INTERVAL; i++) {
			int minute = 60 / MINUTE_INTERVAL * i;
			if (minute < 10) {
				minutes.add("0" + Integer.toString(minute));
			} else {
				minutes.add(Integer.toString(minute));
			}
		}
		return minutes.toArray(new String[minutes.size()]);
	}

	public String getLab() {
		return labComboBox.getSelectedItem().toString();
	}

	public Date getStartDate() {
		Date startDate = (Date) startDateField.getValue();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startHourComboBox
				.getSelectedItem().toString()));
		calendar.set(Calendar.MINUTE, Integer.parseInt(startMinuteComboBox
				.getSelectedItem().toString()));
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public Date getEndDate() {
		Date endDate = (Date) endDateField.getValue();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.set(Calendar.HOUR_OF_DAY,
				Integer.parseInt(endHourComboBox.getSelectedItem().toString()));
		calendar.set(Calendar.MINUTE, Integer.parseInt(endMinuteComboBox
				.getSelectedItem().toString()));
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public int getGraphType(){
		if(rawButton.isSelected()){
			return 1;
		}if(analysedButton.isSelected()){
			return 2;
		}
		 return 3;
	}

	public void datesWrongOrder() {
		JOptionPane.showMessageDialog(frame,
				"End date must be later than start date.");
	}

	private void selectFileType() {
		if (factories.size() > 1)
		{
			Iterator<ParserFactory> it = factories.iterator();
			
			String[] factoryStrings = new String[factories.size()];
	
			int i = 0;
			while (it.hasNext()) {
				String name = it.next().getName();
				factoryStrings[i] = name;
				i++;
			}
	
			String s = null;
			s = (String) JOptionPane.showInputDialog(frame,
					"Which format is the data file in?", "Data format chooser",
					JOptionPane.QUESTION_MESSAGE, null, factoryStrings, null);
	
			it = factories.iterator();
			while (it.hasNext()) {
				ParserFactory factory = it.next();
				if (s != null && s.equals(factory.getName())) {
					selectedFactory = factory;
					break;
				}
			}
		}
		else if (factories.size() == 1)
		{
			selectedFactory = factories.get(0);
		}
	}

	private boolean setFileLocation() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(selectedFactory.getFileSelectionMode());

		int result = Integer.MAX_VALUE;
		result = chooser.showOpenDialog(frame);
		if (result != JFileChooser.CANCEL_OPTION) {
			parser.read(chooser.getSelectedFile().getAbsolutePath(),
					selectedFactory);
			return true;
		} else {
			return false;
		}
	}

	public DataSource getDataSource() {
		return parser;
	}

	@Override
	public int getAxis2Type() {
		
			if(powerButton.isSelected()){
				return 1;
			}else if (splitButton.isSelected()){return 2;}
			else{return 3;}
		
	}
	
	public int getIntervalTime(){
		int[] intervals = {1,12,24,168,672,2016,8064};
		return intervals[durationComboBox.getSelectedIndex()];
	}
	
	public String getIntervalName(){
		
		return durationComboBox.getSelectedItem().toString();
	}
	public boolean getADataType(){
		return aPowerButton.isSelected();
	}
	public void showAnalyser(){
		analysisDataLabel.setVisible(true);
		aPowerButton.setVisible(true);
		aOccupancyButton.setVisible(true);
		
		seriesLabel.setVisible(false);
		cOButton.setVisible(false);
		splitButton.setVisible(false);
		powerButton.setVisible(false);
		
		selectDurationLabel.setVisible(true);
		durationComboBox.setVisible(true);
		
		startHourComboBox.setVisible(true);
		startMinuteComboBox.setVisible(true);
		endHourComboBox.setVisible(true);
		endMinuteComboBox.setVisible(true);
		dropHoursCheck.setVisible(true);

	}
	
	public void showGrapher(){
		analysisDataLabel.setVisible(false);
		aPowerButton.setVisible(false);
		aOccupancyButton.setVisible(false);
		
		seriesLabel.setVisible(true);
		cOButton.setVisible(true);
		splitButton.setVisible(true);
		powerButton.setVisible(true);
		
		selectDurationLabel.setVisible(false);
		durationComboBox.setVisible(false);
		
		startHourComboBox.setVisible(true);
		startMinuteComboBox.setVisible(true);
		endHourComboBox.setVisible(true);
		endMinuteComboBox.setVisible(true);
		dropHoursCheck.setVisible(false);

	}
	
	public void showPredicter(){
		analysisDataLabel.setVisible(false);
		aPowerButton.setVisible(false);
		aOccupancyButton.setVisible(false);
		
		seriesLabel.setVisible(false);
		cOButton.setVisible(false);
		splitButton.setVisible(false);
		powerButton.setVisible(false);
		
		selectDurationLabel.setVisible(false);
		durationComboBox.setVisible(false);
		
		startHourComboBox.setVisible(false);
		startMinuteComboBox.setVisible(false);
		endHourComboBox.setVisible(false);
		endMinuteComboBox.setVisible(false);
		
		dropHoursCheck.setVisible(false);
	}

	@Override
	public boolean getLimitData() {
		return dropHoursCheck.isSelected();
	}
}
