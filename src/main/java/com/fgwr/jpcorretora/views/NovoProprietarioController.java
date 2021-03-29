package com.fgwr.jpcorretora.views;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Proprietario;
import com.fgwr.jpcorretora.enums.Banco;
import com.fgwr.jpcorretora.enums.EstadoCivil;
import com.fgwr.jpcorretora.enums.TipoConta;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NovoProprietarioController {

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
	private TextField obsField;
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
	private ComboBox<String> bancoBox;
	@FXML
	private ComboBox<String> tipoContaBox;

	private List<String> estadoCivilAux = new ArrayList<>();

	private List<String> bancoAux = new ArrayList<>();

	private List<String> tipoContaAux = new ArrayList<>();

	private Stage dialogStage;
	private Proprietario proprietario;
	private DadosBancarios db;
	private boolean okClicked = false;

	private EstadoCivil[] estadoCivil = EstadoCivil.values();
	private Banco[] banco = Banco.values();
	private TipoConta[] tipoConta = TipoConta.values();

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
		bancoBox.setItems(FXCollections.observableArrayList(bancoAux));
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
		if (emailField.getText() == null || emailField.getText().length() == 0) {
			errorMessage += "Email inválido\n";
		}
		if (cpfField.getText() == null || cpfField.getText().length() == 0) {
			errorMessage += "CPF inválido\n";
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
			alert.initStyle(StageStyle.UNDECORATED);
			DialogPane dialogPane = alert.getDialogPane();			
			dialogPane.getStylesheets().add(getClass().getResource("../css/alerts.css").toExternalForm());
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

		if (proprietario != null) {
			ObservableList<String> telefoneData = FXCollections.observableArrayList();
			nomeField.setText(proprietario.getNome());
			emailField.setText(proprietario.getEmail());

			Set<String> telefones = proprietario.getTelefones();
			for (String string : telefones) {
				telefoneData.add(string);
			}
			if (telefoneData.size() == 1) {
				telefonePrefField.setText(telefoneData.get(0));
			}
			if (telefoneData.size() == 2) {
				telefoneAltField.setText(telefoneData.get(0));
				telefonePrefField.setText(telefoneData.get(1));
			} else {
				telefoneAltField.setText("");
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

			if (proprietario.getDadosBancarios() != null) {
				agenciaField.setText(proprietario.getDadosBancarios().getAgencia());
				titularField.setText(proprietario.getDadosBancarios().getTitular());
				contaField.setText(proprietario.getDadosBancarios().getConta());

				bancoBox.setValue(proprietario.getDadosBancarios().getBanco().getFullCod() + " - "
						+ proprietario.getDadosBancarios().getBanco().getDescricao());
				tipoContaBox.setValue(proprietario.getDadosBancarios().getTipo().getDesc());
			} else {
				agenciaField.setText("");
				titularField.setText("");
				contaField.setText("");
				bancoBox.setValue(null);
				tipoContaBox.setValue(null);
			}
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

			proprietario.setNome(nomeField.getText());
			proprietario.setEmail(emailField.getText());

			if (telefoneAltField.getText() != "") {
				proprietario.getTelefones()
						.addAll(Arrays.asList(telefonePrefField.getText(), telefoneAltField.getText()));
			} else {
				proprietario.getTelefones().addAll(Arrays.asList(telefonePrefField.getText()));
			}
			proprietario.setDataNascimento(
					Date.from(dataNascimentoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			proprietario.setCpfOuCnpj(cpfField.getText());
			proprietario.setRg(rgField.getText());
			proprietario.setEstadoCivil(EstadoCivil.valueOfDescricao(estadoCivilBox.getValue()));
			proprietario.setProfissao(profissaoField.getText());
			proprietario.setObs(obsField.getText());
			
			if(titularField.getText() != "") {
			db.setAgencia(agenciaField.getText());
			db.setConta(contaField.getText());
			db.setTitular(titularField.getText());
			 if(bancoBox.getValue() != null) {
			db.setBanco(Banco.valueOfDescricao(bancoBox.getValue().substring(6)));
			 }
			 if (tipoContaBox.getValue() != null) {
			db.setTipo(TipoConta.valueOfDescricao(tipoContaBox.getValue()));
			 }
			 db.setProprietario(proprietario);
			 proprietario.setDadosBancarios(db);
			

			}
			okClicked = true;
			dialogStage.close();
		}
	}

}
