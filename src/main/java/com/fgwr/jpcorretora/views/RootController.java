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
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.DadosBancariosRepository;

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
	
	
	
}

