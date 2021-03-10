package cn.tomleung.csv; // Importa as bibliotecas do arquivo javacvs.jar

import java.sql.Connection; // Importa as bibliotecas do arquivo sqkjbdc.jar
import java.sql.DriverManager; // Importa as bibliotecas também do arquivo sqkjbdc.jar

public class DBConnector {
	// Seleocao driver JDBC
	String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	/*
	 * Configuraçes de conexo com server SQL
	 */
	String dbURL = "jdbc:sqlserver://localhost:1433;DatabaseName=";
	String userName = "";
	String userPwd = "";
	Connection dbConn;

	//método da classe para conectar
	public Connection connect(){
		try{
			Class.forName(driverName);
			dbConn=DriverManager.getConnection(dbURL,userName,userPwd);
		}catch(Exception e){
			e.printStackTrace();
		}
		return dbConn;
	}
}
