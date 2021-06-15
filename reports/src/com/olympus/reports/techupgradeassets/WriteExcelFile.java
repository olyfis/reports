package com.olympus.reports.techupgradeassets;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.olympus.olyutil.Olyutil;

public class WriteExcelFile  {

	XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet sheet = workbook.createSheet("Datatypes in Java");
	
	/***********************************************************************************************************************************/
	/****************************************************************************************************************************************************/
	public static boolean displayHashMap(HashMap<String, Double> hashMap) {
		boolean status = true;
		
		Set<String> keys = hashMap.keySet();  //get all keys
		for(String key: keys) {

		  System.out.println("**----** Key=" + key + "-- Value=" + hashMap.get(key) + "--");
		}
		return(status);
		
	}
	/****************************************************************************************************************************************************/


	
	
	

	/***********************************************************************************************************************************/
	public static XSSFSheet newWorkSheet(XSSFWorkbook workbook, String label) {

		XSSFSheet sheet = workbook.createSheet(label);
		return sheet;
	}

	/***********************************************************************************************************************************/
	public static XSSFWorkbook newWorkbook() {

		XSSFWorkbook workbook = new XSSFWorkbook();
		return workbook;
	}
	
/***********************************************************************************************************************************/
	
	// Active loadWS Totals
	public static void loadTotalWorkSheet(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> strArr, String sep, HashMap<String, Double> map) {
		 String[] strSplit  = null;
		String id = "";
		String rollover = "";
		
		String cust = "";
		String commDate = "";
		double totals = 0.00;
		
		//displayHashMap(map);
		Set<String> keys = map.keySet();  //get all keys
		//int colNum = 0;
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		//sheet.autoSizeColumn(0);
		cell.setCellValue((String) "ContractID");
		cell = row.createCell(1);
		cell.setCellValue((String) "Customer Name");
		
		cell = row.createCell(2);
		cell.setCellValue((String) "Commencement Date");
		
		
		cell = row.createCell(3);
		cell.setCellValue((String) "Contract Equipment Costs");
		
		cell = row.createCell(4);
		cell.setCellValue((String) "Rollover");
		
		cell = row.createCell(5);
		cell.setCellValue((String) "Total");
		
		
		int rowNum = 1;
		for(String key: keys) {
		 row = sheet.createRow(rowNum++);
			 
		  
			strSplit  = key.split("\\^");
			id = strSplit[0];
			rollover = strSplit[1];
			cust = strSplit[2];
			commDate = strSplit[3];
			totals = map.get(key);
			
			//System.out.println("**--LTWS--** Key=" + key + "-- Value=" + map.get(key) + "-- Cust=" + cust + "--");
			
			//System.out.println("**--LTWS-- SZ="  +  strSplit.length   + "-- ID=" + id + "-- Rollover=" + rollover + "--");
			//
			//System.out.println("**--LTWS--** ID=" + strSplit[0] +  "--");
		  
			//colNum++;
			cell = row.createCell(0);
			cell.setCellValue((String) id);
			sheet.autoSizeColumn(0);
			cell = row.createCell(1);
			cell.setCellValue((String) cust);
			cell = row.createCell(2);
			cell.setCellValue((String) commDate);
			cell = row.createCell(3);
			cell.setCellValue((double) totals);
			cell = row.createCell(4);
			cell.setCellValue((String) rollover);
			
			
		}
		
		 for (int columnIndex = 0; columnIndex < 5; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
		
		
	}
	/***********************************************************************************************************************************/
	
	// Inactive
	public static void loadWorkSheetOLD(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> strArr, int rowNum) {
		String[] strSplitArr = null;

		//System.out.println("************* strArr=" + strArr.toString());
		for (String str : strArr) { // iterating ArrayList
			Row row = sheet.createRow(rowNum++);
			strSplitArr = splitStr(str, ":");
			int colNum = 0;
			for (String token : strSplitArr) {
				Cell cell = row.createCell(colNum);
				if (token instanceof String) {
					
					if (colNum == 10){
						System.out.println("*** C=" + colNum + "-- TK" + token  + "--" );
						
						if (Olyutil.isNullStr(token)) {
							cell.setCellValue((String) "N");

						} else {
							cell.setCellValue((String) "Y");
						}
					} else {
						cell.setCellValue((String) token);
					}
					
	
					
				}
				colNum++;
				
			} // End for
		}
		for (int columnIndex = 0; columnIndex < 11; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
	}
	/***********************************************************************************************************************************/
	
	// Active loadWS
	public static void loadWorkSheet(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> strArr, int rowNum, String sep) {
		String[] strSplitArr = null;

		//System.out.println("************* strArr=" + strArr.toString());
		for (String str : strArr) { // iterating ArrayList
			Row row = sheet.createRow(rowNum++);
			strSplitArr = splitStr(str, sep);
			int colNum = 0;
			for (String token : strSplitArr) {
				Cell cell = row.createCell(colNum);
				if (token instanceof String) {
					
					if (colNum == 10){
						//System.out.println("*** C=" + colNum + "-- TK" + token  + "--" );
						
						if (Olyutil.isNullStr(token)) {
							cell.setCellValue((String) "N");

						} else {
							cell.setCellValue((String) "Y");
						}
					} else {
						cell.setCellValue((String) token);
					}
				
					 
				}
				colNum++;
			} // End Inner for
		} // End Outer for
		for (int columnIndex = 0; columnIndex < 11; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
	}
	/***********************************************************************************************************************************/
	public static void loadHeader(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> headerArr) {

		// System.out.println("************* strArr=" + headerArr.toString());
		Row row = sheet.createRow(0);
		int colNum = 0;
		for (Object field : headerArr) {
			Cell cell = row.createCell(colNum++);
			if (field instanceof String) {
				cell.setCellValue((String) field);
			}
		}

	}

	/***********************************************************************************************************************************/
	// method to print array
	public static void printStrArray(ArrayList<String> strArr) {

		for (String str : strArr) { // iterating ArrayList
			System.out.println("*** DATA:" + str + "---");
		}
		// System.out.println(names[index]);
	}

	/***********************************************************************************************************************************/

	public static String[] splitStr(String string, String delimiter) {
		String[] result = string.split(delimiter);
		int array_length = result.length;

		for (int i = 0; i < array_length; i++) {
			result[i] = result[i].trim();
		}
		return result;
	}
	
}
