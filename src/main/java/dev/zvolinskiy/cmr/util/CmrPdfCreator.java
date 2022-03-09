package dev.zvolinskiy.cmr.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dev.zvolinskiy.cmr.entity.CMR;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class CmrPdfCreator {
    public static final String FONT = "./src/main/resources/fonts/arial.ttf";

    public void createPdfFile(CMR cmr) {
        try {
            BaseFont bf=BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font bfBold12 = new Font(bf, 14, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(bf, 12);

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("CMR" + cmr.getNumber() + ".pdf"));
            document.open();
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = {1.5f, 1f, 1.5f, 1f, 1f, 2f};
            table.setWidths(columnWidths);

            String sender = cmr.getSender().getName() + "\n" + cmr.getSender().getAddress() + "\n" + cmr.getSender().getCountry().getName();
            String recipient = cmr.getRecipient().getName() + "\n" + cmr.getRecipient().getAddress() + "\n" + cmr.getRecipient().getCountry().getName();
            String pod = cmr.getPlaceOfDelivery().getAddress() + "\n" + cmr.getPlaceOfDelivery().getCountry().getName();
            String pol = cmr.getPlaceOfLoading().getAddress() + "\n" + cmr.getPlaceOfLoading().getCountry().getName();
            String driver = cmr.getDriver().getLastName() + "\n" + cmr.getDriver().getFirstName() + " " + cmr.getDriver().getMiddleName();

            insertCell(table, sender, Element.ALIGN_LEFT, 3, bf12);
            insertCell(table, cmr.getNumber(), Element.ALIGN_CENTER, 3, bfBold12);

            insertCell(table, recipient, Element.ALIGN_LEFT, 6, bf12);
            insertCell(table, pod, Element.ALIGN_LEFT, 6, bf12);
            insertCell(table, pol, Element.ALIGN_LEFT, 6, bf12);
            insertCell(table, cmr.getDocuments(), Element.ALIGN_LEFT, 6, bf12);

            insertCell(table, cmr.getContainer().getNumber(), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, String.valueOf(cmr.getCargoQuantity()), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, cmr.getCargoName(),Element.ALIGN_CENTER,2,bf12);
            insertCell(table, String.valueOf(cmr.getCargoCode()),Element.ALIGN_CENTER,1,bf12);
            insertCell(table, String.valueOf(cmr.getCargoWeight()),Element.ALIGN_LEFT,2,bf12);

            insertCell(table, cmr.getSendersInstructions(), Element.ALIGN_LEFT, 6, bf12);

            insertCell(table, cmr.getPlaceOfIssue(), Element.ALIGN_RIGHT, 2, bf12);
            insertCell(table, cmr.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), Element.ALIGN_LEFT, 4, bf12);

            insertCell(table, driver, Element.ALIGN_LEFT,6,bfBold12);

            document.add(table);

            document.close();
            writer.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        table.addCell(cell);
    }

}
