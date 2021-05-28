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
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Receita;
import com.fgwr.jpcorretora.domain.Recibo;
import com.fgwr.jpcorretora.domain.Referencia;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.DadosBancariosRepository;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;
import com.fgwr.jpcorretora.repositories.ReceitaRepository;
import com.fgwr.jpcorretora.repositories.ReciboRepository;
import com.fgwr.jpcorretora.repositories.ReferenciaRepository;
import com.fgwr.jpcorretora.services.ReciboPdfGen;
import com.fgwr.jpcorretora.services.exceptions.ObjectNotFoundException;
import com.fgwr.jpcorretora.utils.FilesUtils;
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
	private TableColumn<Duplicata, String> meioPgtoColumn;

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
	private Label corretorLabel;

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

	FrontApp frontApp = new FrontApp();

	ApplicationContext context = SpringContext.getAppContext();

	public List<Cliente> getClienteData() {
		ClienteRepository cliServ = (ClienteRepository) context.getBean("clienteRepository");
		List<Cliente> allcli = cliServ.findByActive(Boolean.TRUE);
		return allcli;
	}

	public List<Duplicata> getDuplicataData(Cliente cliente) {
		DuplicataRepository dupRepo = (DuplicataRepository) context.getBean("duplicataRepository");
		List<Duplicata> alldup = dupRepo.findByClienteAndEstadoNot(cliente, EstadoPagamento.CANCELADO.getCod());
		return alldup;
	}

	public DadosBancarios getDadosBancariosData(Cliente cliente) {
		DadosBancariosRepository dbRepo = (DadosBancariosRepository) context.getBean("dadosBancariosRepository");

		DadosBancarios dadosBancarios = dbRepo.findByCliente(cliente);
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
			if (cliente.getDataNascimento() != null ) {
			dataNascimentoLabel.setText(cliente.getDataNascimentoString());
			}
			estadoCivilLabel.setText(cliente.getEstadoCivil().getDescricao());
			profissaoLabel.setText(cliente.getProfissao());
			if (cliente.getCorretor() != null) {
			corretorLabel.setText(cliente.getCorretor().getNome());
			} else {
				corretorLabel.setText("");
			}

			
			if (cliente.getDadosBancarios().getBanco() != null ) {
				bancoLabel.setText(
						cliente.getDadosBancarios().getBanco().getFullCod() + " - " + cliente.getDadosBancarios().getBanco().getDescricao());
			}
			if (cliente.getDadosBancarios().getTipo() != null ) {
				tipoContaLabel.setText(cliente.getDadosBancarios().getTipo().getDesc());
			}
				agenciaLabel.setText(cliente.getDadosBancarios().getAgencia());
				numeroContaLabel.setText(cliente.getDadosBancarios().getConta());
				titularLabel.setText(cliente.getDadosBancarios().getTitular());
				pixLabel.setText(cliente.getDadosBancarios().getPix());
			
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
			duplicataTable.setItems(FXCollections.observableArrayList(getDuplicataData(cliente)));

			contratoColumn.setCellValueFactory(cellData -> cellData.getValue().contrato());
			parcelaColumn.setCellValueFactory(cellData -> cellData.getValue().parcela());
			vencimentoColumn.setCellValueFactory(cellData -> cellData.getValue().vencimento());
			valorColumn.setCellValueFactory(cellData -> cellData.getValue().valor());
			estadoColumn.setCellValueFactory(cellData -> cellData.getValue().estado());
			dataPgtoColumn.setCellValueFactory(cellData -> cellData.getValue().dataPgto());
			meioPgtoColumn.setCellValueFactory(cellData -> cellData.getValue().meioPgto());
			duplicataTable.getSortOrder().add(contratoColumn);
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
			corretorLabel.setText("");
			bancoLabel.setText("");
			tipoContaLabel.setText("");
			agenciaLabel.setText("");
			numeroContaLabel.setText("");
			titularLabel.setText("");
			pixLabel.setText("");
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
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Exclusão de Cliente");
			alert.setHeaderText("Confirmar Exclusão do Cliente Selecionado?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {

				if (selectedCliente.getContrato() == null) {
					
					selectedCliente.setActive(false);
					cliRepo.save(selectedCliente);
					clienteTable.getItems().remove(selectedCliente);
					clienteTable.refresh();
					
				}

				else {
					Alert alert3 = new Alert(AlertType.ERROR);		
					DialogPane dialogPane3 = alert3.getDialogPane();
					dialogPane3.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
					alert3.initStyle(StageStyle.UNIFIED);
					alert3.setTitle("Falha ao Excluir Cliente");
					alert3.setHeaderText(
							"O cliente ainda está em um contrato ativo. Encerre o contrato para continuar.");
					alert3.showAndWait();

				}
			}
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
			alert.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Confirmar pagamento");
			alert.setHeaderText("Confirmar pagamento da mensalidade selecionada?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				
				boolean okClicked = frontApp.showConfiguraPagamento(selectedDuplicata);
				if (okClicked) {
					selectedDuplicata.setEstado(EstadoPagamento.QUITADO);
					
					Recibo rec = recRepo.findByDuplicata(selectedDuplicata);
					selectedDuplicata.setRecibo(rec);
					showClient(selectedCliente);
					Alert alert2 = new Alert(AlertType.CONFIRMATION);
					alert2.initStyle(StageStyle.UNIFIED);
					DialogPane dialogPane2 = alert2.getDialogPane();
					dialogPane2.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
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
			alert3.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane3 = alert3.getDialogPane();
			dialogPane3.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert3.setTitle("Mensalidade Já Paga");
			alert3.setHeaderText("A mensalidade selecionada já está paga.");
			alert3.showAndWait();
		}
	}

	@FXML
	public void handleCancelaPagamento() throws IOException {
		DuplicataRepository dupRepo = (DuplicataRepository) context.getBean("duplicataRepository");
		Duplicata selectedDuplicata = duplicataTable.getSelectionModel().getSelectedItem();
		ReciboRepository recRepo = (ReciboRepository) context.getBean("reciboRepository");
		ReceitaRepository rRepo = (ReceitaRepository) context.getBean("receitaRepository");
		if (selectedDuplicata != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Cancelar pagamento");
			alert.setHeaderText("Cancelar pagamento da mensalidade selecionada?");
			Optional<ButtonType> result = alert.showAndWait();
			Duplicata temp = new Duplicata();
			if (result.get() == ButtonType.OK) {
				
				temp.setId(selectedDuplicata.getId());
				temp.setEstado(EstadoPagamento.PENDENTE);
				temp.setValor(selectedDuplicata.getValor());
				temp.setParcela(selectedDuplicata.getParcela());
				temp.setDataVencimento(selectedDuplicata.getDataVencimento());
				temp.setCliente(selectedDuplicata.getCliente());
				temp.setContrato(selectedDuplicata.getContrato());
				temp.setReceita(selectedDuplicata.getReceita());
				temp = dupRepo.save(temp);
				recRepo.delete(selectedDuplicata.getRecibo());
				
				Receita receita = selectedDuplicata.getReceita();
				receita.setDataRecebimento(null);
				receita.setEstado(EstadoPagamento.PENDENTE);
				receita.setValorPago(null);
				Integer x = null;
				receita.setMeioPagamento(x);
				rRepo.save(receita);
				

			}
			duplicataTable.getItems().remove(selectedDuplicata);
			duplicataTable.getItems().add(temp);

			duplicataTable.refresh();
		}

	}

	@FXML
	public void visualizaRecibo() throws IOException {
		Duplicata duplicata = duplicataTable.getSelectionModel().getSelectedItem();
		Recibo recibo = duplicata.getRecibo();
		File file = new File(FilesUtils.pathRecibos(recibo));
		Desktop desktop = Desktop.getDesktop();
		desktop.open(file);

	}

	public void visualizaRecibo(Recibo rec) throws IOException {
		File file = new File(FilesUtils.pathRecibos(rec));
		Desktop desktop = Desktop.getDesktop();
		desktop.open(file);
	}

	public void setMainApp(FrontApp frontApp) {
		this.frontApp = frontApp;
	}
}
