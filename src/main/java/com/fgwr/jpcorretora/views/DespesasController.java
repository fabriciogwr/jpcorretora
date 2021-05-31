package com.fgwr.jpcorretora.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.filechooser.FileSystemView;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Despesa;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.enums.Meses;
import com.fgwr.jpcorretora.repositories.DespesaRepository;
import com.fgwr.jpcorretora.services.DespesasMensalGen;
import com.fgwr.jpcorretora.utils.FilesUtils;
import com.fgwr.jpcorretora.utils.StringsUtils;
import com.ibm.icu.util.Calendar;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DespesasController {

    @FXML
    private TableView<Despesa> despesasTable;
    @FXML
    private TableColumn<Despesa, String> catCol;
    @FXML
    private TableColumn<Despesa, String> descCol;
   
    @FXML
    private TableColumn<Despesa, String> recebCol;
    @FXML
    private TableColumn<Despesa, String> vencCol;
    @FXML
    private TableColumn<Despesa, String> pgtoCol;
    @FXML
    private TableColumn<Despesa, String> valorCol;
    @FXML
    private TableColumn<Despesa, String> sitCol;
    @FXML
    private ComboBox<Meses> mesBox;
    @FXML
    private ComboBox<Integer> anoBox;
    @FXML
    private Label pagarLabel;
    @FXML
    private Label pagasLabel;
    
    Double pagas = 0.0;
	Double pagar = 0.0;
    
    Calendar cal = Calendar.getInstance();
    FrontApp frontApp = new FrontApp();
	ApplicationContext context = SpringContext.getAppContext();
    
	private List<Despesa> getDespesaData() {
		DespesaRepository desRepo = (DespesaRepository) context.getBean("despesaRepository");
		List<Despesa> despesas = desRepo.findAll();

		return despesas;
	}

	@FXML
	private void initialize() {

		despesasTable.setItems(FXCollections.observableArrayList(getDespesaData()));
		catCol.setCellValueFactory(cellData -> cellData.getValue().categoria());
		descCol.setCellValueFactory(cellData -> cellData.getValue().descricao());
		recebCol.setCellValueFactory(cellData -> cellData.getValue().recebedor());
		vencCol.setCellValueFactory(cellData -> cellData.getValue().vencimento());
		pgtoCol.setCellValueFactory(cellData -> cellData.getValue().pagamento());
		valorCol.setCellValueFactory(cellData -> cellData.getValue().valor());
		sitCol.setCellValueFactory(cellData -> cellData.getValue().estado());
		mesBox.setItems(FXCollections.observableArrayList(List.of(Meses.values())));
		anoBox.setItems(FXCollections.observableArrayList(List.of(2021)));
		anoBox.setValue(2021);
		Callback<ListView<Meses>, ListCell<Meses>> mesesCellFactory = new Callback<ListView<Meses>, ListCell<Meses>>() {

			@Override
			public ListCell<Meses> call(ListView<Meses> l) {
				return new ListCell<Meses>() {

					@Override
					protected void updateItem(Meses meses, boolean empty) {
						super.updateItem(meses, empty);
						if (meses == null || empty) {
							setGraphic(null);
						} else {
							setText((meses.getCod() + 1) + " - " + meses.getDescricao());
						}
					}
				};
			}

		};
		mesBox.setButtonCell(mesesCellFactory.call(null));
		mesBox.setCellFactory(mesesCellFactory);

		anoBox.setItems(FXCollections.observableArrayList(List.of(2021)));
		anoBox.setValue(2021);
		

		mesBox.valueProperty().addListener((observable, oldValue, newValue) -> listaReceitaMes(newValue.getCod()));
		mesBox.setValue(Meses.toEnum(cal.get(Calendar.MONTH)));
	}

	void listaReceitaMes(Integer mes) {
		pagas = 0.0;
		pagar = 0.0;
		DespesaRepository desRepo = (DespesaRepository) context.getBean("despesaRepository");
		despesasTable.getItems().clear();
		int year;
		Date start;
		Date end;
		List<Despesa> despsPgto = new ArrayList<>();
		List<Despesa> despsAll = new ArrayList<>();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, mes);
		year = anoBox.getValue();
		cal.set(year, cal.get(Calendar.MONTH), cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		start = cal.getTime();
		cal.set(year, cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		end = cal.getTime();
		despsPgto.clear();
		despsAll.clear();
		despsPgto = desRepo.findAllByDataPagamentoBetween(start, end);
		despsPgto.addAll(desRepo.findAllByDataVencimentoBetween(start, end));
		despsAll = despsPgto.stream().distinct().collect(Collectors.toList());
		despesasTable.setItems(FXCollections.observableArrayList(despsAll));
		
		for (Despesa despesa : despsAll) {
			pagas = (despesa.getEstado() == EstadoPagamento.QUITADO) ? pagas + despesa.getValor() : pagas + 0;
			pagar = (despesa.getEstado() == EstadoPagamento.PENDENTE) ? pagar + despesa.getValor() : pagar + 0;
		}
		
		pagasLabel.setText(StringsUtils.formatarRealCifra(pagas));
		pagarLabel.setText(StringsUtils.formatarRealCifra(pagar));
		
	}
    @FXML
    void handleCancelaPagamento() {
    	DespesaRepository desRepo = (DespesaRepository) context.getBean("despesaRepository");
		Despesa selectedDespesa = despesasTable.getSelectionModel().getSelectedItem();
		
		if (selectedDespesa != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Excluir esta Despesa cadastrada?");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK) {
				
				desRepo.delete(selectedDespesa);

			}
			despesasTable.getItems().remove(selectedDespesa);
			

			despesasTable.refresh();
		}
    }

    @FXML
    void handlePagamento() {
    	Despesa selectedDespesa = despesasTable.getSelectionModel().getSelectedItem();
    	

		if (selectedDespesa != null && selectedDespesa.getEstado() == EstadoPagamento.PENDENTE) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Confirmar pagamento");
			alert.setHeaderText("Confirmar pagamento da despesa selecionada?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				
				boolean okClicked = frontApp.showConfiguraPagamentoDespesa(selectedDespesa);
				if (okClicked) {
					selectedDespesa.setEstado(EstadoPagamento.QUITADO);
			//		despesasTable.getItems().remove(selectedDespesa);
			//		despesasTable.getItems().add(selectedDespesa);
					despesasTable.refresh();
					
					
					

				}

			}

		} else {
			Alert alert3 = new Alert(AlertType.WARNING);
			alert3.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane3 = alert3.getDialogPane();
			dialogPane3.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert3.setTitle("Despesa já paga");
			alert3.setHeaderText("A despesa selecionada já está paga.");
			alert3.showAndWait();
		}
    	

    }

    @FXML
    void handleNovaDespesa() {
    	Despesa despesa = new Despesa();
    	boolean okClicked = frontApp.showCadastrarDespesa(despesa);
    	if (okClicked) {
    		despesasTable.getItems().add(despesa);
    		despesasTable.refresh();
    		//workaroud temporário, corrigir somas em despesas e receitas
    		listaReceitaMes(mesBox.getValue().getCod());
    	}
    }
    
    @FXML
	void handleGeraRelatorio() {
		File selectedRestoreDirectory = null;
		Stage primaryStage = null;
		String docFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivo PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Salvar relatório");
		fileChooser.setInitialDirectory(new File(docFolder));
		selectedRestoreDirectory = fileChooser.showSaveDialog(primaryStage);
		String path = selectedRestoreDirectory.getAbsolutePath();
		
		List<Despesa> relatorio = despesasTable.getItems();
		
		DespesasMensalGen dmg = new DespesasMensalGen();
		try {
			dmg.geraRelatorioDespesa(relatorio, path, cal.get(Calendar.MONTH), pagas, pagar);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
