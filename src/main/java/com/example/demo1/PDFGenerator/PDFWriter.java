package com.example.demo1.PDFGenerator;

import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Entities.Patient;
import com.example.demo1.Helpers.BarcodeGenerator;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;

@Service
@Getter
@Setter
@AllArgsConstructor
public class PDFWriter {

    com.itextpdf.text.Document document;
    BarcodeGenerator barcodeGenerator;

    public PDFWriter() throws FileNotFoundException, DocumentException {
        this.document = new Document();
        barcodeGenerator = new BarcodeGenerator();
    }

    public void writePdf(String name) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/examination.pdf"));

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk("Hello World", font);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(barcodeGenerator.generateEAN13BarcodeImage(), "png", baos);
        Image iTextImage = Image.getInstance(baos.toByteArray());
        document.add(chunk);
        document.add(iTextImage);
        document.close();



    }

}
