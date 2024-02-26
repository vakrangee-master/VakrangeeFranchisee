package in.vakrangee.core.commongui.report;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReportHeader implements Serializable {

	@SerializedName("Header")
	private String Header;

	private String ColWidth;

	@SerializedName("HeaderValue")
	private String HeaderValue;

	@SerializedName("ColumnOrder")
	private int ColumnOrder;

	@SerializedName("IsDefaultWidth")
	private boolean IsDefaultWidth = false;

	public String getColWidth() {
		return ColWidth;
	}

	public boolean isIsDefaultWidth() {
		return IsDefaultWidth;
	}

	public void setIsDefaultWidth(boolean isDefaultWidth) {
		IsDefaultWidth = isDefaultWidth;
	}

	public void setColWidth(String colWidth) {
		ColWidth = colWidth;
	}

	public int getColumnOrder() {
		return ColumnOrder;
	}

	public void setColumnOrder(int columnOrder) {
		ColumnOrder = columnOrder;
	}

	public String getHeader() {
		return Header;
	}

	public void setHeader(String header) {
		Header = header;
	}

	public String getHeaderValue() {
		return HeaderValue;
	}

	public void setHeaderValue(String headerValue) {
		HeaderValue = headerValue;
	}


}
