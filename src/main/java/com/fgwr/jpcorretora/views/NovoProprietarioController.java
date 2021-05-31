package com.fgwr.jpcorretora.views;

import java.io.File;
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

import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Proprietario;
import com.fgwr.jpcorretora.enums.Banco;
import com.fgwr.jpcorretora.enums.EstadoCivil;
import com.fgwr.jpcorretora.enums.TipoConta;
import com.fgwr.jpcorretora.repositories.ProprietarioRepository;
import com.fgwr.jpcorretora.utils.AutoCompleteBox;
import com.fgwr.jpcorretora.utils.FilesUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NovoProprietarioController {

	ApplicationContext context = SpringContext.getAppContext();

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
	private TextField profissaoField;
	@FXML
	private TextField agenciaField;
	@FXML
	private TextField contaField;
	@FXML
	private TextField titularField;
	@FXML
	private TextField obsField;
	@FXML
	private TextField pixField;
	@FXML
	private ComboBox<Banco> bancoBox;
	@FXML
	private ComboBox<String> tipoContaBox;

	private List<String> estadoCivilAux = new ArrayList<>();

	private List<String> tipoContaAux = new ArrayList<>();

	private Stage dialogStage;
	private Proprietario proprietario;
	private DadosBancarios db;
	private boolean okClicked = false;

	private EstadoCivil[] estadoCivil = EstadoCivil.values();
	private TipoConta[] tipoConta = TipoConta.values();

	@FXML
	private void initialize() {

		for (EstadoCivil estadoCivil : estadoCivil) {
			estadoCivilAux.add(estadoCivil.getDescricao());
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

		if (nomeField.getText() == null || nomeField.getText().length() == 0) {
			errorMessage += "Nome inválido\n";
		}
		if (telefonePrefField.getText() == null || telefonePrefField.getText().length() == 0) {
			errorMessage += "Adicione ao menos 1 telefone\n";
			if (telefonePrefField.getText().length() < 10 || telefonePrefField.getText().length() > 11) {
				errorMessage += "Telefone inválico\n";
			}
		}

		if (cpfField.getText() == null || cpfField.getText().length() == 0) {
			errorMessage += "Digite um CPF\n";
		} else if (cpfField.getText().length() != 11 || cpfField.getText().matches("[a-zA-Z_]+")) {
			errorMessage += "CPF inválido, digite somente números\n";
		}

		if (rgField.getText() == null || rgField.getText().length() == 0) {
			errorMessage += "RG inválido\n";
		}

		if (estadoCivilBox.getValue() == null) {
			errorMessage += "Selecione um Estado Civil\n";
		}

		if (dataNascimentoField.getValue() == null) {
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

	public void setProprietario(Proprietario proprietario, DadosBancarios db) {
		this.proprietario = proprietario;
		this.db = db;

		if (proprietario.getId() != null) {

			nomeField.setText(proprietario.getNome());
			emailField.setText(proprietario.getEmail());

			telefonePrefField.setText(proprietario.getTelefonePref());
			if (proprietario.getTelefoneAlt().isBlank()) {
				telefoneAltField.setText("");
			} else {
				telefoneAltField.setText(proprietario.getTelefoneAlt());

			}
			if (proprietario.getDataNascimento() != null) {
				dataNascimentoField.setValue(Instant.ofEpochMilli(proprietario.getDataNascimento().getTime())
						.atZone(ZoneId.systemDefault()).toLocalDate());
			} else {
				dataNascimentoField.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
			}

			cpfField.setText(proprietario.getCpfOuCnpj());
			rgField.setText(proprietario.getRg());
			if (proprietario.getEstadoCivil() != null) {
				estadoCivilBox.setValue(proprietario.getEstadoCivil().getDescricao());
			} else {
				estadoCivilBox.setValue(null);
			}
			profissaoField.setText(proprietario.getProfissao());
			obsField.setText(proprietario.getObs());

			if (proprietario.getDadosBancarios() == null) {
				agenciaField.setText("");
				titularField.setText("");
				contaField.setText("");
				pixField.setText("");
				bancoBox.setValue(null);
				tipoContaBox.setValue(null);
			}

			agenciaField.setText(proprietario.getDadosBancarios().getAgencia());
			titularField.setText(proprietario.getDadosBancarios().getTitular());
			contaField.setText(proprietario.getDadosBancarios().getConta());
			if (proprietario.getDadosBancarios().getBanco() != null) {
			}
			bancoBox.setValue(proprietario.getDadosBancarios().getBanco());
			if (proprietario.getDadosBancarios().getTipo() != null) {
				tipoContaBox.setValue(proprietario.getDadosBancarios().getTipo().getDesc());
			}
			pixField.setText(proprietario.getDadosBancarios().getPix());

		} else {
			nomeField.setText("");
			emailField.setText("");
			dataNascimentoField.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
			telefonePrefField.setText("");
			telefoneAltField.setText("");
			cpfField.setText("");
			rgField.setText("");

			estadoCivilBox.setValue(null);

			profissaoField.setText("");

			agenciaField.setText("");
			titularField.setText("");
			contaField.setText("");

			bancoBox.setValue(null);
			tipoContaBox.setValue(null);
		}

	}

	@FXML
	private void handleOk() {
		if (isInputValid()) {

			if (proprietario == null) {
				proprietario = new Proprietario();
			}
			proprietario.setActive(true);
			proprietario.setNome(nomeField.getText().trim());
			proprietario.setEmail(emailField.getText().trim());

			proprietario.setTelefonePref(telefonePrefField.getText().trim());

			if (!telefoneAltField.getText().isBlank()) {
				proprietario.setTelefoneAlt(telefoneAltField.getText().trim());
			} else {
				proprietario.setTelefoneAlt("");
			}

			if (dataNascimentoField.getEditor().getText().isBlank()) {
				proprietario.setDataNascimento(
						Date.from(dataNascimentoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			} else if (!dataNascimentoField.getEditor().getText().isBlank()) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				try {
					Date date = formatter.parse(dataNascimentoField.getEditor().getText().trim());
					proprietario.setDataNascimento(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			proprietario.setCpfOuCnpj(cpfField.getText().trim());
			proprietario.setRg(rgField.getText().trim());
			proprietario.setEstadoCivil(EstadoCivil.valueOfDescricao(estadoCivilBox.getValue()));
			proprietario.setProfissao(profissaoField.getText().trim());
			proprietario.setObs(obsField.getText());

			if (!pixField.getText().isBlank() || !titularField.getText().isBlank()) {
				db.setAgencia(agenciaField.getText().trim());
				db.setConta(contaField.getText().trim());
				db.setPix(pixField.getText().trim());
				db.setTitular(titularField.getText().trim());
				if (bancoBox.getValue() != null) {
					db.setBanco(bancoBox.getValue());
				}
				if (tipoContaBox.getValue() != null) {
					db.setTipo(TipoConta.valueOfDescricao(tipoContaBox.getValue()));
				}
				db.setProprietario(proprietario);
				proprietario.setDadosBancarios(db);

			} else {
				db.setId(null);
				db.setPix(pixField.getText());
				db.setAgencia(agenciaField.getText().trim());
				db.setConta(contaField.getText());
				db.setTitular(titularField.getText().trim());
				if (bancoBox.getValue() != null) {
					db.setBanco(bancoBox.getValue());
				}
				if (tipoContaBox.getValue() != null) {
					db.setTipo(TipoConta.valueOfDescricao(tipoContaBox.getValue()));
				}
				db.setProprietario(proprietario);
				proprietario.setDadosBancarios(db);
				
			}
			ProprietarioRepository propRepo = (ProprietarioRepository) context.getBean("proprietarioRepository");
			propRepo.save(proprietario);
			okClicked = true;
			dialogStage.close();
		}
	}

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

}
