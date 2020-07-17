package com.example.demo.util.word;

import com.example.demo.component.exception.WordException;
import com.example.demo.entity.upload.UploadFileDO;
import com.example.demo.util.container.ContainerUtil;
import com.example.demo.util.file.FileUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.util.word.WordUtil.TYPE.DOC;
import static com.example.demo.util.word.WordUtil.TYPE.DOCX;

/**
 * @author administrator
 * @date 2020/07/17
 * @description: 类描述: Word文档工具类
 **/
public class WordUtil {
    /**
     * Content Type
     */
    private static final String CONTENT_TYPE = "application/msword";

    private static final String REPLACE_DOLLAR = "$";
    private static final String REPLACE_BEGIN_BRACKETS = "{";
    private static final String REPLACE_END_BRACKETS = "}";
    private static final String REPLACE_BEGIN_STR = REPLACE_DOLLAR + REPLACE_BEGIN_BRACKETS;

    private WordUtil() {
    }

    @Getter
    @AllArgsConstructor
    public enum TYPE {
        /**
         * doc 文件后缀
         */
        DOC("doc"),
        /**
         * docx 文件后缀
         */
        DOCX("docx");

        private String suffix;

    }

    /**
     * 获取 excel 文件类型
     */
    public static WordUtil.TYPE getType(File file) {
        return getType(file.getName());
    }

    /**
     * 获取 word 文件类型
     */
    public static WordUtil.TYPE getType(String fileName) {
        if (Objects.equals(FileUtil.getSuffix(fileName), DOC.getSuffix())) {
            return DOC;
        }
        if (Objects.equals(FileUtil.getSuffix(fileName), DOCX.getSuffix())) {
            return DOCX;
        }
        return null;
    }

