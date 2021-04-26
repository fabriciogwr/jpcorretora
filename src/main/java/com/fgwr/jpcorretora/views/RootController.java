package com.fgwr.jpcorretora.views;

import java.io.File;
import java.io.IOException;
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
import com.fgwr.jpcorretora.utils.FilesUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RootController {

	@FXML
	private MenuBar menuBar;
	@FXML
	private Button clientesBtn;
	@FXML
	private Button contratosLBtn;
	@FXML
	private Button contratosVBtn;
	@FXML
	private Button imoveisBtn;
	@FXML
	private Button proprietariosBtn;
	@FXML
	private Button eventosBtn;
	@FXML
	private Button relatoriosBtn;
	@FXML
	private Button manutencaoBtn;

	private Stage secStage;
	private Stage thrStage;

	@FXML
	private BorderPane rootLayout;

	FrontApp frontApp = new FrontApp();
	String dir = System.getProperty("user.dir");

	public void initialize(URL url, ResourceBundle rb) {

	}

	@FXML
	public void showCadastroClientes(ActionEvent event) throws IOException {
		clientesBtn.getStylesheets().clear();
		clientesBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/selected.css")));
		imoveisBtn.getStylesheets().clear();
		imoveisBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		proprietariosBtn.getStylesheets().clear();
		proprietariosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		relatoriosBtn.getStylesheets().clear();
		relatoriosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		contratosLBtn.getStylesheets().clear();
		contratosLBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		contratosVBtn.getStylesheets().clear();
		contratosVBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		eventosBtn.getStylesheets().clear();
		eventosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		manutencaoBtn.getStylesheets().clear();
		manutencaoBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));

		AnchorPane showCadastroClientes = FXMLLoader
				.load(Paths.get(Paths.get(dir + "\\fxml\\TelaClientes.fxml").toUri()).toUri().toURL()); // BUILD
		// AnchorPane showCadastroClientes =
		// FXMLLoader.load(getClass().getResource("TelaClientes.fxml")); //DEV
		rootLayout.setCenter(showCadastroClientes);
	}

	@FXML
	public void showCadastroImoveis(ActionEvent event) throws IOException {
		clientesBtn.getStylesheets().clear();
		clientesBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		imoveisBtn.getStylesheets().clear();
		imoveisBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/selected.css")));
		proprietariosBtn.getStylesheets().clear();
		proprietariosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		relatoriosBtn.getStylesheets().clear();
		relatoriosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		contratosLBtn.getStylesheets().clear();
		contratosLBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		contratosVBtn.getStylesheets().clear();
		contratosVBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		eventosBtn.getStylesheets().clear();
		eventosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		manutencaoBtn.getStylesheets().clear();
		manutencaoBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));

		AnchorPane showCadastroImoveis = FXMLLoader
				.load(Paths.get(Paths.get(dir + "\\fxml\\TelaImoveis.fxml").toUri()).toUri().toURL()); // BUILD
		// AnchorPane showCadastroImoveis =
		// FXMLLoader.load(getClass().getResource("TelaImoveis.fxml")); //DEV
		rootLayout.setCenter(showCadastroImoveis);
	}

	@FXML
	public void showCadastroProprietarios(ActionEvent event) throws IOException {

		clientesBtn.getStylesheets().clear();
		clientesBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		imoveisBtn.getStylesheets().clear();
		imoveisBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		proprietariosBtn.getStylesheets().clear();
		proprietariosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/selected.css")));
		relatoriosBtn.getStylesheets().clear();
		relatoriosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		contratosLBtn.getStylesheets().clear();
		contratosLBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		contratosVBtn.getStylesheets().clear();
		contratosVBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		eventosBtn.getStylesheets().clear();
		eventosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		manutencaoBtn.getStylesheets().clear();
		manutencaoBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));

		AnchorPane showCadastroProprietarios = FXMLLoader
				.load(Paths.get(Paths.get(dir + "\\fxml\\TelaProprietarios.fxml").toUri()).toUri().toURL()); // BUILD
		// AnchorPane showCadastroProprietarios =
		// FXMLLoader.load(getClass().getResource("TelaProprietarios.fxml")); //DEV
		rootLayout.setCenter(showCadastroProprietarios);
	}

	@FXML
	public void showRelatorios(ActionEvent event) throws IOException {

		clientesBtn.getStylesheets().clear();
		clientesBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		imoveisBtn.getStylesheets().clear();
		imoveisBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		proprietariosBtn.getStylesheets().clear();
		proprietariosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		relatoriosBtn.getStylesheets().clear();
		relatoriosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/selected.css")));
		contratosLBtn.getStylesheets().clear();
		contratosLBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		contratosVBtn.getStylesheets().clear();
		contratosVBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		eventosBtn.getStylesheets().clear();
		eventosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		manutencaoBtn.getStylesheets().clear();
		manutencaoBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));

		AnchorPane showCadastroProprietarios = FXMLLoader
				.load(Paths.get(Paths.get(dir + "\\fxml\\Eventos.fxml").toUri()).toUri().toURL()); // BUILD
		// AnchorPane showCadastroProprietarios =
		// FXMLLoader.load(getClass().getResource("TelaProprietarios.fxml")); //DEV
		// rootLayout.setCenter(showCadastroProprietarios);
	}

	@FXML
	public void showContratos(ActionEvent event) throws IOException {

		clientesBtn.getStylesheets().clear();
		clientesBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		imoveisBtn.getStylesheets().clear();
		imoveisBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		proprietariosBtn.getStylesheets().clear();
		proprietariosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		relatoriosBtn.getStylesheets().clear();
		relatoriosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		contratosLBtn.getStylesheets().clear();
		contratosLBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/selected.css")));
		contratosVBtn.getStylesheets().clear();
		contratosVBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		eventosBtn.getStylesheets().clear();
		eventosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		manutencaoBtn.getStylesheets().clear();
		manutencaoBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));

		AnchorPane showContratos = FXMLLoader
				.load(Paths.get(Paths.get(dir + "\\fxml\\TelaContratosL.fxml").toUri()).toUri().toURL()); // BUILD
		// AnchorPane showContratos =
		// FXMLLoader.load(getClass().getResource("TelaContratosL.fxml")); //DEV
		rootLayout.setCenter(showContratos);
	}

	@FXML
	public void showEventos(ActionEvent event) throws IOException {
		clientesBtn.getStylesheets().clear();
		clientesBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		imoveisBtn.getStylesheets().clear();
		imoveisBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		proprietariosBtn.getStylesheets().clear();
		proprietariosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		relatoriosBtn.getStylesheets().clear();
		relatoriosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		contratosLBtn.getStylesheets().clear();
		contratosLBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		contratosVBtn.getStylesheets().clear();
		contratosVBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		manutencaoBtn.getStylesheets().clear();
		manutencaoBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));

		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/TelaClientes.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\Eventos.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new EventosController());
			AnchorPane eventosOverview = (AnchorPane) loader.load();

			secStage = new Stage();
			Scene scene2 = new Scene(eventosOverview);
			secStage.setScene(scene2);
			secStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void handleNovoCliente(ActionEvent event) throws IOException {
		Cliente tempCliente = new Cliente();
		DadosBancarios db = new DadosBancarios();

		boolean okClicked = frontApp.showNovoCliente(tempCliente, db);
		if (okClicked) {
			ApplicationContext context = SpringContext.getAppContext();
			ClienteRepository repoCli = (ClienteRepository) context.getBean("clienteRepository");
			DadosBancariosRepository repoDb = (DadosBancariosRepository) context.getBean("dadosBancariosRepository");
			repoCli.save(tempCliente);
			repoDb.save(db);

			clientesBtn.getStylesheets().clear();
			clientesBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/selected.css")));
			imoveisBtn.getStylesheets().clear();
			imoveisBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			proprietariosBtn.getStylesheets().clear();
			proprietariosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			relatoriosBtn.getStylesheets().clear();
			relatoriosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			contratosLBtn.getStylesheets().clear();
			contratosLBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			contratosVBtn.getStylesheets().clear();
			contratosVBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			eventosBtn.getStylesheets().clear();
			eventosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			manutencaoBtn.getStylesheets().clear();
			manutencaoBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));

			AnchorPane showCadastroClientes = FXMLLoader
					.load(Paths.get(Paths.get(dir + "\\fxml\\TelaClientes.fxml").toUri()).toUri().toURL()); // BUILD
			// AnchorPane showCadastroClientes =
			// FXMLLoader.load(getClass().getResource("TelaCLientes.fxml")); //DEV
			rootLayout.setCenter(showCadastroClientes);
		}
	}

	@FXML
	@Transactional
	private void handleNovoContrato(ActionEvent event) throws IOException {
		Contrato contrato = new Contrato();

		boolean okClicked = frontApp.showNovoContrato(contrato);
		if (okClicked) {

			clientesBtn.getStylesheets().clear();
			clientesBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			imoveisBtn.getStylesheets().clear();
			imoveisBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			proprietariosBtn.getStylesheets().clear();
			proprietariosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			relatoriosBtn.getStylesheets().clear();
			relatoriosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			contratosLBtn.getStylesheets().clear();
			contratosLBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/selected.css")));
			contratosVBtn.getStylesheets().clear();
			contratosVBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			eventosBtn.getStylesheets().clear();
			eventosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			manutencaoBtn.getStylesheets().clear();
			manutencaoBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));

			AnchorPane showContratos = FXMLLoader
					.load(Paths.get(Paths.get(dir + "\\fxml\\TelaContratosL.fxml").toUri()).toUri().toURL()); // BUILD
			// AnchorPane showContratos =
			// FXMLLoader.load(getClass().getResource("TelaContratosL.fxml")); //DEV
			rootLayout.setCenter(showContratos);
		}
	}

	@FXML
	private void handleNovoImovel(ActionEvent event) throws IOException {
		Imovel imovel = new Imovel();
		Endereco endereco = new Endereco();

		boolean okClicked = frontApp.showNovoImovel(imovel, endereco);
		if (okClicked) {

			clientesBtn.getStylesheets().clear();
			clientesBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			imoveisBtn.getStylesheets().clear();
			imoveisBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/selected.css")));
			proprietariosBtn.getStylesheets().clear();
			proprietariosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			relatoriosBtn.getStylesheets().clear();
			relatoriosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			contratosLBtn.getStylesheets().clear();
			contratosLBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			contratosVBtn.getStylesheets().clear();
			contratosVBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			eventosBtn.getStylesheets().clear();
			eventosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
			manutencaoBtn.getStylesheets().clear();
			manutencaoBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));

			AnchorPane showCadastroImoveis = FXMLLoader
					.load(Paths.get(Paths.get(dir + "\\fxml\\TelaImoveis.fxml").toUri()).toUri().toURL()); // BUILD
			// AnchorPane showCadastroImoveis =
			// FXMLLoader.load(getClass().getResource("TelaImoveis.fxml")); //DEV
			rootLayout.setCenter(showCadastroImoveis);
		}
	}

	@FXML
	public void showManutencao(ActionEvent event) throws IOException {
		clientesBtn.getStylesheets().clear();
		clientesBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		imoveisBtn.getStylesheets().clear();
		imoveisBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		proprietariosBtn.getStylesheets().clear();
		proprietariosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		relatoriosBtn.getStylesheets().clear();
		relatoriosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		contratosLBtn.getStylesheets().clear();
		contratosLBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		contratosVBtn.getStylesheets().clear();
		contratosVBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		eventosBtn.getStylesheets().clear();
		eventosBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/style.css")));
		manutencaoBtn.getStylesheets().clear();
		manutencaoBtn.getStylesheets().add(FilesUtils.fileToString(new File("css/selected.css")));

		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/TelaClientes.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\Manutencao.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new ManutencaoController());
			AnchorPane manutencaoOverview = (AnchorPane) loader.load();

			thrStage = new Stage();
			Scene scene3 = new Scene(manutencaoOverview);
			thrStage.setScene(scene3);
			thrStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
