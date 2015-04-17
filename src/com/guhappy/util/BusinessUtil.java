package com.guhappy.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BusinessUtil {
	private static final int BUFFER_SIZE = 16 * 1024;

	// This is synched so we only do one request at a time
	// If yahoo doesn't return stock info we will try to return it from the map
	// in memory
	public static void main(String[] args) {
		List<Stock> regionlist = new ArrayList<Stock>();
		URL yahoofin = null;
		int currentLineNumber = 1;
		CSVParser parser = new CSVParser();
		// now do the region "0"
		try {
			InputStream in = null;
			StringBuffer buf = new StringBuffer();
			in = new BufferedInputStream(new FileInputStream(
					"C:/Users/test/Desktop/china.csv"), BUFFER_SIZE);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));

			String line = reader.readLine();
			while (line != null) {
				Object[] ob = parser.parse(line);
				regionlist.add(new Stock(4, ob[0].toString(),ob[0].toString()+"\t\t"+ob[1].toString()));
				line = reader.readLine();
/*				if (currentLineNumber < 200) {
					buf.append((String) ob[0]).append("+");
					line = reader.readLine();
					currentLineNumber++;
				} else {
					buf.append((String) ob[0]);
					currentLineNumber = 1;
					yahoofin = new URL(
							"http://download.finance.yahoo.com/d/quotes.csv?s="
									+ buf.toString() + "&f=nsj1&e=.csv");
					URLConnection yc = yahoofin.openConnection();
					BufferedReader inreader = new BufferedReader(
							new InputStreamReader(yc.getInputStream()));
					String inputLine;
					while ((inputLine = inreader.readLine()) != null) {
						String[] yahooStockInfo = inputLine.split("\",");
						regionlist.add(new Stock(8,yahooStockInfo[1].replaceAll("\"", ""), yahooStockInfo[1].replaceAll("\"", "")	+ "\t\t" + yahooStockInfo[0].replaceAll("\"", "")));
						if (null != yahooStockInfo[2]
								&& !yahooStockInfo[2].equals("N/A")){
							String a = yahooStockInfo[2].toString();
							if (a.indexOf("M") >0 && Float.valueOf(a.substring(0, a.indexOf("M")))>=52.0){
								regionlist.add(new Stock(8,yahooStockInfo[1].replaceAll("\"", ""), yahooStockInfo[1].replaceAll("\"", "")	+ "\t\t" + yahooStockInfo[0].replaceAll("\"", "")));
							}
						}
					}
					buf.delete(0, buf.length());
					inreader.close();
				}*/
			}
			// close the bufferred reader 600363
			reader.close();
			in.close();
			
			Connection oraCon = null;
			StringBuffer sql = new StringBuffer();
			try {
				try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
				} 
				catch (ClassNotFoundException ex)
					{}
				oraCon = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.13:1521:orcl", "guhappy","guhappy");
				oraCon.setAutoCommit(false);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			sql.append("insert into REF_REGIONLIST (regionindex, ticker, name) VALUES (?,?,?)");
			PreparedStatement pst= oraCon.prepareStatement(sql.toString());
			
			Iterator<Stock> it = regionlist.iterator();
			while( it.hasNext()){
				Stock st = (Stock) it.next();
				pst.setInt(1, st.getRegionindex());
				pst.setString(2,st.getTicker());
				pst.setString(3, st.getName());
				pst.addBatch();
			}
			pst.executeBatch();
			oraCon.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
