package com.fgwr.jpcorretora;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Endereco;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Proprietario;
import com.fgwr.jpcorretora.domain.Referencia;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.services.ClienteService;
import com.fgwr.jpcorretora.services.ImovelService;
import com.fgwr.jpcorretora.views.ClienteController;
import com.fgwr.jpcorretora.views.EditClienteController;
import com.fgwr.jpcorretora.views.EditObsController;
import com.fgwr.jpcorretora.views.EditRefController;
import com.fgwr.jpcorretora.views.ImovelController;
import com.fgwr.jpcorretora.views.NovoClienteController;
import com.fgwr.jpcorretora.views.NovoContratoController;
import com.fgwr.jpcorretora.views.NovoImovelController;
import com.fgwr.jpcorretora.views.NovoProprietarioController;
import com.fgwr.jpcorretora.views.RootController;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
	private BorderPane rootLayout;

	@Autowired
	private ConfigurableApplicationContext applicationContext;

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

		initRootLayout();

		showTelaClientes();
	}

	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(FrontApp.class.getResource("views/RootLayout.fxml"));
			loader.setController(new RootController());
			rootLayout = (BorderPane) loader.load();

			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showTelaClientes() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(FrontApp.class.getResource("views/TelaClientes.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			rootLayout.setCenter(personOverview);
			
			ClienteController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showTelaImoveis() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(FrontApp.class.getResource("views/TelaImoveis.fxml"));
			AnchorPane imovelOverview = (AnchorPane) loader.load();

			rootLayout.setCenter(imovelOverview);
			
			ImovelController controller = loader.getController();
			controller.setMainApp(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean showEditCliente(Cliente cliente) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/EditCliente.fxml"));
	        loader.setController(new EditClienteController());
	        AnchorPane page = (AnchorPane) loader.load();

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Editar Cliente");
	        dialogStage.initModality(Modality.NONE);
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
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/EditCliente.fxml"));
	        loader.setController(new NovoClienteController());
	        AnchorPane page = (AnchorPane) loader.load();

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Novo Cliente");
	        dialogStage.initModality(Modality.NONE);
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
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/NovoProprietario.fxml"));
	        loader.setController(new NovoProprietarioController());
	        AnchorPane page = (AnchorPane) loader.load();

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Dados do Proprietario");
	        dialogStage.initModality(Modality.NONE);
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
	
	public boolean showNovoImovel(Imovel imovel , Endereco endereco) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/NovoImovel.fxml"));
	        loader.setController(new NovoImovelController());
	        AnchorPane page = (AnchorPane) loader.load();

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Dados do Novo Imóvel");
	        dialogStage.initModality(Modality.NONE);
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
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/NovoContrato.fxml"));
	        loader.setController(new NovoContratoController());
	        AnchorPane page = (AnchorPane) loader.load();

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Novo Contrato");
	        dialogStage.initModality(Modality.NONE);
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
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/EditObs.fxml"));
	        loader.setController(new EditObsController());
	        AnchorPane page = (AnchorPane) loader.load();

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Editar Observação");
	        dialogStage.initModality(Modality.NONE);
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
	
	public boolean showEditRef(Cliente cliente, Referencia selectedReferencia) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/EditRef.fxml"));
	        loader.setController(new EditRefController());
	        AnchorPane page = (AnchorPane) loader.load();

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Editar Referência");
	        dialogStage.initModality(Modality.NONE);
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
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args1) {
		launch(args1);
	}

}