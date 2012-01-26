package gui;

import graphing.Grapher;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import occupancy.Utility;

import parser.CISParserFactory;
import parser.DSParserFactory;
import parser.DataSource;
import parser.Parser;
import parser.ParserFactory;

import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;

public class InputGUI implements InputAnalyser {

	private static final int MINUTE_INTERVAL = 4;

	private JFrame frame = new JFrame("Lab Occupancy Analyser");

	private JPanel mainPanel = new JPanel();

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
	
	private JLabel dataTypeLabel = new JLabel("Select data type:");
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rawButton = new JRadioButton("Raw");
	private JRadioButton analysedButton = new JRadioButton("Analysed");

	private JButton goButton = new JButton("Go");

	private List<ParserFactory> factories = new ArrayList<ParserFactory>();
	private ParserFactory selectedFactory;
	private Parser parser = new Parser();

	public void addParser(ParserFactory pf) {
		factories.add(pf);
	}

	private void setup() {

		mainPanel.setLayout(null);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(null);
		contentPane.add(mainPanel);
		mainPanel.setBounds(0, 0, 400, 700);

		selectFileType();

		if (selectedFactory != null) {
			boolean folderSelected = setFileLocation();

			if (folderSelected) {
				setupGUI();

				frame.setLocation(4, 4);
				frame.setSize(375, 200);
				frame.setResizable(false);
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
		mainPanel.add(selectLabLabel);
		selectLabLabel.setBounds(10, 20, 100, 10);
		labComboBox = new JComboBox(getLabNames());
		mainPanel.add(labComboBox);
		labComboBox.setBounds(120, 15, 230, 20);

		mainPanel.add(startDateLabel);
		startDateLabel.setBounds(10, 50, 100, 10);
		mainPanel.add(startDateField);
		startDateField.setBounds(120, 45, 100, 20);
		startHourComboBox = new JComboBox(getHours());
		mainPanel.add(startHourComboBox);
		startHourComboBox.setBounds(230, 45, 40, 20);
		startMinuteComboBox = new JComboBox(getMinutes());
		mainPanel.add(startMinuteComboBox);
		startMinuteComboBox.setBounds(280, 45, 40, 20);

		mainPanel.add(endDateLabel);
		endDateLabel.setBounds(10, 78, 100, 10);
		mainPanel.add(endDateField);
		endDateField.setBounds(120, 73, 100, 20);
		endHourComboBox = new JComboBox(getHours());
		mainPanel.add(endHourComboBox);
		endHourComboBox.setBounds(230, 73, 40, 20);
		endMinuteComboBox = new JComboBox(getMinutes());
		mainPanel.add(endMinuteComboBox);
		endMinuteComboBox.setBounds(280, 73, 40, 20);
		
		mainPanel.add(dataTypeLabel);
		dataTypeLabel.setBounds(10, 103, 100, 15);
		buttonGroup.add(rawButton);
		buttonGroup.add(analysedButton);
		analysedButton.setSelected(true);
		mainPanel.add(rawButton);
		rawButton.setBounds(117, 104, 50, 15);
		mainPanel.add(analysedButton);
		analysedButton.setBounds(167, 104, 100, 15);

		mainPanel.add(goButton);
		goButton.setBounds(120, 131, 50, 30);
		goButton.addActionListener(new GoListener(this, Grapher
				.getInstance()));
	}

	private String[] getLabNames() {
		List<String> names = parser.getLabList();
		names = Utility.asSortedList(names);
		
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

	public static void main(String[] args) {
		InputGUI gui = new InputGUI();

		gui.addParser(new DSParserFactory());
		gui.addParser(new CISParserFactory());
		gui.setup();
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

	public void datesWrongOrder() {
		JOptionPane.showMessageDialog(frame,
				"End date must be later than start date.");
	}

	private void selectFileType() {
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
}
