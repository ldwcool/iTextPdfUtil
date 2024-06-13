package cool.ldw.pdf.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * pdf 工具类
 *
 * @author ldwcool
 */
public class PdfExportUtil {

    /**
     * 获取页眉字体样式
     *
     * @return 页眉字体样式
     */
    public static Font getPageHeaderFont() {
        return new Font(PdfExportUtil.getSimsunBaseFont(), 13f, Font.NORMAL);
    }

    /**
     * 获取水印字体样式
     *
     * @return 页眉字体样式
     */
    public static Font getPageWatermarkFont() {
        return new Font(PdfExportUtil.getSimsunBaseFont(), 130, Font.NORMAL, new BaseColor(223, 223, 223, 120));
    }

    /**
     * 获取正文字体样式
     * 字体：宋体，字号：小四，段落：左对齐，首行缩进2个字符，行距：固定值23磅（pt）
     *
     * @return 正文字体
     */
    public static Font getMainTextFont() {
        return new Font(getSimsunBaseFont(), 12, Font.NORMAL);
    }

    /**
     * 获取正文段落
     * 字体：宋体，字号：小四，段落：左对齐，首行缩进2个字符，行距：固定值23磅（pt）
     *
     * @param content 段落内容
     * @return 正文段落
     */
    public static Paragraph getMainTextParagraph(String content) {
        Paragraph paragraph = new Paragraph(content, getMainTextFont());
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setFirstLineIndent(24);
        paragraph.setLeading(23);
        paragraph.setSpacingAfter(11.5f);
        paragraph.setSpacingBefore(11.5f);
        return paragraph;
    }

    /**
     * 创建表格
     *
     * @param columnNum 表格列个数
     * @return 表格
     */
    public static PdfPTable createTable(int columnNum) {
        float[] cellsWidthConfig = new float[columnNum];
        Arrays.fill(cellsWidthConfig, 1);
        return createTable(columnNum, cellsWidthConfig);
    }

