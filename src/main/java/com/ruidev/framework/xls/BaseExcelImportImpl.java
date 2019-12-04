package com.ruidev.framework.xls;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.monitorjbl.xlsx.impl.StreamingCell;
import com.ruidev.framework.exception.BizException;

public abstract class BaseExcelImportImpl {
	
	protected Map<String, Integer> indexes;
	protected String sheetName;
	private static Map<String, SimpleDateFormat> DATE_FORMATS;
	//protected FormulaEvaluator evaluator;
	
	private static Date getFormatDate(String dateStr, String pattern) throws ParseException {
		return getDateFormat(pattern).parse(dateStr);
	}
	
	private static SimpleDateFormat getDateFormat(String pattern) {
		if(DATE_FORMATS == null) {
			DATE_FORMATS = new HashMap<String, SimpleDateFormat>();
		}
		SimpleDateFormat format = DATE_FORMATS.get(pattern);
		if(format == null) {
			format = new SimpleDateFormat(pattern);
			DATE_FORMATS.put(pattern, format);
		}
		return format;
	}

	public Map<String, Integer> getIndexes() {
		return indexes;
	}
	public void setIndexes(Map<String, Integer> indexes) {
		this.indexes = indexes;
	}
	
	public String[][] mapProperties(){
		return null;
	}
	protected <T> void onStartRowData(Row row, int rowIndex, Sheet sheet, int sheetIndex, List<T> list){
		
	}
	protected <T> void onEndReadData(File file, List<T> list){
		
	}
	protected abstract Object getRowData(Map<String, Integer> cellPropertiesAndIndexes, Row row, int rowIndex, Sheet sheet, int sheetIndex, int dataIndex) throws BizException;
	
	public Long getCellLongValue(Cell cell){
		Double num = getCellDoubleValue(cell);
		if(num != null){
			return Long.valueOf(num.longValue());
		}
		return null;
	}

	public Date getCellDateValue(Cell cell, String pattern) {
		if (cell == null)
			return null;
		CellType cellType = null;
		if (cell instanceof StreamingCell) {
			StreamingCell _cell = (StreamingCell) cell;
			cellType = _cell.getCellTypeEnum();
		} else {
			cellType = cell.getCellType();
		}
		if (cellType.equals(CellType.NUMERIC)) {
			double dateNumber = cell.getNumericCellValue();
			if(dateNumber < 19000000) {
				return cell.getDateCellValue();
			}
		}
		String dateStr = getTrimCellStringValue(cell);
		if(!StringUtils.isEmpty(dateStr)){
			try {
				return getFormatDate(dateStr, pattern);
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}
	
	public Date getCellDateValue(Row row, String prop, String pattern) throws BizException{
		Cell cell = row.getCell(getPropIndex(prop));
		return getCellDateValue(cell, pattern);
	}
	
	public Long getCellLongValue(Row row, String prop) throws BizException{
		Cell cell = row.getCell(getPropIndex(prop));
		return getCellLongValue(cell);
	}
	
	public Integer getCellIntValue(Cell cell){
		Double num = getCellDoubleValue(cell);
		if(num != null){
			return Integer.valueOf(num.intValue());
		}
		return null;
	}
	
	public Integer getCellIntValue(Row row, String prop) throws BizException{
		Cell cell = row.getCell(getPropIndex(prop));
		return getCellIntValue(cell);
	}
	
	public Double getCellDoubleValue(Cell cell){
		if(cell == null){
			return null;
		}
		CellType cellType = null;
		if (cell instanceof StreamingCell) {
			StreamingCell _cell = (StreamingCell) cell;
			cellType = _cell.getCellTypeEnum();
		} else {
			cellType = cell.getCellType();
		}
		if (cellType.equals(CellType.NUMERIC)) {
			Double num = Double.valueOf(cell.getNumericCellValue());
			return num;
		}else if(cellType == CellType.STRING){
			try {
				return Double.valueOf(getTrimCellStringValue(cell));
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}
	
	public Double getCellDoubleValue(Row row, String prop) throws BizException{
		Cell cell = row.getCell(getPropIndex(prop));
		return getCellDoubleValue(cell);
	}
	
	/**
	 * 将单元格中数据转为string格式 支持数字、布尔及字符串格式
	 * @param cell
	 * @return
	 */
	public String getCellStringValue(Cell cell){
		if(cell == null){
			return null;
		}
		CellType type = null;
		if (cell instanceof StreamingCell) {
			StreamingCell _cell = (StreamingCell) cell;
			type = _cell.getCellTypeEnum();
		} else {
			type = cell.getCellType();
		}
		Object _val = null;
		if (type.equals(CellType.NUMERIC)) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				return getDateFormat("yyyy-MM-dd hh:mm:ss").format(cell.getDateCellValue());
			}
			double val = cell.getNumericCellValue();
			Double _num = Double.valueOf(cell.getNumericCellValue());
			if(val - _num.longValue() == 0.0){
				_val = _num.longValue()+"";
			}else{
				_val = _num.toString();
			}
		} else if (type.equals(CellType.BOOLEAN)) {
			_val = cell.getBooleanCellValue();
		} else if (type.equals(CellType.STRING)) {
			_val = cell.getStringCellValue();
		} else if (type.equals(CellType.FORMULA)) {
			_val = cell.getStringCellValue();
		}
		
		if(_val != null){
			return _val.toString();
		}
		return null;
	}

	public static Object getCellValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		CellType type = null;
		if (cell instanceof StreamingCell) {
			StreamingCell _cell = (StreamingCell) cell;
			type = _cell.getCellTypeEnum();
		} else {
			type = cell.getCellType();
		}
		Object _val = null;
		if (type.equals(CellType.NUMERIC)) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			}
			double val = cell.getNumericCellValue();
			Double _num = Double.valueOf(cell.getNumericCellValue());
			if (val - _num.longValue() == 0.0) {
				_val = _num.longValue() + "";
			} else {
				_val = _num.toString();
			}
		} else if (type.equals(CellType.BOOLEAN)) {
			return cell.getBooleanCellValue();
		} else if (type.equals(CellType.STRING)) {
			return cell.getStringCellValue();
		} else if (type.equals(CellType.FORMULA)) {
			return cell.getStringCellValue();
		}

