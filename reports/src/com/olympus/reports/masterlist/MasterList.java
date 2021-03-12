package com.olympus.reports.masterlist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;

import com.olympus.olyutil.Olyutil;
import com.olympus.olyutil.log.OlyLog;
import com.olympus.reports.evergreen.EvergreenExcel;

@WebServlet("/masterlist")
public class MasterList extends HttpServlet {
	
	private final Logger logger = Logger.getLogger(MasterList.class.getName()); // define logger
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res = null;
	static NodeList node = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\masterList_V1.sql";
	/*****************************************************************************************************************************************************/

	
	
	/*****************************************************************************************************************************************************/
	public static ArrayList<String> getDbData() throws IOException {
		FileInputStream fis = null;
		FileReader fr = null;
		String s = new String();
		StringBuffer sb = new StringBuffer();
		ArrayList<String> strArr = new ArrayList<String>();
		
		try {
			fis = new FileInputStream(propFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties connectionProps = new Properties();
		connectionProps.load(fis);

		fr = new FileReader(new File(sqlFile));

		// be sure to not have line starting with "--" or "/*" or any other non
		// alphabetical character
		BufferedReader br = new BufferedReader(fr);
		while ((s = br.readLine()) != null) {
			sb.append(s);

		}
		br.close();
		// displayProps(connectionProps);
		String query = new String();
		query = sb.toString();
		// System.out.println("Query=" + query);
		try {
			con = Olyutil.getConnection(connectionProps);
			if (con != null) {
				// System.out.println("Connected to the database");
				statement = con.prepareStatement(query);

				// System.out.println("***^^^*** contractID=" + contractID);
				// statement.setString(1, id);
				res = Olyutil.getResultSetPS(statement);
				strArr = Olyutil.resultSetArray(res, ";");
				 
				
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return strArr;
	}

	/*****************************************************************************************************************************************************/

	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// Begin setup logging
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");  
	 
		LocalDateTime now = null;
 
		String logFileName = "masterList.log";
		String directoryName = "D:/Kettle/logfiles/masterList";
		Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
	 
		String dispatchJSP = null;
		String connProp = null;
		String sqlFile = null;
		String sep = null;
		String contentType = "application/ms-excel";
		String headerFilenameBRSummary = "C:\\Java_Dev\\props\\headers\\masterListHdr.txt";
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFilenameBRSummary);
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		// System.out.println("in EverGreenExcel - > Date=" + dateStamp);
		String FILE_NAME = "MasterList_" + dateStamp + ".xlsx";
		String mType = "mType";
		String mTypeValue = req.getParameter(mType);
		req.getSession().setAttribute(mType, mTypeValue);
		connProp = "C:\\Java_Dev\\props\\connection.prop";
		now = LocalDateTime.now(); 
		logger.info(dtf.format(now) + ": " + "------------------Begin doGet() ReportType:" +  mTypeValue +  "--------------------");
		dispatchJSP = "/masterlistdetail.jsp";
		sep = ";";
		

	 
     
		 now = LocalDateTime.now();  
		// System.out.println("Begin SQL:" + dtf.format(now));
		logger.info(dtf.format(now) + ": " + "------------------ Begin SQL Query" );
		
		 strArr = getDbData();
		 //Olyutil.printStrArray(strArr);
		 
		 now = LocalDateTime.now();  
		 logger.info(dtf.format(now) + ": " + "------------------ End SQL Query" );
		 //System.out.println("End SQ::" + dtf.format(now));
	    
		//System.out.println("Date2:" + dtf.format(now));
		String formUrl = "formUrl";
		String formUrlValue = "/reports/mlexcel ";
		req.getSession().setAttribute(formUrl, formUrlValue);

		req.getSession().setAttribute("strArr", strArr);
		// req.getSession().setAttribute(paramName, paramValue);
		
		
		 now = LocalDateTime.now();  
		 
		logger.info(dtf.format(now) + ": " + "------------------Begin forward to: " + dispatchJSP);
		fileHandler.flush();
		fileHandler.close();
		 req.getRequestDispatcher(dispatchJSP).forward(req, res);
		// System.out.println("Exit Servlet " + this.getServletName() + " in doGet() ");

	}
	/*****************************************************************************************************************************************************/

	/*****************************************************************************************************************************************************/

	/*****************************************************************************************************************************************************/

	/*****************************************************************************************************************************************************/

	
	
	
	
	
	
	
	
	
	

}
