package com.fgwr.jpcorretora.services;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fgwr.jpcorretora.domain.Recibo;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGen {

	public void geraRecibo(Recibo recibo) {
		
		Document document = new Document();
		try {
			String[] nomeArr = StringUtils.split(recibo.getCliente().getNome());
			PdfWriter.getInstance(document, new FileOutputStream("D:\\Recibos\\" + recibo.getId().toString() + " - " + nomeArr[0] + " " + nomeArr[1] + ".pdf" ));
			document.open();
			
			document.add(new Paragraph("Recibo nº: " + recibo.getId().toString()));
			document.add(new Paragraph("Cliente : " + recibo.getCliente()));
			document.add(new Paragraph("Valor : R$ " + recibo.getValor()));
			document.add(new Paragraph("Data do Pagamento nº: " + recibo.getDataPagamento()));

			
		}catch(DocumentException de) {
            System.err.println(de.getMessage());
        }
        catch(IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();

	}
	
}