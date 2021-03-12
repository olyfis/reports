package com.olympus.reports.orderreleased;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.olympus.olyutil.Olyutil;
import com.olympus.olyutil.log.OlyLog;
 

//RUN: http://localhost:8181/reports/orderrel?startDate=2019-12-01&endDate=2019-12-12
@WebServlet("/orderrel")
public class OrderReleased extends HttpServlet {
	private final Logger logger = Logger.getLogger(OrderReleased.class.getName()); // define logger
	// Service method of servlet
		static Statement stmt = null;
		static Connection con = null;
		static ResultSet res = null; 
		static String s = null;
		static private PreparedStatement statement;
		static String ILpropFile = "C:\\Java_Dev\\props\\unidata.prop";
		static String propFile = "C:\\Java_Dev\\props\\Rapport.prop";		
		//static String sqlFile = "C:\\Java_Dev\\props\\sql\\orderReleasedApp\\ordersReleased_fixedDate_v4_app.sql"; // orig query

		static String sqlFile = "C:\\Java_Dev\\props\\sql\\orderReleasedApp\\ordersReleased_v6_app.sql";
		
		static String sqlFileIL = "C:\\Java_Dev\\props\\sql\\orderReleasedApp\\getcommDate.sql";
		
		/****************************************************************************************************************************************************/
		public static ArrayList<String> addDateToStr( ArrayList<String> strArr, Map<String, String> contractMap ) {
			
			
			 ArrayList<String> strArrMod = new ArrayList<String>();
		String id = "";
		String addDate = "";
		//Olyutil.printHashMap(contractMap);
		//int i = 0;
		//String[] arr;
		
		
		for (String str : strArr) {
			String newStr = new String();
			String[] items = str.split(";");
			id = items[0];
			for (int k = 0; k < items.length; k++) {
				if (k == 14) {
					//System.out.println("***^^B^^*** k=" + k + "-- ContactID:" +   contractMap.get(id)  + "-- Item=" + items[k]);
					 
					newStr += contractMap.get(id) + ";" + items[k] + ";";
					 
					
				} else {
					newStr += items[k] + ";";
				}
			}
			strArrMod.add(newStr);
			
			/*
			// Coverting array to ArrayList 
	        List<String> list = new ArrayList<>(Arrays.asList(items)); 
	        
	        
			
			//addDate = str + ";" +  contractMap.get(id);
			addDate =  contractMap.get(id);	
			
			
			list.add(14, addDate); 
			// Converting the list back to array 
			items = list.toArray(items); 
	  
	        // Printing the original array 
	        System.out.println("Initial Array:\n"
	                        + Arrays.toString(items)); 
			
			strArrMod.add(Arrays.toString(items));
			*/
			//addDate = "";
			// System.out.println("***^^^*** ContactID:" +   id  + "--  Date:" + contractMap.get(id) + "--");
			//System.out.println("***^^^*** ContactID:" +   id + "--");
			//System.out.println(strArrMod.get(i));
			//i++;
		}
		return(strArrMod);
}
		
		
		
		
		/****************************************************************************************************************************************************/
		public static ArrayList<String> addDateToStrOrig( ArrayList<String> strArr, Map<String, String> contractMap ) {
			
			 ArrayList<String> strArrMod = new ArrayList<String>();
		String id = "";
		String addDate = "";
		//Olyutil.printHashMap(contractMap);
		int i = 0;
		for (String str : strArr) {
			String[] items = str.split(";");
			id = items[0];
			addDate = str + ";" +  contractMap.get(id);
			
		
			
			strArrMod.add(addDate);
			addDate = "";
			// System.out.println("***^^^*** ContactID:" +   id  + "--  Date:" + contractMap.get(id) + "--");
			//System.out.println("***^^^*** ContactID:" +   id + "--");
			//System.out.println(strArrMod.get(i));
			i++;
		}
		return(strArrMod);
}
		/****************************************************************************************************************************************************/
		public static ArrayList<String> getDbDataRange(String propFile, String params, String startDate, String endDate, String sqlSrc) throws IOException {
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

			fr = new FileReader(new File(sqlSrc));

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
			//System.out.println(query);
			try {
				con = Olyutil.getConnection(connectionProps);
				if (con != null) {
					//System.out.println("Connected to the database");
					statement = con.prepareStatement(query);

					// System.out.println("***^^^*** contractID=" + contractID);
					//System.out.println("***^^^*** S=" + startDate + "-- E=" + endDate + "--");
					statement.clearParameters();
					
					if ( ! Olyutil.isNullStr(params)) {
						if (params.equals("dates")) {
							statement.setString(1, startDate);
							statement.setString(2, endDate);
						}
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

		/****************************************************************************************************************************************************/
	public static Map<String, String> getContractHash(ArrayList<String> dataArr) {
		Map<String, String> myMap = new HashMap<String, String>();
		String key = "";
		String value = "";
		for (String str : dataArr) {
			String[] items = str.split(";");
			key = items[0];
			value = items[1];
			myMap.put(key, value);
		}

		return myMap;
	}
		
		/****************************************************************************************************************************************************/

		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			Map<String, String> contractMap = new HashMap<String, String>();
			ArrayList<String> strArrMod = new ArrayList<String>();
			String dispatchJSP = "/orderreldetail.jsp";
			String logFileName = "orderReleasedApp.log";
			String directoryName = "D:/Kettle/logfiles/ordersReleasedApp";
			Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
			String dateFmt = "";
			
			String sDate = "";
			String eDate = "";
			JSONArray jsonArr = new JSONArray();
			ArrayList<String> strArr = new ArrayList<String>();
			ArrayList<String> strArrIL = new ArrayList<String>();
			//ArrayList<String> idArr = new ArrayList<String>();
			ArrayList<String> errArr = new ArrayList<String>();
			Map<String, Boolean> contractErrs = null;
			boolean errStat = false;
			PrintWriter out = response.getWriter();
			//response.setContentType("text/JSON");

			String paramName = "startDate";
			String paramValue = request.getParameter(paramName);

			String endDate = "endDate";
			String endDateValue = request.getParameter(endDate);
			//String jFlag = "jflag";
			//String jFlagValue = request.getParameter(jFlag);
			String formUrl = "formUrl";
			
			String formUrlValue = "/reports/orexcel " ;
			request.getSession().setAttribute(formUrl, formUrlValue);

			int errCount = 0;
			if ((paramValue != null && !paramValue.isEmpty())) {
				sDate = paramValue.trim();
				//System.out.println("*** sDate:" + sDate + "--");
			}
			if ((endDateValue != null && !endDateValue.isEmpty())) {
				eDate = endDateValue.trim();
				//System.out.println("*** eDate:" + eDate + "--");
			}

			strArrIL = getDbDataRange(ILpropFile, "", sDate, eDate, sqlFileIL);
			//Olyutil.printStrArray(strArr);
			contractMap = getContractHash(strArrIL);
			
			//Olyutil.printHashMap(contractMap);			
			//System.out.println("***^^^*** Contact:" +  contractMap.get("011-0008964-004")   + "--  SZ:" + contractMap.size() + "--");
		 
			strArr = getDbDataRange(propFile, "dates", sDate, eDate, sqlFile);
			strArrMod = addDateToStr(strArr, contractMap );
			//Olyutil.printStrArray(strArrMod);
			
			/*
			 String id = "";
			for (String str : strArr) {
				String[] items = str.split(";");
				id = items[0];
				System.out.println("***^^^*** ContactID:" +   id  + "--  Date:" + contractMap.get(id) + "--");
			}
		
			 */
			logger.info(dateFmt + ": " + "------------------Begin forward to: " + dispatchJSP);
			fileHandler.flush();
			fileHandler.close();
			request.getSession().setAttribute("strArr", strArr);
			 request.getSession().setAttribute("strArrMod", strArrMod);
			//request.getSession().setAttribute("contractMap", contractMap);
			//Olyutil.printHashMap(contractMap);
			request.getRequestDispatcher(dispatchJSP).forward(request, response);
		}
		
		
		/****************************************************************************************************************************************************/


}
