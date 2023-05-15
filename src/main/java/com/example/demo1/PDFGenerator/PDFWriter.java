package com.example.demo1.PDFGenerator;

import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Helpers.BarcodeGenerator;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

@Service
@Getter
@Setter
public class PDFWriter {

    private com.itextpdf.text.Document document;
    private BarcodeGenerator barcodeGenerator;

    public PDFWriter() {
        this.document = new Document();
        barcodeGenerator = new BarcodeGenerator();
    }

    public void writePdf(MedicalVisit visit) throws Exception {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/examination.pdf"));
        document.open();

        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Paragraph chunk = new Paragraph("Nazwa badania: " + visit.getProcedure().getName(), font);
        chunk.setAlignment(Element.ALIGN_CENTER);


        PdfPTable table = new PdfPTable(4);
        table.addCell(new PdfPCell(new Paragraph(visit.getPatient().getName())));
        table.addCell(new PdfPCell(new Paragraph(visit.getPatient().getLastName())));
        table.addCell(new PdfPCell(new Paragraph(visit.getPatient().getPESEL())));
        table.addCell(new PdfPCell(new Paragraph(visit.getPatient().getCity())));


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
