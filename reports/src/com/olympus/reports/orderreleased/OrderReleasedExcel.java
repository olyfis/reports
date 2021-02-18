package com.olympus.reports.orderreleased;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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

// Run: http://localhost:8181/reports/orderrel?startDate=2021-02-01&endDate=2021-02-10

@WebServlet("/orexcel")
public class OrderReleasedExcel  extends HttpServlet {
 

/***********************************************************************************************************************************/

 public static String formatDate(String dateVal ) throws IOException {
		
	 
		String dateMyFormat = "";
		
		if (Olyutil.isNullStr(dateVal)) {
			return("");
		}
 
		//SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); 
        //SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        
		SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); 
        SimpleDateFormat myFormat = new SimpleDateFormat("M/d/yyyy HH:mm:ss a");
        

        try {
            Date dateFromUser = fromUser.parse(dateVal); // Parse it to the exisitng date pattern and return Date type
            dateMyFormat = myFormat.format(dateFromUser); // format it to the date pattern you prefer
            //System.out.println("DF=" + dateMyFormat); // outputs : 2009-05-19

        } catch (Exception e) {
            e.printStackTrace();
        }
		
		
		
		return(dateMyFormat);
 
	 
	}
	/**
	 **********************************************************************************************************************************/
	public static void loadWorkSheetCell(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> strArr, int rowNum, String sep) throws IOException {
		String[] strSplitArr = null;
		long assetID = 0;
		double equipCost = 0.0;
		double assetRes = 0.0;
		double accountRes = 0.0;
		double cogs = 0.0;
		String sn = "";
		
		ArrayList<String> strArr2 = new ArrayList<String>();
		String[] splitArr = null;
		String newStr = "";
		
		for (String str : strArr) {
			int i = 0;
			splitArr = Olyutil.splitStr(str, sep);	
			//System.out.println("*****^^^^ SZ=" +   splitArr.length   +   "******** Str=" +str);
			for (String tk : splitArr) {
				if ( i != 14) {
					newStr += tk + ";";
				}
				
				i++;
			}
			//System.out.println("************* newStr=" + newStr);
			strArr2.add(newStr);
			newStr = "";
			
		}
		
	 
		//System.out.println("************* strArr=" + strArr.toString());
		int j = 0;
		for (String str : strArr2) { // iterating ArrayList
				
			Row row = sheet.createRow(rowNum++);
			strSplitArr = Olyutil.splitStr(str, sep);	
			int colNum = 0;
			for (String token : strSplitArr) {
				Cell cell = row.createCell(colNum);
		
				if (colNum == 3 ) {
					//System.out.println("Col=" + colNum + " -- TK=" + token);
					String nDate = formatDate(token);
					//System.out.println("**** Formatted -- Col=" + colNum + " -- DF=" + nDate);
					if (token instanceof String) {
						cell.setCellValue((String) nDate);
					}	
										
					
				}	else {			
					if (token instanceof String) {
						cell.setCellValue((String) token.replaceAll("null", ""));
					}
				}
				colNum++;
			 token = "";
				
			}
		}
	}
	
	/***********************************************************************************************************************************/
	
	/*****************************************************************************************************************************************/

	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String headerFilenameBRSummary = "C:\\Java_Dev\\props\\headers\\OrdRelAppHdr2.txt";
		
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFilenameBRSummary);
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		// System.out.println("Date=" + dateStamp);
		String FILE_NAME = "OrderReleasedReport_" + dateStamp + ".xlsx";
		HttpSession session = req.getSession();
		ArrayList<String> strArr = new ArrayList<String>();
		strArr = (ArrayList<String>) session.getAttribute("strArrMod");
		
		//Olyutil.printStrArray(strArr);
		/*****************************************************************************************************************************************/
		// Create Excel file on client
		
	 
		//WriteExcel writeExcel = new WriteExcel();
		workbook = OlyExcel.newWorkbook();
		sheet = OlyExcel.newWorkSheet(workbook, "Order Released Report");
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
