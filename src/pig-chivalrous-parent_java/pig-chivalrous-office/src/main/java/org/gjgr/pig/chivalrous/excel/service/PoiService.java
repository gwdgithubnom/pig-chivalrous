package org.gjgr.pig.chivalrous.excel.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gwd on 2016/5/23.
 * through this service can get the sheet and workbook object.
 * 未实现多线程的模式
 */

public class PoiService {

    private static Logger logger = LoggerFactory.getLogger(PoiService.class.getName());


    public static Workbook openWorkbook(FileInputStream fileInputStream) {

        Workbook workbook = null;

        try {
            workbook = WorkbookFactory.create(fileInputStream);
        } catch (IOException e) {
            logger.info(e.toString());
        } catch (InvalidFormatException e) {
            logger.info(e.toString());
        }

        return workbook;

    }

    public static Workbook openWorkbook(String path) {

        Workbook workbook = null;
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            logger.info(e.toString());
        }
        workbook = openWorkbook(fileInputStream);
        return workbook;
    }


    public static Sheet openSheet(FileInputStream fileInputStream) {

        try {
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            int n = workbook.getNumberOfSheets();
            String name = workbook.getSheetName(0);
            logger.info("workbook has sheet number:" + n + ", the first sheet:" + name);
            Sheet sheet = workbook.getSheet(name);
            return sheet;
        } catch (IOException e) {
            logger.error(e.toString());
            return null;
        } catch (InvalidFormatException e) {
            logger.equals(e);
            return null;
        }

    }

    public static Sheet openSheet(String path) {

        Sheet sheet = null;

        try {

            FileInputStream fileInputStream = new FileInputStream(path);
            sheet = openSheet(fileInputStream);
            fileInputStream.close();

        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }

        return sheet;

    }

    public static boolean saveSheet(Workbook workBook, FileOutputStream fileOutputStream) {

        try {

            workBook.write(fileOutputStream);
            logger.debug("WorkBook has saved.");
            fileOutputStream.close();

        } catch (IOException e) {
            logger.error(e.toString());
            return false;
        }

        return true;

    }

    public static boolean saveSheet(Workbook workbook, String path) {

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            logger.error(e.toString());
        }

        saveSheet(workbook, fileOutputStream);

        return true;
    }

}
