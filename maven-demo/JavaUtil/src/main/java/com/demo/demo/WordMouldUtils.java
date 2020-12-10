package com.demo.demo;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordMouldUtils
{
    public static XWPFDocument readFile(String filePath) throws IOException
    {
        return new XWPFDocument(POIXMLDocument.openPackage(filePath));
    }

    /**
     * 替换段落文本
     *
     * @param document docx解析对象
     * @param textMap  需要替换的信息集合
     */
    public static void reverseText(XWPFDocument document, Map<String, Object> textMap)
    {
        Assert.notNull(document, "Parameter: document");
        Assert.notNull(textMap, "Parameter: textMap");
        //获取段落集合
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        if (CollectionUtils.isNotEmpty(paragraphs))
        {
            for (XWPFParagraph paragraph : paragraphs)
            {
                reverseParagraph(paragraph, textMap);
            }
        }
    }

    /**
     * 替换表格对象方法
     *
     * @param document docx解析对象
     * @param textMap  需要替换的信息集合
     */
    public static void reverseTable(XWPFDocument document, Map<String, Object> textMap)
    {
        Assert.notNull(document, "Parameter: document");
        Assert.notNull(textMap, "Parameter: textMap");

        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        if (CollectionUtils.isNotEmpty(tables))
        {
            for (XWPFTable table : tables)
            {
                System.out.println((">>> Table_getText() = " + table.getText()));
                if (ifReplace(table.getText()))
                {
                    List<XWPFTableRow> rows = table.getRows();
                    if (CollectionUtils.isEmpty(rows))
                    {
                        continue;
                    }

                    for (XWPFTableRow row : rows) // 修改表格中原有需要替换的数据
                    {
                        List<XWPFTableCell> cells = row.getTableCells();
                        if (CollectionUtils.isEmpty(cells))
                        {
                            continue;
                        }
                        for (XWPFTableCell cell : cells)
                        {
                            //判断单元格是否需要替换
                            System.out.println(">>> Table_Cell_getText() = " + cell.getText());
                            if (ifReplace(cell.getText()))
                            {
                                List<XWPFParagraph> paragraphs = cell.getParagraphs();
                                for (XWPFParagraph paragraph : paragraphs)
                                {
                                    reverseParagraph(paragraph, textMap);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 表格新增数据行
     * @param listMap 新增的表格行记录
     * @param tableName 表格名称
     */
    public static void addTableRowRecord(XWPFDocument document, List<Map<String, Object>> listMap, String tableName)
    {
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        if (CollectionUtils.isNotEmpty(tables))
        {
            for (XWPFTable table : tables)
            {
                System.out.println((">>> Table_getText() = " + table.getText()));
                if (ifReplace(table.getText()))
                {
                    List<XWPFTableRow> rows = table.getRows();
                    if (CollectionUtils.isEmpty(rows) && rows.size() > 1)
                    {
                        continue;
                    }
                    XWPFTableRow firstRow = rows.get(0);// 表格第一行存储表格名称
                    String entityTableName = firstRow.getCell(0).getText().trim();
                    XWPFTableRow firstValueRow = rows.get(1);

                    if (StringUtils.isEmpty(entityTableName) || !entityTableName.equals(tableName))
                    {
                        continue;
                    }
                    if (CollectionUtils.isNotEmpty(listMap)) // 新增记录
                    {

                        for (Map<String, Object> entity : listMap)
                        {
                            XWPFTableRow nextRow = table.createRow();//表格追加一行记录
                            copyTableRow(table, firstValueRow, nextRow);
                            XWPFTableRow next = rows.get(rows.size() - 1);

                            List<XWPFTableCell> cells = next.getTableCells();
                            if (CollectionUtils.isEmpty(cells))
                            {
                                continue;
                            }
                            for (XWPFTableCell cell : cells)
                            {
                                //判断单元格是否需要替换
                                System.out.println(">>> Table_Cell_getText() = " + cell.getText());
                                if (ifReplace(cell.getText()))
                                {
                                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                                    for (XWPFParagraph paragraph : paragraphs)
                                    {
                                        reverseParagraph(paragraph, entity);
                                    }
                                }
                            }
                        }
                        table.removeRow(1);
                    }

                    table.removeRow(1);// 移除模板第一行数据
                }
            }
        }
    }

    /**
     * 替换段落中的内容
     */
    public static void reverseParagraph(XWPFParagraph paragraph, Map<String, Object> textMap)
    {
        Assert.notNull(paragraph, "Parameter: paragraph");
        Assert.notNull(textMap, "Parameter: textMap");

        //判断此段落是否需要进行替换
        String text = paragraph.getText();
        System.out.println(">>> Paragraph_getText() = " + paragraph.getText());
        if (ifReplace(text))
        {
            List<XWPFRun> runs = paragraph.getRuns();
            if (CollectionUtils.isNotEmpty(runs))
            {
                for (XWPFRun run : runs)
                {
                    System.out.println(">>> Run_toString() = " + run.toString());

                    List<String> keyList = findReplaceKey(run.toString());
                    if (CollectionUtils.isNotEmpty(keyList))
                    {
                        keyList.forEach(key -> {
                            String runStr = run.toString();
                            Object ob = textMap.getOrDefault(key, null);
                            if (ob instanceof String)
                            {
                                runStr = runStr.replace(key, (String) ob);
                                run.setText(runStr, 0);
                            }
                            if (ob instanceof PictureParamObject)
                            {
                                runStr = runStr.replace(key, "");
                                run.setText(runStr, 0);
                                PictureParamObject pic = (PictureParamObject) ob;
                                int width = Units.pixelToEMU(pic.getWidth());
                                int height = Units.pixelToEMU(pic.getHeight());
                                int picType = getPictureType(pic.getFileType());
                                String fileName = pic.getFileName();

//                                byte[] byteArray = pic.getByteArray();
//                                ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
//                                XWPFDocument document = paragraph.getDocument();
                                try
                                {
                                    FileInputStream inputStream = new FileInputStream(pic.getFile());
//                                    String ind = document.addPictureData(inputStream, picType);
                                    run.addPicture(inputStream, picType, fileName, width, height);
                                } catch (InvalidFormatException | IOException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * 判断文本中时候包含${@param replace}
     */
    public static boolean ifReplace(String context)
    {
        return context != null && context.replaceAll("[\r\n]", "").matches(".*\\$\\{.*}.*");
    }

    /**
     * 获取文本中时候包含${@param replace}
     */
    public static List<String> findReplaceKey(String context)
    {
        if (context != null)
        {
            Pattern p = Pattern.compile(".*(\\$\\{.*}).*");
            Matcher m = p.matcher(context);
            List<String> list = new LinkedList<>();
            if (m.find() && m.groupCount() >= 1)
            {
                for (int i = 1; i <= m.groupCount(); i++)
                {
                    list.add(m.group(i));
                }
            }
            return list;
        }
        return null;
    }

    /**
     * 根据图片类型，取得对应的XWPFDocument图片类型编号
     *
     * @param picType 图片类型
     * @return int
     */
    private static int getPictureType(String picType)
    {
        int res = XWPFDocument.PICTURE_TYPE_PICT;
        if (picType != null)
        {
            if (picType.equalsIgnoreCase("png"))
            {
                res = XWPFDocument.PICTURE_TYPE_PNG;
            } else if (picType.equalsIgnoreCase("dib"))
            {
                res = XWPFDocument.PICTURE_TYPE_DIB;
            } else if (picType.equalsIgnoreCase("emf"))
            {
                res = XWPFDocument.PICTURE_TYPE_EMF;
            } else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg"))
            {
                res = XWPFDocument.PICTURE_TYPE_JPEG;
            } else if (picType.equalsIgnoreCase("wmf"))
            {
                res = XWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }

    public static void copyTableRow(XWPFTable table, XWPFTableRow srcRow, XWPFTableRow objRow)
    {
        List<XWPFTableCell> objCells = objRow.getTableCells();
        List<XWPFTableCell> srcCells = srcRow.getTableCells();

        for (int i = 0; i < srcCells.size(); i++)
        {
            XWPFTableCell objCell = objCells.get(i);
            XWPFTableCell srcCell = srcCells.get(i);

            objCell.setText(new String(srcCell.getText()));
        }
    }
}
