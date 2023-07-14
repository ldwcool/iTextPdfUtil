package cool.ldw.pdf.utils;

import cn.hutool.core.util.StrUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * pdf 事件监听
 *
 * @author ldw
 */
@Getter
@Setter
public class PdfExportListener extends PdfPageEventHelper {

    /**
     * 目录内容页码
     */
    private int catalogPage = 1;

    /**
     * 正文内容页码
     */
    private int bodyPage = 1;

    /**
     * 键：标题序号   值：标题所在页码
     */
    public Map<String, Integer> titlePageMap = new LinkedHashMap<>();

    /**
     * 当前处理的 pdf 节点
     */
    private PdfNode curNode;

    /**
     * 页眉
     */
    private String pageHeader;

    /**
     * 水印
     */
    private String pageWatermark;

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        if (Objects.equals(curNode, PdfNode.CATALOG)) {
            this.catalogPage++;
        } else if (Objects.equals(curNode, PdfNode.BODY)) {
            this.bodyPage++;
        }
    }

    @Override
    public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
        titlePageMap.put(title.getContent(), bodyPage);
    }

    @Override
    public void onSection(PdfWriter writer, Document document, float paragraphPosition, int depth, Paragraph title) {
        onChapter(writer, document, paragraphPosition, title);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        super.onEndPage(writer, document);

        switch (curNode) {
            case COVER:
                // 封面 添加页眉、水印
                this.addPageHeader(writer);
                this.addPageWatermark(writer);
                break;
            case CATALOG:
                // 目录 添加页脚、水印
                this.addPageFooter(writer);
                this.addPageWatermark(writer);
                break;
            case BODY:
                // 正文 添加页眉、页脚、水印
                this.addPageHeader(writer);
                this.addPageFooter(writer);
                this.addPageWatermark(writer);
            default:
                // 添加水印
                this.addPageWatermark(writer);
                break;
        }
    }

    /**
     * 添加页眉
     *
     * @param writer -
     */
    private void addPageHeader(PdfWriter writer) {
        if (StrUtil.isNotBlank(pageHeader)) {
            // 页眉内容
            PdfPTable table = new PdfPTable(1);
            //设置表格宽度 A4纸宽度减去两个边距  比如我一边30  所以减去60
            table.setTotalWidth(PageSize.A4.getWidth() - 60);

            PdfPCell cell = new PdfPCell(new Phrase(this.pageHeader, PdfExportUtil.getPageHeaderFont()));
            // 只保留底部边框和设置高度 设置水平居右和垂直居中
            cell.disableBorderSide(13);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(cell);

            // 再把表格写到页眉处  使用绝对定位
            table.writeSelectedRows(0, -1, 30, PageSize.A4.getHeight() - 14, writer.getDirectContent());
        }
    }

    /**
     * 添加水印
     *
     * @param writer -
     */
    private void addPageWatermark(PdfWriter writer) {
        if (StrUtil.isNotBlank(this.pageWatermark)) {
            Rectangle rectangle = writer.getPageSize();

            // 水印内容
            ColumnText.showTextAligned(writer.getDirectContentUnder(),
                    Element.ALIGN_CENTER, new Phrase(pageWatermark, PdfExportUtil.getPageWatermarkFont()),
                    rectangle.getRight() / 2, rectangle.getTop() / 2, 33);
        }
    }

    /**
     * 添加页脚
     *
     * @param writer -
     */
    private void addPageFooter(PdfWriter writer) {
        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rectangle = writer.getPageSize();

        int page = Objects.equals(this.curNode, PdfNode.CATALOG) ? catalogPage : bodyPage;

        // 页脚内容
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase(page + "", PdfExportUtil.getPageHeaderFont()),
                rectangle.getRight() / 2, rectangle.getBottom() + 20, 0);
    }

    /**
     * PDF 节点
     */
    public enum PdfNode {
        // 封面
        COVER,
        // 目录
        CATALOG,
        // 正文
        BODY
    }

}