    /**
     * 创建表格
     *
     * @param columnNum        表格列个数
     * @param cellsWidthConfig 表格列宽配置
     * @return 表格
     */
    public static PdfPTable createTable(int columnNum, float[] cellsWidthConfig) {
        PdfPTable table = new PdfPTable(columnNum);
        table.setWidthPercentage(100);
        try {
            table.setWidths(cellsWidthConfig);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return table;
    }

    /**
     * 初始化 单元格
     *
     * @param content 单元格内容
     * @return 单元格
     */
    public static PdfPCell initPdfCell(String content) {
        return initPdfCell(content, getMainTextFont());
    }

    /**
     * 初始化 单元格
     *
     * @param content 单元格内容
     * @param font    字体
     * @return 单元格
     */
    public static PdfPCell initPdfCell(String content, Font font) {
        Phrase phrase = new Phrase(content, font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(PdfPCell.LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setMinimumHeight(22);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(5);
        return cell;
    }

    /**
     * 初始化 表格头单元格
     * 黑体、10号、不加粗
     *
     * @param content 单元格内容
     * @return 单元格
     */
    public static PdfPCell initPdfCellHeader(String content) {
        Font font = new Font(getSimheiBaseFont(), 10, Font.NORMAL);
        return initPdfCellHeader(content, font);
    }

    /**
     * 初始化 表格头单元格
     *
     * @param content 单元格内容
     * @param font    字体
     * @return 单元格
     */
    public static PdfPCell initPdfCellHeader(String content, Font font) {
        return initPdfCell(content, font);
    }

    /**
     * 设置单元格内容，字段名和字段内容
     * 字段单元格占据一个单元格
     * 字段内容单元格占据两个单元格
     *
     * @param table        表格
     * @param fieldName    字段名
     * @param fieldContent 字段内容
     */
    public static void setTableCell(PdfPTable table, String fieldName, String fieldContent) {
        setTableCell(table, fieldName, fieldContent, "1-2");
    }

    /**
     * 设置单元格内容，字段名和字段内容
     *
     * @param table        表格
     * @param fieldName    字段名
     * @param fieldContent 字段内容
     * @param span         单元格合并设置
     *                     如：1-2 fieldName 合并1列；fieldContent 合并2列
     *                     2-4 fieldName 合并2列；fieldContent 合并4列
     */
    public static void setTableCell(PdfPTable table, String fieldName, String fieldContent, String span) {
        Integer[] spanArr = Convert.toIntArray(span.split("-"));

        // 单元格字段名
        PdfPCell cell = PdfExportUtil.initPdfCell(fieldName);
        cell.setPaddingLeft(3);
        cell.setColspan(spanArr[0]);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        // 单元格字段内容
        cell = PdfExportUtil.initPdfCell(fieldContent);
        cell.setPaddingLeft(3);
        cell.setColspan(spanArr[1]);
        table.addCell(cell);
    }

    /**
     * 创建 list 类型表格
     *
     * @param header  表格头 键：表头中文名   值：单元格内容对应的字段名
     * @param content 表格内容 键：字段名   值：字段内容
     * @return 表格
     */
    public static PdfPTable buildListTable(Map<String, String> header, List<Map<String, String>> content) {
        float[] cellsWidthConfig = new float[header.size()];
        Arrays.fill(cellsWidthConfig, 1);
        return buildListTable(header, content, cellsWidthConfig);
    }

    /**
     * 创建 list 类型表格
     *
     * @param header    表格头 键：表头中文名   值：单元格内容对应的字段名
     * @param content   表格内容 键：字段名   值：字段内容
     * @param isRowSpan 是否需要行合并。上下相邻单元内容一致将会合并
     * @return 表格
     */
    public static PdfPTable buildListTable(Map<String, String> header, List<Map<String, String>> content, boolean isRowSpan) {
        float[] cellsWidthConfig = new float[header.size()];
        Arrays.fill(cellsWidthConfig, 1);
        return buildListTable(header, content, cellsWidthConfig, isRowSpan, null);
    }

    /**
     * 创建 list 类型表格
     *
     * @param header           表格头 键：表头中文名   值：单元格内容对应的字段名
     * @param content          表格内容 键：字段名   值：字段内容
     * @param cellsWidthConfig 单元格宽度配置
     * @return 表格
     */
    public static PdfPTable buildListTable(Map<String, String> header, List<Map<String, String>> content, float[] cellsWidthConfig) {
        return buildListTable(header, content, cellsWidthConfig, false, null);
    }

    /**
     * 创建 list 类型表格
     * 自定义序号值（如子明细序号）则在 content 参数中的 Map 添加 index 键值即可
     *
     * @param header           表格头 键：表头中文名   值：单元格内容对应的字段名
     * @param content          表格内容 键：字段名   值：字段内容
     * @param cellsWidthConfig 单元格宽度配置
     * @param isRowSpan        是否需要行合并。上下相邻单元内容一致将会合并
     */
    public static PdfPTable buildListTable(Map<String, String> header, List<Map<String, String>> content, float[] cellsWidthConfig, boolean isRowSpan) {
        return buildListTable(header, content, cellsWidthConfig, isRowSpan, null);
    }

    /**
     * 创建 list 类型表格
     * 自定义序号值（如子明细序号）则在 content 参数中的 Map 添加 index 键值即可
     *
     * @param header           表格头 键：表头中文名   值：单元格内容对应的字段名。如果传递为 null 则不显示表格头
     * @param content          表格内容 键：字段名   值：字段内容
     * @param cellsWidthConfig 单元格宽度配置
     * @param isRowSpan        是否需要行合并。上下相邻单元内容一致将会合并
     * @param rowSpanColumn    需要行合并的列。为 null 则所有列都行需要合并
     */
    public static PdfPTable buildListTable(Map<String, String> header, List<Map<String, String>> content, float[] cellsWidthConfig, boolean isRowSpan, String[] rowSpanColumn) {
        return buildListTable(header, content, true, cellsWidthConfig, isRowSpan, rowSpanColumn);
    }

    /**
     * 创建 list 类型表格
     * 自定义序号值（如子明细序号）则在 content 参数中的 Map 添加 index 键值即可
     *
     * @param header           表格头 键：表头中文名   值：单元格内容对应的字段名。如果传递为 null 则不显示表格头
     * @param content          表格内容 键：字段名   值：字段内容
     * @param cellsWidthConfig 单元格宽度配置
     * @param isRowSpan        是否需要行合并。上下相邻单元内容一致将会合并
     * @param rowSpanColumn    需要行合并的列。为 null 则所有列都行需要合并
     */
    public static PdfPTable buildListTable(Map<String, String> header, List<Map<String, String>> content, boolean isShowHeader, float[] cellsWidthConfig, boolean isRowSpan, String[] rowSpanColumn) {
        PdfPTable table = new PdfPTable(cellsWidthConfig.length + 1);
        table.setWidthPercentage(100);

        // 计算序号列宽度占比
        float[] indexCellWidth = new float[1];
        float totalCellWidth = 0;
        for (float item : cellsWidthConfig) {
            totalCellWidth += item;
        }
        indexCellWidth[0] = (float) (totalCellWidth * 0.08);
        try {
            table.setWidths(ArrayUtil.addAll(indexCellWidth, cellsWidthConfig));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        // 单元格内容字段名
        List<String> contentKey = new LinkedList<>();
        contentKey.add("index");

        // 单元格内容字体 宋体 10号 不加粗
        Font contentFont = new Font(getSimsunBaseFont(), 10, Font.NORMAL);

        // 添加表格头
        if (isShowHeader) {
            table.addCell(initPdfCellHeader("序号"));
            for (Map.Entry<String, String> entry : header.entrySet()) {
                PdfPCell cell = initPdfCellHeader(entry.getKey());
                contentKey.add(entry.getValue());
                table.addCell(cell);
            }
        } else {
            contentKey.addAll(header.values());
        }// 添加表格内容
        for (int i = 0; i < content.size(); i++) {
            Map<String, String> row = content.get(i);
            if (!row.containsKey("index")) {
                // 设置序号值
                row.put("index", i + 1 + "");
            }
            for (String key : contentKey) {
                // 当前列是否需要行合并
                boolean isCurrentColumnRowSpan = isRowSpan && (ArrayUtil.isEmpty(rowSpanColumn) || ArrayUtil.contains(rowSpanColumn, key));
                if (isCurrentColumnRowSpan) {
                    // 需要行合并单元格

                    // 当前单元格为第一行 && 表格还有下一行数据 && 当前单元格内容和下一行对应单元格内容一样时 需要合并行单元格
                    boolean isFirstRowSpan = i == 0 && i < content.size() - 1 && Objects.equals(row.get(key), content.get(i + 1).get(key));
                    // 当前单元格不为第一行 && 当前单元格不为最后一行 && 上一行对应单元格内容和当前单元格内容不一样 && 当前单元格内容和下一行对应单元格内容一样时 需要合并行单元格
                    boolean isOtherRowSpan = i > 0 && i < content.size() - 1
                            && (!Objects.equals(content.get(i - 1).get(key), row.get(key)) && Objects.equals(row.get(key), content.get(i + 1).get(key)));

                    if (isFirstRowSpan || isOtherRowSpan) {
                        // 计算单元格序号合并的行数
                        int rowSpanNum = 1;
                        for (int j = i + 1; j < content.size(); j++) {
                            if (Objects.equals(row.get(key), content.get(j).get(key))) {
                                rowSpanNum++;
                            } else {
                                break;
                            }
                        }
                        // 单元格内容
                        PdfPCell cell = initPdfCell(removeListTableSpanGroup(row.get(key)), contentFont);
                        cell.setRowspan(rowSpanNum);
                        table.addCell(cell);
                    } else if (i == 0 || !Objects.equals(content.get(i - 1).get(key), row.get(key))) {
                        // 直接设置单元格内容
                        PdfPCell cell = initPdfCell(removeListTableSpanGroup(row.get(key)), contentFont);
                        table.addCell(cell);
                    }
                } else {
                    // 无需行合并单元格，直接设置单元格内容
                    PdfPCell cell = initPdfCell(removeListTableSpanGroup(row.get(key)), contentFont);
                    table.addCell(cell);
                }
            }
        }
        return table;
    }

    /**
     * 添加 list 类型表格时单元格分组，一组内的单元格独立合并不与其他组合并（即便数据是一样的）
     *
     * @param cellContent 单元格内容
     * @param group       标记内容
     * @return 处理后占位值
     */
    public static String addListTableSpanGroup(String cellContent, Object group) {
        return String.format("%s#group: %s#", cellContent, group);
    }

    /**
     * 移除单元格内容分组标记
     *
     * @param cellContent 单元格内容
     * @return 处理后的单元格内容
     */
    public static String removeListTableSpanGroup(String cellContent) {
        if (StrUtil.isBlank(cellContent)) {
            return "";
        }
        return cellContent.replaceFirst("#group: .*?#", "");
    }

    /**
     * 获取宋体字体
     * 如果创建BaseFont对象时，使用 IDENTITY_H or IDENTITY_V 编码类型，那么Itext会忽略 BaseFont.NOT_EMBEDDED参数并且总是使用该字体的子集（只会嵌入用到的字体）。
     * BaseFont.NOT_EMBEDDED 字体不会嵌入到PDF文件中（没有保存），可能会导致不同电脑显示不正确。
     * BaseFont.EMBEDDED 使用的字体会嵌入（保存）到PDF文件中，这样文件会更大一些，这样PDF文件显示就是跨平台的。
     *
     * @return 宋体
     */
    public static BaseFont getSimsunBaseFont() {
        try {
            return BaseFont.createFont("fonts/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取黑体字体
     *
     * @return 黑体
     */
    public static BaseFont getSimheiBaseFont() {
        try {
            return BaseFont.createFont("fonts/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 格式化字符串
     *
     * @param str 目标字符串
     * @return 格式化之后的字符串
     */
    public static String formatStr(String str) {
        return str == null ? "" : str;
    }

}
