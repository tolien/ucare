package graphing;

import java.util.Date;
import java.util.List;

import parser.DataSource;

public interface AnalysisGraphTool {

	public void setRequestedData(List<String> labs, Date start, Date end, int timePeriod,String timeDescrip, Boolean isPower);
	
	public void setDataSource(DataSource dataSource);

}
