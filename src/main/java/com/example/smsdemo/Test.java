package com.example.smsdemo;

import com.example.smsdemo.controllers.utils.DbUtil;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.Thread.*;

public class Test {
    public static void main(String[] args) {
//        try {
//            File file = new File(HelloApplication.class.getResource("course/resources/0000012/hello.txt").toURI());
//
//            System.out.println((double) Files.size(Path.of(file.getAbsolutePath()))/1024);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}
