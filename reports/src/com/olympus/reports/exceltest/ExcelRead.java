package com.olympus.reports.exceltest;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelRead {

	/********************************************************************************************************************************************/
	public static void readXlsbFile(String xlsbFileName, ArrayList data ) throws IOException {
		
		ArrayList<ArrayList<String>> cellArrayListHolder = new ArrayList<ArrayList<String>>();
		FileInputStream excelFile = new FileInputStream(new File(xlsbFileName));

		Workbook workbook = new XSSFWorkbook(excelFile);
		Sheet datatypeSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = datatypeSheet.iterator();
		 
		   int j = 1;
		   System.out.println("SheetName " + datatypeSheet.getSheetName());
		   
		   datatypeSheet.getRow(3).getPhysicalNumberOfCells();
		   System.out.println("SheetName " + datatypeSheet.getSheetName());
		   
		   
		   
		while (iterator.hasNext()) {
		    ArrayList<String> cellStoreArrayList = new ArrayList<String>();
		    Row currentRow = iterator.next();
		    Iterator<Cell> cellIterator = currentRow.iterator();
		    int column_counting = 0;
		    int patched_count = 0;
		    //System.out.println("*** j=" + j + "-- Row=" +  );
		    
		    while (cellIterator.hasNext()  ) {
		        column_counting ++;
		        Cell currentCell = cellIterator.next();
		        //System.out.println("*** j=" + j + "-- Cell=" +  currentCell.getStringCellValue() );
		        
		        
		        int missed_column = 1 - column_counting + currentCell.getColumnIndex() - patched_count;  
		        for(int i=0; i<missed_column; i++){
		            cellStoreArrayList.add("-MT-");
		            patched_count++;
		        }
		        if (currentCell.getCellType().toString().equals("BLANK")) {
		        	//currentCell.setCellValue("MT");
		        	 //System.out.println("*** j=" + j + "-- Str=" + currentCell.toString()  + "-- Type=" + currentCell.getCellType() + "-- Row=" +currentRow.toString() );

		        	//System.out.println("*** j=" + j + "-- Str=" + currentCell.toString()  + "-- Type=" + currentCell.getCellType() + "--");
		        }
		       // switch (currentCell.getCellType()){
		        switch(currentCell.getCellType()) {
	            case STRING:
	                data.add(currentCell.getStringCellValue());
	                //System.out.println("*** Add Str" + currentCell.getStringCellValue() + "--");
	                break;
	            case NUMERIC:
	                data.add(currentCell.getNumericCellValue());
	                break;
	            case BOOLEAN:
	                data.add(currentCell.getBooleanCellValue());
	                break;
	            case BLANK: 
	            	data.add(currentCell.getCellTypeEnum().BLANK);
	            	break;
	            default:
	            	data.add(currentCell.getStringCellValue());
	            	break;
	            }
		        
		    }
		    cellArrayListHolder.add(cellStoreArrayList);
		    j++;
		}
		
	}
	/********************************************************************************************************************************************/
	public static ArrayList readExcelSheet(String xlsbFileName ) throws IOException {
		ArrayList dataRtn = new ArrayList();
		ArrayList<ArrayList<String>> cellArrayListHolder = new ArrayList<ArrayList<String>>();
		FileInputStream excelFile = new FileInputStream(new File(xlsbFileName));
		DataFormatter dataFormatter = new DataFormatter();
		Workbook workbook = new XSSFWorkbook(excelFile);
		Sheet datatypeSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = datatypeSheet.iterator();
		 
		   int j = 1;
		   System.out.println("SheetName " + datatypeSheet.getSheetName());
		   System.out.println("Retrieving Sheets using Java 8 forEach with lambda");
	        workbook.forEach(sheet -> {
	            System.out.println("sheet => " + sheet.getSheetName());
	        });
	        
	        System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
	        Iterator<Row> rowIterator = datatypeSheet.rowIterator();
	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();

	            System.out.print("*** Last=" + row.getLastCellNum() + "--");
	            // Now let's iterate over the columns of the current row
	            Iterator<Cell> cellIterator = row.cellIterator();
	            boolean r = false;
	            while (cellIterator.hasNext()) {
	                Cell currentCell = cellIterator.next();
	                
	                //System.out.print("*** CA=" + currentCell.getAddress().toString() + "-- CV=" +  currentCell.getStringCellValue().toString() + "-- CT=" + currentCell.getCellType() +  "--");
	                String cellValue = dataFormatter.formatCellValue(currentCell);
	                //System.out.print(cellValue + "\t");
	               //System.out.print("*** CA=" + currentCell.getAddress().toString() + "-- CV=" +  cellValue.toString() + "-- CT=" + currentCell.getCellType() +  "--");

	                
	             // switch (currentCell.getCellType()){
			        switch(currentCell.getCellType()) {
		            case STRING:
		            	dataRtn.add(currentCell.getStringCellValue());
		                //System.out.println("*** Add Str" + currentCell.getStringCellValue() + "--");
		                break;
		            case NUMERIC:
		            	dataRtn.add(currentCell.getNumericCellValue());
		                break;
		            case BOOLEAN:
		            	dataRtn.add(currentCell.getBooleanCellValue());
		                break;
		            case BLANK: 
		            	dataRtn.add(currentCell.getCellTypeEnum().BLANK);
		               //System.out.print("*** CA=" + currentCell.getAddress().toString() + "-- CV=" +  currentCell.getStringCellValue().toString() + "-- CT=" + currentCell.getCellType() +  "--");
		               r=true;
		            	
		            	break;
		            default:
		            	dataRtn.add(currentCell.getStringCellValue());
		            	break;
		            }
	                
	                
	                
	                
	                
	            }
	            r = true;
	            if (r) {
	             System.out.println();
	             r=false;
	            }
	        }
		   return dataRtn;
		
	}
	
	
	
	
	
	/********************************************************************************************************************************************/

	
	
	public static void printCellValue(Cell cell) {
	    switch (cell.getCellTypeEnum()) {
	        case BOOLEAN:
	            System.out.print(cell.getBooleanCellValue());
	            break;
	        case STRING:
	            System.out.print(cell.getRichStringCellValue().getString());
	            break;
	        case NUMERIC:
	            if (DateUtil.isCellDateFormatted(cell)) {
	                System.out.print(cell.getDateCellValue());
	            } else {
	                System.out.print(cell.getNumericCellValue());
	            }
	            break;
	        case FORMULA:
	            System.out.print(cell.getCellFormula());
	            break;
	        case BLANK:
	            System.out.print("");
	            break;
	        default:
	            System.out.print("");
	    }

	    System.out.print("\t");
	    
	    
	     
	}
	/********************************************************************************************************************************************/

	public static void main(String[] args) throws Exception {
		 
		String xlsbFileName = "C:\\TEMP\\SAP_Asset_Upload\\au.xlsx";
		ArrayList data = new ArrayList();
		
		
		FileInputStream excelFile = new FileInputStream(new File(xlsbFileName));
 		Workbook workbook = new XSSFWorkbook(excelFile);
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = sheet.iterator();
		Iterator<Row> rowIterator = sheet.rowIterator();
		//readXlsbFile(xlsbFileName, data );
		
		
		System.out.println("**** NumCells"  + sheet.getRow(3).getPhysicalNumberOfCells() );
		
		
		/*
		data = readExcelSheet(xlsbFileName);
		 for(int i=0; i<data.size();i++){
		     //System.out.println(data.get(i));
		 } 
		 
		 
		Row row2 = rowIterator.next();
		while (rowIterator.hasNext()) {
			  row2 = rowIterator.next();
			// Iterate over all cells in the row, up to at least the 10th column
			int lastColumn = Math.max(row2.getLastCellNum(), 55);
			System.out.println("Last=" + lastColumn);
			
			XSSFCell cell = (XSSFCell) row2.getCell( 0 );
			System.out.println("Last=" + lastColumn);
			cell = (XSSFCell) row2.createCell( 0 );
			  String cellRef1 = CellReference.convertNumToColString( cell.getColumnIndex() );
			  System.err.println( "C: " + cellRef1
			          + Integer.toString( row2.getRowNum() ) );
			
			
			  if( cell.getStringCellValue().trim().isEmpty() )
				{
				  String cellRef = CellReference.convertNumToColString( cell.getColumnIndex() );
				  System.err.println( "Empty cell found: " + cellRef
				          + "-- RN=" + Integer.toString( row2.getRowNum() ) );
				}
			
			
			
		}
		//Row r = row.getRow(); // Logic here to get the row of interest

	
	  
		if( cell == null )
		{
		  cell = (XSSFCell) row2.createCell( 0 );
		  String cellRef2 = CellReference.convertNumToColString( cell.getColumnIndex() );
		  System.err.println( "C: " + cellRef2
		          + Integer.toString( row2.getRowNum() ) );
		  
		}
		
		*/
		


		/*
		sheet.forEach(row -> {
		    row.forEach(cell -> {
		        //printCellValue(cell);
		    });
		    //System.out.println();
		});
		
		*/
		
		System.out.println("*** End");
	}	
}
