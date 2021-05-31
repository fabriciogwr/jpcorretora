package com.fgwr.jpcorretora.views;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Categoria;
import com.fgwr.jpcorretora.enums.TipoMovimento;
import com.fgwr.jpcorretora.repositories.CategoriaRepository;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class NovaCategoriaController {

	private Stage dialogStage;
	private boolean okClicked = false;
	private Categoria categoria;
	
	FrontApp frontApp = new FrontApp();
	ApplicationContext context = SpringContext.getAppContext();
	
	@FXML
	private TipoMovimento tipo;
    @FXML
    private TextField catField;
    @FXML
    private TextField descField;

    
    public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	
	
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}


    @FXML
    private void handleOk() {
    	CategoriaRepository catRepo = (CategoriaRepository)context.getBean("categoriaRepository");
    	categoria.setNome(catField.getText().trim());
    	categoria.setDescricao(descField.getText().trim());
    	categoria = catRepo.save(categoria);
    	okClicked = true;
    	dialogStage.close();

    }

}
