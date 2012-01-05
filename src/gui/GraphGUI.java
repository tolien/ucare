package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GraphGUI {
	
	private JFrame frame = new JFrame("Lab Occupancy Analyser");
	
	private JScrollPane scrollPane;
	
	public GraphGUI(JPanel graph) {
		graph.setPreferredSize(new Dimension(800, 800));
		graph.setBackground(Color.WHITE);
		scrollPane = new JScrollPane(graph);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.setSize(300, 300);
		frame.setMinimumSize(new Dimension(200, 200));
		frame.setMaximumSize(new Dimension(600, 600));
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new GraphGUI(new JPanel());
	}

}
