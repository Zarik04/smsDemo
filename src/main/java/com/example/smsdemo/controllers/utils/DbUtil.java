package com.example.smsdemo.controllers.utils;

import java.sql.*;

public class DbUtil {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static Connection conn = null;
    private static String connStr;

    //    Connection Method
    public static void dbConnect() throws SQLException, ClassNotFoundException {

//        Registering the Driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException ex) {
            System.out.println("Where is your Oracle JDBC Driver?");
            ex.printStackTrace();
            throw ex;
        }


        System.out.println("Oracle JDBC Driver Registered!");

//      Trying to make connection with database
        try {
            conn = DriverManager.getConnection(connStr, "root", "");
            System.out.println("Successful Connection!");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            throw e;
        }

    }


    //Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception ex) {
            throw ex;
        }
    }


    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;

        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnect();
            System.out.println("Select statement: " + queryStmt + "\n");
            //Create statement
            stmt = conn.createStatement();
            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);

        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
//            if (resultSet != null) {
//                //Close resultSet
//                resultSet.close();
//            }
//            if (stmt != null) {
//                //Close Statement
//                stmt.close();
//            }
            //Close connection
//            dbDisconnect();

        }
        return resultSet;

    }

    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnect();
            //Create Statement
            stmt = conn.createStatement();
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
    }

    public static void setDatabase(String database) {
        connStr = "jdbc:mysql://localhost/" + database;
    }

    public static int getSize(ResultSet resultSet) throws Exception{
        int counter=0;
        while (resultSet.next()){
            counter++;
        }

        return counter;
    }
}
