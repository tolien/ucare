package graphing;

import java.util.Date;
import java.util.List;

import parser.DataSource;

public interface AnalysisGraphTool {
	
	public void setDataSource(DataSource dataSource);

	void setRequestedData(List<String> labs, Date start, Date end,
			int timePeriod, String timeDescrip, boolean isPower,
			boolean limitData);

}
