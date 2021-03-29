package com.olympus.reports.sapassetupload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Handler;
import java.util.logging.Logger;

 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.olympus.olyutil.Olyutil;
import com.olympus.olyutil.log.OlyLog;

@WebServlet("/xlreadsap")
public class ReadSAPFile extends HttpServlet {
	private final Logger logger = Logger.getLogger(ReadSAPFile.class.getName()); // define logger
	/*****************************************************************************************************************************************************/
	
	
	/*****************************************************************************************************************************************************/
	public static ArrayList<String> readXlsFile(String xlsFileName) throws IOException {
		ArrayList<String> tgtArr = new ArrayList<String>();
		String  strTok = new String ();
		StringBuffer sb = new StringBuffer();
		int numCols = 0;
		try {
			File file = new File(xlsFileName); // creating a new file instance
			FileInputStream fis = new FileInputStream(file); // obtaining bytes from the file
			// creating Workbook instance that refers to .xlsx file
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object
			Iterator<Row> itr = sheet.iterator(); // iterating over excel file
			FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) wb);
			
			
			DataFormatter objDefaultFormat = new DataFormatter();
			System.out.println("SheetName " + sheet.getSheetName());
			
			// This will find empty cells
			int j = 1;
			for(Row row : sheet) {
				   for(int cn=0; cn<row.getLastCellNum(); cn++) { 
					   if (cn > 6 &&  cn < 18) { // Skip Cols H thru R
						   continue;	   
					   }
				       // If the cell is missing from the file, generate a blank one
				       // (Works by specifying a MissingCellPolicy)
				       Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
				       if (cell.toString().isEmpty() || cell.toString().equals("") ) {
				    	  //System.out.println("** Cell MT=" + cn + "--");
				    	   strTok += " ";
				       } else {
				    	   strTok += cell.toString();
				       }
				       
				       
				       
				       
				      
				       if (cn < row.getLastCellNum()) {
				    	   strTok += "^";
				       }
				       // Print the cell for debugging
				       /*
				       if (j > 2 &&  j < 4) {
						       if (cell.toString().isEmpty() || cell.toString().equals("") ) {
						    	   System.out.println("** Cell MT=" + cn + "--");
						       }
				             //System.out.println("CELL: " + cn + " --> " + cell.toString());     
				        }
				        */
				   } // end inner for
				   tgtArr.add(strTok);
				   strTok = "";
				   
				   j++;
				} // end outer for
			 
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (tgtArr);

}
	/*****************************************************************************************************************************************************/
	 
		// Service method
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
			String fileName = "C:\\Java_Dev\\props\\SAP_Upload\\SAP_Asset_Upload\\SAP.xlsx";
			String sep = "\\^";
			String[] strSplit = null;
			String[] ss = null;
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");  
			ArrayList<String> strArr = new ArrayList<String>();

			LocalDateTime now = null;
	 
			String logFileName = "sapDataUpload.log";
			String directoryName = "D:/Kettle/logfiles/sapDataUpload";
			Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
			strArr = readXlsFile(fileName);
			//System.out.println("arrSZ:" + strArr.size());
			Olyutil.printStrArray(strArr);
		}
	/*****************************************************************************************************************************************************/

}
