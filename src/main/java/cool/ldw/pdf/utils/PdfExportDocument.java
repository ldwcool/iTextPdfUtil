package cool.ldw.pdf.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import lombok.Getter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * pdf 文档
 *
 * @author ldwcool
 */
@Getter
public class PdfExportDocument extends Document {

    /**
     * 一级标题集合
     */
    private final List<Chapter> level1ChapterList = new LinkedList<>();

    /**
     * 二级标题集合
     */
    private final List<Section> level2SectionList = new LinkedList<>();

    /**
     * pdf 事件监听
     */
    private final PdfExportListener pdfListener = new PdfExportListener();

    /**
     * pdfWriter
     */
    private PdfWriter pdfWriter;

    /**
     * 章节序号
     */
    private int chapterIndex = 1;

    /**
     * 获取 PdfDocument 实例
     *
     * @return -
     */
    public static PdfExportDocument getInstance(OutputStream os) {
        return PdfExportDocument.getInstance(os, null, null);
    }

    /**
     * 获取 PdfDocument 实例
     *
     * @return -
     */
    public static PdfExportDocument getInstance(OutputStream os, String pageHeader, String pageWatermark) {
        PdfExportDocument document = new PdfExportDocument();
        document.setPageSize(PageSize.A4);
        document.setMargins(36, 36, 48, 48);
        try {
            document.pdfWriter = PdfWriter.getInstance(document, os);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        document.pdfWriter.setPageEvent(document.pdfListener);
        document.pdfWriter.setLinearPageMode();
        document.pdfListener.setPageHeader(pageHeader);
        document.pdfListener.setPageWatermark(pageWatermark);
        return document;
    }

    /**
     * 关闭
     */
    @Override
    public void close() {
        try {
            // 添加正文内容
            this.pdfListener.setCurNode(PdfExportListener.PdfNode.BODY);
            for (Chapter chapter : this.level1ChapterList) {
                this.add(chapter);
            }

            // 添加封面
            this.newPage();
            this.pdfListener.setCurNode(PdfExportListener.PdfNode.COVER);
            this.createCover();

            // 添加目录
            this.newPage();
            this.pdfListener.setCurNode(PdfExportListener.PdfNode.CATALOG);
            this.createCatalog();

            // 页面重新排序，封面和目录添加到最前面
            this.reorderPage();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }

        super.close();
    }

    /**
     * 添加一级标题
     * 第一层次标题 （字体：黑体，字号：三号，加粗，居中。段落：段前为1行，段后1行。行距23磅。样式：标题+黑体。）
     *
     * @param title 标题内容
     * @return 一级标题
     */
    public Chapter addLevel1Title(String title) {
        Font font = new Font(PdfExportUtil.getSimheiBaseFont(), 16, Font.BOLD);
        Chunk chunk = new Chunk(title, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingBefore(23);
        paragraph.setSpacingAfter(23);
        paragraph.setLeading(23);
        Chapter chapter = new Chapter(paragraph, this.chapterIndex++);
        chapter.setNumberDepth(1);
        // 去掉标题最后面默认添加的点
        chapter.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);
        chunk.setLocalDestination(chapter.getTitle().getContent().replaceFirst("^.*?\\s", ""));
        this.level1ChapterList.add(chapter);
        return chapter;
    }

    /**
     * 添加二级标题
     * 第二层次标题 （字体：黑体，字号：四号。段落：左对齐，段前为0.5行，段后0.5行。行距固定值23磅。）
     *
     * @param title 标题内容
     * @return 二级标题
     */
    public Section addLevel2Title(String title) {
        Font font = new Font(PdfExportUtil.getSimheiBaseFont(), 14, Font.NORMAL);
        Chunk chunk = new Chunk(title, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setSpacingBefore(11.5f);
        paragraph.setSpacingAfter(11.5f);
        paragraph.setLeading(23);
        Section section = CollUtil.getLast(level1ChapterList).addSection(paragraph);
        // 去掉标题最后面默认添加的点
        section.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);
        chunk.setLocalDestination(section.getTitle().getContent().replaceFirst("^.*?\\s", ""));
        level2SectionList.add(section);
        return section;
    }

    /**
     * 获取三级标题
     * 第三层次标题 （字体：黑体，字号：小四号。段落：左对齐，段前为0.5行，段后0.5行。行距固定值23磅。）
     *
     * @param title 标题内容
     * @return 三级标题
     */
    public Section addLevel3Title(String title) {
        Font font = new Font(PdfExportUtil.getSimheiBaseFont(), 12, Font.NORMAL);
        Chunk chunk = new Chunk(title, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setSpacingBefore(11.5f);
        paragraph.setSpacingAfter(11.5f);
        paragraph.setLeading(23);
        Section section = CollUtil.getLast(level2SectionList).addSection(paragraph);
        // 去掉标题最后面默认添加的点
        section.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);
        chunk.setLocalDestination(section.getTitle().getContent().replaceFirst("^.*?\\s", ""));
        return section;
    }

    /**
     * 创建封面
     *
     * @throws DocumentException -
     */
    private void createCover() throws DocumentException {
        this.newPage();
        Paragraph paragraph = PdfExportUtil.getMainTextParagraph("封面");
        this.add(paragraph);
    }

    /**
     * 创建目录
     *
     * @throws DocumentException -
     * @throws IOException -
     */
    private void createCatalog() throws DocumentException, IOException {
        // 目录章节
        Font font = new Font(PdfExportUtil.getSimsunBaseFont(), 16, Font.BOLD);
        Paragraph catalog = new Paragraph("目  录", font);
        catalog.setAlignment(Element.ALIGN_CENTER);
        catalog.setSpacingBefore(12.5f);
        catalog.setSpacingAfter(12.5f);
        Chapter catalogChapter = new Chapter(catalog, 0);
        catalogChapter.setNumberDepth(0);

        // 添加章节目录
        for (Map.Entry<String, Integer> entry : this.pdfListener.titlePageMap.entrySet()) {
            String title = entry.getKey();
            Integer page = entry.getValue();
            int titleLevel = CharSequenceUtil.count(title, ".");

            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_LEFT);
            paragraph.setLeading(25);
            // 一级标题字体样式 黑体 四号 左对齐 行距: 25磅
            Font titleFont = new Font(PdfExportUtil.getSimheiBaseFont(), 14, Font.NORMAL);
            switch (titleLevel) {
                case 0:
                    // 一级标题
                    Chunk level1Chunk = new Chunk(title, titleFont);
                    level1Chunk.setLocalGoto(title.replaceFirst("^.*?\\s", ""));
                    paragraph.add(level1Chunk);
                    break;
                case 1:
                    // 二级标题 字体：宋体，字号：四号。段落：左对齐，行距：固定值，25磅（pt）。
                    titleFont = new Font(PdfExportUtil.getSimsunBaseFont(), 14, Font.NORMAL);
                    Chunk level2Chunk = new Chunk(title, titleFont);
                    level2Chunk.setLocalGoto(title.replaceFirst("^.*?\\s", ""));
                    paragraph.add(level2Chunk);
                    break;
                case 2:
                    // 三级标题 字体：宋体，字号：四号。段落：左对齐，行距：固定值，25磅（pt）
                    titleFont = new Font(PdfExportUtil.getSimsunBaseFont(), 12, Font.NORMAL);
                    Chunk level3Chunk = new Chunk(title, titleFont);
                    level3Chunk.setLocalGoto(title.replaceFirst("^.*?\\s", ""));
                    paragraph.add(level3Chunk);
                    break;
                default:
                    break;
            }

            // ........................
            Chunk dottedChunk = new Chunk(new DottedLineSeparator());
            dottedChunk.setLocalGoto(title.replaceFirst("^.*?\\s", ""));
            paragraph.add(dottedChunk);

            // 页码
            Chunk pageChunk = new Chunk(page + "");
            pageChunk.setLocalGoto(title.replaceFirst("^.*?\\s", ""));
            paragraph.add(pageChunk);

            // 添加章节
            catalogChapter.add(paragraph);
        }
        this.add(catalogChapter);
    }

    /**
     * 页面重新排序
     */
    private void reorderPage() throws DocumentException {
        this.newPage();
        // 获取总页数
        int totalPage = this.pdfWriter.reorderPages(null);
        // 获取正文页数
        int bodyPage = this.pdfListener.getBodyPage() - 1;

        // 页面顺序数组，存放页码   比如：[4, 5, 1, 2, 3] 第一页为原本的第四页，第二页为原本的第五页，第三页为原本的第一页......
        int[] pageOrder = new int[totalPage];
        int pageOrderIndex = 0;
        for (int page = bodyPage + 1; page <= totalPage; page++) {
            pageOrder[pageOrderIndex++] = page;
        }
        for (int page = 1; page <= bodyPage; page++) {
            pageOrder[pageOrderIndex++] = page;
        }

        this.pdfWriter.reorderPages(pageOrder);
    }

}
