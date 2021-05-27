package com.fgwr.jpcorretora;

import java.awt.SplashScreen;
import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.fgwr.jpcorretora.domain.Categoria;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Despesa;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Endereco;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Proprietario;
import com.fgwr.jpcorretora.domain.Receita;
import com.fgwr.jpcorretora.domain.Referencia;
import com.fgwr.jpcorretora.dto.ImovelChecklistDTO;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.services.ClienteService;
import com.fgwr.jpcorretora.services.ImovelService;
import com.fgwr.jpcorretora.views.CadastrarDespesaController;
import com.fgwr.jpcorretora.views.CadastrarReceitaController;
import com.fgwr.jpcorretora.views.ChecklistController;
import com.fgwr.jpcorretora.views.ClienteController;
import com.fgwr.jpcorretora.views.ConfiguraPagamentoController;
import com.fgwr.jpcorretora.views.ConfiguraPagamentoDespesaController;
import com.fgwr.jpcorretora.views.ConfiguraPagamentoReceitaController;
import com.fgwr.jpcorretora.views.ContratoController;
import com.fgwr.jpcorretora.views.EditClienteController;
import com.fgwr.jpcorretora.views.EditObsController;
import com.fgwr.jpcorretora.views.EditRefController;
import com.fgwr.jpcorretora.views.EventosController;
import com.fgwr.jpcorretora.views.ImovelController;
import com.fgwr.jpcorretora.views.NovaCategoriaController;
import com.fgwr.jpcorretora.views.NovoClienteController;
import com.fgwr.jpcorretora.views.NovoContratoController;
import com.fgwr.jpcorretora.views.NovoImovelController;
import com.fgwr.jpcorretora.views.NovoProprietarioController;
import com.fgwr.jpcorretora.views.ProprietarioController;
import com.fgwr.jpcorretora.views.RootController;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SpringBootApplication
public class FrontApp extends Application {

	@Autowired
	ClienteController cc;

	@Autowired
	EditClienteController ecs;

	@Autowired
	ClienteService cs;

	@Autowired
	ImovelController ic;

	@Autowired
	ImovelService is;

	@Autowired
	ClienteRepository repo;

	private Stage primaryStage;
	private Stage secStage;
	private BorderPane rootLayout;

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	String dir = System.getProperty("user.dir");
	// String dir = "D:\\jpcorretora";

	FileResourcesUtils fru = new FileResourcesUtils();

	public FrontApp() {

	}

	@Override
	public void init() {

		ApplicationContextInitializer<GenericApplicationContext> initializer = ac -> {

			ac.registerBean(Application.class, () -> FrontApp.this);
			ac.registerBean(Parameters.class, this::getParameters);
			ac.registerBean(HostServices.class, this::getHostServices);

		};
		this.applicationContext = new SpringApplicationBuilder().sources(JpcorretoraApplication.class)
				.initializers(initializer).run(getParameters().getRaw().toArray(new String[0]));
	}

	@Override
	public void stop() {
		this.applicationContext.close();
		Platform.exit();
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("JPCorretora");
		primaryStage.getIcons().add(new Image("file:\\\\"+ dir+ "\\imgs\\icone.ico"));
		this.secStage = new Stage();
		this.secStage.setTitle("Eventos");

		initRootLayout();

		showTelaClientes();

		showEventos();
	}

