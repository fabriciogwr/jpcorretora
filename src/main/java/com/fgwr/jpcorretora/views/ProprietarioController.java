package com.fgwr.jpcorretora.views;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Proprietario;
import com.fgwr.jpcorretora.domain.Referencia;
import com.fgwr.jpcorretora.repositories.DadosBancariosRepository;
import com.fgwr.jpcorretora.repositories.ProprietarioRepository;
import com.fgwr.jpcorretora.services.DuplicataService;
import com.fgwr.jpcorretora.utils.StringsUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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

	private ObservableList<Proprietario> proprietarioData = FXCollections.observableArrayList();
	private ObservableList<String> telefoneData = FXCollections.observableArrayList();
	private ObservableList<Duplicata> duplicataData = FXCollections.observableArrayList();
	private ObservableList<Duplicata> duplicataAux = FXCollections.observableArrayList();
	private ObservableList<Referencia> referenciaData = FXCollections.observableArrayList();

	@FXML
	private TableView<Proprietario> proprietarioTable;
	@FXML
	private TableColumn<Proprietario, String> codColumn;
	@FXML
	private TableColumn<Proprietario, String> nomeColumn;

	@FXML
	private TableView<Duplicata> duplicataTable;
	@FXML
	private TableColumn<Duplicata, String> contratoColumn;
	@FXML
	private TableColumn<Duplicata, String> parcelaColumn;
	@FXML
	private TableColumn<Duplicata, String> vencimentoColumn;
	@FXML
	private TableColumn<Duplicata, String> valorColumn;
	@FXML
	private TableColumn<Duplicata, String> estadoColumn;
	@FXML
	private TableColumn<Duplicata, String> dataPgtoColumn;

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
	private Label ref1Label;
	@FXML
	private Label ref2Label;
	@FXML
	private Label ref3Label;
	@FXML
	private Label ref1FoneLabel;
	@FXML
	private Label ref2FoneLabel;
	@FXML
	private Label ref3FoneLabel;
	@FXML
	private Label obsLabel;
	@FXML
	private Button ref1Btn;
	@FXML
	private Button ref2Btn;
	@FXML
	private Button ref3Btn;

	
	private Proprietario proprietarioAux;
	private DadosBancarios dadosBancarios;
	private Integer contratoAtual;

	FrontApp frontApp = new FrontApp();

	ApplicationContext context = SpringContext.getAppContext();

	public List<Proprietario> getProprietarioData() {
		ProprietarioRepository propRepo = (ProprietarioRepository) context.getBean("proprietarioRepository");
		List<Proprietario> allProp = propRepo.findAll();
		return allProp;
	}

	public List<Duplicata> getDuplicataData() {
		DuplicataService dupServ = (DuplicataService) context.getBean("duplicataService");
		List<Duplicata> alldup = dupServ.findAll();
		
		return alldup;
	}

	

	public DadosBancarios getDadosBancariosData() {
		DadosBancariosRepository dbRepo = (DadosBancariosRepository) context.getBean("dadosBancariosRepository");
		dadosBancarios = dbRepo.findByProprietario(proprietarioAux);
		return dadosBancarios;
	}

	@FXML
	private void initialize() {
		duplicataData = FXCollections.observableArrayList(getDuplicataData());
		proprietarioTable.setItems(FXCollections.observableArrayList(getProprietarioData()));
		nomeColumn.setCellValueFactory(cellData -> cellData.getValue().nome());
		codColumn.setCellValueFactory(cellData -> cellData.getValue().cod());
		showProprietario(null);
		proprietarioTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showProprietario(newValue));
		duplicataTable.setItems(duplicataAux);
	}

	private void showProprietario(Proprietario proprietario) {
		if (proprietario != null) {
			nomeLabel.setText(proprietario.getNome());
			cpfLabel.setText(StringsUtils.formatarCpf(proprietario.getCpfOuCnpj()));
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
			
			proprietarioAux = proprietario;
			
			
			dadosBancarios = getDadosBancariosData();
			
			if (dadosBancarios.getBanco() !=null) {
			bancoLabel
					.setText(dadosBancarios.getBanco().getFullCod() + " - " + dadosBancarios.getBanco().getDescricao());
			tipoContaLabel.setText(dadosBancarios.getTipo().getDesc());
			agenciaLabel.setText(dadosBancarios.getAgencia());
			numeroContaLabel.setText(dadosBancarios.getConta());
			titularLabel.setText(dadosBancarios.getTitular());
			}
			referenciaData.clear();
			
			obsLabel.setText(proprietario.getObs());
			ref1Btn.setText("Editar");
			if (referenciaData.size() == 0) {
				ref1Btn.setText("Cadastrar");
				ref2Btn.setText("Cadastrar");
				ref3Btn.setText("Cadastrar");
			}
			if (referenciaData.size() == 1) {
				ref1Label.setText(referenciaData.get(0).getNome());
				ref1FoneLabel.setText(StringsUtils.formatarTelefone(referenciaData.get(0).getTelefone()));
			}
			if (referenciaData.size() >= 2) {
				ref2Btn.setText("Editar");
				ref2Label.setText(referenciaData.get(1).getNome());
				ref2FoneLabel.setText(StringsUtils.formatarTelefone(referenciaData.get(1).getTelefone()));
			} else {
				ref2Btn.setText("Cadastrar");
				ref2Label.setText("");
				ref2FoneLabel.setText("");
			}
			if (referenciaData.size() == 3) {
				ref3Btn.setText("Editar");
				ref3Label.setText(referenciaData.get(2).getNome());
				ref3FoneLabel.setText(StringsUtils.formatarTelefone(referenciaData.get(2).getTelefone()));
			} else {
				ref3Btn.setText("Cadastrar");
				ref3Label.setText("");
				ref3FoneLabel.setText("");
			}

			proprietarioTable.refresh();
			for (Duplicata duplicata : duplicataData) {
				if (duplicata.getContrato().getId() == contratoAtual) {
					duplicataAux.add(duplicata);
				}

				if (duplicata != null) {
					contratoColumn.setCellValueFactory(cellData -> cellData.getValue().contrato());
					parcelaColumn.setCellValueFactory(cellData -> cellData.getValue().parcela());
					vencimentoColumn.setCellValueFactory(cellData -> cellData.getValue().vencimento());
					valorColumn.setCellValueFactory(cellData -> cellData.getValue().valor());
					estadoColumn.setCellValueFactory(cellData -> cellData.getValue().estado());
					dataPgtoColumn.setCellValueFactory(cellData -> cellData.getValue().dataPgto());
					duplicataTable.getSortOrder().add(parcelaColumn);

				}
			}

			/*
			 * if (imovel != null) {
			 * logradouroLabel.setText(imovel.getEndereco().getLogradouro());
			 * numeroLabel.setText(imovel.getEndereco().getNumero());
			 * bairroLabel.setText(imovel.getEndereco().getBairro());
			 * cepLabel.setText(imovel.getEndereco().getCep());
			 * cidadeLabel.setText(imovel.getEndereco().getCidade());
			 * complementoLabel.setText(imovel.getEndereco().getComplemento());
			 * dataLocacaoLabel.setText(imovel.getDataLaudo().toString()); } else {
			 * logradouroLabel.setText(""); numeroLabel.setText("");
			 * bairroLabel.setText(""); cepLabel.setText(""); cidadeLabel.setText("");
			 * complementoLabel.setText(""); dataLocacaoLabel.setText(""); }
			 */
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
			ref1Label.setText("");
			ref2Label.setText("");
			ref3Label.setText("");
			ref1FoneLabel.setText("");
			ref2FoneLabel.setText("");
			ref3FoneLabel.setText("");
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
			alert.initStyle(StageStyle.UNDECORATED);
			DialogPane dialogPane = alert.getDialogPane();			
			dialogPane.getStylesheets().add(getClass().getResource("../css/alerts.css").toExternalForm());
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

		if (selectedProprietario != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UNDECORATED);
			DialogPane dialogPane = alert.getDialogPane();			
			dialogPane.getStylesheets().add(getClass().getResource("../css/alerts.css").toExternalForm());
			alert.setTitle("Exclusão de Proprietario");
			alert.setHeaderText("Confirmar Exclusão do Proprietario Selecionado?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				
				propRepo.delete(selectedProprietario);

			}
			proprietarioTable.refresh();
		}
	}

	@FXML
	private void handleEditObs() {
		Proprietario selectedProprietario = proprietarioTable.getSelectionModel().getSelectedItem();
		if (selectedProprietario != null) {
			boolean okClicked = frontApp.showEditObs(selectedProprietario);
			if (okClicked) {
				showProprietario(selectedProprietario);
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initStyle(StageStyle.UNDECORATED);
			DialogPane dialogPane = alert.getDialogPane();			
			dialogPane.getStylesheets().add(getClass().getResource("../css/alerts.css").toExternalForm());
			alert.setTitle("Nenhuma seleção");
			alert.setHeaderText("Nenhuma Pessoa Selecionada");
			alert.setContentText("Por favor, selecione uma pessoa na tabela.");
			alert.showAndWait();
		}
	}

/*	@Transactional
	@FXML
	private void handlePagamento() {
		DuplicataRepository dupRepo = (DuplicataRepository) context.getBean("duplicataRepository");
		Duplicata selectedDuplicata = duplicataTable.getSelectionModel().getSelectedItem();
		ReciboRepository recRepo = (ReciboRepository) context.getBean("reciboRepository");
		Calendar cal = Calendar.getInstance();
		if (selectedDuplicata != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmar pagamento");
			alert.setHeaderText("Confirmar pagamento da mensalidade selecionada?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				selectedDuplicata.setEstado(EstadoPagamento.QUITADO);
				Recibo rec = new Recibo(null, proprietarioTable.getSelectionModel().getSelectedItem(),
						selectedDuplicata.getValor(), selectedDuplicata.getParcela(),
						selectedDuplicata.getDataVencimento(), cal.getTime());
				selectedDuplicata.setDataPagamento(cal.getTime());
				selectedDuplicata.setRecibo(rec);
				dupRepo.save(selectedDuplicata);
				recRepo.save(rec);

				duplicataTable.refresh();

				Alert alert2 = new Alert(AlertType.CONFIRMATION);
				alert2.setTitle("Recibo");
				alert2.setHeaderText("Visualizar o recibo do pagamento?");
				Optional<ButtonType> result2 = alert2.showAndWait();
				if (result2.get() == ButtonType.OK) {
					System.out.println(rec.getValor());

				} else {
					alert.close();
				}
			}

		}
	}

	@FXML
	public void handleCancelaPagamento() {
		DuplicataRepository dupRepo = (DuplicataRepository) context.getBean("duplicataRepository");
		Duplicata selectedDuplicata = duplicataTable.getSelectionModel().getSelectedItem();
		ReciboRepository recRepo = (ReciboRepository) context.getBean("reciboRepository");
		if (selectedDuplicata != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Cancelar pagamento");
			alert.setHeaderText("Cancelar pagamento da mensalidade selecionada?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				selectedDuplicata.setEstado(EstadoPagamento.PENDENTE);
				Recibo rec = selectedDuplicata.getRecibo();
				selectedDuplicata.setDataPagamento(null);
				selectedDuplicata.setRecibo(null);
				dupRepo.save(selectedDuplicata);
				recRepo.delete(rec);
			}
			duplicataTable.refresh();
		}
	}
*/
	public void setMainApp(FrontApp frontApp) {
		this.frontApp = frontApp;
	}
}
