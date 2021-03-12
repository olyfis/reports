package com.olympus.reports.evergreen;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import com.olympus.olyutil.Olyutil;
import org.w3c.dom.NodeList;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.olympus.olyutil.log.*;


@WebServlet("/evergreenExcel")
public class EvergreenExcel extends HttpServlet {
	
	private final Logger logger = Logger.getLogger(EvergreenExcel.class.getName()); // define logger
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res = null;
	static NodeList node = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\eGreen.sql";

	/****************************************************************************************************************************************************/
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

	/*****************************************************************************************************/

	/*****************************************************************************************************/

	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// Begin setup logging
		Date logDate = null;
		String dateFmt = "";
		String logFileName = "evergreen.log";
		String directoryName = "D:/Kettle/logfiles/evergreen";
		Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
		logDate = Olyutil.getCurrentDate();
		dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
		 

		String dispatchJSP = null;
		String connProp = null;
		String sqlFile = null;
		String sep = null;
		String contentType = "application/ms-excel";
		String headerFilenameBRSummary = "C:\\Java_Dev\\props\\headers\\eGreenHdr.txt";
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFilenameBRSummary);
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		// System.out.println("in EverGreenExcel - > Date=" + dateStamp);
		String FILE_NAME = "EverGreenReport_" + dateStamp + ".xlsx";
		String mType = "mType";
		String mTypeValue = req.getParameter(mType);
		req.getSession().setAttribute(mType, mTypeValue);
		connProp = "C:\\Java_Dev\\props\\connection.prop";
		logger.info(dateFmt + ": " + "------------------Begin doGet() ReportType:" +  mTypeValue +  "--------------------");
		dispatchJSP = "/egreendetail.jsp";
		sep = ";";
		strArr = getDbData();
		// Olyutil.printStrArray(strArr);
		String formUrl = "formUrl";
		String formUrlValue = "/reports/egexcel ";
		req.getSession().setAttribute(formUrl, formUrlValue);

		req.getSession().setAttribute("strArr", strArr);
		// req.getSession().setAttribute(paramName, paramValue);
		logDate = Olyutil.getCurrentDate();
		dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
		logger.info(dateFmt + ": " + "------------------Begin forward to: " + dispatchJSP);
		fileHandler.flush();
		fileHandler.close();
		req.getRequestDispatcher(dispatchJSP).forward(req, res);
		// System.out.println("Exit Servlet " + this.getServletName() + " in doGet() ");

	}
	
}
