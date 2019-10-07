package com.olympus.reports.infolease;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.NodeList;

import com.olympus.olyutil.Olyutil;
import com.olympus.olyutil.log.OlyLog;
import com.olympus.reports.evergreen.EvergreenExcel;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


//@WebServlet("/InfoLeaseReports")

@WebServlet("/ilreport")
public class InfoLeaseReports extends HttpServlet {
	private final Logger logger = Logger.getLogger(InfoLeaseReports.class.getName()); // define logger

	/************************************************************************************************************************************************/
	/************************************************************************************************************************************************/
	// Service method of servlet
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res = null;
	static NodeList node = null;
	static String s = null;
	static private PreparedStatement statement;

	/************************************************************************************************************************************************/

	public static ArrayList<String> resultSetArray(ResultSet rs, String sep) throws SQLException {

		ArrayList<String> arrStr = new ArrayList<String>();
		ArrayList<String> xData = new ArrayList<String>();
		String outDataLine = "";
		ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();
		int columnsNumber = rsmd.getColumnCount();
		while (rs.next()) {
			for (int i = 1; i <= columnsNumber; i++) {
				if (i > 1) {
					// System.out.print(sep);
					outDataLine += sep;
				}
				String columnValue = rs.getString(i);
				outDataLine += columnValue;
			}
			// System.out.println(outDataLine);
			arrStr.add(outDataLine);
			// System.out.println("");
			outDataLine = "";
		}
		return arrStr;
	}
	/************************************************************************************************************************************************/
	public static void createExcelDoc(String headerFile, String label, String fileName, HttpServletResponse res,  ArrayList<String> data ) {
		
		
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(label);
        System.out.println("InfoleaseReports: -- Creating Excel report: " + label);
       // System.out.println("InfoleaseReports: -- Data=" + data.toString());
        ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFile);
       // CreateExcel.loadHeader(workbook, sheet, headerArr);
        //CreateExcel.loadWorkSheet(workbook, sheet, data, 1);
        try {
			
			// HttpServletResponse response = getResponse(); // get ServletResponse
			res.setContentType("application/vnd.ms-excel"); // Set up mime type
			res.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			//OutputStream out = res.getOutputStream();
			//workbook.write(out);
			//out.flush();
			
		//********************************************************************************************************************************
		
		} 
        /*
        catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        */
        finally {
			try {
				workbook.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
        
	}
	
	/************************************************************************************************************************************************/
	public static Connection getConnection(Properties connectionProps) throws SQLException {
		Connection conn = null;

		String url = null;
		String myHost = (String) connectionProps.get("myHost");
		String myAccount = (String) connectionProps.get("myAccount");
		String uid = (String) connectionProps.get("userID");
		String pw = (String) connectionProps.get("passWord");
		String dbClass = (String) connectionProps.get("dbClass");
		String dbmsType = (String) connectionProps.get("dbmsType");
		String passWord = pw.trim();
		String userID = uid.trim();
		/*
		 * System.out.println("dbmsType: " + dbmsType + "--");
		 * System.out.println("dbClass: " + dbClass + "--");
		 * System.out.println("myHost: " + myHost + "--"); System.out.println("userID: "
		 * + userID + "--"); System.out.println("passWord: " + passWord + "--");
		 */
		try {
			if (dbmsType.equals("UNIDATA")) {
				Class.forName(dbClass);
				// Class.forName("com.rs.u2.jdbc.UniJDBCDriver");
				url = "jdbc:rs-u2://" + myHost + "/" + myAccount + ";" + "dbmstype=" + dbmsType; // generate URL
				conn = DriverManager.getConnection(url, userID, passWord);
				// System.out.println("getConnection: URL: " + url);
			} else if (dbmsType.equals("rapport")) {
				Class.forName(dbClass);
				url = "jdbc:sqlserver://" + myHost + ":1433;" + "databaseName=rapport;user=Rapport;password=rapport;"; // generate
																														// URL
				conn = DriverManager.getConnection(url, userID, passWord);
			}

			if (conn == null) {
				System.out.println(" %%% Returned null connection");
			} else {
				// System.out.println("Connected - > URL: " + url);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/************************************************************************************************************************************************/

	public static ResultSet getResultSetPS(PreparedStatement stmt) throws SQLException {
		ResultSet rs = stmt.executeQuery();

		return (rs);
	}

	/************************************************************************************************************************************************/
	public static ArrayList<String> getData(String connProp, String sqlFile, String sep) throws IOException {
		//BookingReport br2 = new BookingReport();
		ArrayList<String> strArr = new ArrayList<String>();
		String result = null;
		 
		FileInputStream fis = null;

		String hostname = InetAddress.getLocalHost().getHostName();

		try {
			fis = new FileInputStream(connProp);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Properties connectionProps = new Properties();
		String s = new String();
		StringBuffer sb = new StringBuffer();

		try {
			connectionProps.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FileReader fr = new FileReader(new File(sqlFile));

		// be sure to not have line starting with "--" or "/*" or any other non
		// alphabetical character
		BufferedReader br = new BufferedReader(fr);
		while ((s = br.readLine()) != null) {
			sb.append(s);
		}
		br.close();

		String query2 = new String();
		query2 = sb.toString();

		// System.out.println("Q=" + query2);
		// System.out.println(query2);
		try {
			con = getConnection(connectionProps);
			if (con != null) {
				// System.out.println("Connected to the database");
				// System.out.println("***^^^^***Q=" + query2);
				statement = con.prepareStatement(query2);
				// statement.setString(1, startDate);
				// statement.setString(2, endDate);
				res = getResultSetPS(statement);
				strArr = resultSetArray(res, sep);
				// System.out.println("**** arrSize=" + strArr.size() );
				// ************************************************************************************************************************************************
				// result = jutil.displayResults(res);
			} else {
				System.out.println("**** NOT Connected to the database");
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
			} // end finally try
		}

		return strArr;
	}
	/************************************************************************************************************************************************/

	/************************************************************************************************************************************************/
	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String dispatchJSP = null;
		String connProp = null;
		String sqlFile = null;
		String sep = ":";
		String headerFile = null;
		String sheetLabel = null;
		String fileName = null;
		connProp = "C:\\Java_Dev\\props\\connection.prop";
		// Begin setup logging
				Date logDate = null;
				String dateFmt = "";
				String directoryName = "D:/Kettle/logfiles/infolease";
				String logFileName = "infolease.log";
				logDate = Olyutil.getCurrentDate();
				dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
				 
				Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
				logger.info(dateFmt + ": " + "------------------Begin doGet()--------------------");
				
		final Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		
		ArrayList<String> strArr = new ArrayList<String>();
		PrintWriter out = res.getWriter();
		res.setContentType("text/plain");
		// out.write(" in Service method doGet() ");

		String mType = "mType";
		String mTypeValue = req.getParameter(mType);
		// System.out.println("^^^^^^^ mTypeValue=" + mTypeValue);
		/*
		 * String startDate = "startDate"; String startDateValue =
		 * req.getParameter(startDate); String endDate = "endDate"; String endDateValue
		 * = req.getParameter(endDate);
		 */
		
		String excel = "excel";
		String excelValue = req.getParameter(excel);
		//System.out.println("******* InfoLeaseReports: ^^^^^^^ excelValue=" + excelValue);
		
		if (Olyutil.isNullStr(mTypeValue)) {
			logger.info(dateFmt + ": " + "------------------ Error:  parameter is null. ");
			fileHandler.flush();
			fileHandler.close();
			return;
		} 
		

		if (mTypeValue.equals("UTIL")) {
			sqlFile = "C:\\Java_Dev\\props\\sql\\utilization.sql";
			dispatchJSP = "/utilization.jsp";
		} else if (mTypeValue.equals("CCAN")) {
			sqlFile = "C:\\Java_Dev\\props\\sql\\ccaninfo.sql";
			dispatchJSP = "/ccaninfo.jsp";
		} else if (mTypeValue.equals("SNAP")) {
			sqlFile = "C:\\Java_Dev\\props\\sql\\snapshot.sql";
			dispatchJSP = "/snapshot.jsp";
		} else if (mTypeValue.equals("LSHP")) {
			sqlFile = "C:\\Java_Dev\\props\\sql\\lastship.sql";
			dispatchJSP = "/lastship.jsp";
		
		} else if (mTypeValue.equals("MAST")) {
			sqlFile = "C:\\Java_Dev\\props\\sql\\maturity_assets_all.sql";
			dispatchJSP = "/matasset.jsp";
		} else if (mTypeValue.equals("MALL")) {
			sqlFile = "C:\\Java_Dev\\props\\sql\\maturity_all.sql";
			dispatchJSP = "/matall.jsp";
		} else {
			logger.info(dateFmt + ": " + "------------------ Error: no valid parameter found. ");
			fileHandler.flush();
			fileHandler.close();
			return;
		}
		
		strArr = getData(connProp, sqlFile, sep);
		
		//if (excelValue.equals("EGEXL")) { // Create Excel document
			//createExcelDoc(headerFile, sheetLabel, fileName, res, strArr);
		//}
		/*
		 * System.out.println("connProp=" + connProp); System.out.println("sqlFile=" +
		 * sqlFile); System.out.println("startDateValue=" + startDateValue);
		 * System.out.println("endDateValue=" + endDateValue); System.out.println("sep="
		 * + sep);
		 */
		

		req.getSession().setAttribute("strArr", strArr);
		req.getSession().setAttribute("excel", excelValue);

		// req.getSession().setAttribute(paramName, paramValue);
		logDate = Olyutil.getCurrentDate();
		dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
		logger.info(dateFmt + ": " + "------------------Begin forward to: " + dispatchJSP);
		fileHandler.flush();
		fileHandler.close();
		req.getRequestDispatcher(dispatchJSP).forward(req, res);
		// System.out.println("Exit Servlet " + this.getServletName() + " in doGet() ");
	}

	/************************************************************************************************************************************************/

	@Override
	public void init() throws ServletException {
		System.out.println("Servlet " + this.getServletName() + " has started");
	}

	/************************************************************************************************************************************************/
	@Override
	public void destroy() {
		System.out.println("Servlet " + this.getServletName() + " has stopped");
	}

}
