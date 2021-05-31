package com.fgwr.jpcorretora.views;

import java.io.File;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Endereco;
import com.fgwr.jpcorretora.domain.Referencia;
import com.fgwr.jpcorretora.utils.FilesUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EditEnderecoController {

	@FXML
	private TextField logradouroField;
	@FXML
	private TextField numeroField;
	@FXML
	private TextField bairroField;
	@FXML
	private TextField cepField;
	@FXML
	private TextField cidadeField;
	@FXML
	private TextField estadoField;
	@FXML
	private TextField complementoField;

	private Stage dialogStage;
	private Cliente cliente;
	private boolean okClicked = false;
	private Endereco endereco;

	ApplicationContext context = SpringContext.getAppContext();

	@FXML
	private void initialize() {

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

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;

		if (endereco != null) {
			logradouroField.setText(endereco.getLogradouro());
			numeroField.setText(endereco.getNumero());
			bairroField.setText(endereco.getBairro());
			cepField.setText(endereco.getCep());
			cidadeField.setText(endereco.getCidade());
			estadoField.setText(endereco.getEstado());
			complementoField.setText(endereco.getComplemento());
		} else {
			logradouroField.setText("");
			numeroField.setText("");
			bairroField.setText("");
			cepField.setText("");
			cidadeField.setText("");
			estadoField.setText("");
			complementoField.setText("");
		}
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {
		if (isInputValid()) {

			endereco.setLogradouro(logradouroField.getText().trim());
			endereco.setNumero(numeroField.getText().trim());
			endereco.setBairro(bairroField.getText().trim());
			endereco.setCep(cepField.getText().trim());
			endereco.setCidade(cidadeField.getText().trim());
			endereco.setEstado(estadoField.getText().trim());
			endereco.setComplemento(complementoField.getText().trim());
			
			okClicked = true;
			dialogStage.close();
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private boolean isInputValid() {
		String errorMessage = "";

		if (logradouroField.getText() == null || logradouroField.getText().length() == 0) {
			errorMessage += "Logradouro inválido\n";
		}
		if (numeroField.getText() == null || numeroField.getText().length() == 0 || numeroField.getText().matches("[a-zA-Z_]+")) {
			errorMessage += "Número Inválido\n";
		}
		
		if (cepField.getText() == null || cepField.getText().length() != 8 || cepField.getText().matches("[a-zA-Z_]+")) {
			errorMessage += "CEP Inválido\n";
		}
		if (bairroField.getText() == null || bairroField.getText().length() == 0) {
			errorMessage += "Bairro inválido\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initStyle(StageStyle.UNDECORATED);
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