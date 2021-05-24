package com.fgwr.jpcorretora.views;
import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Categoria;
import com.fgwr.jpcorretora.enums.TipoMovimento;
import com.fgwr.jpcorretora.repositories.CategoriaRepository;
import com.fgwr.jpcorretora.utils.FilesUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CategoriaDespesaController {

    @FXML
    private TableView<Categoria> catTable;

    @FXML
    private TableColumn<Categoria, String> codCol;

    @FXML
    private TableColumn<Categoria, String> catCol;

    @FXML
    private TableColumn<Categoria, String> descCol;

    FrontApp frontApp = new FrontApp();

	CategoriaRepository catRepo = (CategoriaRepository) SpringContext.getAppContext().getBean("categoriaRepository");

	public List<Categoria> getCatData() {
		
		List<Categoria> allCat = catRepo.findByTipo(TipoMovimento.DESPESA.getCod());
		return allCat;
	}
    
    @FXML
    public void initialize() {
    	
    	catTable.setItems(FXCollections.observableArrayList(getCatData()));
    	codCol.setCellValueFactory(cellData -> cellData.getValue().cod());
    	catCol.setCellValueFactory(cellData -> cellData.getValue().nome());
    	descCol.setCellValueFactory(cellData -> cellData.getValue().descricao());
    	
    }
    
    @FXML
    void excluiCategoria() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
		alert.setTitle("Exclusão de Categoria");
		alert.setHeaderText("Confirmar Exclusão da categoria selecionada?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
    	catRepo.delete(catTable.getSelectionModel().getSelectedItem());
		}
    }

    @FXML
    void novaCategoriaDespesa() {
    	Categoria cat = new Categoria(null, TipoMovimento.DESPESA, null, null);
    	boolean okClicked = frontApp.showNovaCategoria(cat);
    	if (okClicked) {
    		catTable.getItems().add(cat);
    		catTable.refresh();
    	}
    	

    }
   

}
