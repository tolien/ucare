package gui;

import java.util.Date;

import parser.DataSource;

public interface InputAnalyser {
	
	public String getLab();
	
	public Date getStartDate();
	
	public Date getEndDate();

	public void datesWrongOrder();
	
	public DataSource getDataSource();
	
	public int getGraphType();
	
	public int getAxis2Type();

	public int getIntervalTime();

	public String getIntervalName();
	
	public boolean getADataType();
	
	public void showAnalyser();
	
	public void showGrapher();
	
	public void showPredicter();

}
