package com.fgwr.jpcorretora.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.StringUtils;

import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Testemunha;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

public class ContratoPdfGen {
	

	public static String fileToString ( File file ) {
	    try {
	        return file.toURI().toURL().toString();
	    } catch ( MalformedURLException e ) {
	        return null;
	    }
	}
	
	private String cpfFormat(String cpf) {
	StringBuilder sb = new StringBuilder(cpf);
	sb.insert(3, ".");
	sb.insert(7, ".");
	sb.insert(11, "-");
	return sb.toString();
	}
	
	String DEST;
	public void geraContrato(Contrato contrato, Testemunha test1, Testemunha test2) throws Exception {

		Calendar cal = Calendar.getInstance();
	//	cal.setTime(contrato.getDataPagamento());
		String docFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		String[] nomeArr = StringUtils.split(contrato.getCliente().getNome());
		PdfWriter writer = new PdfWriter(
				docFolder + "\\Contratos\\" + contrato.getId().toString() + " - " + nomeArr[0] + " " + nomeArr[1] + ".pdf");
		DEST = docFolder + "\\Contratos\\" + contrato.getId().toString() + " - " + nomeArr[0] + " " + nomeArr[1] + ".pdf";
		PdfDocument pdfDoc = new PdfDocument(writer);
		Document document = new Document(pdfDoc);
		
		List<String> telefoneData = new ArrayList<>();
		Set<String> telefones = contrato.getCliente().getTelefones();
		for (String string : telefones) {
			telefoneData.add(string);
		}

		
		TableHeaderEventHandler handler = new TableHeaderEventHandler(document, contrato.getId());
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

        // Calculate top margin to be sure that the table will fit the margin.
        float topMargin = 20 + handler.getTableHeight();
        document.setMargins(topMargin, 36, 36, 36);
        
        Border b3 = new SolidBorder(ColorConstants.BLACK, 1);
		Border b2 = (new SolidBorder(ColorConstants.WHITE, 0));

		Cell cell = new Cell();
		
		Paragraph p = new Paragraph();
		
		
		document.add(new Paragraph());
		
		Table table2 = new Table(1).useAllAvailableWidth();

		Text loc = new Text("LOCATÁRIO").setBold();
		Text adm = new Text("ADMINISTRADOR").setBold();
		Text space = new Text("\s ");
		cell = new Cell();
		
		String nome = contrato.getCliente().getNome();
		Text t1 = new Text("");
		t1.setText(nome);
		t1 = t1.setBold();
		String cpf = contrato.getCliente().getCpfOuCnpj();
		Text t2 = new Text(cpf);
		t2.setText(cpf);
		t2 = t2.setBold();
		p = new Paragraph("\nSão partes neste instrumento :\n");	
		cell.add(p);

		cell.setBorder(b2);

		table2.addCell(cell);

		cell = new Cell();
		Text admStr = new Text("ADMINISTRADOR: ").setBold();
		p = new Paragraph();
		p.add(admStr);
		p.add("JOÃO PAULO SANTOS TEODORO, CPF: 657.114.242-20, CORRETOR DE IMÓVEIS, CRECI/RO 1637, 24° REGIÃO.\n");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);

