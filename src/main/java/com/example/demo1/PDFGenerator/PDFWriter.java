package com.example.demo1.PDFGenerator;

import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Entities.Patient;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@AllArgsConstructor
public class PDFWriter {

    com.itextpdf.text.Document document;

    public PDFWriter() throws FileNotFoundException, DocumentException {
        this.document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/examination.pdf"));
    }

    public Document writePdf(String name) throws DocumentException {
        document.open();
        Paragraph p = new Paragraph("This is a paragraph 1 ",
                FontFactory.getFont(FontFactory.HELVETICA, 40, Font.BOLD));
        document.add(p);
        document.close();

        return document;
    }

}
