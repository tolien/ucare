package gui;

import java.awt.Container;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;

public class InputGUI implements InputAnalyser {

	public static final int GRAPH_HEIGHT = 300;
	public static final int GRAPH_WIDTH = 350;

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

	private JButton goButton = new JButton("Go");

	public InputGUI() {
		mainPanel.setLayout(null);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(null);
		contentPane.add(mainPanel);
		mainPanel.setBounds(0, 0, 400, 700);
		setupLabels();
		frame.setLocation(4, 4);
		frame.setSize(375, 170);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setupLabels() {
		try {
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

			mainPanel.add(goButton);
			goButton.setBounds(120, 101, 50, 30);
			goButton.addActionListener(new GoListener(this));
		} catch (IOException e) {
		}
	}

	private String[] getLabNames() throws IOException {
		FileReader fileReader = new FileReader("LabNames.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> labNames = new ArrayList<String>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			labNames.add(line);
		}
		bufferedReader.close();
		return labNames.toArray(new String[labNames.size()]);
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
		new InputGUI();
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
		System.out.println(calendar.getTime());
		return calendar.getTime();
	}

	public Date getEndDate() {
		Date endDate = (Date) endDateField.getValue();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endHourComboBox
				.getSelectedItem().toString()));
		calendar.set(Calendar.MINUTE, Integer.parseInt(endMinuteComboBox
				.getSelectedItem().toString()));
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		System.out.println(calendar.getTime());
		return calendar.getTime();
	}
	
	public void datesWrongOrder() {
		JOptionPane.showMessageDialog(frame,
	    "End date must be later than start date.");
	}

}
