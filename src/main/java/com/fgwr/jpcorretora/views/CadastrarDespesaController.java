package com.fgwr.jpcorretora.views;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Categoria;
import com.fgwr.jpcorretora.domain.Despesa;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.enums.MeioPagamento;
import com.fgwr.jpcorretora.enums.TipoMovimento;
import com.fgwr.jpcorretora.repositories.CategoriaRepository;
import com.fgwr.jpcorretora.repositories.DespesaRepository;
import com.fgwr.jpcorretora.services.ReciboPdfGen;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CadastrarDespesaController {
	
	FrontApp frontApp = new FrontApp();
	ReciboPdfGen reciboPdfGen = new ReciboPdfGen();
	Calendar cal = Calendar.getInstance();
	private Stage dialogStage;
	private boolean okClicked = false;
	private Despesa despesa = new Despesa();
	
	private TipoMovimento tipo;
	
	@FXML
    private TextField recebedorField;

    @FXML
    private TextField valorField;

    @FXML
    private TextField descField;

    @FXML
    private DatePicker vencimentoField;

    @FXML
    private CheckBox pagarHojeChk;

    @FXML
    private ComboBox<MeioPagamento> meioPgtoBox;

    @FXML
    private ComboBox<Categoria> categoriaBox;

    @FXML
    private CheckBox geraReciboChk;
    
    
	String dir = System.getProperty("user.dir");

	CategoriaRepository catRepo = (CategoriaRepository) SpringContext.getAppContext().getBean("categoriaRepository");
	DespesaRepository despRepo = (DespesaRepository) SpringContext.getAppContext().getBean("despesaRepository");

	public List<Categoria> getCatData() {
		
		List<Categoria> allCat = catRepo.findAll();
		return allCat;
	}
	
	private void callBackCategoria() {
		List<Categoria> cats = new ArrayList<>();
		
		cats = getCatData();
		categoriaBox.getItems().clear();
		categoriaBox.setItems(FXCollections.observableArrayList(cats));
		Callback<ListView<Categoria>, ListCell<Categoria>> categoriaCellFactory = new Callback<ListView<Categoria>, ListCell<Categoria>>() {

			@Override
			public ListCell<Categoria> call(ListView<Categoria> l) {
				return new ListCell<Categoria>() {

					@Override
					protected void updateItem(Categoria categoria, boolean empty) {
						super.updateItem(categoria, empty);
						if (categoria == null || empty) {
							setGraphic(null);
						} else {
							setText(categoria.getDescricao());
						}
					}
				};
			}

		};
		categoriaBox.setButtonCell(categoriaCellFactory.call(null));
		categoriaBox.setCellFactory(categoriaCellFactory);
	}
    @FXML
	private void initialize() {

		Callback<ListView<MeioPagamento>, ListCell<MeioPagamento>> meioPagamentoCellFactory = new Callback<ListView<MeioPagamento>, ListCell<MeioPagamento>>() {

			@Override
			public ListCell<MeioPagamento> call(ListView<MeioPagamento> l) {
				return new ListCell<MeioPagamento>() {

					@Override
					protected void updateItem(MeioPagamento meioPagamento, boolean empty) {
						super.updateItem(meioPagamento, empty);
						if (meioPagamento == null || empty) {
							setGraphic(null);
						} else {
							setText(meioPagamento.getCod() + " - " + meioPagamento.getDescricao());
						}
					}
				};
			}

		};
		meioPgtoBox.setButtonCell(meioPagamentoCellFactory.call(null));
		meioPgtoBox.setCellFactory(meioPagamentoCellFactory);
		meioPgtoBox.setItems(FXCollections.observableArrayList(MeioPagamento.values()));
		
		callBackCategoria();
		

	}
    
    public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
    
    public void setDespesa(Despesa despesa) {
    	this.despesa = despesa;
    }
    
    public boolean isOkClicked() {
		return okClicked;
	}

	public void handleOk() {
		
		if (pagarHojeChk.isSelected()) {
			despesa.setDataPagamento(cal.getTime());
			despesa.setEstado(EstadoPagamento.QUITADO);
			despesa.setValorPago(Double.parseDouble(valorField.getText()));
		} else {
			despesa.setEstado(EstadoPagamento.PENDENTE);
			
		}
		
		if (vencimentoField.getEditor().getText().isBlank()) {
			despesa.setDataVencimento(
					Date.from(vencimentoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		} else if (!vencimentoField.getEditor().getText().isBlank()) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
	            Date date = formatter.parse(vencimentoField.getEditor().getText());
	            despesa.setDataVencimento(date);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
		}
		
		despesa.setDescricao(descField.getText());
		
		despesa.setMeioPagamento(meioPgtoBox.getValue());
		despesa.setValor(Double.parseDouble(valorField.getText()));
		despesa.setCategoria(categoriaBox.getValue());
		despesa.setRecebedor(recebedorField.getText());
		
		
		
		
		despRepo.save(despesa); 
		
		okClicked=true;
		dialogStage.close();
		
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	@FXML
	private void handleCategoriasDespesa() {
		try {
			// FXMLLoader loader = new
			// FXMLLoader(FrontApp.class.getResource("views/TelaClientes.fxml")); //DEV
			FXMLLoader loader = new FXMLLoader(
					Paths.get(Paths.get(dir + "\\fxml\\CategoriasDespesa.fxml").toUri()).toUri().toURL()); // BUILD			
			AnchorPane catDespOverview = (AnchorPane) loader.load();

			
			Stage thrStage = new Stage();
			Scene scene3 = new Scene(catDespOverview);
			thrStage.setScene(scene3);
			thrStage.initStyle(StageStyle.UNIFIED);
			thrStage.initModality(Modality.APPLICATION_MODAL);
			thrStage.show();
			
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
