package org.gjgr.pig.chivalrous.excel.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gwd on 2016/5/23.
 * 未实现多线程模式
 */
public class SheetService {

    private Logger logger = LoggerFactory.getLogger(SheetService.class.getName());

    private Sheet sheet = null;

    public SheetService(Sheet sheet) {
        this.sheet = sheet;
    }

    public boolean modifyColumnByColumnWithVal(String val1, String val2, int iColumn, int jColumn) {
        for (Row row : sheet) {
            logger.info("the row number " + row.getRowNum() + " is modifying");
            if (row.getCell(iColumn).getStringCellValue().equals(val1)) {
                row.getCell(jColumn).setCellValue(val2);
                logger.debug(row.getCell(jColumn).getAddress() + "has change value.");
            }
        }
        return true;
    }

}
