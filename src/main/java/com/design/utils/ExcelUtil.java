package com.design.utils;

import com.design.base.eunms.ExcelEnum;
import com.design.handler.BusinessException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public class ExcelUtil {

    public static <T> InputStream create(final InputStream inputStream, final List<T> datas){
        try {
            if(null == inputStream){
                throw new BusinessException(ExcelEnum.E00002);
            }
            // 取得欄位
            final Map<String, List<String>> columnMaps = getColumnMaps(datas);
            // 初始化
            Workbook workbook = new XSSFWorkbook(inputStream);
            // 設定內容
            workbook = setWorkbook(workbook, columnMaps);
            return output(workbook);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(ExcelEnum.E00001);
        }
    }

    // 設定內容
    private static Workbook setWorkbook(final Workbook workbook, final Map<String, List<String>> columnMaps){
        try{
            Integer columnIndex;
            List<String> values;
            Row dataRow;
            Cell dataCell;
            CellStyle dataCellStyle;
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(1);
            if (null == columnMaps || columnMaps.isEmpty()) {
                for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                    dataCell = headerRow.getCell(i);
                    if (null == dataCell) {
                        dataCell = headerRow.createCell(i);
                    }
                    dataCell.setCellStyle(null);
                    dataCell.setCellValue("");
                }
                return workbook;
            }
            for (String key : columnMaps.keySet()) {
                columnIndex = findColumnIndex(headerRow, key);
                if(-1 == columnIndex){
                    continue;
                }
                dataCellStyle = headerRow.getCell(columnIndex).getCellStyle();
                values = columnMaps.get(key);
                for (int i = 0; i < values.size(); i++) {
                    dataRow = sheet.getRow(i + 1);
                    if (null == dataRow) {
                        dataRow = sheet.createRow(i + 1);
                    }
                    dataCell = dataRow.createCell(columnIndex);
                    dataCell.setCellStyle(dataCellStyle);
                    dataCell.setCellValue(values.get(i));
                }
            }
            return workbook;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(ExcelEnum.E00005);
        }
    }

    // 取得索引
    private static int findColumnIndex(final Row headerRow, String key) {
        Cell cell;
        key = String.format("%s%s%s", "{{", key, "}}");
        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            cell = headerRow.getCell(i);
            if (null != cell && key.equals(cell.getStringCellValue())) {
                return i;
            }
        }
        return -1;
    }

    // 取得所有欄位名稱
    private static <T> Map<String, List<String>> getColumnMaps(final List<T> datas){
        try{
            final Map<String, List<String>> columns = new HashMap<>();
            Field [] fields;
            String key;
            String value;
            List<String> values;
            for(T t : datas){
                fields = t.getClass().getDeclaredFields();
                for(Field field : fields){
                    field.setAccessible(true);
                    key = field.getName();
                    value = formatToString(field.get(t));
                    values = columns.get(key);
                    if(null == values){
                        values = new ArrayList<>();
                    }
                    values.add(value);
                    columns.put(key, values);
                }
            }
            return columns;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(ExcelEnum.E00003);
        }
    }

    // 欄位轉字串
    private static String formatToString(final Object value){
        try{
            if(null == value){
                return "";
            }
            if(value instanceof UUID){
                return value.toString();
            }
            if(value instanceof String){
                return value.toString();
            }
            if(value instanceof Integer){
                return String.valueOf(value);
            }
            if(value instanceof Double){
                return String.valueOf(value);
            }
            if(value instanceof BigDecimal){
                return value.toString();
            }
            if(value instanceof Instant){
                return InstantUtil.to((Instant) value);
            }
            return "";
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(ExcelEnum.E00004);
        }
    }

    // 轉換output
    private static InputStream output(final Workbook workbook){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            byte[] bytes = bos.toByteArray();
            return new ByteArrayInputStream(bytes);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(ExcelEnum.E00006);
        }
    }

}
