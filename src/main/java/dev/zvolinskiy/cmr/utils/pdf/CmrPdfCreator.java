package dev.zvolinskiy.cmr.utils.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dev.zvolinskiy.cmr.entity.CMR;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class CmrPdfCreator {
    public static final String FONT = "./src/main/resources/fonts/arial.ttf";

    public void createPdfFile(CMR cmr) {
        try {
            BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font bfBold12 = new Font(bf, 14, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(bf, 12);

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("CMR" + cmr.getNumber() + ".pdf"));
            document.open();
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = {2f, 1f, 1.5f, 1f, 1f, 2f};
            table.setWidths(columnWidths);

            String sender = cmr.getSender().getName() + "\n" + cmr.getSender().getAddress() + "\n" + cmr.getSender().getCountry().getName();
            String recipient = cmr.getRecipient().getName() + "\n" + cmr.getRecipient().getAddress() + "\n" + cmr.getRecipient().getCountry().getName();
            String pod = cmr.getPlaceOfDelivery().getAddress() + "\n" + cmr.getPlaceOfDelivery().getCountry().getName();
            String pol = cmr.getPlaceOfLoading().getAddress() + "\n" + cmr.getPlaceOfLoading().getCountry().getName();
            String driver = cmr.getDriver().getLastName() + "\n" + cmr.getDriver().getFirstName() + " " + cmr.getDriver().getMiddleName();
            String truck = cmr.getDriver().getTruck() + "\n" + cmr.getDriver().getTrailer();


            insertCell(table, sender, 80f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 5, bf12);
            insertCell(table, cmr.getNumber(), 80f, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 1, bfBold12);


            insertCell(table, recipient, 80f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 6, bf12);

            insertCell(table, pod, 50f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);
            insertCell(table, "", 50f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);
            insertCell(table, pol, 50f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);
            insertCell(table, "", 50f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);

            insertCell(table, cmr.getDocuments(), 50f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 6, bf12);

            insertCell(table, cmr.getContainer().getNumber(), 100f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 1, bf12);
            insertCell(table, String.valueOf(cmr.getCargoQuantity()), 100f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 1, bf12);
            insertCell(table, cmr.getCargoName(), 100f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 2, bf12);
            insertCell(table, String.valueOf(cmr.getCargoCode()), 100f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 1, bf12);
            insertCell(table, String.valueOf(cmr.getCargoWeight()), 100f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 2, bf12);

            insertCell(table, cmr.getSendersInstructions().isEmpty()?"ЕЕ/ЕА":cmr.getSendersInstructions(), 140f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);
            insertCell(table, "", 140f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);

            insertCell(table, cmr.getPlaceOfIssue().isEmpty()?"м. Одеса":cmr.getPlaceOfIssue(), 80f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);
            insertCell(table, cmr.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), 80f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 3, bf12);

            insertCell(table, driver, 60f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 2, bfBold12);
            insertCell(table, truck, 60f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 4, bfBold12);

            document.add(table);

            document.close();
            writer.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void insertCell(PdfPTable table, String text, float height, int align, int valign, int colspan, Font font) {
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
        cell.setPadding(10f);
        cell.setBorder(Rectangle.NO_BORDER);
        if (cellText.equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        table.addCell(cell);
    }

}
