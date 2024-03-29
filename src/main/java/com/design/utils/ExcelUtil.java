package com.design.utils;

import com.design.base.eunms.ExcelEnum;
import com.design.handler.BusinessException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public class ExcelUtil {

    public static <T> InputStream create(InputStream inputStream, List<T> datas, Map<String, String> params){
        try {
            if(null == inputStream){
                throw new BusinessException(ExcelEnum.E00002);
            }
            // 取得欄位
            final Map<String, List<String>> columnMaps = getColumnMaps(datas);
            // 初始化
            Workbook workbook = new XSSFWorkbook(inputStream);
            // 設定內容
            setWorkbook(workbook, columnMaps, datas.size());
            setWorkbook(workbook, params);
            return output(workbook);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(ExcelEnum.E00001);
        }
    }

    // 設定內容
    private static Workbook setWorkbook(Workbook workbook, Map<String, List<String>> columnMaps, Integer dataSize){
        try{
            Integer columnIndex;
            List<String> values;
            Row dataRow;
            Cell dataCell;
            CellStyle dataCellStyle;
            Sheet sheet = workbook.getSheetAt(0);
            int startRowIndex = 11;
            Row headerRow = sheet.getRow(startRowIndex);
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
            // 結算區向下移動
            sheet.shiftRows(startRowIndex + 1, sheet.getLastRowNum(), dataSize - 1, true, true);
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            for (XSSFShape shape : drawing.getShapes()) {
                if (shape instanceof XSSFPicture) {
                    XSSFPicture pic = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = pic.getClientAnchor();
                    if (anchor.getRow1() >= startRowIndex) {
                        anchor.setRow1(anchor.getRow1() + dataSize - 1);
                        anchor.setRow2(anchor.getRow2() + dataSize - 1);
                    }
                }
            }
            short height = headerRow.getHeight();
            for (String key : columnMaps.keySet()) {
                columnIndex = findColumnIndex(headerRow, key);
                if(-1 == columnIndex){
                    continue;
                }
                dataCellStyle = headerRow.getCell(columnIndex).getCellStyle();
                values = columnMaps.get(key);
                for (int i = 0; i < values.size(); i++) {
                    dataRow = sheet.getRow(i + startRowIndex);
                    if (null == dataRow) {
                        dataRow = sheet.createRow(i + startRowIndex);
                        dataRow.setHeight(height);
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

    private static Workbook setWorkbook(Workbook workbook, Map<String, String> params) {
        try {
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        if (cell.getCellType() != CellType.STRING) {
                            continue;
                        }
                        setValue(cell, params);
                    }
                }
            }
            return workbook;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException(ExcelEnum.E00005);
        }
    }

    // 取得索引
    private static int findColumnIndex(Row headerRow, String key) {
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
    private static <T> Map<String, List<String>> getColumnMaps(List<T> datas){
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
    private static String formatToString(Object value){
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
    private static InputStream output(Workbook workbook){
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

    // 設定參數
    private static void setValue(Cell cell, Map<String, String> params){
        try{
            String cellValue = cell.getStringCellValue();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (cellValue.contains("{{" + key + "}}")) {
                    cellValue = cellValue.replaceAll("\\{\\{" + key + "\\}\\}", value);
                    cell.setCellValue(cellValue);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(ExcelEnum.E00005);
        }
    }

}
