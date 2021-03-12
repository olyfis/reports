package com.olympus.reports.masterlist;

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

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.olympus.olyutil.Olyutil;
import com.olympus.reports.evergreen.WriteExcel;

@WebServlet("/mlexcel")
public class MasterListExcel extends HttpServlet {
	
	/*****************************************************************************************************/
	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String headerFilenameBRSummary = "C:\\Java_Dev\\props\\headers\\masterListHdr.txt";
		
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFilenameBRSummary);
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		// System.out.println("Date=" + dateStamp);
		String FILE_NAME = "MasterListReport_" + dateStamp + ".xlsx";
		HttpSession session = req.getSession();
		ArrayList<String> strArr = new ArrayList<String>();
		strArr = (ArrayList<String>) session.getAttribute("strArr");
		
		//Olyutil.printStrArray(strArr);
		/*****************************************************************************************************************************************/
		// Create Excel file on client
		
	 
		WriteExcel writeExcel = new WriteExcel();
		workbook = writeExcel.newWorkbook();
		sheet = writeExcel.newWorkSheet(workbook, "MasterList Report");
		writeExcel.loadHeader(workbook, sheet, headerArr);
		//System.out.println("** Call loadWorkSheet");
		writeExcel.loadWorkSheet(workbook, sheet, strArr, 1, ";");
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
