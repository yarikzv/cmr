package dev.zvolinskiy.cmr.utils.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dev.zvolinskiy.cmr.entity.CMR;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class CmrPdfCreator {
    private final InsertCell insert;

    public static final String FONT = "./src/main/resources/fonts/arial.ttf";

    public void createPdfFile(CMR cmr) {
        try {
            BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font bfBold12 = new Font(bf, 12, Font.BOLD, new BaseColor(0, 0, 0));
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

            String recipientAddress = cmr.getRecipient().getAddress();
            String[] array = recipientAddress.split(" ");
            for (int i = 0; i < array.length; i++) {
                if (i != 0 && i % 5 == 0) {
                    array[i] += "\n";
                }
            }
            recipientAddress = String.join(" ", array);
            String sender = cmr.getSender().getName() + "\n" +
                    (cmr.getSender().getAddress().isEmpty() ? " " : cmr.getSender().getAddress()) +
                    "\n" + cmr.getSender().getCountry().getName();
            String recipient = cmr.getRecipient().getName() + "\n" + recipientAddress + "\n" + cmr.getRecipient().getCountry().getName();
            String pod = cmr.getPlaceOfDelivery().getAddress() + "\n" + cmr.getPlaceOfDelivery().getCountry().getName();
            String pol = cmr.getPlaceOfLoading().getAddress() + "\n" + cmr.getPlaceOfLoading().getCountry().getName();
            String driver = cmr.getDriver().getLastName() + "\n" + cmr.getDriver().getFirstName() + " " + cmr.getDriver().getMiddleName();
            String truck = cmr.getDriver().getTruck() + "\n" + cmr.getDriver().getTrailer();


            insert.insertCell(table, sender, 80f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 5, bf12);
            insert.insertCell(table, cmr.getNumber(), 80f, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 1, bfBold12);

            insert.insertCell(table, recipient, 80f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 6, bf12);

            insert.insertCell(table, pod, 50f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);
            insert.insertCell(table, "", 50f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);
            insert.insertCell(table, pol, 50f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);
            insert.insertCell(table, "", 50f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);

            insert.insertCell(table, cmr.getDocuments(), 50f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 6, bf12);

            insert.insertCell(table, cmr.getContainer().getNumber(), 100f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 1, bf12);
            insert.insertCell(table, String.valueOf(cmr.getCargoQuantity()), 100f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 1, bf12);
            insert.insertCell(table, cmr.getCargoName(), 100f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 2, bf12);
            insert.insertCell(table, String.valueOf(cmr.getCargoCode()), 100f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 1, bf12);
            insert.insertCell(table, String.valueOf(cmr.getCargoWeight()), 100f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 2, bf12);

            insert.insertCell(table, cmr.getSendersInstructions().isEmpty() ? "ЕЕ/ЕА" : cmr.getSendersInstructions(), 140f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);
            insert.insertCell(table, "", 140f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);

            insert.insertCell(table, cmr.getPlaceOfIssue().isEmpty() ? "м. Одеса" : cmr.getPlaceOfIssue(), 80f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 3, bf12);
            insert.insertCell(table, cmr.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), 80f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 3, bf12);

            insert.insertCell(table, driver, 60f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 2, bfBold12);
            insert.insertCell(table, truck, 60f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 4, bfBold12);

            document.add(table);

            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
