package com.fgwr.jpcorretora.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.fgwr.jpcorretora.domain.Recibo;
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
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

public class PdfGen {

	private InputStream getFileFromResourceAsStream(String fileName) {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);

		if (inputStream == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return inputStream;
		}
	}

	private File getFileFromResource(String fileName) throws URISyntaxException {

		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {

			// failed if files have whitespaces or special characters
			// return new File(resource.getFile());

			return new File(resource.toURI());
		}

	}

	public void geraRecibo(Recibo recibo) throws FileNotFoundException {

		Calendar cal = Calendar.getInstance();
		cal.setTime(recibo.getDataPagamento());
		
		String[] nomeArr = StringUtils.split(recibo.getCliente().getNome());
		PdfWriter writer = new PdfWriter(
				"D:\\Recibos\\" + recibo.getId().toString() + " - " + nomeArr[0] + " " + nomeArr[1] + ".pdf");
		PdfDocument pdfDoc = new PdfDocument(writer);
		Document document = new Document(pdfDoc);

		try {

			ImageData header = ImageDataFactory.create(getClass().getResource("../imgs/logo.png").toString());
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
			Paragraph p = new Paragraph("RECIBO \nnº " + recibo.getId());
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
			Text t1 = new Text("");
			t1.setText(nome);
			t1 = t1.setBold();
			String cpf = recibo.getCliente().getCpfOuCnpj();
			Text t2 = new Text(cpf);
			t2.setText(cpf);
			t2 = t2.setBold();
			p = new Paragraph("Recebemos de ");
			p.add(t1);
			p.add(", CPF ");
			p.add(t2);
			p.add(",");
			
			
			cell.add(p);

			cell.setHeight(30);
			cell.setBorder(b2);

			table2.addCell(cell);

			cell = new Cell();
			cell.add(new Paragraph("a importância de " + recibo.getValor() + ","));
			cell.setHeight(30);
			cell.setBorder(b2);
			table2.addCell(cell);

			cell = new Cell();
			cell.add(new Paragraph("Referente quitação da mensalidade " + recibo.getDuplicata().getParcela() + "/"
					+ recibo.getQtdParcelas() +", de validade datada em " + new SimpleDateFormat("dd/MM/yyyy").format(recibo.getDataVencimento()) + ","));
			// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setHeight(30);
			cell.setBorder(b2);
			table2.addCell(cell);

			cell = new Cell();
			cell.add(new Paragraph("Do contrato de locação de imóvel nº " + recibo.getDuplicata().getContrato().getId() + "."));
			// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setHeight(30);
			cell.setBorder(b2);
			cell.setBorderBottom(b1);
			table2.addCell(cell);
			
			cell = new Cell();
			table2.addCell(cell);
			
			cell.add(new Paragraph("Para clareza, firmamos o presente."));
			// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setHeight(30);
			cell.setBorder(b2);
			table2.addCell(cell);
			
			cell = new Cell();
			p = new Paragraph("Vilhena, " + cal.get(Calendar.DAY_OF_MONTH) + " de " + new SimpleDateFormat("MMMMM", new Locale ("pt", "BR")).format(recibo.getDataPagamento()) + " de " + cal.get(Calendar.YEAR) + ".");
			
			cell.add(p);
			cell.setTextAlignment(TextAlignment.RIGHT);
			cell.setHeight(30);
			cell.setBorder(b2);
			table2.addCell(cell);
			
			cell = new Cell();
			table2.addCell(cell);
			p = new Paragraph("____________________________________________\nJoão Paulo Escritório Imobiliário      ");
			cell.add(p);
			cell.setTextAlignment(TextAlignment.RIGHT);
			cell.setHeight(60);
			cell.setBorder(b2);
			table2.addCell(cell);
			
			document.add(table2);
			document.close();

		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();

	}

}