		if (_val != null) {
			return _val.toString();
		}
		return null;
	}

	/**
	 * 复制一个单元格样式到目的单元格样式
	 * 
	 * @param fromStyle
	 * @param toStyle
	 */
	public static void copyCellStyle(CellStyle fromStyle, CellStyle toStyle) {
		toStyle.setAlignment(fromStyle.getAlignment());
		// 边框和边框颜色
		toStyle.setBorderBottom(fromStyle.getBorderBottom());
		toStyle.setBorderLeft(fromStyle.getBorderLeft());
		toStyle.setBorderRight(fromStyle.getBorderRight());
		toStyle.setBorderTop(fromStyle.getBorderTop());
		toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
		toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
		toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
		toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

		// 背景和前景
		toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
		toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

		toStyle.setDataFormat(fromStyle.getDataFormat());
		toStyle.setFillPattern(fromStyle.getFillPattern());
		// toStyle.setFont(fromStyle.getFont(null));
		toStyle.setHidden(fromStyle.getHidden());
		toStyle.setIndention(fromStyle.getIndention());// 首行缩进
		toStyle.setLocked(fromStyle.getLocked());
		toStyle.setRotation(fromStyle.getRotation());// 旋转
		toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
		toStyle.setWrapText(fromStyle.getWrapText());

	}

	public static void copyCellValue(Cell cell, Cell target) {
		if (cell == null) {
			target.setCellValue("");
			return;
		}
		CellType type = null;
		if (cell instanceof StreamingCell) {
			StreamingCell _cell = (StreamingCell) cell;
			type = _cell.getCellTypeEnum();
		} else {
			type = cell.getCellType();
		}
		if (type.equals(CellType.NUMERIC)) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				target.setCellValue(cell.getDateCellValue());
				return;
			}
			target.setCellValue(cell.getNumericCellValue());
			return;
		} else if (type.equals(CellType.BOOLEAN)) {
			target.setCellValue(cell.getBooleanCellValue());
			return;
		} else if (type.equals(CellType.STRING)) {
			target.setCellValue(cell.getStringCellValue());
			return;
		} else if (type.equals(CellType.ERROR)) {
			target.setCellErrorValue(cell.getErrorCellValue());
			return;
		} else if (type.equals(CellType.FORMULA)) {
			target.setCellFormula(cell.getStringCellValue());
			return;
		}
	}

	public String getTrimCellStringValue(Cell cell) {
		try {
			String result = getCellStringValue(cell);
			if(result != null){
				return result.trim();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected Integer getPropIndex(String prop) throws BizException {
		if(indexes == null){
			return null;
		}
		String _prop = prop.replaceAll("[\\s\\u3000]+", "").toLowerCase();
		Integer index = indexes.get(_prop);
		if(index == null){
			throw new BizException("属性为" + prop + "的列不存在");
		}
		return index;
	}
	
	public String getCellStringTrimValue(Row row, String prop) throws BizException{
		Cell cell = row.getCell(getPropIndex(prop));
		return getTrimCellStringValue(cell);
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

}
