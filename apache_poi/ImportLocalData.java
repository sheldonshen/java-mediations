package com.hthk.arg.aging.report.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hthk.arg.aging.report.dao.LocalDataInsert;
import com.hthk.arg.aging.report.entity.RptAgingReportLocalData;
import com.hthk.arg.aging.report.util.Constant;
import com.hthk.arg.common.ARGException;
import com.hthk.arg.common.ConfigUtils;

public class ImportLocalData {

	private static final Logger logger = LoggerFactory
			.getLogger(ImportLocalData.class);

	public static void main(String[] args) throws Exception {
		ConfigUtils.loadConfig(Constant.DBConfigFile);
		writeBlobExcelToTable("201610","80171885002");
	}

	// public static void writeBlobExcelToTable(Blob myBlob){
	public static void writeBlobExcelToTable(String date,String billNo) {
		try {
			ConfigUtils.loadConfig(Constant.DBConfigFile);
		} catch (ARGException e1) {
			e1.printStackTrace();
		}
		// 写入从arg_rept_data_file中读出的Excel文件数据
		// ExcelUtil.readExcelDataFromStream(myBlob.getBinaryStream(),data,billNo);

		// 模拟写入Excel数据(仅供测试)
		try {
			List<RptAgingReportLocalData> localDatas = readExcelDataFromFile(Constant.DESExclFile_LOCALDATA,date,billNo);
			System.out.println("数据集合="+localDatas.size());
			LocalDataInsert LocalDataInsert = new LocalDataInsert();
			LocalDataInsert.insert("RPT_AGING_REPORT_CALLDTL", localDatas);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<RptAgingReportLocalData> readExcelDataFromFile(
			String desExcelFile,String date,String billNo) throws Exception {
		FileInputStream fis = new FileInputStream(new File(desExcelFile));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);

		// 判断文件类型xls或者xlsx
		/*
		 * if(desExcelFile.toLowerCase().endsWith("xlsx")){ workbook = new
		 * XSSFWorkbook(fis); }else
		 * if(desExcelFile.toLowerCase().endsWith("xls")){ workbook = new
		 * HSSFWorkbook(fis); }
		 */

		int numberOfSheets = workbook.getNumberOfSheets();
		List<RptAgingReportLocalData> localDatas = new ArrayList<RptAgingReportLocalData>();
		for (int i = 0; i < numberOfSheets; i++) {
			XSSFSheet sheet = workbook.getSheetAt(i);
			Iterator<Row> rowIterator = sheet.iterator();
			int colNum;
			int rowNum = 1;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yy");
			for(int y = 1; y <= 5; y++){
				if (rowIterator.hasNext()) {
					rowIterator.next();// skip five row(column title row)
				}
			}
			
			while (rowIterator.hasNext()) {
				XSSFRow row = (XSSFRow) rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				colNum = 1;
				System.out.println("rowNum="+ rowNum);
				RptAgingReportLocalData RptAgingReportLocalData = new RptAgingReportLocalData();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (colNum) {
					case 1: // Account No	 =  LD_ACCOUNT_NO	VARCHAR2(30)
						System.out.print("1:" + cell.getCellType() + "  ");
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							RptAgingReportLocalData.setLD_ACCOUNT_NO(new Integer((int)cell
									.getNumericCellValue()).toString());
						} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							RptAgingReportLocalData.setLD_ACCOUNT_NO(cell
									.getRichStringCellValue().getString());
						}
						break;
					case 2: // Mobile No	 =  LD_TELE_NO	Number(15,0)
						System.out.print("2:" + cell.getCellType() + "  ");
						RptAgingReportLocalData.setLD_TELE_NO((int)cell.getNumericCellValue());
						break;
					case 3: // As Of Date	 =  LD_DATE	VARCHAR2(10)
						System.out.print("3:" + cell.getCellType() + "  ");
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							RptAgingReportLocalData.setLD_DATE(new Integer((int)cell
									.getNumericCellValue()).toString());
						} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							RptAgingReportLocalData.setLD_DATE(cell
									.getRichStringCellValue().getString());
						}
						break;
					case 4: // Total KB	 =  LD_TOTAL_KB	Number(10,2)
						System.out.print("4:" + cell.getCellType() + "  ");
						RptAgingReportLocalData.setLD_TOTAL_KB((int) cell
								.getNumericCellValue());
						break;
					case 5: // Total MB     =  LD_TOTAL_MB	Number(10,2)
						System.out.println("5:" + cell.getCellType() + "  ");
						RptAgingReportLocalData.setLD_TOTAL_MB((int) cell
								.getNumericCellValue());
						break;
					}
					colNum++;
				}
				rowNum++;
				RptAgingReportLocalData.setREPT_DATE(date);
				RptAgingReportLocalData.setBILL_ACCOUNT_NO(billNo);
				RptAgingReportLocalData.setCREATED(new Date());
				RptAgingReportLocalData.setCREATED_BY("admin");
				RptAgingReportLocalData.setMODIFIED(new Date());
				RptAgingReportLocalData.setMODIFIED_BY("admin");
				//过滤
				if(RptAgingReportLocalData.getLD_ACCOUNT_NO() != null && RptAgingReportLocalData.getLD_TELE_NO()!=0){
					localDatas.add(RptAgingReportLocalData);
				}	
			}

			fis.close();
		}
		return localDatas;
	}

	// 从Stream读取Excel文件
	public static List<RptAgingReportLocalData> readExcelDataFromStream(
			InputStream fis,String date,String billNo) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook(fis);

		return null;
	}

	public static void cellValueType(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			System.out.print(cell.getNumericCellValue() + "\t\t ");
			break; 
		case Cell.CELL_TYPE_STRING:
			System.out.print(cell.getStringCellValue() + "\t\t ");
			break;
		case Cell.CELL_TYPE_FORMULA:
			System.out.print(cell.getStringCellValue() + "\t\t ");
			break;
		case Cell.CELL_TYPE_BLANK:
			System.out.print(cell.getStringCellValue() + "\t\t ");
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			System.out.print(cell.getStringCellValue() + "\t\t ");
			break;
		case Cell.CELL_TYPE_ERROR:
			System.out.print(cell.getStringCellValue() + "\t\t ");
			break;
		default:
			System.out.println(cell.getDateCellValue());
			break;
		}
	}
}
