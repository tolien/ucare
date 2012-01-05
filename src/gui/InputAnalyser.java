package gui;

import java.util.Date;

public interface InputAnalyser {
	
	public String getLab();
	
	public Date getStartDate();
	
	public Date getEndDate();

	public void datesWrongOrder();

}
