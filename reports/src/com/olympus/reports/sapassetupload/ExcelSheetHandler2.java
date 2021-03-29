package com.olympus.reports.sapassetupload;

import java.util.ArrayList;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

public class ExcelSheetHandler2 implements XSSFSheetXMLHandler.SheetContentsHandler  {
private final StringBuilder sb = new StringBuilder();
private final StringBuilder line = new StringBuilder();
    
private int rowNumber;

public void startSheet(String sheetName) {
    sb.append("<sheet name=\"").append(sheetName).append(">");
}

public void endSheet() {
    sb.append("");
}

@Override
public void startRow(int rowNum) {
    //sb.append("\n Bnum=\"").append(rowNum).append("^");
    sb.append("\n").append(rowNum +1 ).append("^");
    rowNumber = rowNum +1 ;
    //values = new LinkedHashMap<>();
}

@Override
public void endRow(int rowNum) {
    //sb.append("^ Enum=\"").append(rowNum).append("^");
   // sb.append("^ Enum=\"").append(rowNum).append("^");
    
}

@Override
public void cell(String cellReference, String formattedValue, XSSFComment comment) {
    formattedValue = (formattedValue == null || formattedValue.equals("")) ? "--N/A" : formattedValue;
    
    System.out.println("** Row=" + rowNumber + "**CELL:" +  
    cellReference.substring(0, 2) + "-- R=" + cellReference + "-- CA=" + cellReference.charAt(0)); 
    //System.out.println("val=" + formattedValue);
    
    if (comment == null) {
        //sb.append("\n\t<td ref=\"").append(cellReference).append("\">").append(formattedValue).append("</td>");
        
        //sb.append("^\"").append(cellReference).append("\"^").append(formattedValue).append("^");
    	
    	//sb.append("").append(formattedValue).append("^");
    	Integer maxSize = 120;
    	if(formattedValue.length() > maxSize ){
    		formattedValue = formattedValue.substring(0, maxSize);
    	}
    	if(formattedValue.length() <= 0 ||  formattedValue.equals("")    || formattedValue == null){
    		System.out.println("**** FV Null --" + formattedValue);
    		formattedValue = "-N/A";
    	}
    	/***********************************************************************************************************************/
    	
    	//XSSFCell cell = (XSSFCell) row.getCell( index );
    	if (cellReference.isEmpty() ) {
    		System.out.println("**** FV Null --");
    	}
    	
    	if (cellReference.equals("B172626")) {
    		System.out.println("**** FV --" + formattedValue);
    	}
    	/***********************************************************************************************************************/

    	
    	sb.append(cellReference).append("%").append(formattedValue).append("^"); // add cell reference -- lineNo_Col
    	
    	
    	
    	//System.out.println("**** FV=" + formattedValue);
        if (formattedValue.equals("101-0010622-002")) {
        	 
        	//System.out.println("**** ID=" + formattedValue);
         }
        
        
    } else {
    	if(formattedValue.length() <= 0 ||  formattedValue.equals("")    || formattedValue == null){
    		System.out.println("**** FV Null --" + formattedValue);
    		formattedValue = "-N/A";
    	}
    	/*
        sb.append("\n\t<td ref=\"").append(cellReference).append("\">")
                .append(formattedValue)
                .append("<span type=\"comment\" author=\"")
                .append(comment.getAuthor()).append("\">")
                .append(comment.getString().toString().trim()).append("</span>")
                .append("</td>");
                
                
        */
    	//System.out.println("**** Comment != null -- Row="  );
        
    	sb.append("^\"").append(cellReference).append("\"^")
        .append(formattedValue)
        .append("<span type=\"comment\" author=\"")
        .append(comment.getAuthor()).append("\">")
        .append(comment.getString().toString().trim()).append("</span>")
        .append("</td>");
        
    }
}

@Override
public void headerFooter(String text, boolean isHeader, String tagName) {
	/*
    if (isHeader) {
    	 
        sb.append("<header tagName=\"").append(tagName).append("\">").append(text).append("</header>");
    } else {
        sb.append("<footer tagName=\"").append(tagName).append("\">").append(text).append("</footer>");

    }
    */
}

@Override
public String toString() {
    return sb.toString();
}
 
}
