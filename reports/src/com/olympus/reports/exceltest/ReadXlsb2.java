package com.olympus.reports.exceltest;

import java.io.File;
import java.nio.file.Paths;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.extractor.XSSFBEventBasedExcelExtractor;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;

public class ReadXlsb2 {

	public static void main(String[] args) {
		String xlsbFileName = "C:\\TEMP\\SAP_Asset_Upload\\sap_AU.xlsb";
		
		File file = null;
	    OPCPackage pkg = null;      
	    XSSFEventBasedExcelExtractor ext = null;
		
		try {           

	        file = Paths.get(xlsbFileName).toFile();          
	        pkg = OPCPackage.open(file,PackageAccess.READ);
	        ZipSecureFile.setMaxTextSize(110485766);
	        ext = new XSSFBEventBasedExcelExtractor(pkg);  
	         System.out.println(ext.getText());

	        }   catch(Exception ex) {
	            System.out.println(ex.getMessage());
	        }

	}

}
