package com.olympus.reports.leases;

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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;

import com.olympus.olyutil.Olyutil;

@WebServlet("/dailycommencement")
public class DailyCommencement extends HttpServlet {
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
	static NodeList  node  = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\Daily_commencement_report_V4.sql";
	/****************************************************************************************************************************************************/
	public static ArrayList<String> getDbData( String termDate) throws IOException {
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
		try {
			con = Olyutil.getConnection(connectionProps);
			if (con != null) {
				//System.out.println("Connected to the database");
				statement = con.prepareStatement(query);
				
				// System.out.println("***^^^*** termDate=" + termDate + "--");
				 statement.setString(1, termDate);
				 statement.setString(2, termDate);
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
	/****************************************************************************************************************************************************/

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 
		String headerFilenameBRSummary = "C:\\Java_Dev\\props\\headers\\commencementHdr.txt";
		String dispatchJSP = null;
		String connProp = null;
		String sqlFile = null;
		String sep = null;
		String contentType = "application/ms-excel";

		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFilenameBRSummary);
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		 //System.out.println("Date=" + dateStamp);
		
		String dType = "id";
		String dTypeValue = request.getParameter(dType);
		request.getSession().setAttribute(dType, dTypeValue);
		connProp = "C:\\Java_Dev\\props\\connection.prop";
		//System.out.println("Date Param=" + dTypeValue + "--");
		//dispatchJSP = "/egreendetail.jsp";
		dispatchJSP = "/dailycommencementdetail.jsp";
		sep = ";";
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       // System.out.println("Begin Query:" + timestamp);
		strArr = getDbData(dTypeValue );
		 //Olyutil.printStrArray(strArr);
		Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
       // System.out.println("End Query:" + timestamp2);
		String formUrl = "formUrl";
		String formUrlValue = "/reports/commencementwrite?typeval=100" ;
		request.getSession().setAttribute(formUrl, formUrlValue);

		request.getSession().setAttribute("strArr", strArr);
		 // req.getSession().setAttribute(paramName, paramValue);
		//System.out.println("Begin forward to JSP");
		request.getRequestDispatcher(dispatchJSP).forward(request, response);
		// System.out.println("Exit Servlet " + this.getServletName() + " in doGet() ");
		
	}
}
