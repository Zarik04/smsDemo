package com.example.smsdemo;

import com.example.smsdemo.controllers.utils.DbUtil;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.Thread.*;

public class Test {
    public static void main(String[] args) {
        String currentDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        currentDate = formatter.format(LocalDate.now());
        System.out.println(currentDate);
//        DbUtil.setDatabase("sms_courses");
//        try {
//            ResultSet rest = DbUtil.dbExecuteQuery("SELECT * FROM s0000006 WHERE ID = 1;");
//              if (rest.next()) System.out.println("Enrolled");
//              else System.out.println("Not Enrolled");
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }

    }
}
