package com.olympus.reports.leases;

	import java.io.BufferedWriter;
	import java.io.File;
	import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
	import java.io.IOException;
	import java.io.OutputStream;
	import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
	import java.util.Date;
	import java.util.Map;

	import javax.servlet.ServletException;
	import javax.servlet.annotation.WebServlet;
	import javax.servlet.http.HttpServlet;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.*;
	import org.apache.poi.xssf.streaming.SXSSFSheet;
	import org.apache.poi.xssf.streaming.SXSSFWorkbook;
	import org.apache.poi.xssf.usermodel.XSSFSheet;
	import org.apache.poi.xssf.usermodel.XSSFWorkbook;
	 
	import org.apache.poi.ss.usermodel.CreationHelper;

	import com.olympus.olyexcel.OlyExcel;
	import com.olympus.olyutil.Olyutil;
import com.opencsv.CSVReader;

	 
		@WebServlet("/cdbexcel")
		public class ContractDBExcel  extends HttpServlet {
			
			 public static final char FILE_DELIMITER = ',';
			 public static final String FILE_EXTN = ".xlsx";
			 public static final String FILE_NAME = "EXCEL_DATA";
			 
				/***********************************************************************************************************************************/

			 public static String formatDate(String dateVal ) throws IOException {
					
				 
					String dateMyFormat = "";
					
					if (Olyutil.isNullStr(dateVal)) {
						return("");
					}
			 
					//SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); 
			        //SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
			        
					SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd"); 
			       // SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
			        SimpleDateFormat myFormat = new SimpleDateFormat("M/d/yyyy");
			        
			        

			        try {
			            Date dateFromUser = fromUser.parse(dateVal); // Parse it to the exisitng date pattern and return Date type
			            dateMyFormat = myFormat.format(dateFromUser); // format it to the date pattern you prefer
			            //System.out.println("DF=" + dateMyFormat); // outputs : 2009-05-19

			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					
					
					return(dateMyFormat.trim());
			 
				 
				}
			    
			/***********************************************************************************************************************************/
			public static void setBorder(CellStyle style, Cell cell) {
				
				style.setBorderRight(BorderStyle.THIN);
				style.setRightBorderColor(IndexedColors.BLACK.getIndex());
				style.setBorderBottom(BorderStyle.THIN);
				style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				style.setBorderLeft(BorderStyle.THIN);
				style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				style.setBorderTop(BorderStyle.THIN);
				style.setTopBorderColor(IndexedColors.BLACK.getIndex());	
				cell.setCellStyle(style);
			}
			/***********************************************************************************************************************************/
			// call method:  setWorkSheetFont(style, font, 12, "Times New Roman", true, IndexedColors.BLACK.getIndex());
			public static void setWorkSheetFont(CellStyle style, Font font, int i, String fontType, boolean fontBold, short fontColor) {
				// fontColor ->  IndexedColors.BLACK.getIndex()
				font.setFontHeightInPoints((short) i);
		        font.setFontName(fontType);
		        font.setColor(fontColor);
		        font.setBold(fontBold);
		        style.setFont(font);
		         
			}
			/***********************************************************************************************************************************/
			// call method: setColor(style, cell, IndexedColors.TURQUOISE.getIndex(), FillPatternType.SOLID_FOREGROUND );
			public static void setColor(CellStyle style, Cell cell, short fillColor, FillPatternType fillPattern) {
				style.setFillForegroundColor(fillColor);  
		        style.setFillPattern(fillPattern );  	
		        cell.setCellStyle(style);
			}
			/***********************************************************************************************************************************/
			// call method: setSheetTitle( wbss,  sheet,  styles, "Daily Commencement Report", "$A$1:$P$1" )
			public static void setSheetTitle(SXSSFWorkbook wbss, SXSSFSheet sheet, Map<String, CellStyle> styles, String title, String range) {
				
				Row titleRow = sheet.createRow(0);
		        titleRow.setHeightInPoints(45);
		        Cell titleCell = titleRow.createCell(0);
		        titleCell.setCellValue(title);    
		        titleCell.setCellStyle(styles.get("cell"));    
		        titleCell.setCellStyle(styles.get("title"));
		        //setBorder(style, titleCell);
		        sheet.addMergedRegion(CellRangeAddress.valueOf(range));		
			}
			
			/***********************************************************************************************************************************/

			public static void loadHeader2(SXSSFWorkbook wbss, SXSSFSheet sheet, ArrayList<String> headerArr, Map<String, CellStyle> styles) {
				// System.out.println("************* strArr=" + headerArr.toString());
				int rowNum = 4;
				int colNum = 0;
				CellStyle style = wbss.createCellStyle();
				Font font = wbss.createFont();
	  
		        setSheetTitle( wbss,  sheet,  styles, "Contracts Database Report", "$A$1:$P$1" ); 
		        setWorkSheetFont(style, font, 12, "Times New Roman", true, IndexedColors.BLACK.getIndex());
				Row row = sheet.createRow(rowNum);

				for (Object field : headerArr) {
					Cell cell = row.createCell(colNum);
					if (field instanceof String) {
						cell.setCellValue((String) field);
						//sheet.trackColumnForAutoSizing(colNum);
						//sheet.autoSizeColumn(colNum++); 
						setBorder(style, cell);
						setColor(style, cell, IndexedColors.TURQUOISE.getIndex(), FillPatternType.SOLID_FOREGROUND );
					}
				}	 
			}
			/***********************************************************************************************************************************/
			
			
			public static void loadHeader(SXSSFWorkbook workbook, SXSSFSheet sheet, ArrayList<String> headerArr) {

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
			/*****************************************************************************************************/

			public static void writeToFile(ArrayList<String> strArr, String fileName) throws IOException {
			    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			    
			    for (String str : strArr) { // iterating ArrayList
			    	writer.write(str.replaceAll("null", ""));
			    	writer.newLine();
			    }
			    writer.close(); 
			   
			}
			/***********************************************************************************************************************************/
			public static void writeToFile2(ArrayList<String> strArr, String fileName, String sep) throws IOException {
			    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			    String[] strSplitArr = null;

			    for (String str : strArr) { // iterating ArrayList
			    	if (str.matches("OLA FPP LEASE")) {
			    		continue;
			    	}
			    	strSplitArr = Olyutil.splitStr(str, sep);
			    	for (String token : strSplitArr) {
			    		writer.write(token.replaceAll("null", ""));
			    		
			    	}
			    	writer.newLine();
			    }
			    writer.close(); 
			   
			}
			
			/***********************************************************************************************************************************/
			public static void writeExcelFile(SXSSFWorkbook workbook, HttpServletResponse res, String FILE_NAME ) {
				
				try {
					// HttpServletResponse response = getResponse(); // get ServletResponse
					res.setContentType("application/vnd.ms-excel"); // Set up mime type
					res.addHeader("Content-Disposition", "attachment; filename=" + FILE_NAME);
					OutputStream out = res.getOutputStream();
					
					
					workbook.write(out);
					out.flush();
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
			
			/**
			 * @throws IOException *********************************************************************************************************************************/
			public static void loadWorkSheetCells2(SXSSFWorkbook wbss,  Sheet sheet, ArrayList<String> strArr, int rowNum, String sep) throws IOException {
				//SXSSFSheet sheet = null;
		       // CSVReader reader = null;
		        //'Workbook workBook = null;
				CellStyle style = wbss.createCellStyle();
				CreationHelper createHelper = wbss.getCreationHelper();
		        String generatedXlsFilePath = "";
		        String[] strSplitArr = null;
		        Cell cell = null;
				//workBook = new SXSSFWorkbook();
	           // sheet = (SXSSFSheet) workBook.createSheet("Sheet");
	 
		        Date date1 = null;
	          
				Timestamp timestampS = new Timestamp(System.currentTimeMillis());
		        System.out.println("Begin write tokens:" + timestampS);
		        int i = 0;
				for (String str : strArr) { // iterating ArrayList
					 //System.out.println("*** (" + i++ + ") -- loadWorkSheetCells(): " + str);
					Row row = sheet.createRow(rowNum++);
					strSplitArr = Olyutil.splitStr(str, sep);
					int colNum = 0;
					for (String token : strSplitArr) {
						  cell = row.createCell(colNum);
						 if (i == 1) {
							 System.out.println("Col=" +  colNum + "-- Token="   + token + "--");
						 }
					 
						if (colNum == 15) {		
							//System.out.println("*** T=" + token + "--");
							String nTok = token.replaceAll("\\$", "").replaceAll("/", "." ).replaceAll(",", "");
							//System.out.println("***^^ NT=" +nTok + "--");
							 cell.setCellValue((double) Olyutil.strToDouble(nTok));
						} else if (colNum == 18) {
							 
							 cell.setCellValue((double) Olyutil.strToDouble(token.replaceAll("\\$", "").replaceAll(",", "")));
						} else if (colNum == 6 || colNum == 19 || colNum == 29 || colNum == 30 || colNum == 33 ) { 
							String nDate = formatDate(token);
							//System.out.println("Col=" + colNum + " -- DF=" + nDate);
							//style.setDataFormat(createHelper.createDataFormat().getFormat("MM/dd/yyyy"));
							//style.setDataFormat(createHelper.createDataFormat().getFormat("mm/dd/yyyy"));
							//wbss.createDataFormat().getFormat("dd-mm-yyyy");
							style.setDataFormat(wbss.createDataFormat().getFormat("dd/mm/yyyy"));
						
							cell.setCellStyle(style);
						
							//nDate = nDate.replaceAll("^0+(?=.)", "");
						
							cell.setCellValue(nDate.trim());
							System.out.println("Col=" + colNum + " -- DF=" + nDate + "-- Tok=" + token + "--");
							/*
							if (token instanceof String) {
								cell.setCellValue((String) nDate);
							}			
						*/
						
						
						} else {
							if (token instanceof String) {
								if ( token.equals("null") || Olyutil.isNullStr(token)) {
									 cell.setCellValue((String) "");
								} else {
									 cell.setCellValue((String) token);
								}			
							}
						}
						 token = "";
						 
						colNum++;
					} // End for token			
				}	
			}	
			/***********************************************************************************************************************************/

			// Service method
			@Override
			protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
				String headerFilename = "C:\\Java_Dev\\props\\headers\\contractsDBHdr.txt";
				String outputFileName = "C:\\temp\\contractsDB.txt";
				ArrayList<String> modArr = new ArrayList<String>();
				//XSSFWorkbook workbook = null;
				//XSSFSheet sheet = null;
				ArrayList<String> headerArr = new ArrayList<String>();
				headerArr = Olyutil.readInputFile(headerFilename);
				Date date = Olyutil.getCurrentDate();
				String dateStamp = date.toString();
				// System.out.println("Date=" + dateStamp);
				String fileName= "Contracts_Database_Report_" + dateStamp + ".xlsx";
				HttpSession session = req.getSession();
				ArrayList<String> strArr = new ArrayList<String>();
				strArr = (ArrayList<String>) session.getAttribute("strArr");
				//Olyutil.printStrArray(strArr);
				/*****************************************************************************************************************************************/
				// write to file
				Timestamp timestampWb = new Timestamp(System.currentTimeMillis());
		        //System.out.println("Begin writeFile:" + timestampWb);
				writeToFile(strArr,outputFileName);
				//writeToFile2(strArr,outputFileName, ";");
				Timestamp timestampWe = new Timestamp(System.currentTimeMillis());
		        //System.out.println("End writeFile:" + timestampWe);
				
				// Create Excel file on client
				//XSSFWorkbook wb = null;
		        Workbook wb = null;
				wb = new XSSFWorkbook();
				SXSSFWorkbook wbss = new SXSSFWorkbook();
				SXSSFSheet sheet =   wbss.createSheet("Contract Database Report");	
				Map<String, CellStyle> styles = OlyExcel.createStyles(wbss);
				
				//loadHeader(wbss, sheet, headerArr, styles);
				loadHeader(wbss, sheet, headerArr);
				int rowNum = sheet.getLastRowNum() + 1;
				//System.out.println("Row:" + rowNum);
				Row row = sheet.createRow(rowNum); // start at 6
				Cell cell = row.createCell(0);
				
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		        //System.out.println("Begin Load Cells:" + timestamp);
				loadWorkSheetCells2(wbss, sheet, strArr, 1, ";");
				Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
		       // System.out.println("End Load Cells:" + timestamp2);
				
				
				writeExcelFile(wbss, res, fileName );
				 
				wbss.dispose();
				wbss.close();
				/*****************************************************************************************************************************************/

				//ArrayList<String> strArr = new ArrayList<String>();
				//strArr = (ArrayList<String>) session.getAttribute("strArr");
				//modArr = updateArray(strArr);
				//Olyutil.printStrArray(strArr);
				// Create Excel file on client
				


			}


	}
