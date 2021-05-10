package com.ruidev.framework.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.monitorjbl.xlsx.StreamingReader;
import com.opencsv.CSVReader;

public class ExcelImport {
	
	protected static final Logger log = LogManager.getLogger(ExcelImport.class);
	
	public <E extends BaseExcelImportImpl> List<?> importData(Class<E> clazz, File file) throws Exception{
		return importData(clazz, file, 0, 0, "");
	}
	
	public <E extends BaseExcelImportImpl> List<?> importDataXlsx(Class<E> clazz, File file) throws Exception{
		return importDataXlsx(clazz, file, 0, 0, "");
	}
	
	public <E extends BaseExcelImportImpl> List<?> importDataXlsx(Class<E> clazz, File file, int sheetIndex, int rowStartIndex) throws Exception{
		return importDataXlsx(clazz, file, sheetIndex, rowStartIndex, "");
	}
	
	public <E extends BaseExcelImportImpl> List<?> importData(E _import, File file) throws Exception{
		return importData(_import, file, 0, 0, "");
	}
	
	public <E extends BaseExcelImportImpl> List<?> importDataXlsx(E _import, File file) throws Exception{
		return importDataXlsx(_import, file, 0, 0, "");
	}
	
	public <E extends BaseExcelImportImpl> List<?> importDataXlsx(E _import, File file, int sheetIndex, int rowStartIndex) throws Exception{
		return importDataXlsx(_import, file, sheetIndex, rowStartIndex, "");
	}
	
	public static ExcelImport getInstance(){
		return new ExcelImport();
	}
	public <E extends BaseCsvImportImpl> List<?> importCsvData(Class<E> clazz, File file) throws Exception{
		return importCsvData(clazz, file, "");
	}
	public <E extends BaseCsvImportImpl> List<?> importCsvData(Class<E> clazz, File file, char seprator, char quote) throws Exception{
		return importCsvData(clazz, file, "", seprator, quote);
	}
	
