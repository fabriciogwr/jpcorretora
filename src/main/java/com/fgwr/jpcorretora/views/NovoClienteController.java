package com.fgwr.jpcorretora.views;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Corretor;
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Endereco;
import com.fgwr.jpcorretora.enums.Banco;
import com.fgwr.jpcorretora.enums.EstadoCivil;
import com.fgwr.jpcorretora.enums.TipoConta;
import com.fgwr.jpcorretora.repositories.CorretorRepository;
import com.fgwr.jpcorretora.services.ClienteService;
import com.fgwr.jpcorretora.utils.AutoCompleteBox;
import com.fgwr.jpcorretora.utils.FilesUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NovoClienteController {

	@FXML
	private TextField nomeField;
	@FXML
	private TextField emailField;
	@FXML
	private TextField telefonePrefField;
	@FXML
	private TextField telefoneAltField;
	@FXML
	private DatePicker dataNascimentoField;
	@FXML
	private TextField cpfField;
	@FXML
	private TextField rgField;
	@FXML
	private ChoiceBox<String> estadoCivilBox;
	@FXML
	private ComboBox<Corretor> corretorBox;
	@FXML
	private TextField profissaoField;
	@FXML
	private TextField agenciaField;
	@FXML
	private TextField contaField;
	@FXML
	private TextField titularField;
	@FXML
	private TextField pixField;
	@FXML
	private TextField obsField;
	@FXML
	private ComboBox<Banco> bancoBox;
	@FXML
	private ComboBox<String> tipoContaBox;

	private List<String> estadoCivilAux = new ArrayList<>();

	private List<String> bancoAux = new ArrayList<>();

	private List<String> tipoContaAux = new ArrayList<>();

	ApplicationContext context = SpringContext.getAppContext();
	private Stage dialogStage;
	private Cliente cliente;
	private DadosBancarios db;
	FrontApp frontApp = new FrontApp();
	Endereco endereco = new Endereco();
	private boolean okClicked = false;

	private EstadoCivil[] estadoCivil = EstadoCivil.values();
	private Banco[] banco = Banco.values();
	private TipoConta[] tipoConta = TipoConta.values();

	@FXML
	public void handleOnKeyPressed(KeyEvent e) {
		KeyCode code = e.getCode();

		if (code == KeyCode.ENTER) {
			handleOk();
		}
		if (code == KeyCode.ESCAPE) {
			handleCancel();
		}
	}

	private List<Corretor> getCorretorData() {
		CorretorRepository crrRepo = (CorretorRepository) context.getBean("corretorRepository");
		List<Corretor> allCrr = crrRepo.findAll();
		return allCrr;

	}

	@FXML
	private void initialize() {

		for (EstadoCivil estadoCivil : estadoCivil) {
			estadoCivilAux.add(estadoCivil.getDescricao());
		}

		for (Banco banco : banco) {
			bancoAux.add(banco.getFullCod() + " - " + banco.getDescricao());
		}

		for (TipoConta tipoConta : tipoConta) {
			tipoContaAux.add(tipoConta.getDesc());

		}

		estadoCivilBox.setItems(FXCollections.observableArrayList(estadoCivilAux));
		bancoBox.setItems(FXCollections.observableArrayList(Banco.values()));

		Callback<ListView<Banco>, ListCell<Banco>> bancoCellFactory = new Callback<ListView<Banco>, ListCell<Banco>>() {

			@Override
			public ListCell<Banco> call(ListView<Banco> l) {
				return new ListCell<Banco>() {

					@Override
					protected void updateItem(Banco banco, boolean empty) {
						super.updateItem(banco, empty);
						if (banco == null || empty) {
							setGraphic(null);
						} else {
							setText(banco.getFullCod() + " - " + banco.getDescricao());
						}
					}
				};
			}

		};
		bancoBox.setButtonCell(bancoCellFactory.call(null));
		bancoBox.setCellFactory(bancoCellFactory);
		bancoBox.setTooltip(new Tooltip());

		new AutoCompleteBox<Banco>(bancoBox);
		tipoContaBox.setItems(FXCollections.observableArrayList(tipoContaAux));

		corretorBox.setItems(FXCollections.observableArrayList(getCorretorData()));

		Callback<ListView<Corretor>, ListCell<Corretor>> corretorCellFactory = new Callback<ListView<Corretor>, ListCell<Corretor>>() {

			@Override
			public ListCell<Corretor> call(ListView<Corretor> l) {
				return new ListCell<Corretor>() {

					@Override
					protected void updateItem(Corretor corretor, boolean empty) {
						super.updateItem(corretor, empty);
						if (corretor == null || empty) {
							setGraphic(null);
						} else {
							setText(corretor.getNome());
						}
					}
				};
			}
		};

		corretorBox.setButtonCell(corretorCellFactory.call(null));
		corretorBox.setCellFactory(corretorCellFactory);

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private boolean isInputValid() {
		String errorMessage = "";

		if (nomeField.getText() == null || nomeField.getText().length() == 0 || nomeField.getText().matches("[0-9]")) {
			errorMessage += "Nome inválido\n";
		}

		if (cpfField.getText() == null || cpfField.getText().length() == 0) {
			errorMessage += "Digite um CPF\n";
		} else if ((cpfField.getText().length() != 11 && cpfField.getText().length() != 14)
				|| cpfField.getText().matches("[a-zA-Z_]+")) {
			errorMessage += "CPF inválido, digite somente números\n";
		}

		if (cpfField.getText() != cliente.getCpfOuCnpj()) {
			ClienteService cliServ = (ClienteService) context.getBean("clienteService");
			Cliente cli = cliServ.findByCpfOuCnpj(cpfField.getText());
			if (cli != null) {
				if (cli.getId() != cliente.getId()) {
					errorMessage += "CPF já cadastrado para o cliente " + cli.getNome() + "\n";
				}
			}
		}

		if (rgField.getText() == null || rgField.getText().length() == 0 || rgField.getText().matches("[a-zA-Z_]+")) {
			if (cpfField.getText().length() == 11) {
				errorMessage += "RG inválido\n";
			}
		}

		if (estadoCivilBox.getValue() == null) {
			if (cpfField.getText().length() == 11) {
				errorMessage += "Selecione um Estado Civil\n";
			}
		}

		if ((dataNascimentoField.getValue() == null || dataNascimentoField.getEditor().getText().isBlank())
				&& dataNascimentoField.getEditor().getText().length() < 10 && cpfField.getText().length() == 14) {
			errorMessage += "Data de Nascimento inválida\n";

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

	public void setCliente(Cliente cliente, DadosBancarios db, Endereco end) {
		this.cliente = cliente;
		this.db = db;
		this.endereco = end;

		nomeField.setText("");
		emailField.setText("");
		dataNascimentoField.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
		telefonePrefField.setText("");
		telefoneAltField.setText("");
		cpfField.setText("");
		rgField.setText("");
		pixField.setText("");

		estadoCivilBox.setValue(null);

		profissaoField.setText("");

		agenciaField.setText("");
		titularField.setText("");
		contaField.setText("");

		bancoBox.setValue(null);
		tipoContaBox.setValue(null);

	}

	@FXML
	private void handleEditEndereco() throws IOException {
		if (endereco == null) {
			endereco = new Endereco();

			boolean okClicked = frontApp.showEditEndereco(endereco);

			if (okClicked) {
				endereco.setCliente(cliente);
				this.cliente.getEnderecos().add(endereco);
			}

		} else {
			boolean okClicked = frontApp.showEditEndereco(endereco);
			if (okClicked) {
				if (endereco == null) {
					cliente.getEnderecos().clear();
				}
			}
		}
	}

	@FXML
	private void handleOk() {
		if (isInputValid()) {

			cliente.setActive(true);
			cliente.setNome(nomeField.getText().trim());
			cliente.setEmail(emailField.getText().trim());
			cliente.setTelefonePref(telefonePrefField.getText().trim());

			if (!telefoneAltField.getText().isBlank()) {
				cliente.setTelefoneAlt(telefoneAltField.getText().trim());
			} else {
				cliente.setTelefoneAlt("");
			}

			if (dataNascimentoField.getEditor().getText().isBlank()) {
				cliente.setDataNascimento(
						Date.from(dataNascimentoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			} else if (!dataNascimentoField.getEditor().getText().isBlank()) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				try {
					Date date = formatter.parse(dataNascimentoField.getEditor().getText().trim());
					cliente.setDataNascimento(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			cliente.setCpfOuCnpj(cpfField.getText().trim());
			cliente.setRg(rgField.getText().trim());
			if (cpfField.getText().length() == 14) {
				Integer x = null;
				cliente.setEstadoCivil(x);
			} else {
				cliente.setEstadoCivil(EstadoCivil.valueOfDescricao(estadoCivilBox.getValue()));
			}
			cliente.setProfissao(profissaoField.getText().trim());
			cliente.setObs(obsField.getText());

			db.setId(null);
			db.setPix(pixField.getText().trim());
			db.setAgencia(agenciaField.getText().trim());
			db.setConta(contaField.getText().trim());
			db.setTitular(titularField.getText().trim());
			if (bancoBox.getValue() != null) {
				db.setBanco(bancoBox.getValue());
			}
			if (tipoContaBox.getValue() != null) {
				db.setTipo(TipoConta.valueOfDescricao(tipoContaBox.getValue()));
			}

			db.setCliente(cliente);
			cliente.setDadosBancarios(db);

			okClicked = true;
			dialogStage.close();
		}
	}

}
