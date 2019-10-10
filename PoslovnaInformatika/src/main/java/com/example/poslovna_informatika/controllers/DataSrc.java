package com.example.poslovna_informatika.controllers;

import javax.sql.DataSource;

import com.mysql.cj.jdbc.MysqlDataSource;


public class DataSrc {
	
	public static DataSource getMysqlDataSource() {
	    MysqlDataSource dataSource = new MysqlDataSource();

	    dataSource.setServerName("localhost");
	    dataSource.setPortNumber(3306);
	    dataSource.setDatabaseName("pi_db");
	    dataSource.setUser("root");
	    dataSource.setPassword("root");
	    return dataSource;
	  }

}
