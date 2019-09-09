package com.ijs.util;

import com.ijs.core.util.CacheUtil;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

/**
 * 针对企业财务的excel导入
 * @author zm
 */
public class ExcelImportUtil {

    /**
     *
     * @param filePath  需要读取的文件路径
     * @param column  指定需要获取的列数，例如第一列 1
     * @param startRow 指定从第几行开始读取数据
     * @param index 指定读取的sheet
     * @param  endRow 指定结束行
     * @return 返回读取列数据的set
     */
    public static HashSet<String> getColumnSet(String filePath, int column, int startRow , int endRow, int index){
        Workbook wb = readExcel(filePath); //文件
        Sheet sheet = wb.getSheetAt(index); //sheet
        int rownum = sheet.getPhysicalNumberOfRows(); //行数
        Row row = null;
        HashSet<String> result = new HashSet<>();
        String cellData = null;
        if(wb != null){
            for (int i=startRow-1; i<endRow; i++){
                System.out.println(i);
                row = sheet.getRow(i);
                if(row !=null){
                    cellData = (String) getCellFormatValue(row.getCell(column-1));
                    result.add(cellData.replaceAll(" ", ""));
                }else{
                    break;
                }
                System.out.println(cellData);
            }
        }
        return  result;
    }

    /**
     *
     * @param filePath 需要读取的文件路径
     * @param column 指定需要获取的列数，例如第一列 1
     * @param startRow 指定从第几行开始读取数据
     * @param index 指定读取的sheet
     * @return  返回读取列数据的set
     */
    public static HashSet<String> getColumnSet(String filePath, int column, int startRow, int index){
        Workbook wb = readExcel(filePath); //文件
        Sheet sheet = wb.getSheetAt(index); //sheet
        int rownum = sheet.getPhysicalNumberOfRows(); //行数
        System.out.println("sumrows " + rownum);

        return  getColumnSet(filePath, column, startRow , rownum-1, index);
    }



    //读取excel
    public static Workbook readExcel(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellTypeEnum()){
                case NUMERIC:{
                    cell.setCellType(CellType.STRING);  //将数值型cell设置为string型
                    cellValue = cell.getStringCellValue();
                    break;
                }
                case FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }

    /**
     * 根据行和列获取指定单元格的内容
     * @param wb 读取后的Workbook
     * @param row 指定的行
     * @param col 指定的列
     * @param index 指定的sheet
     * @return
     */
    public static Object getResultByRowAndCol(Workbook wb, int row, int col, int index) throws Exception{
        try {
            Sheet sheet = wb.getSheetAt(index);
            Cell cell = sheet.getRow(row-1).getCell(col-1);
            Object msg = getCellFormatValue(cell);
            return msg;
        } catch (Exception e) {
            throw e;
        }
    }
    public static void main(String[] args) {
        try {
//            HashSet<String> columnSet = ExcelImportUtil.getColumnSet("E:/baoxiao.xlsx", 1, 90, 0);  //读取第一列的从第90行开始往后的数据 到set
//            System.out.println(columnSet.size());
//            System.out.println("--------------"+columnSet.toString());
            Workbook wb;
            String filePath = "E:/baoxiao.xlsx";
            wb = ExcelImportUtil.readExcel(filePath);
            Object shijian = ExcelImportUtil.getResultByRowAndCol(wb,2,4,0);
            Object zongzichan = ExcelImportUtil.getResultByRowAndCol(wb,37,4,0);
            Object zongfuzhai = ExcelImportUtil.getResultByRowAndCol(wb,28,7,0);
            Object jingzichan = ExcelImportUtil.getResultByRowAndCol(wb,35,7,0);
            Object yingye_shouru = ExcelImportUtil.getResultByRowAndCol(wb,5,7,1);
            Object yingye_chengben = ExcelImportUtil.getResultByRowAndCol(wb,6,7,1);
            Object jinglirun = ExcelImportUtil.getResultByRowAndCol(wb,21,7,1);

            Calendar currYear = Calendar.getInstance();
            Calendar calendar = new GregorianCalendar(1900,0,-1);
            calendar.add(Calendar.DATE,Integer.valueOf(shijian.toString()));
            System.out.println(currYear.get(Calendar.YEAR)-1);
            Timestamp ts = new Timestamp(calendar.getTime().getTime());

            System.out.println("shijian="+ts);
            System.out.println("zongzichan="+zongzichan);
            System.out.println("zongfuzhai="+zongfuzhai);
            System.out.println("jingzichan="+jingzichan);
            System.out.println("yingye_shouru="+yingye_shouru);
            System.out.println("yingye_chengben="+yingye_chengben);
            System.out.println("jinglirun="+jinglirun);
        } catch (Exception e) {
            System.out.println("导入excel时产生了异常" + e);
        }
    }
}
