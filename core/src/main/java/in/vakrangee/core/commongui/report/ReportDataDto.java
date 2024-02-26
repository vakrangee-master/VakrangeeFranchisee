package in.vakrangee.core.commongui.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportDataDto {

	public List<ReportHeader> reportHeaderList;
	public List<Map<String, String>> reportDataList;

	//Constructor
	public ReportDataDto() {
		reportHeaderList = new ArrayList<ReportHeader>();
		reportDataList = new ArrayList<Map<String, String>>();

	}
}
