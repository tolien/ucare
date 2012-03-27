package graphing;

import java.util.Date;
import java.util.List;

import parser.DataSource;

public interface GraphTool {

	public void setRequestedData(List<String> labs, Date start, Date end, int axisType);
	
	public void setDataSource(DataSource dataSource);
}
