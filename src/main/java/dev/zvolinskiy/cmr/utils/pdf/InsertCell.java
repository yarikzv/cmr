package dev.zvolinskiy.cmr.utils.pdf;

import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.stereotype.Component;

@Component
public class InsertCell {
    public InsertCell() {
    }

    public void insertCell(PdfPTable table, String text, float height, int align, int valign, int colspan, Font font) {
        String cellText;
        if (text != null) {
            cellText = text.trim();
        } else {
            cellText = "";
        }
        PdfPCell cell = new PdfPCell(new Phrase(cellText, font));
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(valign);
        cell.setColspan(colspan);
        cell.setFixedHeight(height);
        cell.setPadding(5f);
        cell.setBorder(Rectangle.NO_BORDER);
        if (cellText.equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        table.addCell(cell);
    }
}
