package com.olympus.reports.checks;

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
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.olympus.olyutil.Olyutil;
@WebServlet("/scr14chk")
public class Screen14Chk extends HttpServlet {
	// Service method of servlet
			static Statement stmt = null;
			static Connection con = null;
			static ResultSet res = null; 
			static String s = null;
			static private PreparedStatement statement;
			static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
			 		
			static String sqlFile = "C:\\Java_Dev\\props\\sql\\screen14chk.sql";
			
			/****************************************************************************************************************************************************/
			public static ArrayList<String> getDbData(String propFile,  String sqlSrc) throws IOException {
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
						
						/* if ( ! Olyutil.isNullStr(params)) {
							if (params.equals("dates")) {
								statement.setString(1, startDate);
								statement.setString(2, endDate);
							}
						}  */
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
			public static void chkData(ArrayList<String> dataArr) throws IOException {
				String id = "";
				double cost = 0.0;
				for (String str : dataArr) {
					String[] items = str.split(";");
					id = items[2];
					cost = Olyutil.strToDouble(items[4]);
					if (cost > 100000) {
						//System.out.println("ContractID:" +  id + "--  Cost:" + cost +"--");
						System.out.println("ContractID: " +  id + "  Cost:" + cost );
					}
				}
			}
			/****************************************************************************************************************************************************/

			@Override
			protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				ArrayList<String> strArr = new ArrayList<String>();			
				strArr = getDbData(propFile,  sqlFile);
				//Olyutil.printStrArray(strArr, "ArrayData");
				chkData(strArr);	
			}
			 
}
