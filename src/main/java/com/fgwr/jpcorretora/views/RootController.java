package com.fgwr.jpcorretora.views;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

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
import com.fgwr.jpcorretora.domain.Endereco;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.DadosBancariosRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RootController {

	@FXML
	private MenuBar menuBar;
	
	@FXML
	private BorderPane rootLayout;
	
	FrontApp frontApp = new FrontApp();
	
	String dir = System.getProperty("user.dir");
	
	public void initialize(URL url, ResourceBundle rb) {
	    
	} 
	
	@FXML
    public void showCadastroClientes(ActionEvent event) throws IOException {
      AnchorPane showCadastroClientes = FXMLLoader.load(Paths.get(Paths.get(dir+"\\fxml\\TelaClientes.fxml").toUri()).toUri().toURL()); //BUILD
    //    AnchorPane showCadastroClientes = FXMLLoader.load(getClass().getResource("TelaClientes.fxml")); //DEV    
        rootLayout.setCenter(showCadastroClientes);
    }
	
	@FXML
    public void showCadastroImoveis(ActionEvent event) throws IOException {
      AnchorPane showCadastroImoveis = FXMLLoader.load(Paths.get(Paths.get(dir+"\\fxml\\TelaImoveis.fxml").toUri()).toUri().toURL()); //BUILD
	//	AnchorPane showCadastroImoveis = FXMLLoader.load(getClass().getResource("TelaImoveis.fxml")); //DEV 
        rootLayout.setCenter(showCadastroImoveis);
    }
	
	@FXML
    public void showCadastroProprietarios(ActionEvent event) throws IOException {
      AnchorPane showCadastroProprietarios = FXMLLoader.load(Paths.get(Paths.get(dir+"\\fxml\\TelaProprietarios.fxml").toUri()).toUri().toURL()); //BUILD
	//	AnchorPane showCadastroProprietarios = FXMLLoader.load(getClass().getResource("TelaProprietarios.fxml")); //DEV  
        rootLayout.setCenter(showCadastroProprietarios);
    }
	
	@FXML
	private void handleNovoCliente(ActionEvent event)  throws IOException {
	    Cliente tempCliente = new Cliente();
	    DadosBancarios db = new DadosBancarios();
	    
	    boolean okClicked = frontApp.showNovoCliente(tempCliente, db);
	    if (okClicked) {
	    	ApplicationContext context = SpringContext.getAppContext();
        	ClienteRepository repoCli = (ClienteRepository)context.getBean("clienteRepository");
        	DadosBancariosRepository repoDb = (DadosBancariosRepository)context.getBean("dadosBancariosRepository");
	        repoCli.save(tempCliente);
	        repoDb.save(db);
	        
	      AnchorPane showCadastroClientes = FXMLLoader.load(Paths.get(Paths.get(dir+"\\fxml\\TelaClientes.fxml").toUri()).toUri().toURL()); //BUILD
	   //     AnchorPane showCadastroClientes = FXMLLoader.load(getClass().getResource("TelaCLientes.fxml")); //DEV
	        rootLayout.setCenter(showCadastroClientes);
	    }
	}
	
	@FXML
	@Transactional
	private void handleNovoContrato(ActionEvent event)  throws IOException {
	    Contrato contrato = new Contrato();
	    
	    boolean okClicked = frontApp.showNovoContrato(contrato);
	    if (okClicked) {
	      AnchorPane showCadastroClientes = FXMLLoader.load(Paths.get(Paths.get(dir+"\\fxml\\TelaClientes.fxml").toUri()).toUri().toURL()); //BUILD
	   // 	AnchorPane showCadastroClientes = FXMLLoader.load(getClass().getResource("TelaClientes.fxml")); //DEV
	        rootLayout.setCenter(showCadastroClientes);
	    }
	}
	
	@FXML
	private void handleNovoImovel(ActionEvent event)  throws IOException {
	    Imovel imovel = new Imovel();
	    Endereco endereco = new Endereco();	   
	    
	    boolean okClicked = frontApp.showNovoImovel(imovel, endereco);
	    if (okClicked) {
	      AnchorPane showCadastroImoveis = FXMLLoader.load(Paths.get(Paths.get(dir+"\\fxml\\TelaImoveis.fxml").toUri()).toUri().toURL()); //BUILD
	    //    AnchorPane showCadastroImoveis = FXMLLoader.load(getClass().getResource("TelaImoveis.fxml")); //DEV
	        rootLayout.setCenter(showCadastroImoveis);
	    }
	}
	
	
}

