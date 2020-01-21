package com.olympus.reports.leases;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.olympus.olyexcel.OlyExcel;
import com.olympus.olyutil.Olyutil;

@WebServlet("/commencementwrite")
public class DailyCommencementWriteFile  extends HttpServlet {
	/***********************************************************************************************************************************/
	 public static String formatDate(String dateVal ) throws IOException {
			
			String dateMyFormat = "";
			if (Olyutil.isNullStr(dateVal)) {
				return("");
			}
			//SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); 
	        //SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
	        
			SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd"); 
	        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
	        
	        try {
	            Date dateFromUser = fromUser.parse(dateVal); // Parse it to the exisitng date pattern and return Date type
	            dateMyFormat = myFormat.format(dateFromUser); // format it to the date pattern you prefer
	            //System.out.println("DF=" + dateMyFormat); // outputs : 2009-05-19

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return(dateMyFormat);	 
		}
	
	/***********************************************************************************************************************************/

	
	public static void loadWorkSheetCells(SXSSFWorkbook wbss,  Sheet sheet, ArrayList<String> strArr, int rowNum, String sep) throws IOException {
		String[] strSplitArr = null;
		long assetID = 0;
		double equipCost = 0.0;
		CellStyle style = wbss.createCellStyle();
		Cell cell = null;
		Cell cell2 = null;
		Cell cell3 = null;
		int colNum = 0;
		
		for (String str : strArr) { // iterating ArrayList
			
			strSplitArr = Olyutil.splitStr(str, sep);
			Row row = sheet.createRow(rowNum++);
			colNum = 0;
			for (String token : strSplitArr) {
				
				 cell = row.createCell(colNum);
				//cell.setCellValue((String) token );
				
				//System.out.print(token + "--");
				if (colNum == 6) {						
					 cell.setCellValue((long) Olyutil.strToLong(token));
				} else if (colNum == 10) {
					 cell.setCellValue((double) Olyutil.strToDouble(token));
				} else if (colNum == 3 || colNum == 4) {
					String nDate = formatDate(token);
					//System.out.println("Col=" + colNum + " -- DF=" + nDate);
					if (token instanceof String) {
						cell.setCellValue((String) nDate);
					}	
				
				
				} else {
					if (token instanceof String) {
						if ( token.equals("null") || Olyutil.isNullStr(token)) {
							 cell.setCellValue((String) "");
						} else {
							 cell.setCellValue((String) token);
						}			
					}
				}
				 
				colNum++;
				
			}
			//colNum = 0;
			

		}
		
	}
	
	/***********************************************************************************************************************************/
	
	public static void loadWorkSheetCells_OLD(SXSSFWorkbook wbss,  Sheet sheet, ArrayList<String> strArr, int rowNum, String sep) {
		String[] strSplitArr = null;
		long assetID = 0;
		double equipCost = 0.0;
		CellStyle style = wbss.createCellStyle();
		Cell cell = null;
		int j = 0;
		for (String str : strArr) { // iterating ArrayList
			 //System.out.println("Row: " + j + " -- " + str);
			if ( j > 5000) {
				return;
			}
			Row row = sheet.createRow(rowNum++);
			strSplitArr = Olyutil.splitStr(str, sep);
			int colNum = 0;
			for (String token : strSplitArr) {
				  cell = row.createCell(colNum);
				//System.out.print(token + "--");
				if (colNum == 6) {						
					 cell.setCellValue((long) Olyutil.strToLong(token));
				} else if (colNum == 10) {
					 cell.setCellValue((double) Olyutil.strToDouble(token));
				} else {
					if (token instanceof String) {
						if ( token.equals("null") || Olyutil.isNullStr(token)) {
							 cell.setCellValue((String) "");
						} else {
							 cell.setCellValue((String) token);
						}			
					}
				}
				 setBorder(style, cell);
				colNum++;
			}
			j++;
			//System.out.println();
		}
		
	}
	/***********************************************************************************************************************************/
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
	public static void loadHeader(SXSSFWorkbook wbss, SXSSFSheet sheet, ArrayList<String> headerArr, Map<String, CellStyle> styles) {
		// System.out.println("************* strArr=" + headerArr.toString());
		int rowNum = 4;
		int colNum = 0;
		CellStyle style = wbss.createCellStyle();
		Font font = wbss.createFont();

        setSheetTitle( wbss,  sheet,  styles, "Daily Commencement Report", "$A$1:$P$1" ); 
        setWorkSheetFont(style, font, 12, "Times New Roman", true, IndexedColors.BLACK.getIndex());
		Row row = sheet.createRow(rowNum);

		for (Object field : headerArr) {
			Cell cell = row.createCell(colNum);
			if (field instanceof String) {
				//System.out.println("*** Writing Header: " + field);
				cell.setCellValue((String) field);
				 //sheet.trackColumnForAutoSizing(colNum); // Takes too much time to run on large data
				  //sheet.autoSizeColumn(colNum); 
				setBorder(style, cell);
				setColor(style, cell, IndexedColors.TURQUOISE.getIndex(), FillPatternType.SOLID_FOREGROUND );
			}
			colNum++;
		}	 
	}
	 /***********************************************************************************************************************************/

	
	/***********************************************************************************************************************************/

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
		OutputStream out = null;
		
		try {
			// HttpServletResponse response = getResponse(); // get ServletResponse
			res.setContentType("application/vnd.ms-excel"); // Set up mime type
			res.addHeader("Content-Disposition", "attachment; filename=" + FILE_NAME);
			  out = res.getOutputStream();
			workbook.write(out);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
				out.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}	
	/***********************************************************************************************************************************/

	public static void writeFileToCsv( HttpServletResponse response, String fileName,  ArrayList<String> strArr, ArrayList<String> hdrArr) throws IOException {
		//ServletOutputStream out = response.getOutputStream();
		
		String mimeType = "application/text/plain";		
		response.setContentType(mimeType); // Set up mime type
		response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
		OutputStream out = response.getOutputStream();
		Writer writer = new OutputStreamWriter(out,"UTF-8") ;
		int hdrSZ = hdrArr.size();
		int i = 1;
		for (String data : hdrArr) {
			//System.out.print("I=" + i  );
			writer.write(data );
			if (i++ < hdrSZ) {
				writer.write(";");
			}	 
		}
		 writer.write("\n");
		
		//System.out.println();
		for (String data : strArr) {
			String data2 = data.replaceAll("null", "");
	        writer.write(data2);
	        writer.write("\n");
		}
		writer.flush();
		writer.close();
		out.close();
	}
	/***********************************************************************************************************************************/
	public static JSONArray createJsonArray( HttpServletResponse response, String fileName,  ArrayList<String> strArr, ArrayList<String> hdrArr, String sep) throws IOException {
		
		// ServletOutputStream out = response.getOutputStream();
		String[] strSplitArr = null;
	 
		JSONArray jsonArr = new JSONArray();

		if (strArr.size() > 0) {
			for (String data : strArr) {
				// String data2 = data.replaceAll("null", "");
				strSplitArr = Olyutil.splitStr(data, sep);
				int k = 0;
				JSONObject cellObj = new JSONObject();
				for (String token : strSplitArr) {
					int l = strSplitArr.length;
					String token2 = token.replaceAll("null", "");
					String hdr = hdrArr.get(k++).replaceAll(" ", "_");
					cellObj.put(hdr, token2);
					// System.out.println("SZ=" + l + "ID=" + strSplitArr[0] );			
				}
				jsonArr.put(cellObj);
			}
		}

		return (jsonArr);
	}
	/***********************************************************************************************************************************/
	public static void writeFileToJson( HttpServletResponse response, JSONArray jsonArr ) throws IOException {
		PrintWriter out = response.getWriter();
     	response.setContentType("application/json");
     	response.setCharacterEncoding("UTF-8");
        
        for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject row = jsonArr.getJSONObject(i);

			if (row instanceof JSONObject) {
				Set<String> keys = ((JSONObject) row).keySet();

				// System.out.println("%%%%%%%%% KEYS %%%%%%%%" + keys.toString() + "keyNum=" +
				// keys.size());
				if (i == 0) {
					out.write("[");
				}
				int k = 0;
				for (String key : keys) {
					// System.out.println(key + ":" + jsonObject.get(key));
					//System.out.println("*******Key: " + key + " -> " + ((JSONObject) row).get(key));
				
					if (k == (jsonArr.length() - 1)) {
						out.write(jsonArr.getJSONObject(k).toString());
					} else {
						out.write(jsonArr.getJSONObject(k).toString() + ",");
					}
					//System.out.println( jsonArr.getJSONObject(k).toString() );
					
				 k++;
				}
				out.write(",");
				
			}
			out.write("]");
			out.println();
        }
	}
	/***********************************************************************************************************************************/
	public static void displayJsonArray(JSONArray jArr) {

		for (int i = 0; i < jArr.length(); i++) {
			JSONObject row = jArr.getJSONObject(i);

			if (row instanceof JSONObject) {
				Set<String> keys = ((JSONObject) row).keySet();

				// System.out.println("%%%%%%%%% KEYS %%%%%%%%" + keys.toString() + "keyNum=" +
				// keys.size());
				for (String key : keys) {
					// System.out.println(key + ":" + jsonObject.get(key));
					System.out.println("*******Key: " + key + " -> " + ((JSONObject) row).get(key));
				}
			}
		}
	}
	/***********************************************************************************************************************************/

	
	/***********************************************************************************************************************************/

	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HashMap<String, Object> status = new HashMap<>();
		String typeCsv = "";
		String typeExcel = "";
		JSONArray jsonArrRtn = new JSONArray();
		
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		String headerFilename  = "C:\\Java_Dev\\props\\headers\\commencementHdr.txt";
		String outputFileName = "C:/temp/commencementData.txt";
		String jsonOutputFileName = "C:/temp/commencementDataJson.json";
		