	public <E extends BaseCsvImportImpl> List<?> importCsvData(Class<E> clazz, File file, String code) throws Exception{
		E _import = clazz.newInstance();
		return importCsvData(_import, file, code, ',', '"');
	}
	public <E extends BaseCsvImportImpl> List<?> importCsvData(Class<E> clazz, File file, String code, char seprator, char quote) throws Exception{
		E _import = clazz.newInstance();
		return importCsvData(_import, file, code, seprator, quote);
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation"})
	public <E extends BaseCsvImportImpl> List<?> importCsvData(E _import, File file, String code, char separator, char quote) throws Exception{
		List list = new ArrayList();
		InputStreamReader fReader = null;
		if(code.isEmpty()) {
			fReader = new FileReader(file);  
		} else {
			fReader = new InputStreamReader(new FileInputStream(file), code);
		}
//		CSVReader csvReader = new CSVReader(fReader, separator, quote);
		CSVReader csvReader = new CSVReader(fReader);
        String[] strs = csvReader.readNext();
        int rowIndex = 0;
        try {
			if(strs != null && strs.length > 0){
				_import.onStartRowData(strs, rowIndex, list);
				String[][] propertiesMap = _import.mapProperties();
				Map<String, Integer> indexes = new HashMap<String, Integer>();
				Character uSpace = (char)65279;
				for(int i=0,ilen=strs.length;i<ilen;i++){
					String prop = strs[i];
					String propName = null;
					if(propertiesMap != null){
						for(String[] properties : propertiesMap){
							String origProp = properties[0];
								if(prop.equals(origProp)){
									if(properties.length == 1){
										propName = origProp;
									}else{
										propName = properties[1];
									}
									break;
								}
						}
						if(propName != null){
							propName = propName.replaceAll("[\\s\\u3000]+", "");
							indexes.put(propName.replaceAll(uSpace.toString(), ""), i);
						}
					}
					prop = prop.replaceAll("[\\s\\u3000]+", "");
					indexes.put(prop.replaceAll(uSpace.toString(), ""), i);
				}
				_import.setIndexes(indexes);
				rowIndex ++;
				strs = csvReader.readNext();
				int headerSize = indexes.size();
				while(strs != null && strs.length > 0){
					if(strs.length < headerSize){
						log.warn("Row data size less than header size, row:{}, data size:{}, header size:{}, file: {}", rowIndex, strs.length, headerSize, file.getName());
						rowIndex ++;
						strs = csvReader.readNext();
						_import.onReadDataError(strs, rowIndex, list);
						continue;
					}
					Object obj = _import.getRowData(indexes, strs, rowIndex);
					if(obj == null){
						break;
					}
					list.add(obj);
					rowIndex ++;
					strs = csvReader.readNext();

				}
			}
		} catch (Exception e1) {
			log.error("Error happened: {}", e1.getMessage());
			throw e1;
		}finally{
			csvReader.close();
			fReader.close();
		}
 
        try{
        	_import.onEndReadData(file, list);
        }catch(Exception e){}
		return list;
	}

	public <E extends BaseExcelImportImpl> List<?> importData(Class<E> clazz, File file, int sheetIndex, int rowStartIndex, String sheetName) throws Exception{
		E _import = clazz.newInstance();
		return importData(_import, file, sheetIndex, rowStartIndex, sheetName);
	}
	
	public <E extends BaseExcelImportImpl> List<?> importDataXlsx(Class<E> clazz, File file, int sheetIndex, int rowStartIndex, String sheetName) throws Exception{
		E _import = clazz.newInstance();
		return importDataXlsx(_import, file, sheetIndex, rowStartIndex, sheetName);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	public <E extends BaseExcelImportImpl> List<?> importDataXlsx(E _import, File file, int sheetIndex, int rowStartIndex, String sheetName) throws Exception{
		FileInputStream in = new FileInputStream(file);
        Workbook wk = StreamingReader.builder()
                .rowCacheSize(200)  //缓存到内存中的行数，默认是10
                .bufferSize(4096)  //读取资源时，缓存到内存的字节大小，默认是1024
                .open(in);  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
        Sheet sheet = wk.getSheetAt(sheetIndex);
        List list = new ArrayList();
        int dataIndex = 0;
        while(sheet != null){
			int rowIndex = 0;
			Map<String, Integer> indexes = new HashMap<String, Integer>();
			for(Row row : sheet) {
				if(row == null)break;
				if(rowIndex < rowStartIndex) {
					rowIndex++;
					continue;
				}
				if(rowIndex == rowStartIndex) {
					_import.onStartRowData(row, rowIndex, sheet, sheetIndex, list);
					String[][] propertiesMap = _import.mapProperties();
					int cellIndex = 0;
					Cell cell = row.getCell(cellIndex);
					Character uSpace = (char)65279;
					while(cell != null){
						String prop = cell.getStringCellValue().trim();
						String propName = null;
						if(propertiesMap != null){
							for(String[] properties : propertiesMap){
								String origProp = properties[0];
									if(prop.equals(origProp)){
										if(properties.length == 1){
											propName = origProp;
										}else{
											propName = properties[1];
										}
										break;
									}
							}
							if(propName != null){
								propName = propName.replaceAll("[\\s\\u3000]+", "");
								indexes.put(propName.replaceAll(uSpace.toString(), "").toLowerCase(), cellIndex);
							}
						}
						prop = prop.replaceAll("[\\s\\u3000]+", "");
						indexes.put(prop.replaceAll(uSpace.toString(), "").toLowerCase(), cellIndex);
						cellIndex++;
						cell = row.getCell(cellIndex);
					}
					_import.setIndexes(indexes);
					rowIndex++;
				}else {
					dataIndex++;
					Object obj = _import.getRowData(indexes, row, rowIndex, sheet, sheetIndex, dataIndex);
					if(obj == null){
						break;
					}
					list.add(obj);
					rowIndex++;
				}
			}
			sheetIndex++;
			try {
				sheet = wk.getSheetAt(sheetIndex);
			} catch (Exception e) {
				sheet = null;
			}
		}
		if(in != null){
			in.close();
		}
		if(wk != null){
			wk.close();
		}
		try{
        	_import.onEndReadData(file, list);
        }catch(Exception e){}
		return list;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	public <E extends BaseExcelImportImpl> List<?> importData(E _import, File file, int sheetIndex, int rowStartIndex, String sheetName) throws Exception{
		InputStream myxls = new FileInputStream(file);
		Workbook wb = null;
		Sheet sheet = null;
		try {
			wb = new XSSFWorkbook(myxls);
			if(!sheetName.isEmpty())
				sheet = wb.getSheet(sheetName);
			else
				sheet = wb.getSheetAt(sheetIndex);
		} catch (Exception e) {
			myxls = new FileInputStream(file);
			wb = new HSSFWorkbook(myxls);
			if(!sheetName.isEmpty()) {
				sheet = wb.getSheet(sheetName);
			}else {
				sheet = wb.getSheetAt(sheetIndex);
			}
		}
		List list = new ArrayList();
		int dataIndex = 0;
		while(sheet != null){
			_import.setSheetName(sheet.getSheetName());
			int rowIndex = 0;//rowStartIndex;
			Row row = sheet.getRow(rowIndex);
			if(row == null)break;
			_import.onStartRowData(row, rowIndex, sheet, sheetIndex, list);
			String[][] propertiesMap = _import.mapProperties();
			Map<String, Integer> indexes = new HashMap<String, Integer>();
			int cellIndex = 0;
			Cell cell = row.getCell(cellIndex);
			Character uSpace = (char)65279;
			while(cell != null){
				String prop = cell.getStringCellValue().trim();
				String propName = null;
				if(propertiesMap != null){
					for(String[] properties : propertiesMap){
						String origProp = properties[0];
							if(prop.equals(origProp)){
								if(properties.length == 1){
									propName = origProp;
								}else{
									propName = properties[1];
								}
								break;
							}
					}
					if(propName != null){
						propName = propName.replaceAll("[\\s\\u3000]+", "");
						indexes.put(propName.replaceAll(uSpace.toString(), "").toLowerCase(), cellIndex);
					}
				}
				prop = prop.replaceAll("[\\s\\u3000]+", "");
				indexes.put(prop.replaceAll(uSpace.toString(), "").toLowerCase(), cellIndex);
				cellIndex++;
				cell = row.getCell(cellIndex);
			}
			_import.setIndexes(indexes);
			rowIndex++;
			row = sheet.getRow(rowIndex);
			while(row != null){
				dataIndex++;
				Object obj = _import.getRowData(indexes, row, rowIndex, sheet, sheetIndex, dataIndex);
				if(obj == null){
					break;
				}
				list.add(obj);
				rowIndex++;
				row = sheet.getRow(rowIndex);
			}
			if(!sheetName.isEmpty()) {
				sheet = null;
			} else {
				sheetIndex++;
				try {
					sheet = wb.getSheetAt(sheetIndex);
				} catch (Exception e) {
					sheet = null;
				}
			}
		}
		if(myxls != null){
			myxls.close();
		}
		if(wb != null){
			wb.close();
		}
		try{
        	_import.onEndReadData(file, list);
        }catch(Exception e){}
		return list;
	}
}
