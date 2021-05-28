package com.olympus.reports.techupgradeassets;

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

import java.io.File;
import java.util.TreeMap;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
import com.olympus.olyutil.Olyutil;
 


@WebServlet("/tuaexcel2")
public class WriteExcel extends HttpServlet {
	
/****************************************************************************************************************************************************/
	
	 
	public static void doWriteData(XSSFWorkbook workbook, String tab, ArrayList<String> sArr,   String dateStamp, String sep ) throws IOException {
		
		 
		if (sArr.size() > 0) {	
			//System.out.println("*** arrSZ=" + sArr.size());
			//Olyutil.printStrArray(sArr);
			int row = 0;
			for (String s : sArr) {	
				String[] items = s.split(sep);	
				//System.out.println("*** SZ=" + items.length + "-- " + s );
				//System.out.print("*** Row=" + row + "-- ");
				for (int i = 0; i < items.length; i++ ) { // i = col
					//System.out.print("Item=" + items[i] + "-- ");
					
					
					
				}
				row++;
				//System.out.println(); // part of debug code above
			}
		} else { // error no data
			
		}
		

	}
	
	/****************************************************************************************************************************************************/

	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession();
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		String sep = ";";
		ArrayList<String> headerArr = new ArrayList<String>();
		ArrayList<String> strArr = new ArrayList<String>();
		
		
		// String contractHeaderFile =
		// "C:\\Java_Dev\\props\\headers\\NBVA\\NBVA_ContractHrd.txt";
		String headerFile = "C:\\Java_Dev\\props\\headers\\techAssetHdr.txt";
		// String ageFile = "C:\\Java_Dev\\props\\nbvabuy\\dailyAge.csv";
		String FILE_NAME = "TechUpgradeAssets_Report_" + dateStamp + ".xlsx";
		String excelTemplate = "C:\\Java_Dev\\props\\excel\\templates\\tua_template.xlsx";
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		strArr = (ArrayList<String>) session.getAttribute("strArr");
		
		// displayDataMapStr( invDateMapDB, "From database");
		//System.out.println("** Call Read Header");
		headerArr = Olyutil.readInputFile(headerFile);
		//Olyutil.printStrArray(headerArr);
		 
		
		workbook = new XSSFWorkbook(new FileInputStream(excelTemplate));
		FileOutputStream fileOut = new FileOutputStream(FILE_NAME);
		String excelTemplateNew = "TechUpgradeAssets_" + dateStamp + ".xlsx";
		//doWriteData(workbook, "TUA", strArr, dateStamp, sep);
		
		// Create Excel file on client
		
		 
			WriteExcelFile writeExcel = new WriteExcelFile();
			workbook = writeExcel.newWorkbook();
			sheet = writeExcel.newWorkSheet(workbook, "Tech Upgrade Assets");
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
 
