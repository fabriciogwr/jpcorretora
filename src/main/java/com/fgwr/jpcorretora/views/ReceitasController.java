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
import com.fgwr.jpcorretora.domain.Receita;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.enums.Meses;
import com.fgwr.jpcorretora.repositories.ReceitaRepository;
import com.fgwr.jpcorretora.services.ReceitasMensalGen;
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
public class ReceitasController {

	@FXML
	private TableView<Receita> receitasTable;
	@FXML
	private TableColumn<Receita, String> catCol;
	@FXML
	private TableColumn<Receita, String> descCol;

	@FXML
	private TableColumn<Receita, String> pagCol;
	@FXML
	private TableColumn<Receita, String> vencCol;
	@FXML
	private TableColumn<Receita, String> pgtoCol;
	@FXML
	private TableColumn<Receita, String> valorCol;
	@FXML
	private TableColumn<Receita, String> sitCol;
	@FXML
	private ComboBox<Meses> mesBox;
	@FXML
	private ComboBox<Integer> anoBox;
	@FXML
	private Label recebidasLabel;
	@FXML
	private Label receberLabel;
	
	Double recebidas = 0.0;
	Double receber = 0.0;

	Calendar cal = Calendar.getInstance();
	FrontApp frontApp = new FrontApp();
	ApplicationContext context = SpringContext.getAppContext();

	private List<Receita> getReceitaData() {
		ReceitaRepository desRepo = (ReceitaRepository) context.getBean("receitaRepository");
		List<Receita> receitas = desRepo.findAll();

		return receitas;
	}

	@FXML
	private void initialize() {

		receitasTable.setItems(FXCollections.observableArrayList(getReceitaData()));
		catCol.setCellValueFactory(cellData -> cellData.getValue().categoria());
		descCol.setCellValueFactory(cellData -> cellData.getValue().descricao());
		pagCol.setCellValueFactory(cellData -> cellData.getValue().pagador());
		vencCol.setCellValueFactory(cellData -> cellData.getValue().vencimento());
		pgtoCol.setCellValueFactory(cellData -> cellData.getValue().pagamento());
		valorCol.setCellValueFactory(cellData -> cellData.getValue().valor());
		sitCol.setCellValueFactory(cellData -> cellData.getValue().estado());
		mesBox.setItems(FXCollections.observableArrayList(List.of(Meses.values())));

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
		ReceitaRepository desRepo = (ReceitaRepository) context.getBean("receitaRepository");
		receitasTable.getItems().clear();
		int year;
		Date start;
		Date end;
		List<Receita> despsPgto = new ArrayList<>();
		List<Receita> despsAll = new ArrayList<>();
		cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, mes);
		year = anoBox.getValue();
		cal.set(year, cal.get(Calendar.MONTH), cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		start = cal.getTime();
		cal.set(year, cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		end = cal.getTime();
		despsPgto.clear();
		despsAll.clear();
		despsPgto = desRepo.findAllByDataRecebimentoBetween(start, end);
		despsPgto.addAll(desRepo.findAllByDataVencimentoBetween(start, end));
		despsAll = despsPgto.stream().distinct().collect(Collectors.toList());
		receitasTable.setItems(FXCollections.observableArrayList(despsAll));
		
		for (Receita receita : despsAll) {
			recebidas = (receita.getEstado() == EstadoPagamento.QUITADO) ? recebidas + receita.getValor() : recebidas + 0;
			receber = (receita.getEstado() == EstadoPagamento.PENDENTE) ? receber + receita.getValor() : receber + 0;
		}
		
		recebidasLabel.setText(StringsUtils.formatarRealCifra(recebidas));
		receberLabel.setText(StringsUtils.formatarRealCifra(receber));
		

	}

	@FXML
	void handleExcluiRecebimento() {
		ReceitaRepository desRepo = (ReceitaRepository) context.getBean("receitaRepository");
		Receita selectedReceita = receitasTable.getSelectionModel().getSelectedItem();

		if (selectedReceita != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Excluir esta Receita cadastrada?");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {

				desRepo.delete(selectedReceita);

			}
			receitasTable.getItems().remove(selectedReceita);

			receitasTable.refresh();
		}
	}

	@FXML
	void handleRecebimento() {
		Receita selectedReceita = receitasTable.getSelectionModel().getSelectedItem();

		if (selectedReceita != null && selectedReceita.getEstado() == EstadoPagamento.PENDENTE) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Confirmar pagamento");
			alert.setHeaderText("Confirmar pagamento da receita selecionada?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {

				boolean okClicked = frontApp.showConfiguraPagamentoReceita(selectedReceita);
				if (okClicked) {
					selectedReceita.setEstado(EstadoPagamento.QUITADO);

				}

			}

		} else {
			Alert alert3 = new Alert(AlertType.WARNING);
			alert3.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane3 = alert3.getDialogPane();
			dialogPane3.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert3.setTitle("Receita já paga");
			alert3.setHeaderText("A receita selecionada já está paga.");
			alert3.showAndWait();
		}

	}

	@FXML
	void handleNovaReceita() {
		Receita receita = new Receita();
		boolean okClicked = frontApp.showCadastrarReceita(receita);
		if (okClicked) {
			receitasTable.getItems().add(receita);
			receitasTable.refresh();
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
		
		List<Receita> relatorio = receitasTable.getItems();
		
		ReceitasMensalGen rmg = new ReceitasMensalGen();
		try {
			rmg.geraRelatorioReceita(relatorio, path, cal.get(Calendar.MONTH), recebidas, receber);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
