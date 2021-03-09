package com.fgwr.jpcorretora.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Endereco;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Proprietario;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.ContratoRepository;
import com.fgwr.jpcorretora.repositories.DadosBancariosRepository;
import com.fgwr.jpcorretora.repositories.EnderecoRepository;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.repositories.ProprietarioRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@FxmlView("RootLayout.fxml")
public class RootController {

	@FXML
	private MenuBar menuBar;
	
	@FXML
	private BorderPane rootLayout;
	
	FrontApp frontApp = new FrontApp();
	
	ClienteController cc;

	public void initialize(URL url, ResourceBundle rb) {
	    
	} 
	
	@FXML
    public void showCadastroClientes(ActionEvent event) throws IOException {
        AnchorPane showCadastroClientes = FXMLLoader.load(getClass().getResource("TelaClientes.fxml"));
        rootLayout.setCenter(showCadastroClientes);
    }
	
	@FXML
    public void showCadastroImoveis(ActionEvent event) throws IOException {
        AnchorPane showCadastroImoveis = FXMLLoader.load(getClass().getResource("TelaImoveis.fxml"));
        rootLayout.setCenter(showCadastroImoveis);
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
	        
	        AnchorPane showCadastroClientes = FXMLLoader.load(getClass().getResource("TelaClientes.fxml"));
	        rootLayout.setCenter(showCadastroClientes);
	    }
	}
	
	@FXML
	private void handleNovoContrato(ActionEvent event)  throws IOException {
	    Contrato contrato = new Contrato();
	    
	    boolean okClicked = frontApp.showNovoContrato(contrato);
	    if (okClicked) {
	    	ApplicationContext context = SpringContext.getAppContext();
        	ContratoRepository contRepo = (ContratoRepository)context.getBean("contratoRepository");
	        contRepo.save(contrato);
	        
	        AnchorPane showCadastroClientes = FXMLLoader.load(getClass().getResource("TelaClientes.fxml"));
	        rootLayout.setCenter(showCadastroClientes);
	    }
	}
	
	@FXML
	private void handleNovoImovel(ActionEvent event)  throws IOException {
	    Imovel imovel = new Imovel();
	    Endereco endereco = new Endereco();
	    Proprietario proprietario = new Proprietario();
	    DadosBancarios db = new DadosBancarios();
	    ApplicationContext context = SpringContext.getAppContext();
	    
	    boolean okClicked = frontApp.showNovoProprietario(proprietario, db);
	    if (okClicked) {
	    	
	    	ProprietarioRepository propRepo = (ProprietarioRepository)context.getBean("proprietarioRepository");
	    	DadosBancariosRepository dbRepo = (DadosBancariosRepository)context.getBean("dadosBancariosRepository");
	        propRepo.save(proprietario);
	        dbRepo.save(db);
	    }
	    
	    boolean okClicked2 = frontApp.showNovoImovel(imovel, proprietario, endereco);
	    if (okClicked2) {
	    	ProprietarioRepository propRepo = (ProprietarioRepository)context.getBean("proprietarioRepository");
        	ImovelRepository imvRepo = (ImovelRepository )context.getBean("imovelRepository");
        	EnderecoRepository endRepo = (EnderecoRepository)context.getBean("enderecoRepository");
        	imovel.setEndereco(endereco);
        	imovel.setProprietario(proprietario);
        	imvRepo.save(imovel);
        	proprietario.getImovel().add(imovel);
        	endereco.setImovel(imovel);
	        propRepo.save(proprietario);
	        endRepo.save(endereco);
	        
	        AnchorPane showCadastroClientes = FXMLLoader.load(getClass().getResource("TelaClientes.fxml"));
	        rootLayout.setCenter(showCadastroClientes);
	    }
	}
	
	
}

