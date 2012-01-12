package gui;

import java.util.Date;

import parser.DataSource;

public interface InputAnalyser {
	
	public String getLab();
	
	public Date getStartDate();
	
	public Date getEndDate();

	public void datesWrongOrder();
	
	public DataSource getDataSource();

}