		String fileName= "Daily_Commencemment_Report_" + dateStamp + ".xlsx";
		
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFilename );
		
		// System.out.println("Date=" + dateStamp);
		
		HttpSession session = req.getSession();
		ArrayList<String> strArr = new ArrayList<String>();
		strArr = (ArrayList<String>) session.getAttribute("strArr");
		String paramName = "csv";
		String paramValue = req.getParameter(paramName);
		String paramName2 = "excel";
		String paramValue2 = req.getParameter(paramName2);
		
		//String v = (String) session.getAttribute("typeval");
		
		if ((paramValue != null && !paramValue.isEmpty())) {	
			 typeCsv= paramValue.trim();
			 //System.out.println("*** typeVal:" + typeCsv + "--");			 
		} 
		if ((paramValue2 != null && !paramValue2.isEmpty())) {	
			 typeExcel= paramValue2.trim();
			 //System.out.println("*** typeVal2:" + typeExcel + "--");			 
		} 
		
		//Olyutil.printStrArray(strArr);
		/*****************************************************************************************************************************************/
		// write to file
		
      
		//writeToFile(strArr,outputFileName); // write text file
		if (typeCsv.equals("csv")) {
			writeFileToCsv(res, outputFileName, strArr, headerArr); // write CSV
		} else if (typeExcel.equals("excel")) {
			Timestamp timestampExb = new Timestamp(System.currentTimeMillis());
			//System.out.println("Begin writeExcelFile:" + timestampExb);
			// Create Excel file on client
			XSSFWorkbook wb = null;
			wb = new XSSFWorkbook();
			SXSSFWorkbook wbss = new SXSSFWorkbook(wb, 500);
			SXSSFSheet sheet = wbss.createSheet("Daily Commencement Report");
			Map<String, CellStyle> styles = OlyExcel.createStyles(wbss);
			loadHeader(wbss, sheet, headerArr, styles);
			int rowNum = sheet.getLastRowNum() + 1;
			//System.out.println("Next Avail Row:" + rowNum);
			 
			Row row = sheet.createRow(rowNum); // start at 6
			Cell cell = row.createCell(0);
			
			Timestamp timestampLCB = new Timestamp(System.currentTimeMillis());
	        
			loadWorkSheetCells(wbss, sheet, strArr, 6, ";");
			//System.out.println("Begin Load Cells:" + timestampLCB);
			Timestamp timestampLCE = new Timestamp(System.currentTimeMillis());
	       // System.out.println("End Load Cells:" + timestampLCE);
	       
			writeExcelFile(wbss, res, fileName);

			wbss.dispose();
			wbss.close();
			Timestamp timestampExe = new Timestamp(System.currentTimeMillis());
			//System.out.println("End writeExcelFile:" + timestampExe);
		}
        
        //jsonArrRtn = createJsonArray(res, jsonOutputFileName, strArr, headerArr, ";");
       // writeFileToJson(res, jsonArrRtn ); // write JSON file
        
        //displayJsonArray(jsonArrRtn);
		//writeToFile2(strArr,outputFileName, ";");
        //System.out.println("Begin writeFile:" + timestampWb);
		//Timestamp timestampWe = new Timestamp(System.currentTimeMillis());
        //System.out.println("End writeFile:" + timestampWe);
	}

}
