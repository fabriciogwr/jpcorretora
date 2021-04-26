package com.fgwr.jpcorretora.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.fgwr.jpcorretora.domain.Recibo;
import com.fgwr.jpcorretora.utils.FilesUtils;
import com.fgwr.jpcorretora.utils.StringsUtils;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

public class ReciboPdfGen {

	public String fileToString ( File file ) {
	    try {
	        return file.toURI().toURL().toString();
	    } catch ( MalformedURLException e ) {
	        return null;
	    }
	}
	
	public void geraRecibo(Recibo recibo) throws FileNotFoundException {

		Calendar cal = Calendar.getInstance();
		cal.setTime(recibo.getDataPagamento());
		PdfWriter writer = new PdfWriter(FilesUtils.pathRecibos(recibo));
		PdfDocument pdfDoc = new PdfDocument(writer);
		Document document = new Document(pdfDoc);

		try {

			ImageData header = ImageDataFactory.create(fileToString( new File("imgs/logo.png")));
			Image logoHeader = new Image(header);
			logoHeader.setWidth(250);

			PdfFont titleFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

			Table table = new Table(2).useAllAvailableWidth();
			Border b1 = new SolidBorder(ColorConstants.BLACK, 2);
			Border b2 = (new SolidBorder(ColorConstants.WHITE, 0));

			Cell cell = new Cell();
			cell.add(logoHeader.setWidth(150));
			cell.setBorder(b2);

			cell.setBorderTop(b1);
			cell.setBorderBottom(b1);
			cell.setBackgroundColor(ColorConstants.YELLOW);

			table.addCell(cell);

			cell = new Cell();
			Paragraph p = new Paragraph("RECIBO \nR$" + StringsUtils.formatarReal(recibo.getValor()));
			p.setFont(titleFont);
			p.setFontSize(16);
			p.setTextAlignment(TextAlignment.RIGHT);
			p.setMarginRight(20);
			cell.add(p);
			cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			cell.setBorder(b2);
			cell.setBorderTop(b1);
			cell.setBorderBottom(b1);
			cell.setBackgroundColor(ColorConstants.YELLOW);

			table.addCell(cell);
			document.add(table);
			
			document.add(new Paragraph());
			
			Table table2 = new Table(1).useAllAvailableWidth();
		

			cell = new Cell();
			String nome = recibo.getCliente().getNome();
			Text t1 = new Text(nome).setBold();
			String cpf = StringsUtils.formatarCpfOuCnpj(recibo.getCliente().getCpfOuCnpj());
			Text t2 = new Text(cpf).setBold();
			p = new Paragraph("Recebemos de ");
			p.add(t1);
			p.add(", CPF ");
			p.add(t2);
			p.add(",");	
			
			String valor = StringsUtils.formatarRealCifra(recibo.getValor());
			Text textValor = new Text(valor).setBold();
			p.add(" a importância de ");
			p.add(textValor);
			
			p.add(" (" + ValorExtenso.valorPorExtenso(recibo.getValor()) + ")"); 
			p.add(",");
			
			p.add(" Referente quitação da mensalidade " + recibo.getDuplicata().getParcela() + "/"
					+ recibo.getQtdParcelas() +", de validade datada em " + new SimpleDateFormat("dd/MM/yyyy").format(recibo.getDataVencimento()) + ",");
			
			p.add(" do contrato "+ recibo.getDuplicata().getContrato().getId() + " de locação de imóvel.");
			cell.add(p);
			cell.setHeight(80);
			cell.setBorder(b2);
			cell.setTextAlignment(TextAlignment.JUSTIFIED);
			table2.addCell(cell);
			
			cell = new Cell();
			table2.addCell(cell);
			
			cell.add(new Paragraph("Para clareza, firmamos o presente recibo para que produza os seus efeitos, dando plena, rasa e irrevogável quitação, pelo valor recebido."));
			cell.setHeight(50);
			cell.setBorder(b2);
			cell.setTextAlignment(TextAlignment.JUSTIFIED);
			table2.addCell(cell);
			
			cell = new Cell();
			p = new Paragraph("Vilhena, " + cal.get(Calendar.DAY_OF_MONTH) + " de " + new SimpleDateFormat("MMMMM", new Locale ("pt", "BR")).format(recibo.getDataPagamento()) + " de " + cal.get(Calendar.YEAR) + ".");
			
			cell.setTextAlignment(TextAlignment.RIGHT);
			cell.setHeight(40);
			cell.setBorder(b2);
			cell.add(p);
			table2.addCell(cell);
			
			cell = new Cell();
			table2.addCell(cell);
			table2.addCell(cell);
			p = new Paragraph("________________________________________\nJoão Paulo Escritório Imobiliário\n\n\n\s ");
			cell.add(p);
			cell.setTextAlignment(TextAlignment.RIGHT);
			cell.setHeight(90);
			cell.setBorder(b2);
			table2.addCell(cell);
			
			document.add(table2);
			
			

			Table table3 = new Table(2).useAllAvailableWidth();

			Cell cell2 = new Cell();
			cell2.add(logoHeader.setWidth(150));
			cell2.setBorder(b2);

			cell2.setBorderTop(b1);
			cell2.setBorderBottom(b1);
			cell2.setBackgroundColor(ColorConstants.YELLOW);

			table3.addCell(cell2);

			cell2 = new Cell();
			Paragraph p2 = new Paragraph("RECIBO \nR$" + StringsUtils.formatarReal(recibo.getValor()));
			p2.setFont(titleFont);
			p2.setFontSize(16);
			p2.setTextAlignment(TextAlignment.RIGHT);
			p2.setMarginRight(20);
			cell2.add(p2);
			cell2.setVerticalAlignment(VerticalAlignment.MIDDLE);
			cell2.setBorder(b2);
			cell2.setBorderTop(b1);
			cell2.setBorderBottom(b1);
			cell2.setBackgroundColor(ColorConstants.YELLOW);

			table3.addCell(cell2);
			document.add(table3);
			
			document.add(new Paragraph());
			
			Table table4 = new Table(1).useAllAvailableWidth();
		

			cell2 = new Cell();
			p2 = new Paragraph("Recebemos de ");
			p2.add(t1);
			p2.add(", CPF ");
			p2.add(t2);
			p2.add(",");	
			
			
			p2.add(" a importância de ");
			p2.add(textValor);
			
			p2.add(" (" + ValorExtenso.valorPorExtenso(recibo.getValor()) + ")"); 
			p2.add(",");
			
			p2.add(" Referente quitação da mensalidade " + recibo.getDuplicata().getParcela() + "/"
					+ recibo.getQtdParcelas() +", de validade datada em " + new SimpleDateFormat("dd/MM/yyyy").format(recibo.getDataVencimento()) + ",");
			
			p2.add(" do contrato "+ recibo.getDuplicata().getContrato().getId() + " de locação de imóvel.");
			cell2.add(p2);
			cell2.setHeight(80);
			cell2.setBorder(b2);
			cell2.setTextAlignment(TextAlignment.JUSTIFIED);
			table4.addCell(cell2);
			
			cell2 = new Cell();
			table4.addCell(cell2);
			
			cell2.add(new Paragraph("Para clareza, firmamos o presente recibo para que produza os seus efeitos, dando plena, rasa e irrevogável quitação, pelo valor recebido."));
			cell2.setHeight(50);
			cell2.setBorder(b2);
			cell2.setTextAlignment(TextAlignment.JUSTIFIED);
			table4.addCell(cell2);
			
			cell2 = new Cell();
			p2 = new Paragraph("Vilhena, " + cal.get(Calendar.DAY_OF_MONTH) + " de " + new SimpleDateFormat("MMMMM", new Locale ("pt", "BR")).format(recibo.getDataPagamento()) + " de " + cal.get(Calendar.YEAR) + ".");
			
			cell2.setTextAlignment(TextAlignment.RIGHT);
			cell2.setHeight(40);
			cell2.setBorder(b2);
			cell2.add(p2);
			table4.addCell(cell2);
			
			cell2 = new Cell();
			table4.addCell(cell2);
			table4.addCell(cell2);
			p2 = new Paragraph("________________________________________\nJoão Paulo Escritório Imobiliário");
			cell2.add(p);
			cell2.setTextAlignment(TextAlignment.RIGHT);
			cell2.setHeight(60);
			cell2.setBorder(b2);
			table4.addCell(cell2);
			
			document.add(table4);
			document.close();

		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();

	}

}