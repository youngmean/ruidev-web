package com.ruidev.framework.xls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ruidev.framework.constant.BaseConstants;
import com.ruidev.framework.exception.BizException;
import com.ruidev.framework.util.DateTimeUtil;

public class ExcelExport {
	
	public final static String BG_ERROR_COLOR_PREFIX = "@ERROR";
	public final static String BG_WARN_COLOR_PREFIX = "@WARN";

	public static <E extends IExcelExportImpl> void export(Class<E> implClass, List<Object> list, String filePath) throws Exception {
		Workbook wb = export(implClass, list);
		writeExcelFile(wb, filePath);
	}
	
	public static <E extends IExcelExportImpl> void export(E impl, List<Object> list, String filePath) throws Exception {
		Workbook wb = export(impl, list);
		writeExcelFile(wb, filePath);
	}
	
	public static <E extends IExcelExportImpl> void export(Class<E> implClass, List<Object> list, OutputStream outputStream) throws Exception {
		Workbook wb = export(implClass, list);
		writeExcelFile(wb, outputStream);
	}
	
	public static <E extends IExcelExportImpl> void export(E impl, List<Object> list, OutputStream outputStream) throws Exception {
		Workbook wb = export(impl, list);
		writeExcelFile(wb, outputStream);
	}
	

	protected static <E extends IExcelExportImpl>Workbook export(Class<E> implClass, List<Object> list) throws Exception {
		return export(implClass.newInstance(), list);
	}
	protected static <E extends IExcelExportImpl>Workbook export(E impl, List<Object> list) throws Exception {
		
		Workbook wb = new XSSFWorkbook();
		
		/*CellStyle unSubmitStyle = wb.createCellStyle();
		unSubmitStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);*/
		
		Sheet sheet = wb.createSheet("data");
		String[] headers = impl.getHeaders();
		if(headers == null || headers.length < 1){
			throw new BizException("length of headers must greater than 0");
		}
		CellStyle warningBgStyle = wb.createCellStyle();
		warningBgStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		warningBgStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		impl.setWarningBgStyle(warningBgStyle);
		CellStyle errorBgStyle = wb.createCellStyle();
		errorBgStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
		errorBgStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		impl.setErrorBgStyle(errorBgStyle);
		Row row = sheet.createRow(0);
		Integer index = 0;
		for(String name : impl.getHeaders()){
			setCellValue(row, name, index, impl);
			index ++;
		}
		Integer readRowIndex = 1;
		int writeRowIndex = 1;
		String dateFormat = impl.getDateFormat();
		if(StringUtils.isEmpty(dateFormat)) {
			dateFormat = BaseConstants.DATE_PATTERN;
		}
		for(Object obj : list){
			Object[] rowData = impl.getRowData(readRowIndex, obj);
			if(rowData == null) {
				readRowIndex++;
				continue;
			}
			row = sheet.createRow(writeRowIndex);
			Integer _index = 0;
			for(Object data : rowData){
				if(data != null && data instanceof Date) {
					setCellValue(row, DateTimeUtil.getFormatDate((Date) data, dateFormat), _index, impl);
				}else {
					setCellValue(row, data, _index, impl);
				}
				_index ++;
			}
			readRowIndex ++;
			writeRowIndex ++;
		}
		if(impl.getSummaryHeaders() != null){
			readRowIndex ++;
			writeRowIndex ++;
			row = sheet.createRow(writeRowIndex);
			index = 0;
			for(String name : impl.getSummaryHeaders()){
				setCellValue(row, name, index, impl);
				index ++;
			}
			readRowIndex ++;
			writeRowIndex ++;
		}
		Object[] rowData = impl.getSummaryData(readRowIndex, list);
		if(rowData != null){
			row = sheet.createRow(writeRowIndex);
			Integer _index = 0;
			for(Object data : rowData){
				setCellValue(row, data, _index, impl);
				_index ++;
			}
			readRowIndex ++;
		}
		return wb;
	}
	
	public static <E extends IExcelExportImpl> Cell setCellValue(Row row, Object value, Integer index, E exp){
		if(value == null) return null;
		Cell cell = row.createCell(index);
		if(value instanceof String) {
			String val = (String) value;
			if(val.contains(BG_WARN_COLOR_PREFIX)) {
				cell.setCellStyle(exp.getWarningBgStyle());
				cell.setCellValue(val.substring(0, val.indexOf(BG_WARN_COLOR_PREFIX)));
				return cell;
			}
			if(val.contains(BG_ERROR_COLOR_PREFIX)) {
				cell.setCellStyle(exp.getErrorBgStyle());
				cell.setCellValue(val.substring(0, val.indexOf(BG_ERROR_COLOR_PREFIX)));
				return cell;
			}
			cell.setCellValue(value.toString());
		}else if(value instanceof Integer) {
			cell.setCellValue((Integer)value);
		}else if(value instanceof Long) {
			cell.setCellValue((Long)value);
		}else if(value instanceof Double) {
			cell.setCellValue((Double)value);
		}else if(value instanceof Date) {
			cell.setCellValue((Date)value);
		}else if(value instanceof Boolean) {
			cell.setCellValue((Boolean)value);
		}else {
			cell.setCellValue(value.toString());
		}
		return cell;
	}
	
/*	private static void setCellValue(Row row, String value, Integer index, CellStyle style){
		Cell cell = setCellValue(row, value, index);
		if(cell != null && style != null) {
			cell.setCellStyle(style);
		}
	}*/
	
	public static void writeExcelFile(Workbook wb, String path) {
		try {
			String dir = path.substring(0, path.lastIndexOf("/")+1);
			File f = new File(dir);
			if(!f.exists()){
				f.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(path);
			writeExcelFile(wb, fos);
		} catch (Exception e) {
		}
	}
	
	public static void writeExcelFile(Workbook wb, OutputStream os) {
		try {
			wb.write(os);
			wb.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}