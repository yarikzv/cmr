package dev.zvolinskiy.cmr.utils.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dev.zvolinskiy.cmr.entity.CMR;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class FirmPowerOfAttorneyPdfCreator {
    private final InsertCell insert;

    public static final String FONT = "./src/main/resources/fonts/arial.ttf";
    public static final String IMAGE ="./src/main/resources/fx/images/poa_blank.png";

    public void createPoaPdfFile(CMR cmr, String blankImage) {
        try {
            BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font bf12 = new Font(bf, 12);

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("PowerOfAttorneyBlank" + cmr.getNumber() + ".pdf"));
            document.open();
            PdfContentByte canvas = writer.getDirectContentUnder();
            Image image = Image.getInstance(blankImage);
            image.scaleAbsolute(PageSize.A4);
            image.setAbsolutePosition(0, 0);
            canvas.addImage(image);

            Image blankImg = Image.getInstance(IMAGE);
            blankImg.scaleAbsolute(PageSize.A4);
            blankImg.setAbsolutePosition(0, 0);
            canvas.addImage(blankImg);

            String number = cmr.getNumber();
            String issueDate = cmr.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            String expiredDate = cmr.getDate().plusDays(9).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            String driver = cmr.getDriver().getLastName() + " " + cmr.getDriver().getFirstName() + " " + cmr.getDriver().getMiddleName();
            String pass = cmr.getDriver().getPassport().getNumber();
            String passNumber;
            String passSeries = null;
            if (pass.matches("[A-ZА-Я]{2}\\d{6}")) {
                passSeries = pass.substring(0, 2);
                passNumber = pass.substring(2, 8);
            } else {
                passNumber = pass;
            }
            String issued = cmr.getDriver().getPassport().getIssue();
            String order = cmr.getOrderNumber();
            String container = cmr.getContainer().getNumber();
            String contType = cmr.getContainer().getType();

            PdfPTable firstPageTable = new PdfPTable(6);
            firstPageTable.setWidthPercentage(90);
            firstPageTable.setSpacingBefore(10f);
            firstPageTable.setSpacingAfter(10f);

            float[] columnWidths = {1f, 1f, 0.5f, 0.5f, 1f, 1.5f};
            firstPageTable.setWidths(columnWidths);

            insert.insertCell(firstPageTable, "", 165f, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 5, bf12);
            insert.insertCell(firstPageTable, expiredDate, 165f, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 1, bf12);

            insert.insertCell(firstPageTable, "", 65f, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM, 5, bf12);
            insert.insertCell(firstPageTable, number, 65f, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 1, bf12);

            insert.insertCell(firstPageTable, issueDate, 30f, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, 4, bf12);
            insert.insertCell(firstPageTable, "", 30f, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, 2, bf12);

            insert.insertCell(firstPageTable, "", 25f, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 1, bf12);
            insert.insertCell(firstPageTable, driver, 25f, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 6, bf12);

            insert.insertCell(firstPageTable, "", 23f, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 3, bf12);
            insert.insertCell(firstPageTable, "паспорт", 23f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 3, bf12);

            insert.insertCell(firstPageTable, passSeries, 25f, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 1, bf12);
            insert.insertCell(firstPageTable, passNumber, 25f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 2, bf12);
            insert.insertCell(firstPageTable, "", 25f, Element.ALIGN_CENTER, Element.ALIGN_TOP, 1, bf12);
            insert.insertCell(firstPageTable, issueDate, 25f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 2, bf12);

            insert.insertCell(firstPageTable, "", 23f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 1, bf12);
            insert.insertCell(firstPageTable, issued, 23f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 5, bf12);

            insert.insertCell(firstPageTable, "", 23f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 2, bf12);
            insert.insertCell(firstPageTable, "terminal", 23f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 4, bf12);

            insert.insertCell(firstPageTable, "", 30f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 2, bf12);
            insert.insertCell(firstPageTable, order, 30f, Element.ALIGN_LEFT, Element.ALIGN_TOP, 4, bf12);

            document.add(firstPageTable);

            PdfPTable secondPageTable = new PdfPTable(4);
            secondPageTable.setWidthPercentage(90);
            secondPageTable.setSpacingBefore(10f);
            secondPageTable.setSpacingAfter(10f);

            float[] columnWidthsSecond = {0.5f, 3f, 0.5f, 3f};
            secondPageTable.setWidths(columnWidthsSecond);

            insert.insertCell(secondPageTable, "1", 70f, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM, 1, bf12);
            insert.insertCell(secondPageTable, container, 70f, Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1, bf12);
            insert.insertCell(secondPageTable, contType, 70f, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 1, bf12);
            insert.insertCell(secondPageTable, "один", 70f, Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1, bf12);

            document.add(secondPageTable);
            document.close();
            writer.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