	public void initRootLayout() {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/RootLayout.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\RootLayout.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new RootController());
			rootLayout = (BorderPane) loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setMaximized(true);
			final SplashScreen splash = SplashScreen.getSplashScreen();
			if (splash != null) {
				splash.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showTelaClientes() {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/TelaClientes.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\TelaClientes.fxml").toUri()).toUri().toURL()); // BUILD
			AnchorPane personOverview = (AnchorPane) loader.load();

			rootLayout.setCenter(personOverview);

			ClienteController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showEventos() {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/TelaClientes.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\Eventos.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new EventosController());
			AnchorPane eventosOverview = (AnchorPane) loader.load();

			Scene scene2 = new Scene(eventosOverview);
			secStage.setScene(scene2);
			secStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showTelaImoveis() {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/TelaImoveis.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\TelaImoveis.fxml").toUri()).toUri().toURL()); // BUILD
			AnchorPane imovelOverview = (AnchorPane) loader.load();

			rootLayout.setCenter(imovelOverview);

			ImovelController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showTelaProprietarios() {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/TelaProprietarios.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\TelaProprietarios.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new ProprietarioController());
			AnchorPane imovelOverview = (AnchorPane) loader.load();

			rootLayout.setCenter(imovelOverview);

			ProprietarioController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showContratos() {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/TelaContratosL.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\TelaContratosL.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new ContratoController());
			AnchorPane imovelOverview = (AnchorPane) loader.load();

			rootLayout.setCenter(imovelOverview);

			ContratoController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean showEditCliente(Cliente cliente) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/EditCliente.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\EditCliente.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new EditClienteController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar Cliente");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			EditClienteController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCliente(cliente);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showNovoCliente(Cliente cliente, DadosBancarios db) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/EditCliente.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\EditCliente.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new NovoClienteController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Novo Cliente");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			NovoClienteController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCliente(cliente, db);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showNovoProprietario(Proprietario proprietario, DadosBancarios db) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/NovoProprietario.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\NovoProprietario.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new NovoProprietarioController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados do Proprietario");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			NovoProprietarioController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setProprietario(proprietario, db);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showNovoImovel(Imovel imovel, Endereco endereco) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/NovoImovel.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\NovoImovel.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new NovoImovelController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados do Novo Imóvel");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			NovoImovelController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setImovel(imovel, endereco);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showNovoContrato(Contrato contrato) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/NovoContrato.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\NovoContrato.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new NovoContratoController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Novo Contrato");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			NovoContratoController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setContrato(contrato);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showEditObs(Cliente cliente) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/EditObs.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\EditObs.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new EditObsController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar Observação");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			EditObsController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCliente(cliente);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showEditObs(Proprietario proprietario) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/EditObs.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\EditObs.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new EditObsController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar Observação");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			EditObsController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setProprietario(proprietario);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showConfiguraPagamento(Duplicata duplicata) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/ConfiguraPagamento.fxml"));
			// //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\ConfiguraPagamento.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new ConfiguraPagamentoController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Configurar Pagamento");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			ConfiguraPagamentoController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setDuplicata(duplicata);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean showConfiguraPagamentoDespesa(Despesa despesa) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/ConfiguraPagamentoDespesa.fxml"));
			// //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\ConfiguraPagamentoDespesa.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new ConfiguraPagamentoDespesaController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Configurar Pagamento");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			ConfiguraPagamentoDespesaController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setDespesa(despesa);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean showCadastrarDespesa(Despesa despesa) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/ConfiguraPagamentoDespesa.fxml"));
			// //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\Despesas.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new CadastrarDespesaController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Configurar Despesa");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			CadastrarDespesaController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setDespesa(despesa);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean showConfiguraPagamentoReceita(Receita receita) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/ConfiguraPagamentoReceita.fxml"));
			// //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\ConfiguraPagamentoReceita.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new ConfiguraPagamentoReceitaController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Configurar Pagamento");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			ConfiguraPagamentoReceitaController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setReceita(receita);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean showCadastrarReceita(Receita receita) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/ConfiguraPagamentoReceita.fxml"));
			// //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\Receitas.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new CadastrarReceitaController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Configurar Receita");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			CadastrarReceitaController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setReceita(receita);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showEditRef(Cliente cliente, Referencia selectedReferencia) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/EditRef.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\EditRef.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new EditRefController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar Referência");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			EditRefController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setClienteReferencia(cliente, selectedReferencia);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean showNovaCategoria(Categoria categoria) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/NovaCategoria.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\NovaCategoria.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new NovaCategoriaController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Nova Categoria");
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			NovaCategoriaController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCategoria(categoria);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showChecklist(ImovelChecklistDTO checklist) {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/Checklist.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\Checklist.fxml").toUri()).toUri().toURL()); // BUILD
			loader.setController(new ChecklistController());
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Checklist");

			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UNIFIED);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			ChecklistController controller = loader.getController();
			page.setOnKeyPressed(controller::handleOnKeyPressed);
			controller.setDialogStage(dialogStage);
			controller.setChecklist(checklist);
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args1) {
		launch(args1);
	}

}