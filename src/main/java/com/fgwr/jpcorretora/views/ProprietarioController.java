package com.fgwr.jpcorretora.views;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Proprietario;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.repositories.ProprietarioRepository;
import com.fgwr.jpcorretora.utils.FilesUtils;
import com.fgwr.jpcorretora.utils.StringsUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.StageStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ProprietarioController {

	@FXML
	private BorderPane rootLayout;

	@FXML
	private TableView<Proprietario> proprietarioTable;
	@FXML
	private TableColumn<Proprietario, String> codColumn;
	@FXML
	private TableColumn<Proprietario, String> nomeColumn;

	@FXML
	private TableView<Imovel> imovelTable;
	@FXML
	private TableColumn<Imovel, String> codIColumn;
	@FXML
	private TableColumn<Imovel, String> enderecoColumn;
	@FXML
	private TableColumn<Imovel, String> dataAngariacaoColumn;
	@FXML
	private TableColumn<Imovel, String> locacaoColumn;

	@FXML
	private Label nomeLabel;
	@FXML
	private Label cpfLabel;
	@FXML
	private Label dataNascimentoLabel;
	@FXML
	private Label rgLabel;
	@FXML
	private Label emailLabel;
	@FXML
	private Label telefonePrefLabel;
	@FXML
	private Label telefoneAltLabel;
	@FXML
	private Label estadoCivilLabel;
	@FXML
	private Label profissaoLabel;

	@FXML
	private Label bancoLabel;
	@FXML
	private Label agenciaLabel;
	@FXML
	private Label tipoContaLabel;
	@FXML
	private Label numeroContaLabel;
	@FXML
	private Label titularLabel;
	@FXML
	private Label pixLabel;

	@FXML
	private Label logradouroLabel;
	@FXML
	private Label numeroLabel;
	@FXML
	private Label bairroLabel;
	@FXML
	private Label cepLabel;
	@FXML
	private Label cidadeLabel;
	@FXML
	private Label complementoLabel;
	@FXML
	private Label dataLocacaoLabel;
	@FXML
	private Label obsLabel;

	FrontApp frontApp = new FrontApp();

	ApplicationContext context = SpringContext.getAppContext();

	public List<Proprietario> getProprietarioData() {
		ProprietarioRepository propRepo = (ProprietarioRepository) context.getBean("proprietarioRepository");
		List<Proprietario> allProp = propRepo.findByActive(Boolean.TRUE);
		return allProp;
	}

	public List<Imovel> getImovelData(Proprietario p) {
		ImovelRepository imServ = (ImovelRepository) context.getBean("imovelRepository");
		List<Imovel> imoveis = imServ.findByProprietario(p);
		return imoveis;
	}

	@FXML
	private void initialize() {

		proprietarioTable.setItems(FXCollections.observableArrayList(getProprietarioData()));
		nomeColumn.setCellValueFactory(cellData -> cellData.getValue().nome());
		codColumn.setCellValueFactory(cellData -> cellData.getValue().cod());
		showProprietario(null);
		proprietarioTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showProprietario(newValue));

	}

	private void showProprietario(Proprietario proprietario) {
		if (proprietario != null) {
			nomeLabel.setText(proprietario.getNome());
			cpfLabel.setText(StringsUtils.formatarCpfOuCnpj(proprietario.getCpfOuCnpj()));
			rgLabel.setText(proprietario.getRg());
			emailLabel.setText(proprietario.getEmail());

			telefonePrefLabel.setText(StringsUtils.formatarTelefone(proprietario.getTelefonePref()));
			if (!proprietario.getTelefoneAlt().isBlank()) {
				telefoneAltLabel.setText(StringsUtils.formatarTelefone(proprietario.getTelefoneAlt()));

			} else {
				telefoneAltLabel.setText("");
			}
			dataNascimentoLabel.setText(proprietario.getDataNascimentoString());
			estadoCivilLabel.setText(proprietario.getEstadoCivil().getDescricao());
			profissaoLabel.setText(proprietario.getProfissao());

			if (proprietario.getDadosBancarios().getBanco() != null) {
				bancoLabel.setText(proprietario.getDadosBancarios().getBanco().getFullCod() + " - "
						+ proprietario.getDadosBancarios().getBanco().getDescricao());
			}
			if (proprietario.getDadosBancarios().getTipo() != null) {
				tipoContaLabel.setText(proprietario.getDadosBancarios().getTipo().getDesc());
			}
			agenciaLabel.setText(proprietario.getDadosBancarios().getAgencia());
			numeroContaLabel.setText(proprietario.getDadosBancarios().getConta());
			titularLabel.setText(proprietario.getDadosBancarios().getTitular());
			pixLabel.setText(proprietario.getDadosBancarios().getPix());

			obsLabel.setText(proprietario.getObs());

			proprietarioTable.refresh();

			imovelTable.setItems(FXCollections.observableArrayList(getImovelData(proprietario)));

			codIColumn.setCellValueFactory(cellData -> cellData.getValue().cod());
			enderecoColumn.setCellValueFactory(cellData -> cellData.getValue().endereco());
			dataAngariacaoColumn.setCellValueFactory(cellData -> cellData.getValue().dataAngariacao());
			locacaoColumn.setCellValueFactory(cellData -> cellData.getValue().locacao());
			imovelTable.getSortOrder().add(codIColumn);

		} else {
			nomeLabel.setText("");
			cpfLabel.setText("");
			rgLabel.setText("");
			dataNascimentoLabel.setText("");
			telefonePrefLabel.setText("");
			telefoneAltLabel.setText("");
			emailLabel.setText("");
			estadoCivilLabel.setText("");
			profissaoLabel.setText("");
			bancoLabel.setText("");
			tipoContaLabel.setText("");
			agenciaLabel.setText("");
			numeroContaLabel.setText("");
			titularLabel.setText("");
			pixLabel.setText("");
			obsLabel.setText("");
		}

	}

	@FXML
	private void handleEditProprietario() {
		Proprietario selectedProprietario = proprietarioTable.getSelectionModel().getSelectedItem();
		DadosBancarios db = selectedProprietario.getDadosBancarios();
		if (selectedProprietario != null) {
			boolean okClicked = frontApp.showNovoProprietario(selectedProprietario, db);
			if (okClicked) {
				showProprietario(selectedProprietario);

			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.initStyle(StageStyle.UNIFIED);
				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
				alert.setTitle("Nenhuma seleção");
				alert.setHeaderText("Nenhum Proprietário Selecionado");
				alert.setContentText("Por favor, selecione um proprietário na tabela.");
				alert.showAndWait();
			}
		}
	}

	@FXML
	private void handleRemoveProprietario() {

		ProprietarioRepository propRepo = (ProprietarioRepository) context.getBean("proprietarioRepository");
		Proprietario selectedProprietario = proprietarioTable.getSelectionModel().getSelectedItem();
		ImovelRepository imvRepo = (ImovelRepository) context.getBean("imovelRepository");
		List<Imovel> allImv = imvRepo.findByProprietario(selectedProprietario);

		if (selectedProprietario != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.initStyle(StageStyle.UNIFIED);
			alert.setTitle("Exclusão de Proprietario");
			alert.setHeaderText("Confirmar Exclusão do Proprietario Selecionado?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {

				Integer lock = 0;

				for (Imovel imovel : allImv) {
					if (imovel.getContrato() != null) {
						lock++;
					}

				}
				if (lock == 0) {

					for (Imovel imovel : allImv) {
						imovel.setActive(false);
					}
					imvRepo.saveAll(allImv);
					selectedProprietario.setActive(false);

					propRepo.save(selectedProprietario);
					proprietarioTable.getItems().remove(selectedProprietario);
					proprietarioTable.refresh();
				}
				{
					Alert alert3 = new Alert(AlertType.ERROR);
					alert3.initStyle(StageStyle.UNIFIED);
					DialogPane dialogPane3 = alert3.getDialogPane();
					dialogPane3.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
					alert3.setTitle("Falha ao Excluir Proprietário");
					alert3.setHeaderText(
							"O imóvel do proprietário ainda está em um contrato ativo. Encerre o contrato para continuar.");
					alert3.showAndWait();

				}

			}

		}
	}

	@FXML
	private void handleEditObs() {
		Proprietario selectedProprietario = proprietarioTable.getSelectionModel().getSelectedItem();
		if (selectedProprietario == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Nenhuma seleção");
			alert.setHeaderText("Nenhuma Pessoa Selecionada");
			alert.setContentText("Por favor, selecione uma pessoa na tabela.");
			alert.showAndWait();

		} else {

			boolean okClicked = frontApp.showEditObs(selectedProprietario);
			if (okClicked) {
				showProprietario(selectedProprietario);
			}

		}
	}

	public void setMainApp(FrontApp frontApp) {
		this.frontApp = frontApp;
	}
}
