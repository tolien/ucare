package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GraphGUI {

	private static final int MIN_WIDTH = 800;

	private JFrame frame = new JFrame("Lab Occupancy Analyser");

	private JScrollPane scrollPane;

	public GraphGUI(JPanel graph) {
		graph.setBackground(Color.WHITE);
		scrollPane = new JScrollPane(graph);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.pack();
		int minWidth = MIN_WIDTH;
		int graphWidth = graph.getPreferredSize().width;
		if (graphWidth < minWidth) {
			minWidth = graphWidth;
		}
		Dimension size = new Dimension(minWidth, frame.getSize().height
				+ scrollPane.getHorizontalScrollBar().getPreferredSize().height);
		frame.setPreferredSize(size);
		frame.setSize(size);
		frame.setMinimumSize(size);
		frame.setVisible(true);
	}

}
