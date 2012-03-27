package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModeListener implements ActionListener{
	private InputAnalyser input;
	
	public ModeListener(InputAnalyser input) {
		this.input = input;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int type = input.getGraphType();
		if(type ==1){
			input.showGrapher();
		}
		if(type ==2){
			input.showAnalyser();
		}
	}

}
