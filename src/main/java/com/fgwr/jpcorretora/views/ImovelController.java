package com.fgwr.jpcorretora.views;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Checklist;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.services.ImovelService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ImovelController {
	
	private ObservableList<Imovel> imovelData = FXCollections.observableArrayList();
	
	private ObservableList<Checklist> imovelAux = FXCollections.observableArrayList();
	
	
	@FXML
	private TableView<Imovel> imovelTable;
	@FXML
	private TableColumn<Imovel, String> codColumn;
	@FXML
	private TableColumn<Imovel, String> proprietarioColumn;
	
	@FXML
	private Label logradouroLabel;
	@FXML
	private Label numeroLabel;
	@FXML
	private Label cepLabel;
	@FXML
	private Label bairroLabel;
	@FXML
	private Label proprietarioLabel;
	@FXML
	private Label dataAngariacaoLabel;
	
	@FXML
	private TableView<Checklist> checklistTable;
	@FXML
	private TableColumn<Checklist, String> itemColumn;
	@FXML
	private TableColumn<Checklist, String> defeitoColumn;
	
	
	
	
	FrontApp frontApp = new FrontApp();
	
	public ObservableList<Imovel> getImovelData() {
		ApplicationContext context = SpringContext.getAppContext();
    	ImovelService imserv = (ImovelService)context.getBean("imovelService");
		
		List<Imovel> allimv = imserv.findAll();
		for (Imovel imovel : allimv) {
			imovelData.add(imovel);
		} return imovelData;
	}
	
	public ObservableList<Checklist> getImovelChecklist(Imovel imv) {
		ApplicationContext context = SpringContext.getAppContext();
    	ImovelService imserv = (ImovelService)context.getBean("imovelService");
    	
		List<Checklist> chklst = imserv.getChecklist(imv);
		for (Checklist checklist : chklst) {
			imovelAux.add(checklist);
		} return imovelAux;
	}
	
	@FXML
	private void initialize() {
		imovelData = getImovelData();
		imovelTable.setItems(imovelData);
		proprietarioColumn.setCellValueFactory(cellData -> cellData.getValue().proprietario());
		codColumn.setCellValueFactory(cellData -> cellData.getValue().cod());
		showImovel(null);
		
		imovelTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showImovel(newValue));
		
		}
	
	private void showImovel(Imovel imovel) {
		if (imovel != null) {
			logradouroLabel.setText(imovel.getEndereco().getLogradouro());
			numeroLabel.setText(imovel.getEndereco().getNumero());
			bairroLabel.setText(imovel.getEndereco().getBairro());
			cepLabel.setText(imovel.getEndereco().getCep());
			dataAngariacaoLabel.setText(imovel.getDataAngariacaoString());
		//	imovelTable.refresh();
			imovelAux.clear();
			imovelAux = getImovelChecklist(imovel);
			checklistTable.setItems(imovelAux);
			
			itemColumn.setCellValueFactory(cellData -> cellData.getValue().item());
			defeitoColumn.setCellValueFactory(cellData -> cellData.getValue().status());
			checklistTable.refresh();
			
			
		}else {
			logradouroLabel.setText("");
			numeroLabel.setText("");
			bairroLabel.setText("");
			cepLabel.setText("");
			dataAngariacaoLabel.setText("");
			
		}
	}
	public void setMainApp(FrontApp frontApp) {
        this.frontApp = frontApp;
	}
}
