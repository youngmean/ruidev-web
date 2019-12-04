package com.ruidev.framework.xls;

import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;

public abstract class IExcelExportImpl {

	protected CellStyle warningBgStyle;
	protected CellStyle errorBgStyle;
	protected int size;
	protected List<?> data;
	protected String dateFormat;

	public abstract String[] getHeaders();

	/**
	 * 返回的数组大小为<b>表头列数+1</b>,且数组最后一条数据为<b>Collection&lt;Object[]&gt;</b>时,为当前数据尚有更多数据输出
	 * @param index
	 * @param data
	 * @return
	 */
	public abstract Object[] getRowData(Integer index, Object data);

	public abstract String[] getSummaryHeaders();

	public abstract Object[] getSummaryData(Integer index, Object data);

	public CellStyle getWarningBgStyle() {
		return warningBgStyle;
	}

	public void setWarningBgStyle(CellStyle warningBgStyle) {
		this.warningBgStyle = warningBgStyle;
	}

	public CellStyle getErrorBgStyle() {
		return errorBgStyle;
	}

	public void setErrorBgStyle(CellStyle errorBgStyle) {
		this.errorBgStyle = errorBgStyle;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
}
