package com.fgwr.jpcorretora.views;

import java.io.IOException;
import java.util.Calendar;
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
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Recibo;
import com.fgwr.jpcorretora.domain.Referencia;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.repositories.DadosBancariosRepository;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;
import com.fgwr.jpcorretora.repositories.ReciboRepository;
import com.fgwr.jpcorretora.repositories.ReferenciaRepository;
import com.fgwr.jpcorretora.services.ClienteService;
import com.fgwr.jpcorretora.services.DuplicataService;
import com.fgwr.jpcorretora.services.ImovelService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ClienteController {
	
	@FXML
	private BorderPane rootLayout;
	
	private ObservableList<Cliente> clienteData = FXCollections.observableArrayList();
	private ObservableList<String> telefoneData = FXCollections.observableArrayList();
	private ObservableList<Duplicata> duplicataData = FXCollections.observableArrayList();
	private ObservableList<Duplicata> duplicataAux = FXCollections.observableArrayList();
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
	
	private Imovel imovel;
	private Cliente clienteAux;
	private DadosBancarios dadosBancarios;	
	private Integer contratoAtual;
		
	FrontApp frontApp = new FrontApp();
	
	ApplicationContext context = SpringContext.getAppContext();
		
	public ObservableList<Cliente> getClienteData() {
		ClienteService cliServ = (ClienteService)context.getBean("clienteService");
		List<Cliente> allcli = cliServ.findAll();
		for (Cliente cliente : allcli) {
			clienteData.add(cliente);
		} return clienteData;
	}
	
	public ObservableList<Duplicata> getDuplicataData() {
		DuplicataService dupServ = (DuplicataService)context.getBean("duplicataService");
		List<Duplicata> alldup = dupServ.findAll();
		for (Duplicata duplicata : alldup) {
			duplicataData.add(duplicata);		
		} return duplicataData;
	}
	
	public Imovel getImovelData() {
		ImovelService imServ = (ImovelService)context.getBean("imovelService");
    	imovel = imServ.findByContrato(clienteAux.getContrato());
			return imovel;
	}
	
	public DadosBancarios getDadosBancariosData() {
		DadosBancariosRepository dbRepo = (DadosBancariosRepository)context.getBean("dadosBancariosRepository");
		dadosBancarios = dbRepo.findByCliente(clienteAux);
			return dadosBancarios;
	}
	
	public ObservableList<Referencia> getReferenciaData(Cliente cliente) {
		ReferenciaRepository refRepo = (ReferenciaRepository)context.getBean("referenciaRepository");
    	List<Referencia> allRef = refRepo.findByCliente(cliente);
		for (Referencia referencia : allRef) {
			referenciaData.add(referencia);
	}return referenciaData;
	}
	
	@FXML
	private void initialize() {
		duplicataData = getDuplicataData();
		clienteTable.setItems(getClienteData());
		nomeColumn.setCellValueFactory(cellData -> cellData.getValue().nome());
		codColumn.setCellValueFactory(cellData -> cellData.getValue().cod());
		showClient(null);
		clienteTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showClient(newValue));
		duplicataTable.setItems(duplicataAux);		
		}

	private void showClient(Cliente cliente) {
		if (cliente != null) {
			nomeLabel.setText(cliente.getNome());
			cpfLabel.setText(cliente.getCpfOuCnpj());
			rgLabel.setText(cliente.getRg());
			emailLabel.setText(cliente.getEmail());
			
			Set<String> telefones = cliente.getTelefones();
			for (String string : telefones) {
				telefoneData.add(string);
			}
			telefonePrefLabel.setText(telefoneData.get(0));
			if (telefoneData.size() == 2 ) {
			telefoneAltLabel.setText(telefoneData.get(0)); telefonePrefLabel.setText(telefoneData.get(1)); } else telefoneAltLabel.setText("");
			telefoneData.clear();
			dataNascimentoLabel.setText(cliente.getDataNascimentoString());
			estadoCivilLabel.setText(cliente.getEstadoCivil().getDescricao());
			profissaoLabel.setText(cliente.getProfissao());
			if (cliente.getContrato() != null ) {
			contratoAtual = cliente.getContrato().getId();
			} else {
				contratoAtual = null;
			}
			clienteAux = cliente;
			duplicataAux.clear();
			imovel = getImovelData();
			dadosBancarios = getDadosBancariosData();
			bancoLabel.setText(dadosBancarios.getBanco().getFullCod() + " - " + dadosBancarios.getBanco().getDescricao());	
			tipoContaLabel.setText(dadosBancarios.getTipo().getDesc());
			agenciaLabel.setText(dadosBancarios.getAgencia());
			numeroContaLabel.setText(dadosBancarios.getConta());
			titularLabel.setText(dadosBancarios.getTitular());
			referenciaData.clear();
			referenciaData = getReferenciaData(cliente);
			ref1Label.setText(referenciaData.get(0).getNome()); ref1FoneLabel.setText(referenciaData.get(0).getTelefone());
			if (referenciaData.size()>=2) {ref2Label.setText(referenciaData.get(1).getNome()); ref2FoneLabel.setText(referenciaData.get(1).getTelefone());}
			else {ref2Label.setText(""); ref2FoneLabel.setText("");}
			if (referenciaData.size()==3) {ref3Label.setText(referenciaData.get(2).getNome()); ref3FoneLabel.setText(referenciaData.get(2).getTelefone());}
			else {ref3Label.setText(""); ref3FoneLabel.setText("");}

			obsLabel.setText(cliente.getObs());
			
			clienteTable.refresh();
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
					duplicataTable.getSortOrder().add(parcelaColumn);
					if (duplicata.getDataPagamento() != null ) {
					dataPgtoColumn.setCellValueFactory(cellData -> cellData.getValue().dataPgto());
					}
			}
			}
				
		/*		if (imovel != null) {
					logradouroLabel.setText(imovel.getEndereco().getLogradouro());
					numeroLabel.setText(imovel.getEndereco().getNumero());
					bairroLabel.setText(imovel.getEndereco().getBairro());
					cepLabel.setText(imovel.getEndereco().getCep());
					cidadeLabel.setText(imovel.getEndereco().getCidade());
					complementoLabel.setText(imovel.getEndereco().getComplemento());
					dataLocacaoLabel.setText(imovel.getDataLaudo().toString());
				} else {
					logradouroLabel.setText("");
					numeroLabel.setText("");
					bairroLabel.setText("");
					cepLabel.setText("");
					cidadeLabel.setText("");
					complementoLabel.setText("");
					dataLocacaoLabel.setText("");
				}*/
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
	    if (referenciaData.get(0) == null ) {
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
	    if (referenciaData.size() == 2 ) {
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
    	referenciaData = getReferenciaData(clienteAux);
	}
	
	@FXML
	private void handleEditRef3() throws IOException {
		Cliente selectedCliente = clienteTable.getSelectionModel().getSelectedItem();
		Referencia selectedReferencia = null;

		if (referenciaData.size() == 1) {
			referenciaData.add(new Referencia());
			selectedReferencia = referenciaData.get(1);
		}else if (referenciaData.size() == 2) {
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
	
	@Transactional
	@FXML
	private void handlePagamento() {
		DuplicataRepository dupRepo = (DuplicataRepository)context.getBean("duplicataRepository");
		Duplicata selectedDuplicata = duplicataTable.getSelectionModel().getSelectedItem();
		ReciboRepository recRepo = (ReciboRepository)context.getBean("reciboRepository");
		Calendar cal = Calendar.getInstance();
		if (selectedDuplicata != null ) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmar pagamento");
			alert.setHeaderText("Confirmar pagamento da mensalidade selecionada?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
			selectedDuplicata.setEstado(EstadoPagamento.QUITADO);
			Recibo rec = new Recibo(null, clienteTable.getSelectionModel().getSelectedItem(), selectedDuplicata.getValor(), selectedDuplicata.getParcela(), selectedDuplicata.getDataVencimento(), cal.getTime());
			selectedDuplicata.setDataPagamento(cal.getTime());
			selectedDuplicata.setRecibo(rec);
			dupRepo.save(selectedDuplicata);
			recRepo.save(rec);
			
			duplicataTable.refresh();
			
			Alert alert2 = new Alert(AlertType.CONFIRMATION);
			alert2.setTitle("Recibo");
			alert2.setHeaderText("Visualizar o recibo do pagamento?");
			Optional<ButtonType> result2 = alert2.showAndWait();
			if (result2.get() == ButtonType.OK){
			System.out.println(rec.getValor());
			
			
			} else {
				alert.close();
			}
			}
			
			}
	}
	
	@FXML
	public void handleCancelaPagamento() {
		DuplicataRepository dupRepo = (DuplicataRepository)context.getBean("duplicataRepository");
		Duplicata selectedDuplicata = duplicataTable.getSelectionModel().getSelectedItem();
		ReciboRepository recRepo = (ReciboRepository)context.getBean("reciboRepository");
		if (selectedDuplicata != null ) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Cancelar pagamento");
			alert.setHeaderText("Cancelar pagamento da mensalidade selecionada?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
			selectedDuplicata.setEstado(EstadoPagamento.PENDENTE);
			Recibo rec = selectedDuplicata.getRecibo();
			selectedDuplicata.setDataPagamento(null);
			selectedDuplicata.setRecibo(null);
			dupRepo.save(selectedDuplicata);
			recRepo.delete(rec);
			}
			duplicataTable.refresh();
	}}
	
	public void setMainApp(FrontApp frontApp) {
        this.frontApp = frontApp;
	}
}
