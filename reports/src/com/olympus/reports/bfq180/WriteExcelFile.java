package com.olympus.reports.bfq180;

 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcelFile  {

	XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet sheet = workbook.createSheet("Datatypes in Java");

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
	public static void loadWorkSheet(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> strArr, int rowNum) {
		String[] strSplitArr = null;

		//System.out.println("************* strArr=" + strArr.toString());
		for (String str : strArr) { // iterating ArrayList
			Row row = sheet.createRow(rowNum++);
			strSplitArr = splitStr(str, ":");
			int colNum = 0;
			for (String token : strSplitArr) {
				Cell cell = row.createCell(colNum++);
				if (token instanceof String) {
					cell.setCellValue((String) token);
				}
			}
		}
	}
	/***********************************************************************************************************************************/
	public static void loadWorkSheet(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> strArr, int rowNum, String sep) {
		String[] strSplitArr = null;

		//System.out.println("************* strArr=" + strArr.toString());
		for (String str : strArr) { // iterating ArrayList
			Row row = sheet.createRow(rowNum++);
			strSplitArr = splitStr(str, sep);
			int colNum = 0;
			for (String token : strSplitArr) {
				Cell cell = row.createCell(colNum++);
				if (token instanceof String) {
					cell.setCellValue((String) token);
				}
			}
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
