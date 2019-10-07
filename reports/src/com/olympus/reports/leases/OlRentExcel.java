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


@WebServlet("/olrentexcel")
public class OlRentExcel  extends HttpServlet {

	public static ArrayList<String> updateArray( ArrayList<String> sArr) throws IOException {
		ArrayList<String> modArr = new ArrayList<String>();
		String cval = "";
		
		//String cval = sArr.get(7).concat(sArr.get(8));
		//sArr.set(9, cval);
		int j = 0;
		String sn = "";
		
		
		for (String str : sArr) { // iterating ArrayList
			//System.out.println("L=" + j++ + " " + str);
			String[] array = str.split(";");
			
			if (array[7].isEmpty() || array[7].equals("null")) {
				array[7] = "";
			}
			if (array[8].isEmpty() || array[8].equals("null")) {
				array[8] = "";
			}
			if (!Olyutil.isNullStr(array[8])) {
				cval = array[7].concat(array[8]);
			} else {
				cval = array[7];
			}
			array[9] = cval;
			String joinedString = String.join(";", array);
			modArr.add(joinedString);
			
			
			
			
		}
		return(modArr);
	}
	
	/***********************************************************************************************************************************/
	public static void loadWorkSheetCell(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> strArr, int rowNum, String sep) {
		String[] strSplitArr = null;
		long assetID = 0;
		double raM = 0.0;
		double raC = 0.0;
		double raY = 0.0;
	 
		//System.out.println("************* strArr=" + strArr.toString());
		for (String str : strArr) { // iterating ArrayList
			
					
					
			Row row = sheet.createRow(rowNum++);
			strSplitArr = Olyutil.splitStr(str, sep);	
			int colNum = 0;
			for (String token : strSplitArr) {
				Cell cell = row.createCell(colNum);
				if (colNum == 5) {
					if (! Olyutil.isNullStr(strSplitArr[5])) {
						assetID = Long.valueOf(strSplitArr[5]);
					}			
					cell.setCellValue((long) assetID);
				} else if (colNum == 11) {
					if (! Olyutil.isNullStr(strSplitArr[11])) {
						raM = Double.valueOf(strSplitArr[11]);
					}				 
					cell.setCellValue((double) raM);
				} else if (colNum == 12) {
					if (! Olyutil.isNullStr(strSplitArr[12])) {
						raC = Double.valueOf(strSplitArr[12]);
					}  	
					cell.setCellValue((double) raC);
				} else if (colNum == 13) {
					if (! Olyutil.isNullStr(strSplitArr[13])) {
						raY = Double.valueOf(strSplitArr[13]);
					}					
					cell.setCellValue((double) raY);
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
	/*****************************************************************************************************/
	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String headerFilenameBRSummary = "C:\\Java_Dev\\props\\headers\\olRent.txt";
		ArrayList<String> modArr = new ArrayList<String>();
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFilenameBRSummary);
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		// System.out.println("Date=" + dateStamp);
		String FILE_NAME = "OL_Lease_Rents_Accrued_Report_" + dateStamp + ".xlsx";
		HttpSession session = req.getSession();
		ArrayList<String> strArr = new ArrayList<String>();
		strArr = (ArrayList<String>) session.getAttribute("strArr");
		modArr = updateArray(strArr);
		//Olyutil.printStrArray(strArr);
		/*****************************************************************************************************************************************/
		// Create Excel file on client
		
	 
		//WriteExcel writeExcel = new WriteExcel();
		workbook = OlyExcel.newWorkbook();
		sheet = OlyExcel.newWorkSheet(workbook, "OL_Lease_Rents_Accrued Report");
		OlyExcel.loadHeader(workbook, sheet, headerArr);
		//System.out.println("** Call loadWorkSheet");
		loadWorkSheetCell(workbook, sheet, modArr, 1, ";");
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