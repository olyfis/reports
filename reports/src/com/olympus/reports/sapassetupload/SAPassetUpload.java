package com.olympus.reports.sapassetupload;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.olympus.olyutil.Olyutil;
import com.olympus.olyutil.log.OlyLog;
import com.olympus.reports.exceltest.SheetHandler;
import com.olympus.reports.masterlist.MasterList;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.binary.XSSFBSharedStringsTable;
import org.apache.poi.xssf.binary.XSSFBSheetHandler;
import org.apache.poi.xssf.binary.XSSFBStylesTable;
import org.apache.poi.xssf.eventusermodel.XSSFBReader;
import org.apache.poi.xssf.extractor.XSSFBEventBasedExcelExtractor;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.XmlException;
import org.xml.sax.SAXException;

 
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@WebServlet("/sapassetupt")
public class SAPassetUpload extends HttpServlet {
	
	private final Logger logger = Logger.getLogger(SAPassetUpload.class.getName()); // define logger
	/*****************************************************************************************************************************************************/
	public static void printStrArray_2(ArrayList<String> strArr, String tag) {
		 int i = 0;
		for (String str : strArr) { // iterating ArrayList
			 if ( i > 172321 && i < 173340) {
				
			//if ( i > 86420 && i < 86440) {	
				System.out.println(tag + str);
				
			 }
			 i++;
			
		}
		// System.out.println(names[index]);
	}
	/*****************************************************************************************************************************************************/
	public static ArrayList<String> readXlsbFile(String xlsbFileName) throws IOException {
		ArrayList<String> tgtArr = new ArrayList<String>();
       
        ExcelSheetHandler2  exlSheetHandler = new ExcelSheetHandler2();       
        OPCPackage pkg;
        XSSFEventBasedExcelExtractor ext = null;
        try {
            pkg = OPCPackage.open(xlsbFileName);
            
           
            /*****************************************************************************************************/
            
            XSSFBReader r = new XSSFBReader(pkg);
            XSSFBSharedStringsTable sst = new XSSFBSharedStringsTable(pkg);
            XSSFBStylesTable xssfbStylesTable = r.getXSSFBStylesTable();
            XSSFBReader.SheetIterator it = (XSSFBReader.SheetIterator) r.getSheetsData();
 
            List<String> sheetTexts = new ArrayList<>();
            InputStream is = null;
            
            while (it.hasNext()) {
                is = it.next();
                String name = it.getSheetName();
                
                 
                if (! name.equals("Asset Upload")) {
                	continue;
                }
                
          
				// SheetHandler exlSheetHandler = new SheetHandler();
				exlSheetHandler.startSheet(name);
				XSSFBSheetHandler sheetHandler = new XSSFBSheetHandler(is, xssfbStylesTable, it.getXSSFBSheetComments(),
						sst, exlSheetHandler, new DataFormatter(), false);

				sheetHandler.parse();
				exlSheetHandler.endSheet();

				// sheetTexts.add(exlSheetHandler.toString());

				System.out.println("ST:" + sheetTexts.size());
				System.out.println("STL:" + sheetTexts.add(exlSheetHandler.toString()));
                
                /*-----------------------------------------------------------------------------------------------------------*/

            }
            
			// System.out.println("output text:"+sheetTexts);
/*
			String[] arr = exlSheetHandler.toString().split("\n");
			//System.out.println("arrSZ:" + arr.length);
			int j = 0;
			for (String s : arr) {
				tgtArr.add(s);
				if (j > 86438 && j < 86435) {
					System.out.println("********" + s +" ");
				}
				
				j++;
			}
			
	*/		
        	String[] arr = exlSheetHandler.toString().split("\n");
            
           // System.out.println("arrSZ:" + arr.length);
            
            
            for(String s: arr) {
            	tgtArr.add(s);
                 //System.out.println("**" + s+" ");
            }
            
            
            

		 if (is != null) {
			 is.close();
		 }
		
			
			
        } catch (InvalidFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OpenXML4JException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
		
		return(tgtArr);
	}
	/*****************************************************************************************************************************************************/

	 
	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String xlsbFileName = "C:\\Java_Dev\\props\\SAP_Upload\\SAP_Asset_Upload\\sap_AU.xlsb";
		String sep = "\\^";
		String[] strSplit = null;
		String[] ss = null;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");  
		ArrayList<String> strArr = new ArrayList<String>();

		LocalDateTime now = null;
 
		String logFileName = "sapAssetUpload.log";
		String directoryName = "D:/Kettle/logfiles/sapAssetUpload";
		Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
		strArr = readXlsbFile(xlsbFileName);
		
		// 101-0010622-002
		//String tgtID = "101-0010622-002";
		String tgtID = "101-0010095-033";
		//System.out.println("arrSZ:" + strArr.size());
		 //Olyutil.printStrArray(strArr);
//printStrArray_2(strArr, ("***"));
		
		
		int i=0;
		 //System.out.println("S=" + strArr.size());
		for (String str : strArr) {
			
			String[] result = str.split("\\^");
			//strSplit = splitStr(str, sep);
			 	
			 // System.out.println("SZ=" + str.length() + "-- i=" + i + "-- " + str);	
			
			if (i > 1 && i < strArr.size()) {
				
				//System.out.println("ID=" + tgtID + "-- SZ=" + result.length + "-- i=" + i + "-- Cell[3]=" + result[3]);	
				  
				/* Save for debug
				if (i > 86425 && i < 86440) {
					  System.out.println("ID=" + tgtID + "-- SZ=" + result.length + "-- i=" + i + "-- str=" + str);	
	
				}
				
				*/
				//System.out.println("ID=" + tgtID + "-- SZ=" + result.length + "-- i=" + i + "-- Cell[3]=" + result[3]);	
				
				
				
				
				//ss = splitStr(strSplit[0], sep);
				//System.out.println("SZ=" + strSplit.length + "-- ssSZ=" + ss.length);
				
	  
				//System.out.println(Arrays.toString(strSplit));
				//System.out.println("ss=" + strSplit[0] + "-- ssSZ=" + strSplit.length);
				  //
				
				//System.out.println("ID=" + tgtID + "-- SZ=" + result.length + "-- i=" + i + "-- " + str);	
				
				if ( result.length > 4) {
					if (result[3].equals(tgtID)) {
						 //System.out.println("** i=" + i);
						  System.out.println("SZ=" + result.length + "-- i=" + i + "-- " + result[3]);			
					 } 
				} else {
					 //System.out.println("***^^^*** SZ=" + result.length + "-- i=" + i + "-- ");
				 }
				 
				 
			}
			i++;
			
			//System.out.println("S=" + strSplit[3] );		
			//System.out.println("S=" + strSplit.length );	
			//
			
		} // End for
		
	
		
		System.out.println("** End:");
	}
	
	
	
	
	
	
	
	
	
	
	

}
