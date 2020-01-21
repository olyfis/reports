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

@WebServlet("/actexcel")
public class ActiveContractsExcel  extends HttpServlet  {
	/********************************************************************************************************************************************************/
	public static void loadWorkSheetCell(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> strArr, int rowNum, String sep) {
		String[] strSplitArr = null;
		long assetID = 0;
		double equipCost = 0.0;
		double gross = 0.0;
		double yield = 0.0;
	 
		//System.out.println("************* strArr=" + strArr.toString());
		for (String str : strArr) { // iterating ArrayList
		
			Row row = sheet.createRow(rowNum++);
			strSplitArr = Olyutil.splitStr(str, sep);	
			int colNum = 0;
			for (String token : strSplitArr) {
				Cell cell = row.createCell(colNum);
			  if (colNum == 27) {
					if (! Olyutil.isNullStr(strSplitArr[27])) {
						gross = Double.valueOf(strSplitArr[27]);
					}				 
					cell.setCellValue((double) gross);
				} else if (colNum == 28) {
					if (! Olyutil.isNullStr(strSplitArr[28])) {
						equipCost = Double.valueOf(strSplitArr[28]);
					}  	
					cell.setCellValue((double) equipCost);
				} else if (colNum == 36) {
					if (! Olyutil.isNullStr(strSplitArr[36])) {
						yield = Double.valueOf(strSplitArr[36]);
					}					
					cell.setCellValue((double) yield);
				} else {			
					if (token instanceof String) {
						cell.setCellValue((String) token.replaceAll("null", " "));
					}
				}
				colNum++;
			 ;
				
			}
		}
	}
	
	/********************************************************************************************************************************************************/
	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String headerFilename = "C:\\Java_Dev\\props\\headers\\activeContractsHdr.txt";
		ArrayList<String> modArr = new ArrayList<String>();
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFilename);
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		// System.out.println("Date=" + dateStamp);
		String FILE_NAME = "Active_Contracts_Report_" + dateStamp + ".xlsx";
		HttpSession session = req.getSession();
		ArrayList<String> strArr = new ArrayList<String>();
		strArr = (ArrayList<String>) session.getAttribute("strArr");
		 
		//Olyutil.printStrArray(strArr);
		/*****************************************************************************************************************************************/
		workbook = OlyExcel.newWorkbook();
		sheet = OlyExcel.newWorkSheet(workbook, "Active_Contracts Report");
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
		//********************************************************************************************************************************
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
