package com.olympus.reports.leases;

import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;

import com.olympus.olyutil.Olyutil;
import com.olympus.olyutil.log.OlyLog;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Logger;
/* 
 * To run: http://localhost:8181/reports/lp?startDate=2019/11/01&endDate=2019/11/12
 */
@WebServlet("/lp")
public class LeasePortfolio  extends HttpServlet  {
	
	/********************************************************************************************************************************************************/
	private final Logger logger = Logger.getLogger(LeasePortfolio.class.getName()); // define logger
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res = null;
	static NodeList node = null;
	static String s = null;
	static private PreparedStatement statement;
	//static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String propFile = "C:\\Java_Dev\\props\\Rapport.prop";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\LeasePortfolio.sql";
	 
	String tHdr = "C:\\Java_Dev\\props\\headers\\leaseportfolioHdr.txt";
	String logFileName = "leaseportfolio.log";
	String directoryName = "D:/Kettle/logfiles/leaseportfolio";
	
	/********************************************************************************************************************************************************/
	public static ArrayList<String> getDbData(String sDate, String eDate) throws IOException {
		boolean args = false;
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
		//System.out.println("***^^^^^ Q=  " + query);
		try {
			con = Olyutil.getConnection(connectionProps);
			if (con != null) {
				//System.out.println("Connected to the database");
				statement = con.prepareStatement(query);

					///System.out.println("***^^^*** query=  " + query);
				 
					statement.setString(1, sDate);
					statement.setString(2, eDate);
				res = Olyutil.getResultSetPS(statement);
				strArr = Olyutil.resultSetArray(res, ";");
			} else {
				System.out.println("***^^^*** Could not get DB connection!");
			}
		} catch (SQLException se) {
			//System.out.println("***^^^*** Issue with DB");
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
		//System.out.println("***^^^*** SZ=" + strArr.size() );
		return(strArr);
	}
	/********************************************************************************************************************************************************/
	// Service method
			@Override
			protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
				String formUrl = "formUrl";
				String formUrlValue = "/reports/lpexcel ";
				req.getSession().setAttribute(formUrl, formUrlValue);
				// Begin setup logging
				Date logDate = null;
				String dateFmt = "";
				String dispatchJSP = "/lprange.jsp";
				String sep = ";";
				Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
				logDate = Olyutil.getCurrentDate();
				dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
		
				String connProp = null;
				String sqlFile = null;
				 	
				ArrayList<String> strArr = new ArrayList<String>();
				ArrayList<String> headerArr = new ArrayList<String>();
				headerArr = Olyutil.readInputFile(tHdr);
				Date date = Olyutil.getCurrentDate();
				String dateStamp = date.toString();
				String sDate = "startDate";
				String sValue = req.getParameter(sDate);
				req.getSession().setAttribute(sDate, sValue);
				String eDate = "endDate";
				String eValue = req.getParameter(eDate);
				req.getSession().setAttribute(eDate, eValue);
				req.getSession().setAttribute("strArr", strArr);
				// req.getSession().setAttribute(paramName, paramValue);
				logDate = Olyutil.getCurrentDate();
				dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
				logger.info(dateFmt + ": " + "------------------Begin forward to: " + dispatchJSP);
				strArr = getDbData(sValue, eValue);
				Olyutil.printStrArray(strArr);
				req.getSession().setAttribute("strArr", strArr);
				fileHandler.flush();
				fileHandler.close();
				//req.getRequestDispatcher(dispatchJSP).forward(req, res);
				
			}
	/********************************************************************************************************************************************************/

	/********************************************************************************************************************************************************/

	
}
