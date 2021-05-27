package com.fgwr.jpcorretora.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.fgwr.jpcorretora.domain.Receita;
import com.fgwr.jpcorretora.enums.Meses;
import com.fgwr.jpcorretora.utils.StringsUtils;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DoubleBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

public class ReceitasMensalGen {

	public String fileToString(File file) {
		try {
			return file.toURI().toURL().toString();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public void geraRelatorioReceita(List<Receita> receitas, String path, Integer mes, Double recebidas, Double receber)
			throws FileNotFoundException {

		PdfWriter writer = new PdfWriter(path);
		PdfDocument pdfDoc = new PdfDocument(writer);
		Document document = new Document(pdfDoc);

		int tableLines = 0;
		try {

			ImageData header = ImageDataFactory.create(fileToString(new File("imgs/logo.png")));
			Image logoHeader = new Image(header);
			logoHeader.setWidth(250);

			PdfFont titleFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

			Table table = new Table(2).useAllAvailableWidth();
			Border b1 = new SolidBorder(ColorConstants.BLACK, 2);
			Border b3 = new SolidBorder(ColorConstants.BLACK, 1.5f);
			Border b4 = new DoubleBorder(ColorConstants.BLACK, 2f);

			Cell cell = new Cell();
			cell.add(logoHeader.setWidth(150));
			cell.setBorder(Border.NO_BORDER);
			cell.setBorderTop(b1);
			cell.setBorderBottom(b1);
			cell.setBackgroundColor(new DeviceRgb(255, 207, 51));

			table.addCell(cell);

			cell = new Cell();
			Paragraph p = new Paragraph("Relatório de Receitas \n" + Meses.toEnum(mes).getDescricao());
			p.setFont(titleFont);
			p.setFontSize(16);
			p.setTextAlignment(TextAlignment.RIGHT);
			p.setMarginRight(20);
			cell.add(p);
			cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			cell.setBorder(Border.NO_BORDER);
			cell.setBorderTop(b1);
			cell.setBorderBottom(b1);
			cell.setBackgroundColor(new DeviceRgb(255, 207, 51));

			table.addCell(cell);
			document.add(table);

			document.add(new Paragraph());
			document.add(new Paragraph());
			document.add(new Paragraph());

			Table table2 = new Table(4).useAllAvailableWidth();

			var headers = List.of("Categoria", "Vencimento", "Pagamento", "Valor");

			for (String head : headers) {
				cell = new Cell();
				String categoria = head;
				Text t1 = new Text(categoria).setBold();

				p = new Paragraph();
				p.add(t1);
				cell.add(p);
				cell.setHeight(17);
				cell.setBorder(Border.NO_BORDER);
				cell.setBorderTop(b3);
				cell.setBackgroundColor(new DeviceRgb(255, 207, 51));
				cell.setBorderBottom(b3);
				cell.setBorderRight(Border.NO_BORDER);
				cell.setTextAlignment(TextAlignment.JUSTIFIED);
				table2.addCell(cell);
			}

			for (Receita receita : receitas) {
				tableLines++;
				var line = List.of(receita.getCategoria().getNome(),
						StringsUtils.formatarData(receita.getDataVencimento()),
						StringsUtils.formatarData(receita.getDataRecebimento()),
						StringsUtils.formatarRealCifra(receita.getValor()));
				for (String string : line) {
					cell = new Cell();
					String categoria = string;
					Text t1 = new Text(categoria);

					p = new Paragraph();
					p.add(t1);
					cell.add(p);
					cell.setHeight(16);

					cell.setBorder(Border.NO_BORDER);
					cell.setTextAlignment(TextAlignment.JUSTIFIED);
					if (tableLines % 2 == 1) {
						cell.setBackgroundColor(new DeviceRgb(214, 214, 214));
						// cell.setBorderRight(new SolidBorder(new DeviceRgb(214, 214, 214), 1));
					}
					table2.addCell(cell);
				}

			}

			cell = new Cell();
			String recebidasT = "Total de receitas recebidas";
			Text t1 = new Text(recebidasT);
			p = new Paragraph();
			p.add(t1);
			cell.add(p);
			cell.setHeight(17);
			cell.setBorder(Border.NO_BORDER);
			cell.setBorderTop(b4);
			cell.setTextAlignment(TextAlignment.JUSTIFIED);
			table2.addCell(cell);

			cell = new Cell();
			cell.setBorder(Border.NO_BORDER);
			cell.setBorderTop(b4);
			table2.addCell(cell);

			cell = new Cell();
			cell.setBorder(Border.NO_BORDER);
			cell.setBorderTop(b4);
			table2.addCell(cell);

			cell = new Cell();
			String recebidasValorTotal = StringsUtils.formatarRealCifra(recebidas);
			Text t2 = new Text(recebidasValorTotal);
			p = new Paragraph();
			p.add(t2);
			cell.add(p);
			cell.setHeight(17);
			cell.setBorder(Border.NO_BORDER);
			cell.setBorderTop(b4);
			cell.setTextAlignment(TextAlignment.JUSTIFIED);
			table2.addCell(cell);

			cell = new Cell();
			String receberT = "Total de receitas ainda a receber";
			Text t3 = new Text(receberT);
			p = new Paragraph();
			p.add(t3);
			cell.add(p);
			cell.setHeight(17);
			cell.setBorder(Border.NO_BORDER);
			cell.setBorderBottom(b3);
			cell.setTextAlignment(TextAlignment.JUSTIFIED);
			table2.addCell(cell);

			cell = new Cell();
			cell.setBorder(Border.NO_BORDER);
			cell.setBorderBottom(b3);
			table2.addCell(cell);

			cell = new Cell();
			cell.setBorder(Border.NO_BORDER);
			cell.setBorderBottom(b3);
			table2.addCell(cell);

			cell = new Cell();
			String receberValorTotal = StringsUtils.formatarRealCifra(receber);
			Text t4 = new Text(receberValorTotal);
			p = new Paragraph();
			p.add(t4);
			cell.add(p);
			cell.setHeight(17);
			cell.setBorder(Border.NO_BORDER);
			cell.setBorderBottom(b3);
			cell.setTextAlignment(TextAlignment.JUSTIFIED);
			table2.addCell(cell);

			document.add(table2);

			document.close();

		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();

	}

}