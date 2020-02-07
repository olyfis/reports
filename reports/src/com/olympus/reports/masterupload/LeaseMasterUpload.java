package com.olympus.reports.masterupload;
 
// RUN: http://cvyhj3a27:8181/reports/leasemaster
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
import java.sql.Timestamp;
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
import com.olympus.reports.leases.ContractEOT;

@WebServlet("/leasemaster")
public class LeaseMasterUpload extends HttpServlet {
	private final Logger logger = Logger.getLogger(ContractEOT.class.getName()); // define logger
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\leaseMasterUpload.sql";
	/****************************************************************************************************************************************************/
	
	/****************************************************************************************************************************************************/

	/****************************************************************************************************************************************************/

	/****************************************************************************************************************************************************/
	public static ArrayList<String> getDbData(String  dateParam ) throws IOException {
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
		
		// be sure to not have line starting with "--" or "/*" or any other non alphabetical character
		BufferedReader br = new BufferedReader(fr);
		while((s = br.readLine()) != null){
		      sb.append(s);
		       
		}
		br.close();
		//displayProps(connectionProps);
		String query = new String();
		query = sb.toString();	
		//System.out.println("Query=" + query);	
		//System.out.println( query);	
		try {
			con = Olyutil.getConnection(connectionProps);
			if (con != null) {
				//System.out.println("Connected to the database");
				
				//System.out.println("***^^^*** dateParam=" + dateParam);
				String newQuery = query.replaceFirst("!", dateParam);
				//System.out.println( newQuery);	
				
				statement = con.prepareStatement(newQuery);
				
				
				
				//statement.setString(1, dateParam);
				res = Olyutil.getResultSetPS(statement);		 	 
				strArr = Olyutil.resultSetArray(res, ";");			
			}		
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
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
	/****************************************************************************************************************************************************/

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String logFileName = "leaseMaster.log";
		String directoryName = "D:/Kettle/logfiles/leaseMaster";
		Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
		String dateFmt = "";
		
		//System.out.println("%%%%%%%%%%%%%%%%%%%% in ContractEOT: doGet() ");
		String headerFilenameBRSummary = "C:\\Java_Dev\\props\\headers\\leaseMasterHdr.txt";
		String dispatchJSP = null;
		String connProp = null;
		String sqlFile = null;
		String sep = null;
		String contentType = "application/ms-excel";
		String searchDate = "";
		Date date = null;

		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFilenameBRSummary);
		date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		//System.out.println("Date=" + dateStamp);
		String FILE_NAME = "leaseMasterReport_" + dateStamp + ".xlsx";

		//connProp = "C:\\Java_Dev\\props\\connection.prop";
	 
		 
		dispatchJSP = "/leasemasterdetail.jsp";
		sep = ";";

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		// System.out.println("Begin Query:" + timestamp);
		logger.info(timestamp + ": " + "------------------ Begin Query: ");
		strArr = getDbData(searchDate);

		Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
		logger.info(timestamp2 + ": " + "------------------ End Query: ");
		// System.out.println("End Query:" + timestamp2);

		// Olyutil.printStrArray(strArr);
		String formUrl = "formUrl";
	 
		
		
		String formUrlValue = "/reports/lmasterexcel " ;
		request.getSession().setAttribute(formUrl, formUrlValue);

		request.getSession().setAttribute("strArr", strArr);
		 // req.getSession().setAttribute(paramName, paramValue);
		dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
		logger.info(dateFmt + ": " + "------------------Begin forward to: " + dispatchJSP);
		
		
		fileHandler.flush();
		fileHandler.close();
		//System.out.println("*** strArr Size:" + strArr.size());
		 request.getRequestDispatcher(dispatchJSP).forward(request, response);
		 
		
	}
	
	
	
}