    /**
     * 设置 Word 文件流响应属性
     *
     * @param response HTTP响应对象
     * @param fileName Word 文件名
     */
    public static void setResponse(HttpServletResponse response, String fileName) {
        if (StringUtils.isNotBlank(fileName)) {
            // 中文解析
            fileName = new String(fileName.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
        }
        // 设置响应头
        // 兼容不同浏览器的中文乱码问题
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setContentType(CONTENT_TYPE);
    }


    /**
     * 设置Word文件流响应属性
     *
     * @param response HTTP响应对象
     * @param fileName Excel 文件名
     */
    public static void setResponseWithUrlEncoding(HttpServletResponse response, String fileName) {
        if (StringUtils.isNotBlank(fileName)) {
            // 中文解析
            try {
                fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        //设置响应头
        //兼容不同浏览器的中文乱码问题
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setContentType(CONTENT_TYPE);
    }


    /**
     * 导出WORD文件
     *
     * @param dataMapList  要导出的数据
     * @param outputStream 响应对象的输出流
     * @param uploadFileDO 空白的word文件
     * @throws IOException e
     */
    public static void downloadDateToWord(List<Map<String, Serializable>> dataMapList, OutputStream outputStream, UploadFileDO uploadFileDO) throws IOException {
        // 判断要导出的数据是否为空
        if (ContainerUtil.isNotEmpty(dataMapList)) {
            // 获取空白word文件
            File file = new File(uploadFileDO.getPath());
            XWPFDocument xwpfDocument = new XWPFDocument();
            for (Map<String, Serializable> dataMap : dataMapList) {
                //新建一个段落
                XWPFParagraph paragraph = xwpfDocument.createParagraph();
                //设置对齐方式
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                //创建文本对象
                XWPFRun run = paragraph.createRun();
                for (Map.Entry<String, Serializable> entry : dataMap.entrySet()) {
                    //获取导出的文本
                    String text = String.valueOf(entry.getValue());
                    if (StringUtils.isNotBlank(text)) {
                        //替换掉富文本中的标签
                        text = text.replaceAll("<.*?>", "");
                        //设置内容
                        run.setText(text);
                        //字体
                        run.setFontFamily("宋体");
                        //字体大小
                        run.setFontSize(11);
                        //添加换行
                        run.addCarriageReturn();
                    }
                }
                //将文本对象内容写入WORD文档
                xwpfDocument.write(new BufferedOutputStream(new FileOutputStream(file)));
            }
            //将word写入响应对象的输出流中
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
                byte[] b = new byte[bufferedInputStream.available()];
                int i;
                while ((i = bufferedInputStream.read(b)) != -1) {
                    outputStream.write(b, 0, i);
                }
                outputStream.flush();
            } finally {
                outputStream.close();
            }

        }
    }


    /**
     * 保存word文档
     *
     * @param document 文档对象
     * @param savePath 保存路径
     */
    public static void saveDocument(XWPFDocument document, String savePath) {
        try (OutputStream os = new FileOutputStream(savePath)) {
            document.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并行
     *
     * @param table    表格
     * @param row      合并的行
     * @param fromCell 起始列
     * @param toCell   终止列
     * @throws WordException e
     */
    public static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) throws WordException {
        XWPFTableRow tableRow = table.getRow(row);
        int size = tableRow.getTableCells().size();
        if (fromCell < 0 || toCell < fromCell || fromCell > size) {
            throw new WordException("列索引设置错误!");
        }
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cell = tableRow.getCell(cellIndex);
            if (cellIndex == fromCell) {
                // 合并的起点
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                // 合并的结束点
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 合并列
     *
     * @param table   表格
     * @param col     合并的列
     * @param fromRow 起始行
     * @param toRow   终止行
     * @throws WordException e
     */
    public static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) throws WordException {
        List<XWPFTableRow> rowList = table.getRows();
        if (fromRow < 0 || toRow < fromRow || toRow > rowList.size()) {
            throw new WordException("行索引设置错误!");
        }
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if (rowIndex == fromRow) {
                // 合并的起点
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                // 合并的结束点
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 获取标记的游标
     *
     * @param document      文档对象
     * @param mark          标记
     * @param cleanMarkFlag 是否清除标记内容
     * @return map
     */
    public static XmlCursor getMarkCursorByDocument(XWPFDocument document, String mark, boolean cleanMarkFlag) {
        List<XWPFParagraph> paragraphList = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphList) {
            List<XWPFRun> runList = paragraph.getRuns();
            for (XWPFRun run : runList) {
                String text = run.text();
                if (text.contains(mark)) {
                    if (cleanMarkFlag) {
                        // 替换占位符
                        text = text.replace(mark, "");
                        run.setText(text, 0);
                    }
                    XmlCursor cursor = paragraph.getCTP().newCursor();
                    // 新生成一个段落, 防止通过标记连续生成多张表的时候, 多张表是连在一起的
                    XWPFParagraph newParagraph = document.insertNewParagraph(cursor);
                    return newParagraph.getCTP().newCursor();
                }
            }
        }
        return null;
    }

    /**
     * 删除表格
     *
     * @param table 表格
     */
    public static void deleteTable(XWPFTable table) {
        List<XWPFTableRow> rows = table.getRows();
        for (int pos = rows.size(); pos >= 0; pos--) {
            table.removeRow(pos);
        }
    }

    /**
     * 复制表格
     *
     * @param targetTable 目标表格
     * @param sourceTable 源表格
     */
    public static void copyTable(XWPFTable targetTable, XWPFTable sourceTable) {
        if (null != sourceTable) {
            // 复制表格属性
            targetTable.getCTTbl().setTblPr(sourceTable.getCTTbl().getTblPr());
            // 复制行
            for (int pos = 0; pos < sourceTable.getRows().size(); pos++) {
                XWPFTableRow targetRow = targetTable.getRow(pos);
                XWPFTableRow sourceRow = sourceTable.getRow(pos);
                if (targetRow == null) {
                    targetRow = targetTable.createRow();
                }
                // 如果有,复制行
                copyTableRow(targetRow, sourceRow);
            }
        }
    }

    /**
     * 复制行
     *
     * @param targetRow 目标行
     * @param sourceRow 源行
     */
    public static void copyTableRow(XWPFTableRow targetRow, XWPFTableRow sourceRow) {
        if (sourceRow != null) {
            //复制行属性
            targetRow.getCtRow().setTrPr(sourceRow.getCtRow().getTrPr());
            //复制单元格
            for (int pos = 0; pos < sourceRow.getTableCells().size(); pos++) {
                XWPFTableCell targetCell = targetRow.getCell(pos);
                XWPFTableCell sourceCell = sourceRow.getCell(pos);
                if (targetCell == null) {
                    targetCell = targetRow.addNewTableCell();
                }
                copyTableCell(targetCell, sourceCell);
            }
        }
    }

    /**
     * 复制单元格
     *
     * @param targetCell 目标单元格
     * @param sourceCell 源单元格
     */
    public static void copyTableCell(XWPFTableCell targetCell, XWPFTableCell sourceCell) {
        // 复制单元格属性
        targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
        // 删除单元格段落
        for (int pos = 0; pos < targetCell.getParagraphs().size(); pos++) {
            targetCell.removeParagraph(pos);
        }
        //添加段落
        for (XWPFParagraph sourceParagraph : sourceCell.getParagraphs()) {
            XWPFParagraph targetParagraph = targetCell.addParagraph();
            copyParagraph(targetParagraph, sourceParagraph);
        }
    }

    /**
     * 复制段落
     *
     * @param targetParagraph 目标段落
     * @param sourceParagraph 源段落
     */
    public static void copyParagraph(XWPFParagraph targetParagraph, XWPFParagraph sourceParagraph) {
        // 复制段落属性
        targetParagraph.getCTP().setPPr(sourceParagraph.getCTP().getPPr());
        // 移除所有的run
        for (int pos = targetParagraph.getRuns().size() - 1; pos >= 0; pos--) {
            targetParagraph.removeRun(pos);
        }
        // copy新的run
        for (XWPFRun sourceRun : sourceParagraph.getRuns()) {
            XWPFRun targetRun = targetParagraph.createRun();
            copyRun(targetRun, sourceRun);
        }
    }

    /**
     * 复制 Run
     *
     * @param targetRun 目标Run
     * @param sourceRun 源Run
     */
    public static void copyRun(XWPFRun targetRun, XWPFRun sourceRun) {
        // 复制targetRun属性
        targetRun.getCTR().setRPr(sourceRun.getCTR().getRPr());
        // 复制文本
        targetRun.setText(sourceRun.text());
        //处理图片
        List<XWPFPicture> pictures = sourceRun.getEmbeddedPictures();
        try {
            for (XWPFPicture picture : pictures) {
                copyPicture(targetRun, picture);
            }
        } catch (WordException e) {
            e.printStackTrace();
        }

    }

    /**
     * 复制图片从sourcePicture到 targetRun
     *
     * @param targetRun     目标Run
     * @param sourcePicture 源图片
     * @throws WordException e
     */
    public static void copyPicture(XWPFRun targetRun, XWPFPicture sourcePicture) throws WordException {
        XWPFPictureData picData = sourcePicture.getPictureData();
        // 图片名称
        String fileName = picData.getFileName();
        try (InputStream picInIsData = new ByteArrayInputStream(picData.getData())) {
            int picType = picData.getPictureType();
            int width = (int) sourcePicture.getCTPicture().getSpPr().getXfrm().getExt().getCx();
            int height = (int) sourcePicture.getCTPicture().getSpPr().getXfrm().getExt().getCy();
            targetRun.addPicture(picInIsData, picType, fileName, width, height);
        } catch (InvalidFormatException | IOException e) {
            throw new WordException(e.getMessage());
        }
    }

    /**
     * 替换段落
     *
     * @param document 文档对象
     * @param map      数据map
     */
    public static void replaceParagraph(XWPFDocument document, Map<String, Object> map) {
        // 获取段落集合
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            // 段落中的小段
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                // 替换段落的文本
                replaceParagraphText(run, map);
            }
        }
    }

    /**
     * 替换段落的文本
     *
     * @param run 小段
     * @param map 数据map
     */
    public static void replaceParagraphText(XWPFRun run, Map<String, Object> map) {
        String text = run.text();
        if (checkText(text)) {
            text = replaceText(text, map);
            run.setText(text, 0);
        }
    }

    /**
     * 替换文本
     *
     * @param text      文本
     * @param paramsMap 参数map
     * @return String
     */
    public static String replaceText(String text, Map<String, Object> paramsMap) {
        // 截取 ${} 中的值
        String[] markContents = getMarkContent(text);
        if (ContainerUtil.isNotEmpty(markContents)) {
            // 去掉重复的
            List<String> markContentList = Arrays.stream(markContents).distinct().collect(Collectors.toList());
            for (String markContent : markContentList) {
                if (paramsMap.containsKey(markContent)) {
                    // 文字替换
                    Object obj = paramsMap.get(markContent);
                    text = text.replace(REPLACE_BEGIN_STR + markContent + REPLACE_END_BRACKETS, Objects.nonNull(obj) ? String.valueOf(obj) : "");
                } else {
                    // 没有要替换的数据把占位符去掉
                    text = text.replace(REPLACE_BEGIN_STR + markContent + REPLACE_END_BRACKETS, "");
                }
            }
        }
        return text;
    }

    /**
     * 获取标签中的内容 例如: ABC${name}abc${age}ABC -> [name,age]
     *
     * @param text 内容
     * @return String
     */
    public static String[] getMarkContent(String text) {
        return StringUtils.substringsBetween(text, REPLACE_BEGIN_STR, REPLACE_END_BRACKETS);
    }

    /**
     * 判断文本中是否包含$
     *
     * @param text 文本
     * @return 包含返回true, 不包含返回false
     */
    public static boolean checkText(String text) {
        return StringUtils.isNotBlank(text) && text.contains(REPLACE_DOLLAR);
    }

    /**
     * 删除表格尾列
     *
     * @param table    表格对象
     * @param delCount 删除的列数量
     */
    public static void deleteTableCol(XWPFTable table, int delCount) {
        if (delCount == 0) {
            return;
        }
        // 遍历行
        for (int pos = 0; pos < table.getRows().size(); pos++) {
            // 每一行
            XWPFTableRow row = table.getRow(pos);
            for (int index = 0; index < delCount; index++) {
                row.removeCell(row.getTableCells().size() - 1);
            }
            // 创建新的一行 添加在当前行和下一行的中间
            XWPFTableRow newRow = table.insertNewTableRow(pos + 1);
            // 复制行
            WordUtil.copyTableRow(newRow, row);
            // 删除模板行
            table.removeRow(pos);
        }
    }

    /**
     * 合并空白尾行
     *
     * @param table 表格
     */
    public static void mergeBlankEndRow(XWPFTable table) {
        // 合并行：如果一行中最后面的单元格全是空行,则合并它们
        List<XWPFTableRow> rowList = table.getRows();
        for (XWPFTableRow row : rowList) {
            // 记录从第几个单元格以后开始为空的
            int cellHasText = 0;
            List<XWPFTableCell> cellList = row.getTableCells();
            for (int pos = cellList.size() - 1; pos >= 0; pos--) {
                String text = row.getCell(pos).getText();
                if (StringUtils.isNotBlank(text)) {
                    cellHasText = pos;
                    break;
                }
            }
            // 开始合并
            for (int i = cellHasText; i < cellList.size(); i++) {
                if (i == cellHasText) {
                    row.getCell(i).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
                } else {
                    row.getCell(i).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
                }
            }
        }
    }

    /**
     * 合并具有相同值的列
     *
     * @param table 表格
     * @param col   合并的列索引
     */
    public static void mergeSameTextCol(XWPFTable table, int col) {
        List<XWPFTableRow> rowList = table.getRows();
        if (ContainerUtil.isNotEmpty(rowList)) {
            Map<String, List<Integer>> map = Maps.newHashMap();
            for (int index = 0; index < rowList.size(); index++) {
                XWPFTableRow row = table.getRow(index);
                if (row.getTableCells().size() > col) {
                    String text = table.getRow(index).getCell(col).getText();
                    if (map.containsKey(text)) {
                        map.get(text).add(index);
                    } else {
                        List<Integer> list = Lists.newArrayList();
                        list.add(index);
                        map.put(text, list);
                    }
                }
            }
            try {
                for (Map.Entry<String, List<Integer>> entry : map.entrySet()) {
                    List<Integer> list = entry.getValue();
                    int fromRow = Collections.min(list);
                    int toRow = Collections.max(list);
                    if (fromRow < toRow) {
                        WordUtil.mergeCellsVertically(table, col, fromRow, toRow);
                    }
                }
            } catch (WordException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 替换固定格式表格占位符
     *
     * @param table 表格对象
     * @param map   数据map
     */
    public static void replaceFixTable(XWPFTable table, Map<String, Object> map) {
        List<XWPFTableRow> rowList = table.getRows();
        if (ContainerUtil.isNotEmpty(rowList)) {
            for (XWPFTableRow row : rowList) {
                replaceFixTableRow(row, map);
            }
        }
    }

    /**
     * 替换固定格式行占位符
     *
     * @param row 行
     * @param map 数据map
     */
    public static void replaceFixTableRow(XWPFTableRow row, Map<String, Object> map) {
        List<XWPFTableCell> cellList = row.getTableCells();
        if (ContainerUtil.isNotEmpty(cellList)) {
            for (XWPFTableCell cell : cellList) {
                replaceFixCell(cell, map);
            }
        }
    }

    /**
     * 替换固定格式单元格占位符
     *
     * @param cell 单元格
     * @param map  数据map
     */
    public static void replaceFixCell(XWPFTableCell cell, Map<String, Object> map) {
        replaceTableText(cell, map);
    }

    /**
     * 替换表格指定的一行
     *
     * @param table       表格
     * @param list        数据列表
     * @param rowIndex    行索引
     * @param markContent 标记内容
     */
    public static void handlerTableRow(XWPFTable table, List<Object> list, int rowIndex, String markContent) {
        if (ContainerUtil.isNotEmpty(list)) {
            XWPFTableRow row = table.getRow(rowIndex);
            handlerTableRow(row, list, markContent);
        }
    }

    /**
     * 替换表格行
     *
     * @param row         表格行
     * @param list        数据列表
     * @param markContent 标记内容
     */
    public static void handlerTableRow(XWPFTableRow row, List<Object> list, String markContent) {
        int index = 0;
        for (XWPFTableCell cell : row.getTableCells()) {
            String text = cell.getText();
            if (checkText(text) && text.contains(markContent)) {
                Map<String, Object> map = Maps.newHashMap();
                if (index < list.size()) {
                    map.put(markContent, list.get(index++));
                }
                WordUtil.replaceTableText(cell, map);
            }
        }
    }

    /**
     * 处理表格
     *
     * @param table            表格
     * @param list             数据列表
     * @param templateRowIndex 模板行索引
     */
    public static void handlerTable(XWPFTable table, List<Map<String, Object>> list, int templateRowIndex) {
        handlerTable(table, list, templateRowIndex, templateRowIndex + 1);
    }

    /**
     * 处理表格
     *
     * @param table            表格
     * @param list             数据列表
     * @param templateRowIndex 模板行索引
     * @param topOfRow         新生成行在表格的行数
     */
    public static void handlerTable(XWPFTable table, List<Map<String, Object>> list, int templateRowIndex, int topOfRow) {
        // 模板行
        XWPFTableRow sourceRow = table.getRow(templateRowIndex);
        for (Map<String, Object> map : list) {
            // 创建新的一行
            XWPFTableRow newRow;
            if (topOfRow > 0) {
                newRow = table.insertNewTableRow(topOfRow++);
            } else {
                newRow = table.createRow();
            }
            // 复制行
            WordUtil.copyTableRow(newRow, sourceRow);
            for (XWPFTableCell cell : newRow.getTableCells()) {
                String text = cell.getText();
                if (checkText(text)) {
                    // 替换文本
                    text = replaceText(text, map);
                    // 删除表格单元原来数据
                    removeTableCellParagraphs(cell);
                    // 生成新内容
                    XWPFParagraph paragraph = cell.addParagraph();
                    // 段落水平居中
                    paragraph.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun run = paragraph.createRun();
                    run.setText(text);
                    // 内容垂直居中
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                }
            }
        }
        // 删除模版行
        table.removeRow(templateRowIndex);
    }

    /**
     * 替换表格中的文本
     *
     * @param cell 表格单元格
     * @param map  数据map
     */
    private static void replaceTableText(XWPFTableCell cell, Map<String, Object> map) {
        String text = cell.getText();
        if (checkText(text)) {
            text = replaceText(text, map);
            // 删除表格单元原来数据
            removeTableCellParagraphs(cell);
            // 新添加一个段落
            XWPFParagraph paragraph = new XWPFParagraph(cell.getCTTc().addNewP(), cell);
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun tableRun = paragraph.createRun();
            tableRun.setText(text);
            cell.setParagraph(paragraph);
            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        }
    }

    /**
     * 删除表格单元所有数据+样式
     *
     * @param cell 表格单元
     */
    private static void removeTableCellParagraphs(XWPFTableCell cell) {
        List<XWPFParagraph> paragraphs = cell.getParagraphs();
        if (ContainerUtil.isNotEmpty(paragraphs)) {
            for (int index = 0; index < paragraphs.size(); ) {
                cell.removeParagraph(index);
            }
        }
    }

    /**
     * 获取文档对象
     *
     * @param path 文件路径
     * @return r
     * @throws IOException e
     */
    public static XWPFDocument getDocument(String path) throws IOException {
        return getDocument(new File(path));
    }

    public static XWPFDocument getDocument(File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return getDocument(inputStream);
        }
    }

    public static XWPFDocument getDocument(InputStream inputStream) throws IOException {
        return new XWPFDocument(inputStream);
    }

    /**
     * 替换图片，通过书签的方式
     *
     * @param document 文档
     * @param map      图片map
     */
    public static void replaceParagraphPictureByBookmark(XWPFDocument document, Map<String, PictureData> map) {
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        if (ContainerUtil.isNotEmpty(paragraphs)) {
            for (XWPFParagraph paragraph : paragraphs) {
                replaceParagraphPictureByBookmark(paragraph, map);
            }
        }
    }

    /**
     * 替换图片，通过书签的方式
     *
     * @param paragraph 段落
     * @param map       图片map
     */
    private static void replaceParagraphPictureByBookmark(XWPFParagraph paragraph, Map<String, PictureData> map) {
        CTP ctp = paragraph.getCTP();
        List<CTBookmark> bookmarkStartList = ctp.getBookmarkStartList();
        if (ContainerUtil.isNotEmpty(bookmarkStartList)) {
            if (ContainerUtil.isNotEmpty(map)) {
                for (CTBookmark ctBookmark : bookmarkStartList) {
                    for (Map.Entry<String, PictureData> entry : map.entrySet()) {
                        String key = entry.getKey();
                        if (Objects.equals(key, ctBookmark.getName())) {
                            PictureData value = entry.getValue();
                            XWPFRun newParaRun = paragraph.createRun();
                            try (FileInputStream inputStream = new FileInputStream(value.getPath())) {
                                newParaRun.addPicture(
                                        inputStream,
                                        value.getPictureType(),
                                        value.getFileName(),
                                        Units.toEMU(value.getWidth()),
                                        Units.toEMU(value.getHeight()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public static int getTypeBySuffix(String suffix) throws WordException {
        suffix = suffix.toLowerCase();
        switch (suffix) {
            case "emf":
                return Document.PICTURE_TYPE_EMF;
            case "wmf":
                return Document.PICTURE_TYPE_WMF;
            case "pict":
                return Document.PICTURE_TYPE_PICT;
            case "jpeg":
                return Document.PICTURE_TYPE_JPEG;
            case "png":
                return Document.PICTURE_TYPE_PNG;
            case "dib":
                return Document.PICTURE_TYPE_DIB;
            case "gif":
                return Document.PICTURE_TYPE_GIF;
            case "tiff":
                return Document.PICTURE_TYPE_TIFF;
            case "eps":
                return Document.PICTURE_TYPE_EPS;
            case "bmp":
                return Document.PICTURE_TYPE_BMP;
            case "wpg":
                return Document.PICTURE_TYPE_WPG;
            default:
                throw new WordException("找不到类型!");
        }
    }


}
