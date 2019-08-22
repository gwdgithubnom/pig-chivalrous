package org.gjgr.pig.chivalrous.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.gjgr.pig.chivalrous.excel.service.PoiService;
import org.gjgr.pig.chivalrous.excel.service.SheetService;
import org.gjgr.pig.chivalrous.excel.util.VirtualEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gwd on 2016/5/23.
 */
public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class.getName());

    private static Workbook workbook = null;

    private static String path = VirtualEnvironment.System_Path;

    public static void main(String[] args) {

    }

    public void test1() {

        Sheet sheet1 = PoiService.openSheet(path + "/alibaba-result.xlsx");
        Sheet sheet2 = PoiService.openSheet(path + "/alibaba.xlsx");
        SheetService sheetService = new SheetService(sheet1);

        int i = 0;
        while (i < sheet2.getLastRowNum()) {

            Row row = sheet2.getRow(i++);
            logger.debug(row.getCell(2).toString());
            int n = (int) Double.parseDouble(row.getCell(2).toString());
            sheetService.modifyColumnByColumnWithVal(row.getCell(0).getStringCellValue(), n + "", 0, 1);

        }

        PoiService.saveSheet(sheet1.getWorkbook(), path + "/alibaba.xlsx");

    }

}
