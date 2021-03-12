package com.olympus.reports.vapoexpired;

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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;

import com.olympus.olyutil.Olyutil;
import com.olympus.olyutil.log.OlyLog;
import com.olympus.reports.evergreen.EvergreenExcel;

@WebServlet("/poexpire")
public class VaPoExpired  extends HttpServlet {
	/********************************************************************************************************************************************************/
	private final Logger logger = Logger.getLogger(EvergreenExcel.class.getName()); // define logger
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res = null;
	static NodeList node = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\vapoexpired.sql";
	static String sqlFileAll = "C:\\Java_Dev\\props\\sql\\vapoexpiredall.sql";
	String VaPoExpiredHdr = "C:\\Java_Dev\\props\\headers\\vapoexpiredhdr.txt";
	String logFileName = "vapoexpired.log";
	String directoryName = "D:/Kettle/logfiles/vapoexpired";

	/****************************************************************************************************************************************************/
	public static ArrayList<String> getDbData(String brch) throws IOException {
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
		
		if (Olyutil.isNullStr(brch)) {
			fr = new FileReader(new File(sqlFileAll));
			
		} else {
			fr = new FileReader(new File(sqlFile));
			args = true;
		}

		 

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
				if (args == true) {
					statement.setString(1, brch);
				}
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

	
	/********************************************************************************************************************************************************/
	// Service method
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
			String formUrl = "formUrl";
			String formUrlValue = "/reports/vapoexcel ";
			req.getSession().setAttribute(formUrl, formUrlValue);
			// Begin setup logging
			Date logDate = null;
			String dateFmt = "";
			String dispatchJSP = "/vapoexpireddetail2.jsp";
			String sep = ";";
			Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
			logDate = Olyutil.getCurrentDate();
			dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
	
			String connProp = null;
			String sqlFile = null;
			 	
			ArrayList<String> strArr = new ArrayList<String>();
			ArrayList<String> headerArr = new ArrayList<String>();
			headerArr = Olyutil.readInputFile(VaPoExpiredHdr);
			Date date = Olyutil.getCurrentDate();
			String dateStamp = date.toString();
			String brch = "brch";
			String brchValue = req.getParameter(brch);
			req.getSession().setAttribute(brch, brchValue);
			
			
			
			req.getSession().setAttribute("strArr", strArr);
			// req.getSession().setAttribute(paramName, paramValue);
			logDate = Olyutil.getCurrentDate();
			dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
			logger.info(dateFmt + ": " + "------------------Begin forward to: " + dispatchJSP);
			strArr = getDbData(brchValue);
			//Olyutil.printStrArray(strArr);
			req.getSession().setAttribute("strArr", strArr);
			fileHandler.flush();
			fileHandler.close();
			 req.getRequestDispatcher(dispatchJSP).forward(req, res);
			
		}
	/********************************************************************************************************************************************************/
	/********************************************************************************************************************************************************/

}