		cell = new Cell();
		Text locStr = new Text("LOCATÁRIO: ").setBold();
		p = new Paragraph();
		p.add(locStr);
		p.add(contrato.getCliente().getNome().toUpperCase() + ", CPF: " + cpfFormat(contrato.getCliente().getCpfOuCnpj()) + ", RG: "
				+ contrato.getCliente().getRg() + ", " + contrato.getCliente().getEstadoCivil().getDescricao().toUpperCase()
				+ ", " + contrato.getCliente().getProfissao().toUpperCase()
				+ ", residente e domiciliado(a) nesta cidade de VILHENA-RO, Telefone " + telefoneData.get(0) + ".\n");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);

		cell = new Cell();
		
		Text cl1 = new Text("\nCLÁUSULA PRIMEIRA: ").setBold();
		p = new Paragraph();
		p.add(cl1);
		p.add("O objeto do presente Contrato de Locação é o imóvel localizado na "
				+ contrato.getImovel().getEndereco().getLogradouro() + ", nº "
				+ contrato.getImovel().getEndereco().getNumero() + ", bairro "
				+ contrato.getImovel().getEndereco().getBairro() + ", na cidade de "
				+ contrato.getImovel().getEndereco().getCidade() + "-" + contrato.getImovel().getEndereco().getEstado() + ". ");
		
		Text bodyA = new Text(
				"Fica atribuída ao LOCATÁRIO a responsabilidade de zelar pela conservação e limpeza do imóvel, efetuando as reformas necessárias para sua manutenção, sendo que os gastos e pagamentos decorrentes de tais manutenções correrão por conta do LOCATÁRIO. O LOCATÁRIO está obrigado a devolver o imóvel em perfeitas condições de conservação, limpeza e pintura, quando finda ou rescindida esta avença, conforme constatado no LAUDO DE VISTORIA. O LOCATÁRIO não poderá realizar obras que alterem ou modifiquem a estrutura do imóvel locado sem prévia autorização por escrito do ADMINISTRADOR. Caso este consinta na realização das obras, estas ficarão desde logo, incorporadas ao imóvel, sem que assista ao LOCATÁRIO qualquer indenização pelas obras ou retenção por benfeitorias. As benfeitorias removíveis poderão ser retiradas, desde que não desfigurem o imóvel locado.\n").setBold();
		p.add(bodyA);
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		Text cl2 = new Text("\nCLÁUSULA SEGUNDA: ").setBold();
		p = new Paragraph();
		p.add(cl2);
		p.add("O prazo da locação é de ");
		
		Text prazoInt = new Text(contrato.getQtdParcelas().toString()).setBold();
		p.add(prazoInt);
		p.add(space);
		Text prazoStr = new Text(ValorExtenso.valorPorExtenso(contrato.getQtdParcelas())).setBold();
		Text aParenteses = new Text("(").setBold();
		Text fParenteses = new Text(")").setBold();
		Text meses = new Text(" meses").setBold();
		
		p.add(aParenteses);
		p.add(prazoStr);
		p.add(fParenteses);
		p.add(meses);
		Text virgula = new Text(", ").setBold();
		p.add(virgula);
		p.add("iniciando-se em ");
		Calendar gCal = new GregorianCalendar();
		gCal = Calendar.getInstance();
		gCal.setTime(contrato.getData());
		Text start = new Text(gCal.get(Calendar.DAY_OF_MONTH) + " de " +new SimpleDateFormat("MMMMM", new Locale ("pt", "BR")).format(contrato.getData()) + " de " + gCal.get(Calendar.YEAR)).setBold();
		gCal = GregorianCalendar.getInstance();
		gCal.setTime(contrato.getDuplicatas().get(1).getDataVencimento());
		
		gCal.add(Calendar.MONTH, contrato.getQtdParcelas()-1);			
		Text end = new Text(gCal.get(Calendar.DAY_OF_MONTH) + " de " +new SimpleDateFormat("MMMMM", new Locale ("pt", "BR")).format(gCal.getTime()) + " de " + gCal.get(Calendar.YEAR)).setBold();
		
		p.add(start);
		p.add(", a terminar em ");
		p.add(end);
		p.add(", independentemente de aviso, notificação ou interpelação judicial ou mesmo extrajudicial.");
		
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		Text cl3 = new Text("\nCLÁUSULA TERCEIRA: ").setBold();
		p = new Paragraph();
		p.add(cl3);
		p.add("As mensalidades do presente contrato de locação de imóvel deverão ser pagas até o dia ");
		cal.setTime(contrato.getDuplicatas().get(1).getDataVencimento());
		Integer vencimento = cal.get(Calendar.DAY_OF_MONTH);
		Text vencimentos = new Text(vencimento.toString()).setBold();
		Text vencimentosExt = new Text(ValorExtenso.valorPorExtenso(vencimento)).setBold();
		p.add(vencimentos);
		p.add(space);
		p.add(aParenteses);
		p.add(vencimentosExt);
		p.add(fParenteses);
		p.add(" de cada mês durante a vigência do contrato pelo ");
		p.add(loc);
		p.add(", cada mensalidade no valor de ");
		
		NumberFormat real = NumberFormat.getNumberInstance();
		real.setMinimumFractionDigits(2);
		real.setMaximumFractionDigits(2);
		
		
		String valor = real.format(contrato.getValorDeCadaParcela());
		Text textValor = new Text(valor).setBold();
		Text textValorT = new Text(valor);
		Text sinal = new Text("R$ ").setBold();
		Text sinalT = new Text("R$ ");
		
		Text valorExt = new Text(ValorExtenso.valorPorExtenso(contrato.getValorDeCadaParcela()));
		p.add(sinal);
		p.add(textValor);
		p.add(space);
		p.add(aParenteses);
		p.add(valorExt);
		p.add(fParenteses);
		p.add(".");
		
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		Text cl4 = new Text("\nCLÁUSULA QUARTA: ").setBold();
		p = new Paragraph();
		p.add(cl4);
		p.add("O ");
		p.add(loc);
		p.add(" deve pagar todas as despesas geradas para ligação e consumo de energia elétrica e água, que serão pagas diretamente às concessionárias dos referidos serviços. O ");
		p.add(loc);
		p.add(" deve fornecer ao ");
		p.add(adm);	
		p.add(" do imóvel uma cópia dos comprovantes de pagamento das contas de energia elétrica e água para que sejam arquivados junto ao presente contrato de locação de imóvel.");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		Text cl5 = new Text("\nCLÁUSULA QUINTA: ").setBold();
		p = new Paragraph();
		p.add(cl5);
		p.add("Em caso de atraso no pagamento do aluguel, aplicar-se-há uma multa de 2% (dois por cento) sobre o valor devido e juros mensais de 1% (um por cento) sob montante devido.");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		Text cl6 = new Text("\nCLÁUSULA SEXTA: ").setBold();
		p = new Paragraph();
		p.add(cl6);
		p.add("Fica convencionado ainda pelos contratantes que o pagamento da multa não significa a renúncia de qualquer direito ou aceitação da emenda judicial da mora, em caso de qualquer procedimento judicial contra o ");
		
		p.add(loc);
		Text dot = new Text(".");
		p.add(dot);
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		Text cl7 = new Text("\nCLÁUSULA SÉTIMA: ").setBold();
		p = new Paragraph();
		p.add(cl7);
		p.add("As obras e despesas com a conservação, limpeza e asseio do imóvel correrão por conta, risco e ônus do ");
		p.add(loc);
		p.add(", ficando este obrigado a devolver o imóvel em perfeitas condições de limpeza, asseio, conservação e pintura, quando finda ou rescindida esta avença, sem qualquer responsabilidade pecuniária para o ");
		
		p.add(adm);
		p.add(". O ");
		p.add(loc);
		p.add(" não poderá realizar obras de vulto e nem modificar a estrutura do imóvel ora locado, sem prévia autorização por escrito do ");
		p.add(adm);
		p.add(". Caso ");
		Text este = new Text("este").setBold();
		p.add(este);
		p.add(" consinta na realização das obras, estas ficarão desde logo, incorporadas ao imóvel, sem que assista ao ");
		p.add(loc);
		p.add(" qualquer indenização pelas obras ou retenção por benfeitorias. As benfeitorias removíveis poderão ser retiradas, desde que não desfigurem o imóvel locado.");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		Text cl8 = new Text("\nCLÁUSULA OITAVA: ").setBold();
		p = new Paragraph();
		p.add(cl8);
		p.add("O ");
		p.add(loc);
		p.add(" declara que o imóvel ora locado destina-se para seu uso familiar.");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		Text cl9 = new Text("\nCLÁUSULA NONA: ").setBold();
		p = new Paragraph();
		p.add(cl9);
		p.add("O ");
		p.add(loc);
		p.add(" não poderá sublocar, transferir ou ceder o imóvel, sendo nulo de pleno direito qualquer ato praticado com este fim sem o consentimento prévio e por escrito do ");
		p.add(adm);
		p.add(".");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		Text cl10 = new Text("\nCLÁUSULA DÉCIMA: ").setBold();
		p = new Paragraph();
		p.add(cl10);
		p.add("Em caso de sinistro parcial ou total do imóvel locado que o torne inabitável, o presente contrato ficará rescindido, de pleno direito, independentemente de aviso ou interpelação judicial ou extrajudicial. No caso de incêndio parcial, obrigando a obras de reconstrução, o presente contrato terá suspendido a sua vigência e reduzida a renda do imóvel durante o período da reconstrução à metade do que na época for o aluguel, e sendo após a reconstrução devolvida o ");
		p.add(loc);
		p.add(" pelo prazo restante do contrato, que ficará prorrogado pelo mesmo tempo de duração das obras de reconstrução.");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		Text cl11 = new Text("\nCLÁUSULA DÉCIMA PRIMEIRA: ").setBold();
		p = new Paragraph();
		p.add(cl11);
		p.add("Em caso de desapropriação total ou parcial do imóvel locado, ficará rescindido de pleno direito o presente contrato de locação, independente de quaisquer indenizações de ambas as partes ou contratantes.");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		Text cl12 = new Text("\nCLÁUSULA DÉCIMA SEGUNDA: ").setBold();
		p = new Paragraph();
		p.add(cl12);
		p.add("No caso de alienação do imóvel, obriga-se o ");
		p.add(adm);
		p.add(" a dar preferência ao ");
		p.add(loc);
		p.add(", e se o mesmo não se utilizar dessa prerrogativa, o ");
		p.add(adm);
		p.add(" deverá constar da respectiva escritura pública à existência do presente contrato, para que o adquirente o respeite nos termos da legislação vigente. ");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);

		cell = new Cell();
		Text cl13 = new Text("\nCLÁUSULA DÉCIMA TERCEIRA: ").setBold();
		p = new Paragraph();
		p.add(cl13);
		p.add("Cabe ao ");
		p.add(loc);
		p.add(" o cumprimento, dentro dos prazos legais, de quaisquer multas ou intimações por infrações das leis, portarias ou regulamentos vigentes, originários de quaisquer repartições ou entidades. Obriga-se ainda, a entregara ao ");
		p.add(adm);
		p.add(" dentro de prazos que permita o seu cumprimento, aviso ou notificação de interesse do imóvel, sob pena de, não o fazendo, assumir integral responsabilidade pela falta.");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		Text cl14 = new Text("\nCLÁUSULA DÉCIMA QUARTA: ").setBold();
		p = new Paragraph();
		p.add(cl14);
		p.add("Em caso de quebra de contrato por qualquer das partes, fica combinado a multa de ");
		Text multaResc = new Text("1 (uma) vez o valor de mensalidade de aluguel ").setBold();
		
		p.add(multaResc);
		p.add("(");
		p.add(sinalT);
		p.add(textValorT);
		p.add(space);
		p.add("(");
		p.add(valorExt);
		p.add(")");
		p.add("), tomando por base o último aluguel, cobrável ou não, por ação executiva, sem prejuízo da rescisão imediata deste contrato, além do pagamento de todas as despesas por procedimentos judiciais e outras sanções que o caso indicar, proporcionalmente ao período de contrato (pro-rata).");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		Text cl15 = new Text("\nCLÁUSULA DÉCIMA QUINTA: ").setBold();
		p = new Paragraph();
		p.add(cl15);
		p.add("As partes contratantes obrigam-se por si, herdeiros e/ou sucessores, elegendo o Foro da Cidade de Vilhena para o processamento de qualquer ação oriunda da presente avença, renunciando, de futuro, a qualquer outro, por mais privilegiado que seja o domicílio dos mesmos.");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);
		
		cell = new Cell();
		p = new Paragraph();
		p.add("\n ");
		p.setFirstLineIndent(24);
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);		
		
		cell = new Cell();
		p = new Paragraph();
		p.add("E por assim estarem justos e contratados, mandaram extrair o presente instrumento em 2 (duas) vias, para um só efeito, assinando-as, juntamente com as testemunhas, a tudo presente.");
		p.setFirstLineIndent(24);
		cell.add(p);
		cell.setTextAlignment(TextAlignment.JUSTIFIED);
		cell.setBorder(b2);
		table2.addCell(cell);		
		
		cell = new Cell();
		p = new Paragraph();
		cal = Calendar.getInstance();
		p.add("\nVilhena, "  + cal.get(Calendar.DAY_OF_MONTH) + " de "  + new SimpleDateFormat("MMMMM", new Locale ("pt", "BR")).format(cal.getTime()) + " de " + cal.get(Calendar.YEAR) + ".");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.RIGHT);
		cell.setBorder(b2);
		table2.addCell(cell);
		document.add(table2);
		
		document.add(new Paragraph("\n\n\n "));
		
		Table table3 = new Table(new float[] {4, 2, 4}).useAllAvailableWidth();
		cell = new Cell();
		p = new Paragraph(contrato.getCliente().getNome() + "\nCPF: " + cpfFormat(contrato.getCliente().getCpfOuCnpj()) + "\nLocatário");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorder(b2);
		cell.setBorderTop(b3);
		table3.addCell(cell);
		
		
		
		cell = new Cell();
		table3.addCell(cell);
		p = new Paragraph("João Paulo Santos Teodoro\nCPF: 657.114.292-20\nAdministrador");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorder(b2);
		cell.setBorderTop(b3);
		table3.addCell(cell);
		
		cell = new Cell();
		cell.setHeight(50);
		cell.setBorder(b2);
		table3.addCell(cell);
		table3.addCell(cell);
		table3.addCell(cell);
		
		cell = new Cell();
		p = new Paragraph(test1.getNome() + "\nCPF: " + cpfFormat(test1.getCpf()) + "\nTestemunha");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorder(b2);
		cell.setBorderTop(b3);
		table3.addCell(cell);
		
		cell = new Cell();
		cell.setBorder(b2);
		table3.addCell(cell);
		
		cell = new Cell();
		p = new Paragraph(test2.getNome() + "\nCPF: " + cpfFormat(test2.getCpf())+ "\nTestemunha");
		cell.add(p);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorder(b2);
		cell.setBorderTop(b3);
		table3.addCell(cell);
		
		document.add(table3);
		document.close();
	

	}
	
	private static class TableHeaderEventHandler implements IEventHandler {
        private Table table;
        private float tableHeight;
        private Document doc;

        public TableHeaderEventHandler(Document doc, Integer contratoId) throws IOException {
            this.doc = doc;
            initTable(contratoId);

            TableRenderer renderer = (TableRenderer) table.createRendererSubTree();
            renderer.setParent(new DocumentRenderer(doc));

            // Simulate the positioning of the renderer to find out how much space the header table will occupy.
            LayoutResult result = renderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4)));
            tableHeight = result.getOccupiedArea().getBBox().getHeight();
        }

        @Override
        public void handleEvent(Event currentEvent) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
            PageSize pageSize = pdfDoc.getDefaultPageSize();
            float coordX = pageSize.getX() + doc.getLeftMargin();
            float coordY = pageSize.getTop() - doc.getTopMargin();
            float width = pageSize.getWidth() - doc.getRightMargin() - doc.getLeftMargin();
            float height = getTableHeight();
            Rectangle rect = new Rectangle(coordX, coordY, width, height);

            new Canvas(canvas, rect)
                    .add(table)
                    .close();
        }

        public float getTableHeight() {
            return tableHeight;
        }

        private void initTable(Integer contratoId) throws IOException
        {
        	
        	ImageData header = ImageDataFactory.create(fileToString( new File("imgs/logo.png")));
			Image logoHeader = new Image(header);
			logoHeader.setWidth(250);

			PdfFont titleFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
			
        	table = new Table(2).useAllAvailableWidth();
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
			Paragraph p = new Paragraph("CONTRATO DE LOCAÇÃO DE IMÓVEL\nnº " + contratoId);
			p.setFont(titleFont);
			p.setFontSize(16);
			p.setTextAlignment(TextAlignment.RIGHT);
			p.setMarginRight(5);
			cell.add(p);
			cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			cell.setBorder(b2);
			cell.setBorderTop(b1);
			cell.setBorderBottom(b1);
			cell.setBackgroundColor(ColorConstants.YELLOW);

			table.addCell(cell);
        }
    }
    
}