package com.example.smsdemo;

import com.example.smsdemo.controllers.utils.DbUtil;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.Thread.*;

public class Test {
    public static void main(String[] args) throws Exception {
        File fromFile = new File("course resources/0000013/r0000001/0000001_hello.txt");
        File toFile = new File("course resources/0000012/hello copy.txt");
        toFile.createNewFile();
        FileChannel src = new FileInputStream(fromFile.getAbsolutePath()).getChannel();
        FileChannel dest = new FileOutputStream(toFile.getAbsolutePath()).getChannel();
        try {
            dest.transferFrom(src, 0, src.size());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            src.close();
            dest.close();
        }

    }
}
