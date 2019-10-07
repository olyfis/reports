package com.olympus.reports.leases;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import com.olympus.olyutil.Olyutil;
import com.olympus.olyutil.excel.OlyExcel;


@WebServlet("/ceotexcel")
public class CeotExcel  extends HttpServlet{
 
	/***********************************************************************************************************************************/
	public static void loadWorkSheetCell(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> strArr, int rowNum, String sep) {
		String[] strSplitArr = null;
		long assetID = 0;
		double equipCost = 0.0;
		double assetRes = 0.0;
		double accountRes = 0.0;
		double cogs = 0.0;
		String sn = "";
	 
		//System.out.println("************* strArr=" + strArr.toString());
		for (String str : strArr) { // iterating ArrayList
				
			Row row = sheet.createRow(rowNum++);
			strSplitArr = Olyutil.splitStr(str, sep);	
			int colNum = 0;
			for (String token : strSplitArr) {
				Cell cell = row.createCell(colNum);
				if (colNum == 8) {
					if (! Olyutil.isNullStr(strSplitArr[8])) {
						assetID = Long.valueOf(strSplitArr[8]);
					}			
					cell.setCellValue((long) assetID);
				} else if (colNum == 10) {
					if ( Olyutil.isNullStr(strSplitArr[10]) || strSplitArr[10].equals("null") ) {						
						sn = "";
					} else {
						sn =  strSplitArr[10];
					}
					cell.setCellValue((String) sn);
				} else if (colNum == 11) {
					if (! Olyutil.isNullStr(strSplitArr[11])) {
						equipCost = Double.valueOf(strSplitArr[11].replaceAll(",", ""));
					}				 
					cell.setCellValue((double) equipCost);
				} else if (colNum == 12) {
					if (! Olyutil.isNullStr(strSplitArr[12])) {
						assetRes = Double.valueOf(strSplitArr[12].replaceAll(",", ""));
					}  	
					cell.setCellValue((double) assetRes);
				} else if (colNum == 13) {
					if (! Olyutil.isNullStr(strSplitArr[13])) {
						accountRes = Double.valueOf(strSplitArr[13].replaceAll(",", ""));
					}					
					cell.setCellValue((double) accountRes);
				} else if (colNum == 17) {
					if (! Olyutil.isNullStr(strSplitArr[17])) {
						cogs = Double.valueOf(strSplitArr[17].replaceAll(",", ""));
					}					
					cell.setCellValue((double) cogs);
				} else {			
					if (token instanceof String) {
						cell.setCellValue((String) token);
					}
				}
				colNum++;
			 ;
				
			}
		}
	}
	
	/***********************************************************************************************************************************/

	/*****************************************************************************************************************************************/

	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String headerFilenameBRSummary = "C:\\Java_Dev\\props\\headers\\ContractEOT.txt";
		
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFilenameBRSummary);
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		// System.out.println("Date=" + dateStamp);
		String FILE_NAME = "ContractEOTReport_" + dateStamp + ".xlsx";
		HttpSession session = req.getSession();
		ArrayList<String> strArr = new ArrayList<String>();
		strArr = (ArrayList<String>) session.getAttribute("strArr");
		
		//Olyutil.printStrArray(strArr);
		/*****************************************************************************************************************************************/
		// Create Excel file on client
		
	 
		//WriteExcel writeExcel = new WriteExcel();
		workbook = OlyExcel.newWorkbook();
		sheet = OlyExcel.newWorkSheet(workbook, "Contract EOT Report");
		OlyExcel.loadHeader(workbook, sheet, headerArr);
		//System.out.println("** Call loadWorkSheet");
		loadWorkSheetCell(workbook, sheet, strArr, 1, ";");
		//BufferedInputStream in = null; 
		try {
			// HttpServletResponse response = getResponse(); // get ServletResponse
			res.setContentType("application/vnd.ms-excel"); // Set up mime type
			res.addHeader("Content-Disposition", "attachment; filename=" + FILE_NAME);
			OutputStream out2 = res.getOutputStream();
			workbook.write(out2);
			out2.flush();
			/*****************************************************************************************************************************************/

 		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
 
	}
 
}