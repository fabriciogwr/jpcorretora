package com.fgwr.jpcorretora.views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Recibo;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.ContratoRepository;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.repositories.ReciboRepository;
import com.fgwr.jpcorretora.services.exceptions.ObjectNotFoundException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ContratoController {
	
	FrontApp frontApp = new FrontApp();
	ApplicationContext context = SpringContext.getAppContext();
	
	@FXML
    private TableView<Contrato> contratoTable;
    @FXML
    private TableColumn<Contrato, String> numColumn;
    @FXML
    private TableColumn<Contrato, String> periodoColumn;
    @FXML
    private TableColumn<Contrato, String> clienteColumn;
    @FXML
    private TableColumn<Contrato, String> proprietarioColumn;
    @FXML
    private TableColumn<Contrato, String> inicioColumn;
    @FXML
    private TableColumn<Contrato, String> fimColumn;
    @FXML
    private TableColumn<Contrato, String> mensalidadesColumn;

    private List<Contrato> getContratoData() {
    	ContratoRepository contRepo = (ContratoRepository) context.getBean("contratoRepository");
    	List<Contrato> contratos = contRepo.findAll();
		
    	return contratos;
    } 
    
    @FXML
    private void initialize() {
    	
    	contratoTable.setItems(FXCollections.observableArrayList(getContratoData()));
    	numColumn.setCellValueFactory(cellData -> cellData.getValue().num());
    	clienteColumn.setCellValueFactory(cellData -> cellData.getValue().cliente());
    	periodoColumn.setCellValueFactory(cellData -> cellData.getValue().vigencia());
    	proprietarioColumn.setCellValueFactory(cellData -> cellData.getValue().proprietario());
    	inicioColumn.setCellValueFactory(cellData -> cellData.getValue().dataInicio());
    	fimColumn.setCellValueFactory(cellData -> cellData.getValue().dataFim());
    	mensalidadesColumn.setCellValueFactory(cellData -> cellData.getValue().mensalidade());
    	
		
		
    	
    }
    
    public Cliente findCliente (Integer id) {
    	ClienteRepository cliRepo = (ClienteRepository) context.getBean("clienteRepository");
    	Optional<Cliente> obj = cliRepo.findById(id);
    	return obj.orElseThrow(() -> new ObjectNotFoundException("Não encontrado"));
    }
    
    public Imovel findImovel (Integer id) {
    	ImovelRepository imvRepo = (ImovelRepository) context.getBean("imovelRepository");
    	Optional<Imovel> obj = imvRepo.findById(id);
    	return obj.orElseThrow(() -> new ObjectNotFoundException("Não encontrado"));
    }
    
    @FXML
	private void handleCancelaContrato() {

    	ContratoRepository contRepo = (ContratoRepository) context.getBean("contratoRepository");
    	ImovelRepository imvRepo = (ImovelRepository) context.getBean("imovelRepository");
    	ClienteRepository cliRepo = (ClienteRepository) context.getBean("clienteRepository");
		Contrato selectedContrato = contratoTable.getSelectionModel().getSelectedItem();

		if (selectedContrato != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Rescisão de Contrato");
			alert.setHeaderText("Confirmar Rescisão do Contrato Selecionado?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				Cliente cliente = findCliente(selectedContrato.getCliente().getId());
				Imovel imv = findImovel(selectedContrato.getImovel().getId());
				imv.setContrato(null);
				cliente.setContrato(null);
				cliRepo.save(cliente);
				imvRepo.save(imv);
				contRepo.delete(selectedContrato);
				contratoTable.getItems().remove(selectedContrato);
			}
			
		}
	}
    
    @FXML
    public void visualizaContrato() throws IOException {
    	ContratoRepository contRepo = (ContratoRepository) context.getBean("contratoRepository");
		Contrato selectedContrato = contratoTable.getSelectionModel().getSelectedItem();
    	
		String[] nomeArr = StringUtils.split(selectedContrato.getCliente().getNome());
		String docFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		File file = new File(docFolder + "\\Contratos\\" + selectedContrato.getId().toString() + " - " + nomeArr[0] + " " + nomeArr[1] + ".pdf");
		Desktop desktop = Desktop.getDesktop();
		desktop.open(file);
	}

	public void setMainApp(FrontApp frontApp) {
		this.frontApp = frontApp;
	}
    
    
    
    

    
    
    
}


