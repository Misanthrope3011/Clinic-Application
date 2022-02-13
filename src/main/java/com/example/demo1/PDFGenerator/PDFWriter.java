package com.example.demo1.PDFGenerator;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;

public class PDFWriter {

    com.itextpdf.text.Document document;

    public PDFWriter() throws FileNotFoundException, DocumentException {
        this.document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));
    }

}
