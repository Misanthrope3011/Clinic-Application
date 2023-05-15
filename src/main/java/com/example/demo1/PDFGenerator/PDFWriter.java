package com.example.demo1.PDFGenerator;

import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Helpers.BarcodeGenerator;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;

@Service
@Getter
@Setter
@AllArgsConstructor
public class PDFWriter {

    private com.itextpdf.text.Document document;
    private BarcodeGenerator barcodeGenerator;

    public PDFWriter() throws FileNotFoundException, DocumentException {
        this.document = new Document();
        barcodeGenerator = new BarcodeGenerator();
    }

    public void writePdf(MedicalVisit visit) throws Exception {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/examination.pdf"));
        document.open();

        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Paragraph chunk = new Paragraph("Nazwa badania: " + visit.getMedicalProcedure().getName(), font);
        chunk.setAlignment(Element.ALIGN_CENTER);


        PdfPTable table = new PdfPTable(4);
        table.addCell(new PdfPCell(new Paragraph(visit.getPatientId().getName())));
        table.addCell(new PdfPCell(new Paragraph(visit.getPatientId().getLast_name())));
        table.addCell(new PdfPCell(new Paragraph(visit.getPatientId().getPESEL())));
        table.addCell(new PdfPCell(new Paragraph(visit.getPatientId().getCity())));


        Paragraph paragraph = new Paragraph();
        paragraph.add(visit.getDescription());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(barcodeGenerator.generateEAN13BarcodeImage(), "png", baos);
        Image iTextImage = Image.getInstance(baos.toByteArray());
        iTextImage.setAlignment(Element.ALIGN_RIGHT);

        PdfPTable signing = new PdfPTable(2);
        PdfPCell userSign = new PdfPCell();
        userSign.setVerticalAlignment(Element.ALIGN_LEFT);
        userSign.setPaddingRight(30);
        userSign.setBorder(Rectangle.NO_BORDER);
        userSign.addElement(new Paragraph("Podpis pacjenta \n .............."));

        PdfPCell doctorSign = new PdfPCell();
        doctorSign.setVerticalAlignment(Element.ALIGN_RIGHT);
        doctorSign.setBorder(Rectangle.NO_BORDER);
        doctorSign.setPaddingLeft(30);
        doctorSign.addElement(new Paragraph("Podpis doktora \n .............."));

        signing.addCell(userSign);
        signing.addCell(doctorSign);

        document.add(table);
        document.add(chunk);
        document.add(Chunk.NEWLINE);
        document.add(paragraph);
        document.add(iTextImage);
        document.add(Chunk.NEWLINE);
        document.add(signing);

        document.close();



    }

}
