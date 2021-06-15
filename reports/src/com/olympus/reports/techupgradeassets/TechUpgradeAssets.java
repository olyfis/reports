package com.olympus.reports.techupgradeassets;

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
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
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
//import com.olympus.reports.evergreen.EvergreenExcel;

@WebServlet("/techupgradeassets")
public class TechUpgradeAssets  extends HttpServlet{
	private final Logger logger = Logger.getLogger(TechUpgradeAssets.class.getName()); // define logger
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res = null;
	static NodeList node = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\TechUpgradeAssets_V2.sql";

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
	public HashMap<String, Double>   getEquipTotal(ArrayList<String> strArr) throws IOException {
		HashMap<String, Double> map = new HashMap<String, Double>();

		String[] items = null;
		String key = "";
		String cust = "";
		String commDate = "";
		double tot = 0.00;
		
		// Positional data -- Update if query changes
		for (String str : strArr) { // iterating ArrayList
			items = str.split(";");
			//System.out.println("**^B^** Cust=" + items[1] + "--");
			cust = items[1].replaceAll(" ", "_");
			//System.out.println("**^A^** Cust=" + cust + "--");
			
			
			commDate = items[3];
			
			if (Olyutil.isNullStr(items[10]) ) { // Rollover == N
				key = items[0] + "^N^" + cust + "^" + commDate + "^" + items[9].replaceAll(" ", "^");
				//System.out.println("** ID=" + items[0] + "-- R=N -- Key=" + key);
				
				if (map.containsKey(key)) {
					
					tot = map.get(key);
					tot += Olyutil.strToDouble(items[2]);
					map.put(key, tot);
					//System.out.println("** ID=" + items[0] + "-- R=N -- Key=" + key + "-- Total=" + tot + "--");
					
				} else {
					map.put(key, Olyutil.strToDouble(items[2]));
				}
				
			} else {
				key = items[0] + "^Y^" + cust + "^" + commDate + "^" + items[9].replaceAll(" ", "^");
				//System.out.println("** ID=" + items[0] + "-- R=Y -- Key=" + key);
				
				if (map.containsKey(key)) {
					
					tot = map.get(key);
					tot += Olyutil.strToDouble(items[2]);
					map.put(key, tot);
					//System.out.println("** ID=" + items[0] + "-- R=N -- Key=" + key + "-- Total=" + tot + "--");
					
				} else {
					map.put(key, Olyutil.strToDouble(items[2]));
				}
				
				
			}
		
			key = "";
		}
		//displayHashMap(map);
		return(map);
	}
	/*****************************************************************************************************/
	/****************************************************************************************************************************************************/
	public static boolean displayHashMap(HashMap<String, Double> hashMap) {
		boolean status = true;
		
		Set<String> keys = hashMap.keySet();  //get all keys
		for(String key: keys) {

		  System.out.println("**----** Key=" + key + "-- Value=" + hashMap.get(key) + "--");
		}
		return(status);
		
	}
	/****************************************************************************************************************************************************/



	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// Begin setup logging
		HashMap<String, Double> mapRtn = new HashMap<String, Double>();
		Date logDate = null;
		String dateFmt = "";
		String logFileName = "techupgradeassets.log";
		String directoryName = "D:/Kettle/logfiles/techupgradeassets";
		Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
		logDate = Olyutil.getCurrentDate();
		dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
		 

		String dispatchJSP = null;
		String connProp = null;
		String sqlFile = null;
		String sep = null;
		String contentType = "application/ms-excel";
		String headerFilenameBRSummary = "C:\\Java_Dev\\props\\headers\\techAssetHdr.txt";
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFilenameBRSummary);
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		// System.out.println("in  Excel - > Date=" + dateStamp);
		String FILE_NAME = "TechUpgradeAssets_" + dateStamp + ".xlsx";
		String mType = "mType";
		String mTypeValue = req.getParameter(mType);
		req.getSession().setAttribute(mType, mTypeValue);
		connProp = "C:\\Java_Dev\\props\\connection.prop";
		logger.info(dateFmt + ": " + "------------------Begin doGet() ReportType:" +  mTypeValue +  "--------------------");
		 dispatchJSP = "/techassetsdetail.jsp";
		 
		
		sep = ";";
		strArr = getDbData();
		mapRtn = getEquipTotal( strArr);
		//System.out.println("*** Begin display hashMap");
		//displayHashMap(mapRtn);
		//System.out.println("*** End display hashMap");
		
		 // Olyutil.printStrArray(strArr);
		String formUrl = "formUrl";
		String formUrlValue = "/reports/tuaexcel2";
		req.getSession().setAttribute(formUrl, formUrlValue);
		req.getSession().setAttribute("mapRtn", mapRtn);
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
