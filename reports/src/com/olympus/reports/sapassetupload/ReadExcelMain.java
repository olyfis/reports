package com.olympus.reports.sapassetupload;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.binary.XSSFBSharedStringsTable;
import org.apache.poi.xssf.binary.XSSFBSheetHandler;
import org.apache.poi.xssf.binary.XSSFBStylesTable;
import org.apache.poi.xssf.eventusermodel.XSSFBReader;
import org.apache.poi.xssf.extractor.XSSFBEventBasedExcelExtractor;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.xmlbeans.XmlException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadExcelMain {
	
	public static void main (String [] args){

        String xlsbFileName = "C:\\TEMP\\SAP_Asset_Upload\\sap_AU.xlsb";

        OPCPackage pkg;
        XSSFEventBasedExcelExtractor ext = null;
        try {
            pkg = OPCPackage.open(xlsbFileName);
            XSSFBReader r = new XSSFBReader(pkg);
            XSSFBSharedStringsTable sst = new XSSFBSharedStringsTable(pkg);
            XSSFBStylesTable xssfbStylesTable = r.getXSSFBStylesTable();
            XSSFBReader.SheetIterator it = (XSSFBReader.SheetIterator) r.getSheetsData();

            List<String> sheetTexts = new ArrayList<>();
            while (it.hasNext()) {
                InputStream is = it.next();
                String name = it.getSheetName();
                 
                if (! name.equals("Asset Upload")) {
                	continue;
                }
            
                
                
                
                TestSheetHandler testSheetHandler = new TestSheetHandler();
                
            
                
                testSheetHandler.startSheet(name);
                XSSFBSheetHandler sheetHandler = new XSSFBSheetHandler(is,
                        xssfbStylesTable,
                        it.getXSSFBSheetComments(),
                        sst, testSheetHandler,
                        new DataFormatter(),
                        false);
                
                
             
                
               sheetHandler.parse();
                testSheetHandler.endSheet();
                sheetTexts.add(testSheetHandler.toString());
                
               
            }
            System.out.println("output text:"+sheetTexts);

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
    }
}

