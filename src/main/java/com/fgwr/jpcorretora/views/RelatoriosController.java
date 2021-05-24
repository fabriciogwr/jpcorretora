package com.fgwr.jpcorretora.views;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;



@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RelatoriosController {
	
	ApplicationContext context = SpringContext.getAppContext();
	String dir = System.getProperty("user.dir");
	
	@FXML
	private TableView<String> relatoriosTable;
	@FXML
	private TableColumn<String, String> relatorioColumn;
	@FXML
	private TableView<Duplicata> contasReceberTable;
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
	private AnchorPane anchorRight;
	
	
	public void initialize() {
		
		List<String> tipoRelatorio = new ArrayList<> ();
		
		tipoRelatorio.add("Contas a Receber");
		tipoRelatorio.add("Contas em Pendência");
		tipoRelatorio.add("Entradas de um período");
		tipoRelatorio.add("Entradas de um mês");
		tipoRelatorio.add("Fluxo de um período");
		tipoRelatorio.add("Clientes Cadastrados");
		tipoRelatorio.add("Imóveis Cadastrados");
		
		
		relatoriosTable.setItems(FXCollections.observableArrayList(tipoRelatorio));
	}
	
	@FXML
	private void contasReceber() throws MalformedURLException, IOException {
		anchorRight = FXMLLoader.load(Paths.get(Paths.get(dir+"\\fxml\\contasReceber.fxml").toUri()).toUri().toURL()); //BUILD
		DuplicataRepository dupRepo = (DuplicataRepository) context.getBean("duplicataRepository");
		List<Duplicata> duplicatasReceber = dupRepo.findByEstado(EstadoPagamento.PENDENTE.getCod());
			
	}
	
}