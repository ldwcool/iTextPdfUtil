package cool.ldw.pdf;

import cn.hutool.core.io.FileUtil;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import cool.ldw.pdf.utils.PdfExportDocument;
import cool.ldw.pdf.utils.PdfExportUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * pdf 生成例子
 *
 * @author ldwcool
 */
public class Demo {

    public static void main(String[] args) {
        PdfExportDocument document = PdfExportDocument.getInstance(FileUtil.getOutputStream("D:/demo.pdf"));
        document.open();

        // 添加一级标题
        Chapter chapter = document.addLevel1Title("第一章");
        Paragraph paragraph = PdfExportUtil.getMainTextParagraph("第一章描述");
        chapter.add(paragraph);
        // 添加二级标题
        Section section = document.addLevel2Title("第一小节");
        paragraph = PdfExportUtil.getMainTextParagraph("第一小节描述");
        section.add(paragraph);
        // 添加二级标题
        section = document.addLevel2Title("第二小节");
        paragraph = PdfExportUtil.getMainTextParagraph("第二小节描述");
        section.add(paragraph);

        // 添加一级标题
        chapter = document.addLevel1Title("第二章");
        paragraph = PdfExportUtil.getMainTextParagraph("第二章描述");
        chapter.add(paragraph);
        // 添加二级标题
        section = document.addLevel2Title("第一小节");
        paragraph = PdfExportUtil.getMainTextParagraph("第一小节描述");
        section.add(paragraph);
        // 添加二级标题
        section = document.addLevel2Title("第二小节");
        paragraph = PdfExportUtil.getMainTextParagraph("第二小节描述");
        section.add(paragraph);

        // 添加一级标题
        chapter = document.addLevel1Title("第三章 表格基本用法");
        paragraph = PdfExportUtil.getMainTextParagraph("以下是关于表格基本用法例子描述");
        chapter.add(paragraph);

        // 创建包含 3 列的表格
        PdfPTable table1 = PdfExportUtil.createTable(3);
        // 设置表格头
        PdfPCell col1 = PdfExportUtil.initPdfCellHeader("列 1");
        table1.addCell(col1);
        // 设置表格表格头并设置字体
        PdfPCell col2 = PdfExportUtil.initPdfCellHeader("列 2", PdfExportUtil.getMainTextFont());
        table1.addCell(col2);
        PdfPCell col3 = PdfExportUtil.initPdfCellHeader("列 3");
        table1.addCell(col3);
        // 设置表格内容
        PdfPCell cell1 = PdfExportUtil.initPdfCell("值 1");
        table1.addCell(cell1);
        PdfPCell cell2 = PdfExportUtil.initPdfCell("值 2");
        table1.addCell(cell2);
        PdfPCell cell3 = PdfExportUtil.initPdfCell("值 3");
        table1.addCell(cell3);
        // 设置表格内容并设置字体
        PdfPCell cell4 = PdfExportUtil.initPdfCell("值 4", PdfExportUtil.getPageHeaderFont());
        table1.addCell(cell4);
        PdfPCell cell5 = PdfExportUtil.initPdfCell("值 5");
        table1.addCell(cell5);
        PdfPCell cell6 = PdfExportUtil.initPdfCell("值 6");
        table1.addCell(cell6);
        chapter.add(table1);

        // 添加一级标题
        chapter = document.addLevel1Title("第四章 表单类型表格");
        paragraph = PdfExportUtil.getMainTextParagraph("以下是关于表单类型表格例子描述");
        chapter.add(paragraph);
        // 添加二级标题
        section = document.addLevel2Title("基础用法");
        paragraph = PdfExportUtil.getMainTextParagraph("基础用法展示用法。");
        section.add(paragraph);

        // 创建包含 6 列的表格
        PdfPTable table2 = PdfExportUtil.createTable(6);
        // 设置内容
        PdfExportUtil.setTableCell(table2, "姓名", "张三");
        PdfExportUtil.setTableCell(table2, "性别", "男");
        PdfExportUtil.setTableCell(table2, "民族", "汉");
        PdfExportUtil.setTableCell(table2, "年龄", "18");
        PdfExportUtil.setTableCell(table2, "国籍", "中国");
        PdfExportUtil.setTableCell(table2, "联系方式", "18888888888");
        section.add(table2);

        // 添加二级标题
        section = document.addLevel2Title("自定义表格列宽度");
        paragraph = PdfExportUtil.getMainTextParagraph("自定义表格列宽度展示用法。");
        section.add(paragraph);

        // 创建包含 6 列的表格
        PdfPTable table3 = PdfExportUtil.createTable(6, new float[]{4, 2, 2.5f, 4, 2, 2.5f});
        // 设置内容
        PdfExportUtil.setTableCell(table3, "姓名", "王五");
        PdfExportUtil.setTableCell(table3, "性别", "女");
        PdfExportUtil.setTableCell(table3, "民族", "汉");
        PdfExportUtil.setTableCell(table3, "年龄", "18");
        PdfExportUtil.setTableCell(table3, "国籍", "中国");
        PdfExportUtil.setTableCell(table3, "联系方式", "19999999999");
        section.add(table3);

        // 添加二级标题
        section = document.addLevel2Title("自定义合并列数");
        paragraph = PdfExportUtil.getMainTextParagraph("自定义合并列数展示用法。");
        section.add(paragraph);

        // 创建包含 6 列的表格
        PdfPTable table4 = PdfExportUtil.createTable(6, new float[]{4, 2, 2.5f, 4, 2, 2.5f});
        // 设置内容
        PdfExportUtil.setTableCell(table4, "姓名", "赵六", "1-3");
        PdfExportUtil.setTableCell(table4, "性别", "女", "1-1");
        PdfExportUtil.setTableCell(table4, "民族", "汉");
        PdfExportUtil.setTableCell(table4, "年龄", "18");
        PdfExportUtil.setTableCell(table4, "国籍", "中国");
        PdfExportUtil.setTableCell(table4, "联系方式", "18999999999");
        section.add(table4);

        // 添加一级标题
        chapter = document.addLevel1Title("第五章 列表类型表格");
        paragraph = PdfExportUtil.getMainTextParagraph("以下是关于列表类型表格例子描述");
        chapter.add(paragraph);
        // 添加二级标题
        section = document.addLevel2Title("基础用法");
        paragraph = PdfExportUtil.getMainTextParagraph("基础用法展示用法。");
        section.add(paragraph);
        // 表格头
        Map<String, String> header = new LinkedHashMap<>(5);
        header.put("姓名", "name");
        header.put("年龄", "age");
        header.put("性别", "sex");
        // 表格内容
        List<Map<String, String>> content = new LinkedList<>();
        Map<String, String> row1 = new LinkedHashMap<>(5);
        row1.put("name", "张三");
        row1.put("age", "18");
        row1.put("sex", "男");
        content.add(row1);
        Map<String, String> row2 = new LinkedHashMap<>(5);
        row2.put("name", "李四");
        row2.put("age", "18");
        row2.put("sex", "女");
        content.add(row2);
        Map<String, String> row3 = new LinkedHashMap<>(5);
        row3.put("name", "王五");
        row3.put("age", "18");
        row3.put("sex", "女");
        content.add(row3);
        PdfPTable table5 = PdfExportUtil.buildListTable(header, content);
        section.add(table5);

        // 添加二级标题
        section = document.addLevel2Title("自定义列宽");
        paragraph = PdfExportUtil.getMainTextParagraph("自定义列宽展示用法。");
        section.add(paragraph);
        PdfPTable table6 = PdfExportUtil.buildListTable(header, content, new float[] {2, 0.8f, 1f});
        section.add(table6);

        // 添加二级标题
        section = document.addLevel2Title("自动行合并");
        paragraph = PdfExportUtil.getMainTextParagraph("自动行合并展示用法。");
        section.add(paragraph);
        PdfPTable table7 = PdfExportUtil.buildListTable(header, content, true);
        section.add(table7);

        // 添加二级标题
        section = document.addLevel2Title("指定列的行合并");
        paragraph = PdfExportUtil.getMainTextParagraph("指定列的行合并展示用法。");
        section.add(paragraph);
        PdfPTable table8 = PdfExportUtil.buildListTable(header, content, new float[] {2, 0.8f, 1f}, true, new String[] {"sex"});
        section.add(table8);

        // 添加二级标题
        section = document.addLevel2Title("不显示表格头");
        paragraph = PdfExportUtil.getMainTextParagraph("不显示表格头展示用法。");
        section.add(paragraph);
        PdfPTable table9 = PdfExportUtil.buildListTable(header, content, false, new float[] {1, 1, 1}, false, new String[] {});
        section.add(table9);

        // 添加二级标题
        section = document.addLevel2Title("分组行合并");
        paragraph = PdfExportUtil.getMainTextParagraph("分组行合并展示用法。");
        section.add(paragraph);
        // 表格内容
        content = new LinkedList<>();
        row1 = new LinkedHashMap<>(5);
        row1.put("name", "张三");
        row1.put("age", "18");
        row1.put("sex", PdfExportUtil.addListTableSpanGroup("男", "group1"));
        content.add(row1);
        row2 = new LinkedHashMap<>(5);
        row2.put("name", "张三");
        row2.put("age", "18");
        row2.put("sex", PdfExportUtil.addListTableSpanGroup("男", "group1"));
        content.add(row2);
        row3 = new LinkedHashMap<>(5);
        row3.put("name", "张三");
        row3.put("age", "18");
        row3.put("sex", PdfExportUtil.addListTableSpanGroup("男", "group1"));
        Map<String, String> row4 = new LinkedHashMap<>(5);
        row4.put("name", "李四");
        row4.put("age", "18");
        row4.put("sex", PdfExportUtil.addListTableSpanGroup("男", "group2"));
        content.add(row4);
        Map<String, String> row5 = new LinkedHashMap<>(5);
        row5.put("name", "李四");
        row5.put("age", "18");
        row5.put("sex", PdfExportUtil.addListTableSpanGroup("男", "group2"));
        content.add(row5);
        PdfPTable table10 = PdfExportUtil.buildListTable(header, content, new float[] {2, 0.8f, 1f}, true, new String[] {"sex"});
        section.add(table10);

        // 添加二级标题
        section = document.addLevel2Title("自定义序号");
        paragraph = PdfExportUtil.getMainTextParagraph("自定义序号展示用法。");
        section.add(paragraph);
        // 表格头
        header = new LinkedHashMap<>(5);
        header.put("姓名", "name");
        header.put("年龄", "age");
        header.put("性别", "sex");
        // 表格内容
        content = new LinkedList<>();
        row1 = new LinkedHashMap<>(6);
        row1.put("index", "1");
        row1.put("name", "张三");
        row1.put("age", "18");
        row1.put("sex", PdfExportUtil.addListTableSpanGroup("男", "group1"));
        content.add(row1);
        row2 = new LinkedHashMap<>(6);
        row2.put("index", "1.1");
        row2.put("name", "张三");
        row2.put("age", "18");
        row2.put("sex", PdfExportUtil.addListTableSpanGroup("男", "group1"));
        content.add(row2);
        row3 = new LinkedHashMap<>(6);
        row3.put("index", "1.2");
        row3.put("name", "张三");
        row3.put("age", "18");
        row3.put("sex", "女");
        row4 = new LinkedHashMap<>(6);
        row4.put("index", "2");
        row4.put("name", "李四");
        row4.put("age", "18");
        row4.put("sex", "男");
        content.add(row4);
        row5 = new LinkedHashMap<>(6);
        row5.put("index", "2.1");
        row5.put("name", "李四");
        row5.put("age", "18");
        row5.put("sex", "女");
        content.add(row5);
        PdfPTable table11 = PdfExportUtil.buildListTable(header, content);
        section.add(table11);

        document.close();
    }

}
