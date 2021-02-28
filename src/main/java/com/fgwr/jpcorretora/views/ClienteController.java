package com.fgwr.jpcorretora.views;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.repositories.DadosBancariosRepository;
import com.fgwr.jpcorretora.services.ClienteService;
import com.fgwr.jpcorretora.services.DuplicataService;
import com.fgwr.jpcorretora.services.ImovelService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ClienteController {
	
	private ObservableList<Cliente> clienteData = FXCollections.observableArrayList();
	@FXML
	private TableView<Cliente> clienteTable;
	@FXML
	private TableColumn<Cliente, String> codColumn;
	@FXML
	private TableColumn<Cliente, String> nomeColumn;
	
	private ObservableList<Duplicata> duplicataData = FXCollections.observableArrayList();
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
	private Label estadoCivilLabel;
	@FXML
	private Label profissaoLabel;

	private Integer contratoAtual;
	
	private Cliente clienteAux;
	
	private DadosBancarios dadosBancarios;
	
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
	
	private ObservableList<Duplicata> duplicataAux = FXCollections.observableArrayList();
	
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
	
	private Imovel imovel;
	
	FrontApp frontApp = new FrontApp();
	
	public ObservableList<Cliente> getClienteData() {
		ApplicationContext context = SpringContext.getAppContext();
    	ClienteService cliServ = (ClienteService)context.getBean("clienteService");
		
    	List<Cliente> allcli = cliServ.findAll();
		for (Cliente cliente : allcli) {
			clienteData.add(cliente);
		} return clienteData;
	}
	
	public ObservableList<Duplicata> getDuplicataData() {
		ApplicationContext context = SpringContext.getAppContext();
    	DuplicataService dupServ = (DuplicataService)context.getBean("duplicataService");
		
		List<Duplicata> alldup = dupServ.findAll();
		for (Duplicata duplicata : alldup) {
			duplicataData.add(duplicata);		
		} return duplicataData;
	}
	
	public Imovel getImovelData() {
		ApplicationContext context = SpringContext.getAppContext();
    	ImovelService imServ = (ImovelService)context.getBean("imovelService");
		
		
		imovel = imServ.findByContrato(clienteAux.getContrato());
			return imovel;
			}
	
	public DadosBancarios getDadosBancariosData() {
		ApplicationContext context = SpringContext.getAppContext();
    	DadosBancariosRepository dbRepo = (DadosBancariosRepository)context.getBean("dadosBancariosRepository");
		
		
		dadosBancarios = dbRepo.findByCliente(clienteAux);
			return dadosBancarios;
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
			dataNascimentoLabel.setText(cliente.getDataNascimentoString());
			estadoCivilLabel.setText(cliente.getEstadoCivil().getDescricao());
			profissaoLabel.setText(cliente.getProfissao());
			contratoAtual = cliente.getContrato().getId();
			clienteAux = cliente;
			duplicataAux.clear();
			imovel = getImovelData();
			dadosBancarios = getDadosBancariosData();
			bancoLabel.setText(dadosBancarios.getBanco().getFullCod() + " - " + dadosBancarios.getBanco().getDescricao());
			
			tipoContaLabel.setText(dadosBancarios.getTipo().getDesc());
			agenciaLabel.setText(dadosBancarios.getAgencia());
			numeroContaLabel.setText(dadosBancarios.getConta());
			titularLabel.setText(dadosBancarios.getTitular());
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
					//dataPgtoColumn.setCellValueFactory(cellData -> cellData.getValue().dataPgto());
					}
			}
			
				
				if (imovel != null) {
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
				}
			} else {
			nomeLabel.setText("");
			cpfLabel.setText("");
			rgLabel.setText("");
			dataNascimentoLabel.setText("");
			emailLabel.setText("");
			estadoCivilLabel.setText("");
			profissaoLabel.setText("");
			bancoLabel.setText("");
			tipoContaLabel.setText("");
			agenciaLabel.setText("");
			numeroContaLabel.setText("");
			titularLabel.setText("");
		}
	
	}
	
	@FXML
	private void handleNovoCliente() {
	    Cliente tempCliente = new Cliente();
	    boolean okClicked = frontApp.showEditCliente(tempCliente);
	    if (okClicked) {
	        getClienteData().add(tempCliente);
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
	
	public void setMainApp(FrontApp frontApp) {
        this.frontApp = frontApp;
	}
}
