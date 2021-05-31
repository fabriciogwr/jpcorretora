package com.fgwr.jpcorretora.views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Receita;
import com.fgwr.jpcorretora.domain.Recibo;
import com.fgwr.jpcorretora.domain.Testemunha;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.ContratoRepository;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.repositories.ReciboRepository;
import com.fgwr.jpcorretora.services.CategoriaService;
import com.fgwr.jpcorretora.services.ClienteService;
import com.fgwr.jpcorretora.services.ContratoPdfGen;
import com.fgwr.jpcorretora.services.DuplicataService;
import com.fgwr.jpcorretora.services.ImovelService;
import com.fgwr.jpcorretora.utils.FilesUtils;
import com.fgwr.jpcorretora.utils.StringsUtils;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class NovoContratoController {

	FrontApp frontApp = new FrontApp();
	private Stage dialogStage;
	private boolean okClicked = false;
	
	Double primeiraParcela;
	ContratoPdfGen pdfGen = new ContratoPdfGen();

	ObservableList<Imovel> imoveis;
	ObservableList<Cliente> clientes;
	@FXML
	private ComboBox<Cliente> clienteBox;
	@FXML
	private ComboBox<Imovel> imovelBox;
	@FXML
	private DatePicker iniciaEmBox;
	@FXML
	private TextField tempoLocacaoField;
	@FXML
	private TextField valorField;
	@FXML
	private TextField vencimentosField;
	@FXML
	private TextField primeiraParcelaField;
	@FXML
	private TextField testemunha1Field;
	@FXML
	private TextField testemunha1CpfField;
	@FXML
	private TextField testemunha2Field;
	@FXML
	private TextField testemunha2CpfField;
	@FXML
	private Label primeiraParcelaLabel;

	ApplicationContext context = SpringContext.getAppContext();

	private Contrato contrato;

	public List<Imovel> getImovelData() {
		ImovelRepository imRepo = (ImovelRepository) context.getBean("imovelRepository");
		List<Imovel> imoveis = imRepo.findByActiveOrderByProprietarioNomeAsc(true);
		imoveis.removeIf(imovel -> imovel.getContrato() != null);

		return imoveis;
	}

	public List<Cliente> getClienteData() {
		ClienteRepository cliRepo = (ClienteRepository) context.getBean("clienteRepository");
		List<Cliente> clientes = cliRepo.findByActiveOrderByNomeAsc(true);
		return clientes;
	}

	public void initialize() {
		

		imovelBox.setItems(FXCollections.observableArrayList(getImovelData()));

		Callback<ListView<Imovel>, ListCell<Imovel>> imovelCellFactory = new Callback<ListView<Imovel>, ListCell<Imovel>>() {

			@Override
			public ListCell<Imovel> call(ListView<Imovel> l) {
				return new ListCell<Imovel>() {

					@Override
					protected void updateItem(Imovel imovel, boolean empty) {
						super.updateItem(imovel, empty);
						if (imovel == null || empty) {
							setGraphic(null);
						} else {
							setText(imovel.getProprietario().getNome() + " - " + imovel.getEndereco().getLogradouro()
									+ ", " + imovel.getEndereco().getNumero());
						}
					}
				};
			}
		};

		imovelBox.setButtonCell(imovelCellFactory.call(null));
		imovelBox.setCellFactory(imovelCellFactory);

		clienteBox.setItems(FXCollections.observableArrayList(getClienteData()));

		Callback<ListView<Cliente>, ListCell<Cliente>> clienteCellFactory = new Callback<ListView<Cliente>, ListCell<Cliente>>() {

			@Override
			public ListCell<Cliente> call(ListView<Cliente> l) {
				return new ListCell<Cliente>() {

					@Override
					protected void updateItem(Cliente cliente, boolean empty) {
						super.updateItem(cliente, empty);
						if (cliente == null || empty) {
							setGraphic(null);
						} else {
							setText(cliente.getNome());
						}
					}
				};
			}
		};

		clienteBox.setButtonCell(clienteCellFactory.call(null));
		clienteBox.setCellFactory(clienteCellFactory);

		Calendar cal = Calendar.getInstance();

		Date now = new Date();

		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		vencimentosField.textProperty().addListener((observable, oldValue, newValue) -> {
			cal.setTime(now);
			pause.setOnFinished(event -> correct(newValue, iniciaEmBox.getValue(), now));
			pause.playFromStart();

		});
		
		iniciaEmBox.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
		iniciaEmBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			pause.setOnFinished(event -> correct(vencimentosField.getText().trim(), newValue, now));
			pause.playFromStart();
		});
		
		testemunha1Field.setText("Débora Fernanda Rodrigues");
		testemunha1CpfField.setText("05095351277");
		testemunha2Field.setText("Grasiela da Silva Weyh");
		testemunha2CpfField.setText("88443892234");

	}

	public void correct(String vencimentos, LocalDate loc, Date now) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(Date.from(loc.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		if (vencimentos.isBlank() || Integer.parseInt(vencimentos) == 0) {
			primeiraParcelaField.setText("");
		} else if (!valorField.getText().isBlank() && !vencimentos.isBlank() && Integer.parseInt(vencimentos) > 0
				&& Integer.parseInt(vencimentos) > cal.get(Calendar.DAY_OF_MONTH)) {
			Double pc1 = (Double.parseDouble(valorField.getText()) / 100) * 1;
			Double pc2 = (Double.parseDouble(valorField.getText()) / 100) * 2;

			Double valorPorDia = pc1 / cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			Double diferencaDeVencimento = pc2
					+ (valorPorDia * (Integer.parseInt(vencimentos) - cal.get(Calendar.DAY_OF_MONTH)));
			primeiraParcela = Double.parseDouble(valorField.getText()) + diferencaDeVencimento;
			primeiraParcelaField.setText("R$ " + StringsUtils.formatarDecimals(primeiraParcela));

		} else if (!valorField.getText().isBlank() && !vencimentos.isBlank()
				&& Integer.parseInt(vencimentos) <= cal.get(Calendar.DAY_OF_MONTH)) {
			Double pc1 = (Double.parseDouble(valorField.getText()) / 100) * 1;
			Double pc2 = (Double.parseDouble(valorField.getText()) / 100) * 2;
			Double valorPorDia = pc1 / cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			LocalDate date = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			cal.add(Calendar.MONTH, 1);
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), Integer.parseInt(vencimentos));
			LocalDate date2 = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			Double diferencaDeVencimento = pc2 + (valorPorDia * Math.toIntExact(ChronoUnit.DAYS.between(date, date2)));

			primeiraParcela = Double.parseDouble(valorField.getText()) + diferencaDeVencimento;
			primeiraParcelaField.setText("R$ " + StringsUtils.formatarDecimals(primeiraParcela));

		} else {
			primeiraParcelaField.setText("");
		}
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	public void handleOnKeyPressed(KeyEvent e) throws IOException {
		KeyCode code = e.getCode();

		if (code == KeyCode.ENTER) {
			handleOk();
		}
		if (code == KeyCode.ESCAPE) {
			handleCancel();
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;

	}

	@FXML
	private void handleOk() throws IOException {
		if (isInputValid()) {
			CategoriaService catServ = (CategoriaService) context.getBean("categoriaService");
			DuplicataService ds = (DuplicataService) context.getBean("duplicataService");
			ContratoRepository contRepo = (ContratoRepository) context.getBean("contratoRepository");
			DuplicataRepository dupRepo = (DuplicataRepository) context.getBean("duplicataRepository");
			ClienteRepository cliRepo = (ClienteRepository) context.getBean("clienteRepository");
			ImovelRepository imRepo = (ImovelRepository) context.getBean("imovelRepository");
			ImovelService is = (ImovelService) context.getBean("imovelService");
			ClienteService cs = (ClienteService) context.getBean("clienteService");

			contrato.setCliente(clienteBox.getValue());
			contrato.setImovel(imovelBox.getValue());
			contrato.setData(Date.from(iniciaEmBox.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			contrato.setId(null);
			contrato.setQtdParcelas(Integer.parseInt(tempoLocacaoField.getText().trim()));
			contrato.setValorDeCadaParcela(Double.parseDouble(valorField.getText().trim()));

			List<Duplicata> dups;
			if (vencimentosField.getText().isBlank()) {
				dups = ds.preencherDuplicata(contrato, clienteBox.getValue());

			} else {
				dups = ds.preencherDuplicata(contrato, Integer.parseInt(vencimentosField.getText().trim()),
						clienteBox.getValue());
			}

			Cliente cliente = cs.find(clienteBox.getValue().getId());
			cliente.setContrato(contrato);

			Imovel imovel = is.find(imovelBox.getValue().getId());
			imovel.setContrato(contrato);

			contrato.setActive(true);
			contrato.setCliente(cliente);
			contrato.setImovel(imovel);
			contrato.setDuplicatas(dups);

			contrato = contRepo.save(contrato);
			cliRepo.save(cliente);
			
			imRepo.save(imovel);

			Testemunha t1 = new Testemunha(testemunha1Field.getText().trim(), testemunha1CpfField.getText().trim());
			Testemunha t2 = new Testemunha(testemunha2Field.getText().trim(), testemunha2CpfField.getText().trim());
			try {
				pdfGen.geraContrato(contrato, t1, t2);
				// new TableHeader().manipulatePdf(path);
			} catch (Exception e) {
				System.out.println("Falha ao iniciar comando gerar contrato");
				e.printStackTrace();
			}

			for (Duplicata duplicata : dups) {
				Receita receita = new Receita();
				if (duplicata.getParcela() == 1) {
					receita.setDataVencimento(duplicata.getDataVencimento());
					receita.setDescricao("Comissão de locação de imóvel - contrato nº "
							+ duplicata.getContrato().getId().toString());
					receita.setEstado(duplicata.getEstado());
					receita.setPagador(duplicata.getCliente().getNome());
					receita.setValor(duplicata.getValor() / 2);
					receita.setCategoria(catServ.find(1));
				} else {
					receita.setDataVencimento(duplicata.getDataVencimento());
					receita.setDescricao("Comissão de locação de imóvel - contrato nº "
							+ duplicata.getContrato().getId().toString());
					receita.setEstado(duplicata.getEstado());
					receita.setPagador(duplicata.getCliente().getNome());
					receita.setValor(duplicata.getValor() / 10);
					receita.setCategoria(catServ.find(1));
				}
				duplicata.setReceita(receita);
			}
			dupRepo.saveAll(dups);
			
			List<Duplicata> sDups = ds.findByContrato(contrato);
			dupRepo.flush();
			Duplicata selDup = new Duplicata();
			for (Duplicata duplicata : sDups) {
				if(duplicata.getParcela() == 1) {
					selDup = duplicata;
				}
			}
			
			sDups.clear();
			if (selDup != null) {
			selDup = handlePagamento(selDup);
			}
			
			okClicked = true;

			dialogStage.close();

		}
	}

	private Duplicata handlePagamento(Duplicata dup) throws IOException {
		ReciboRepository rRepo = (ReciboRepository) context.getBean("reciboRepository");
		if (dup != null && dup.getEstado() == EstadoPagamento.PENDENTE) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Confirmar pagamento");
			alert.setHeaderText("Confirmar pagamento da primeira mensalidade?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {

				boolean okClicked = frontApp.showConfiguraPagamento(dup);
				if (okClicked) {
					Recibo rec = rRepo.findByDuplicata(dup);
					Alert alert2 = new Alert(AlertType.CONFIRMATION);
					alert2.initStyle(StageStyle.UNIFIED);
					DialogPane dialogPane2 = alert2.getDialogPane();
					dialogPane2.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
					alert2.setTitle("Recibo");
					alert2.setHeaderText("Visualizar o recibo do pagamento?");
					Optional<ButtonType> result2 = alert2.showAndWait();
					if (result2.get() == ButtonType.OK) {
						visualizaRecibo(rec);

					} else {
						alert.close();
					}

				}

			}
			

		}
		return dup;
		
	}

	public void visualizaRecibo(Recibo rec) throws IOException {
		File file = new File(FilesUtils.pathRecibos(rec));
		Desktop desktop = Desktop.getDesktop();
		desktop.open(file);
	}

	private boolean isInputValid() {
		String errorMessage = "";

		if (clienteBox.getValue() == null) {
			errorMessage += "Selecione um cliente\n";
		}
		if (imovelBox.getValue() == null) {
			errorMessage += "Selecione um Imovel\n";
		}
		if (tempoLocacaoField.getText().isBlank()) {
			errorMessage += "Informe um tempo de locação\n";
		}

		if (valorField.getText().isBlank()) {
			errorMessage += "Informe o valor da mensalidade\n";
		}

		if (vencimentosField.getText().length() >= 3) {
			errorMessage += "Data de vencimentos inválida\n";
		}

		if (testemunha1Field.getText() == null || testemunha1Field.getText().isBlank()) {
			errorMessage += "Informe a testemunha 1\n";
		}

		if (testemunha1CpfField.getText() == null || testemunha1CpfField.getText().isBlank()
				|| testemunha1CpfField.getText().length() != 11) {
			errorMessage += "CPF da testemunha 1 inválido\n";
		}

		if (testemunha1Field.getText() == null || testemunha2Field.getText().isBlank()) {
			errorMessage += "Informe a testemunha 2\n";
		}

		if (testemunha2Field.getText() == null || testemunha2CpfField.getText().isBlank()
				|| testemunha2CpfField.getText().length() != 11) {
			errorMessage += "CPF da testemunha 2 inválido\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Campos Inválidos");
			alert.setHeaderText("Por favor, corrija os campos inválidos");
			alert.setContentText(errorMessage);
			alert.showAndWait();

			return false;
		}
	}

}