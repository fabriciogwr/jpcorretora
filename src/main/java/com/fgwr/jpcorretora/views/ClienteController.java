package com.fgwr.jpcorretora.views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
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
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Recibo;
import com.fgwr.jpcorretora.domain.Referencia;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.DadosBancariosRepository;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;
import com.fgwr.jpcorretora.repositories.ReciboRepository;
import com.fgwr.jpcorretora.repositories.ReferenciaRepository;
import com.fgwr.jpcorretora.services.ClienteService;
import com.fgwr.jpcorretora.services.DuplicataService;
import com.fgwr.jpcorretora.services.ReciboPdfGen;
import com.fgwr.jpcorretora.services.exceptions.ObjectNotFoundException;
import com.fgwr.jpcorretora.utils.FileUtils;
import com.fgwr.jpcorretora.utils.StringsUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.StageStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ClienteController {

	ReciboPdfGen reciboPdfGen = new ReciboPdfGen();
	@FXML
	private BorderPane rootLayout;

	private ObservableList<Referencia> referenciaData = FXCollections.observableArrayList();

	@FXML
	private TableView<Cliente> clienteTable;
	@FXML
	private TableColumn<Cliente, String> codColumn;
	@FXML
	private TableColumn<Cliente, String> nomeColumn;

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
	private Accordion accordion;
	@FXML
	private TitledPane financeiro;
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

	private DadosBancarios dadosBancarios;

	FrontApp frontApp = new FrontApp();

	ApplicationContext context = SpringContext.getAppContext();

	public List<Cliente> getClienteData() {
		ClienteService cliServ = (ClienteService) context.getBean("clienteService");
		List<Cliente> allcli = cliServ.findAll();
		return allcli;
	}

	public List<Duplicata> getDuplicataData(Contrato contrato) {
		DuplicataService dupServ = (DuplicataService) context.getBean("duplicataService");
		List<Duplicata> alldup = dupServ.findByContrato(contrato);
		return alldup;
	}

	public DadosBancarios getDadosBancariosData(Cliente cliente) {
		DadosBancariosRepository dbRepo = (DadosBancariosRepository) context.getBean("dadosBancariosRepository");

		dadosBancarios = dbRepo.findByCliente(cliente);
		if (dadosBancarios.getTitular().isBlank()) {
			dadosBancarios = null;
		}
		return dadosBancarios;
	}

	public List<Referencia> getReferenciaData(Cliente cliente) {
		ReferenciaRepository refRepo = (ReferenciaRepository) context.getBean("referenciaRepository");
		List<Referencia> allRef = refRepo.findByCliente(cliente);
		return allRef;
	}

	@FXML
	private void initialize() {
		clienteTable.setItems(FXCollections.observableArrayList(getClienteData()));
		nomeColumn.setCellValueFactory(cellData -> cellData.getValue().nome());
		codColumn.setCellValueFactory(cellData -> cellData.getValue().cod());
		showClient(null);
		clienteTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showClient(newValue));

	}

	private void showClient(Cliente cliente) {
		if (cliente != null) {
			nomeLabel.setText(cliente.getNome());
			cpfLabel.setText(StringsUtils.formatarCpfOuCnpj(cliente.getCpfOuCnpj()));
			rgLabel.setText(cliente.getRg());
			emailLabel.setText(cliente.getEmail());

			telefonePrefLabel.setText(StringsUtils.formatarTelefone(cliente.getTelefonePref()));
			if (!cliente.getTelefoneAlt().isBlank()) {
				telefoneAltLabel.setText(StringsUtils.formatarTelefone(cliente.getTelefoneAlt()));

			} else {
				telefoneAltLabel.setText("");
			}
			dataNascimentoLabel.setText(cliente.getDataNascimentoString());
			estadoCivilLabel.setText(cliente.getEstadoCivil().getDescricao());
			profissaoLabel.setText(cliente.getProfissao());

			dadosBancarios = getDadosBancariosData(cliente);

			if (dadosBancarios != null) {
				bancoLabel.setText(
						dadosBancarios.getBanco().getFullCod() + " - " + dadosBancarios.getBanco().getDescricao());
				tipoContaLabel.setText(dadosBancarios.getTipo().getDesc());
				agenciaLabel.setText(dadosBancarios.getAgencia());
				numeroContaLabel.setText(dadosBancarios.getConta());
				titularLabel.setText(dadosBancarios.getTitular());
			}
			referenciaData.clear();
			referenciaData = FXCollections.observableArrayList(getReferenciaData(cliente));
			obsLabel.setText(cliente.getObs());
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

			clienteTable.refresh();
			duplicataTable.getItems().clear();
			duplicataTable.setItems(FXCollections.observableArrayList(getDuplicataData(cliente.getContrato())));

			contratoColumn.setCellValueFactory(cellData -> cellData.getValue().contrato());
			parcelaColumn.setCellValueFactory(cellData -> cellData.getValue().parcela());
			vencimentoColumn.setCellValueFactory(cellData -> cellData.getValue().vencimento());
			valorColumn.setCellValueFactory(cellData -> cellData.getValue().valor());
			estadoColumn.setCellValueFactory(cellData -> cellData.getValue().estado());
			dataPgtoColumn.setCellValueFactory(cellData -> cellData.getValue().dataPgto());
			duplicataTable.getSortOrder().add(parcelaColumn);
			
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
	private void handleEditCliente() {
		Cliente selectedCliente = clienteTable.getSelectionModel().getSelectedItem();
		if (selectedCliente != null) {
			boolean okClicked = frontApp.showEditCliente(selectedCliente);
			if (okClicked) {
				showClient(selectedCliente);
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Nenhuma seleção");
			alert.setHeaderText("Nenhuma Pessoa Selecionada");
			alert.setContentText("Por favor, selecione uma pessoa na tabela.");
			alert.showAndWait();
		}
	}

	@FXML
	private void handleRemoveCliente() {

		ClienteRepository cliRepo = (ClienteRepository) context.getBean("clienteRepository");
		Cliente selectedCliente = clienteTable.getSelectionModel().getSelectedItem();

		if (selectedCliente != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Exclusão de Cliente");
			alert.setHeaderText("Confirmar Exclusão do Cliente Selecionado?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				clienteTable.getItems().remove(selectedCliente);
				cliRepo.delete(selectedCliente);

			}
			clienteTable.refresh();
		}
	}

	@FXML
	private void handleEditObs() {
		Cliente selectedCliente = clienteTable.getSelectionModel().getSelectedItem();
		if (selectedCliente != null) {
			boolean okClicked = frontApp.showEditObs(selectedCliente);
			if (okClicked) {
				showClient(selectedCliente);
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Nenhuma seleção");
			alert.setHeaderText("Nenhuma Pessoa Selecionada");
			alert.setContentText("Por favor, selecione uma pessoa na tabela.");
			alert.showAndWait();
		}
	}

	@FXML
	private void handleEditRef1() throws IOException {
		Cliente selectedCliente = clienteTable.getSelectionModel().getSelectedItem();
		Referencia selectedReferencia;
		if (referenciaData.size() == 0) {
			selectedReferencia = new Referencia();
		} else {
			selectedReferencia = referenciaData.get(0);
		}

		if (selectedCliente != null && selectedReferencia != null) {
			boolean okClicked = frontApp.showEditRef(selectedCliente, selectedReferencia);

			if (okClicked) {

				showClient(selectedCliente);
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Nenhuma seleção");
			alert.setHeaderText("Nenhuma Pessoa Selecionada");
			alert.setContentText("Por favor, selecione uma pessoa na tabela.");
			alert.showAndWait();
		}
	}

	@FXML
	private void handleEditRef2() throws IOException {
		Cliente selectedCliente = clienteTable.getSelectionModel().getSelectedItem();
		Referencia selectedReferencia;
		if (referenciaData.size() == 2) {
			selectedReferencia = referenciaData.get(1);
		} else {
			referenciaData.add(new Referencia());
			selectedReferencia = referenciaData.get(1);
		}

		if (selectedCliente != null && selectedReferencia != null) {
			boolean okClicked = frontApp.showEditRef(selectedCliente, selectedReferencia);

			if (okClicked) {

				showClient(selectedCliente);
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Nenhuma seleção");
			alert.setHeaderText("Nenhuma Pessoa Selecionada");
			alert.setContentText("Por favor, selecione uma pessoa na tabela.");
			alert.showAndWait();
		}
		referenciaData.clear();
		referenciaData = FXCollections.observableArrayList(getReferenciaData(selectedCliente));
	}

	@FXML
	private void handleEditRef3() throws IOException {
		Cliente selectedCliente = clienteTable.getSelectionModel().getSelectedItem();
		Referencia selectedReferencia = null;

		if (referenciaData.size() == 1) {
			referenciaData.add(new Referencia());
			selectedReferencia = referenciaData.get(1);
		} else if (referenciaData.size() == 2) {
			referenciaData.add(new Referencia());
			selectedReferencia = referenciaData.get(2);
		} else if (referenciaData.size() == 3) {
			selectedReferencia = referenciaData.get(2);
		}

		if (selectedCliente != null && selectedReferencia != null) {
			boolean okClicked = frontApp.showEditRef(selectedCliente, selectedReferencia);

			if (okClicked) {
				showClient(selectedCliente);

			}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Nenhuma seleção");
			alert.setHeaderText("Nenhuma Pessoa Selecionada");
			alert.setContentText("Por favor, selecione uma pessoa na tabela.");
			alert.showAndWait();
		}
	}

	public Recibo findRecibo(Integer id) {
		ReciboRepository recRepo = (ReciboRepository) context.getBean("reciboRepository");
		Optional<Recibo> obj = recRepo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Não encontrado"));
	}

	@Transactional
	@FXML
	private void handlePagamento() throws IOException {
		Duplicata selectedDuplicata = duplicataTable.getSelectionModel().getSelectedItem();
		Cliente selectedCliente = clienteTable.getSelectionModel().getSelectedItem();
		ReciboRepository recRepo = (ReciboRepository) context.getBean("reciboRepository");

		if (selectedDuplicata != null && selectedDuplicata.getEstado() == EstadoPagamento.PENDENTE) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UNDECORATED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FileUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Confirmar pagamento");
			alert.setHeaderText("Confirmar pagamento da mensalidade selecionada?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				selectedDuplicata.setEstado(EstadoPagamento.QUITADO);
				boolean okClicked = frontApp.showConfiguraPagamento(selectedDuplicata);
				if (okClicked) {
					showClient(selectedCliente);
					Recibo rec = recRepo.findByDuplicata(selectedDuplicata);
					selectedDuplicata.setRecibo(rec);

					Alert alert2 = new Alert(AlertType.CONFIRMATION);
					alert2.initStyle(StageStyle.UNDECORATED);
					DialogPane dialogPane2 = alert2.getDialogPane();
					dialogPane2.getStylesheets().add(FileUtils.fileToString(new File("css/alerts.css")));
					alert2.setTitle("Recibo");
					alert2.setHeaderText("Visualizar o recibo do pagamento?");
					Optional<ButtonType> result2 = alert2.showAndWait();
					if (result2.get() == ButtonType.OK) {
						visualizaRecibo(selectedDuplicata.getRecibo());

					} else {
						alert.close();
					}

				}

			}

		} else {
			Alert alert3 = new Alert(AlertType.WARNING);
			alert3.initStyle(StageStyle.UNDECORATED);
			DialogPane dialogPane3 = alert3.getDialogPane();
			dialogPane3.getStylesheets().add(FileUtils.fileToString(new File("css/alerts.css")));
			alert3.setTitle("Mensalidade Já Paga");
			alert3.setHeaderText("A mensalidade selecionada já está paga.");
		}
	}

	@FXML
	public void handleCancelaPagamento() throws IOException {
		DuplicataRepository dupRepo = (DuplicataRepository) context.getBean("duplicataRepository");
		Duplicata selectedDuplicata = duplicataTable.getSelectionModel().getSelectedItem();
		ReciboRepository recRepo = (ReciboRepository) context.getBean("reciboRepository");
		if (selectedDuplicata != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UNDECORATED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FileUtils.fileToString(new File("css/alerts.css")));
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

	@FXML
	public void visualizaRecibo() throws IOException {
		Duplicata duplicata = duplicataTable.getSelectionModel().getSelectedItem();
		Recibo recibo = duplicata.getRecibo();
		File file = new File(FileUtils.pathRecibos(recibo));
		Desktop desktop = Desktop.getDesktop();
		desktop.open(file);

	}

	public void visualizaRecibo(Recibo rec) throws IOException {
		File file = new File(FileUtils.pathRecibos(rec));
		Desktop desktop = Desktop.getDesktop();
		desktop.open(file);
	}

	public void setMainApp(FrontApp frontApp) {
		this.frontApp = frontApp;
	}
}
