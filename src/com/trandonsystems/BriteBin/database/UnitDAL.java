package com.trandonsystems.BriteBin.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnitDAL {

	public static long saveMessage(int unitId, byte[] msg, int userId) throws SQLException{

		System.out.println("UnitDAL.saveMessage(unitId, msg)");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			System.out.println("ERROR: Can't create instance of driver" + ex.getMessage());
		}

		String spCall = "{ call SaveUnitMessage(?, ?, ?) }";
		System.out.println("SP Call: " + spCall);

		long id = 0;
		try (Connection conn = DriverManager.getConnection(Util.connUrl, Util.username, Util.password);
				CallableStatement spStmt = conn.prepareCall(spCall)) {

			spStmt.setInt(1, unitId);
			spStmt.setBytes(2, msg);
			spStmt.setInt(3, userId);
			ResultSet rs = spStmt.executeQuery();
			
			if (rs.next()) {
				id = rs.getInt("id");
			}

		} catch (SQLException ex) {
			System.out.println("ERROR: " + ex.getMessage());
			throw ex;
		}
		
		return id;
	}

}
