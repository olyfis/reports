package com.olympus.reports.flash;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.olympus.olyutil.Olyutil;

@WebServlet("/flashsum")
public class GetFlashData extends HttpServlet {
	/*********************************************************************************************************************************************************/

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	/*********************************************************************************************************************************************************/
	public static ArrayList<String> getFlashData(String fileName) throws IOException {

		ArrayList<String> strArr = new ArrayList<String>();
		strArr = Olyutil.readInputFile(fileName);
		return (strArr);
	}
	/*********************************************************************************************************************************************************/
	public static JSONObject parseFlashData(ArrayList<String> strArr) throws IOException {
		ArrayList<String> rtnArr = new ArrayList<String>();
		JSONObject obj = new JSONObject();
		double msSum = 0;
		double mtSum = 0;
		double qsSum = 0;
		double qtSum = 0;
		double ysSum = 0;
		double ytSum = 0;
		
		String[] items = null;
		for (String str : strArr) {
			//System.out.println("*** STR=" + str);
			if (str.contains("Total")) {
				//System.out.println(str);	
				rtnArr =  Olyutil.customSplitSpecific(str);
				//System.out.println(str);
				 String ms = rtnArr.get(4).replace("$", "").replace(",", "").replace("\"", "")    ;
				 String mt = rtnArr.get(5).replace("$", "").replace(",", "").replace("\"", "");
				 String qs = rtnArr.get(8).replace("$", "").replace(",", "").replace("\"", "");
				 String qt = rtnArr.get(9).replace("$", "").replace(",", "").replace("\"", "");
				 String ys = rtnArr.get(12).replace("$", "").replace(",", "").replace("\"", "");
				 String yt = rtnArr.get(13).replace("$", "").replace(",", "").replace("\"", "");
				 
				//System.out.println("MS:" + rtnArr.get(4)  +  " - MT:" + rtnArr.get(5)  + " - QS:" + rtnArr.get(8)  +  " - QT:" + rtnArr.get(9) + " - YS:" + rtnArr.get(12)  +  " - YT:" + rtnArr.get(13) );
				//System.out.println("MS:" + ms +  " - MT:" + mt  + " - QS:" + qs  +  " - QT:" + qt + " - YS:" + ys  +  " - YT:" + yt );
				msSum += Olyutil.strToDouble(ms);			 
				mtSum += Olyutil.strToDouble(mt);
				qsSum += Olyutil.strToDouble(qs);
				qtSum += Olyutil.strToDouble(qt);
				ysSum += Olyutil.strToDouble(ys);
				ytSum += Olyutil.strToDouble(yt);
				
				
			} else {
				continue;
			}
		} // End For
		/*
		System.out.println("*****************************************");
		System.out.printf("msTotal: %10.2f\n",   msSum);
		System.out.printf("mtTotal: %10.2f\n",   mtSum);
		System.out.printf("qsTotal: %10.2f\n",   qsSum);
		System.out.printf("qtTotal: %10.2f\n",   qtSum);
		System.out.printf("ysTotal: %10.2f\n",   ysSum);
		System.out.printf("ytTotal: %10.2f\n",   ytSum);
		*/
		obj.put("msSum", round(msSum, 2));
		obj.put("mtSum", round(mtSum, 2));
		obj.put("qsSum", round(qsSum, 2));
		obj.put("qtSum", round(qtSum, 2));
		obj.put("ysSum", round(ysSum, 2));
		obj.put("ytSum", round(ytSum, 2));
		
		return(obj);
	}
	
	/*********************************************************************************************************************************************************/
	/*****************************************************************************************************/
	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String flashDataFile = "C:\\Java_Dev\\props\\flash\\flashData.csv";
		ArrayList<String> strArr = new ArrayList<String>();
		JSONObject obj = new JSONObject();
		PrintWriter out = res.getWriter();
		
		try {
			strArr = getFlashData(flashDataFile);
		} catch (IOException e) {
			  
			e.printStackTrace();
		}
		
		obj = parseFlashData(strArr);
		JSONArray jsonArr = new JSONArray();
		jsonArr.put(obj);
		
		
		double msTot =  (double) obj.get("msSum");
		double mtTot =  (double) obj.get("mtSum");
		double qsTot =  (double) obj.get("qsSum");
		double qtTot =  (double) obj.get("qtSum");
		double ysTot =  (double) obj.get("ysSum");
		double ytTot =  (double) obj.get("ytSum");
		
		/*
		System.out.printf("msTotal: %10d\n",   msTot);
		System.out.printf("mtTotal: %10d\n",   mtTot);
		System.out.printf("qsTotal: %10d\n",   qsTot);
		System.out.printf("qtTotal: %10d\n",   qtTot);
		System.out.printf("ysTotal: %10d\n",   ysTot);
		System.out.printf("ytTotal: %10d\n",   ytTot);
		*/
		out.write("[");
		for (int k = 0; k < jsonArr.length(); k++) {
	
			if (k == (jsonArr.length() - 1)) {
				out.write(jsonArr.getJSONObject(k).toString());
			} else {
				out.write(jsonArr.getJSONObject(k).toString() + ",");
			}
			// System.out.println("k=" + k + "Val:" + jsonArr.getJSONObject(k).toString() );
		}
		
		out.write("]");
	}
	/*********************************************************************************************************************************************************/

	
}
