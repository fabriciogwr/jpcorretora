package com.fgwr.jpcorretora.views;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Checklist;
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Endereco;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Proprietario;
import com.fgwr.jpcorretora.dto.ImovelChecklistDTO;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.repositories.ProprietarioRepository;
import com.fgwr.jpcorretora.services.ImovelService;
import com.fgwr.jpcorretora.utils.FileUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ImovelController {
	
	ApplicationContext context = SpringContext.getAppContext();
	
	private ObservableList<Imovel> imovelData = FXCollections.observableArrayList();
	
	private Stage stage;
	@FXML
	private GridPane checklistPane;
	
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
	@FXML
	private AnchorPane fotoAnchorPane;
	@FXML
	private GridPane fotoGridPane;
	
	@FXML
	private DatePicker dataLaudo;

	@FXML
	private CheckBox danoSalaCheck;
	@FXML
	private CheckBox danoCozinhaCheck;
	@FXML
	private CheckBox danoQuartoCheck;
	@FXML
	private CheckBox danoBanheiroCheck;
	@FXML
	private CheckBox danoDispensaCheck;
	@FXML
	private CheckBox danoAreaServicoCheck;
	@FXML
	private CheckBox danoGaragemCheck;
	@FXML
	private CheckBox danoTomadasCheck;
	@FXML
	private CheckBox danoLampadasCheck;
	@FXML
	private CheckBox danoChuveiroCheck;
	@FXML
	private CheckBox danoFechadurasCheck;
	@FXML
	private CheckBox danoChavesCheck;
	@FXML
	private CheckBox danoPortaoEletroCheck;
	@FXML
	private CheckBox danoCercaEletricaCheck;
	@FXML
	private CheckBox danoControleCheck;
	@FXML
	private CheckBox danoInfiltracaoCheck;
	@FXML
	private CheckBox danoPiaCheck;
	@FXML
	private CheckBox danoVasoSanitarioCheck;
	@FXML
	private CheckBox danoBoxCheck;
	@FXML
	private CheckBox danoArCondicionadoCheck;
	@FXML
	private CheckBox danoMoveisVinculadosCheck;
	@FXML
	private CheckBox danoPortasCheck;
	@FXML
	private CheckBox danoJanelasCheck;
	@FXML
	private CheckBox danoPortaoCheck;
	@FXML
	private CheckBox danoPinturaInternaCheck;
	@FXML
	private CheckBox danoPinturaExternaCheck;
	@FXML
	private TextArea descricaoField;
	@FXML
	private TextArea obsField;

	
	Desktop desktop = Desktop.getDesktop();
	FileChooser fc = new FileChooser();
	FrontApp frontApp = new FrontApp();

	
	
	public ObservableList<Imovel> getImovelData() {
		ApplicationContext context = SpringContext.getAppContext();
    	ImovelService imserv = (ImovelService)context.getBean("imovelService");
		
		List<Imovel> allimv = imserv.findAll();
		for (Imovel imovel : allimv) {
			imovelData.add(imovel);
		} return imovelData;
	}
	
	@FXML
	private void initialize() throws IOException {
		imovelData = getImovelData();
		imovelTable.setItems(imovelData);
		proprietarioColumn.setCellValueFactory(cellData -> cellData.getValue().proprietario());
		codColumn.setCellValueFactory(cellData -> cellData.getValue().cod());
		showImovel(null);
		
		imovelTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					try {
						showImovel(newValue);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
		
		EventHandler<MouseEvent> handler = MouseEvent::consume;

		// block events
		checklistPane.addEventFilter(MouseEvent.ANY, handler);
		
		
		}
	
	private void showImovel(Imovel imovel) throws IOException {
		if (imovel != null) {
			logradouroLabel.setText(imovel.getEndereco().getLogradouro());
			numeroLabel.setText(imovel.getEndereco().getNumero());
			bairroLabel.setText(imovel.getEndereco().getBairro());
			cepLabel.setText(imovel.getEndereco().getCep());
			dataAngariacaoLabel.setText(imovel.getDataAngariacaoString());

			obsField.setText(readObs(imovel.getId()));
			obsField.setEditable(false);

			dataLaudo.setValue(Instant.ofEpochMilli(imovel.getDataLaudo().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
			danoArCondicionadoCheck.setSelected(imovel.isDanoArCondicionado());
			danoAreaServicoCheck.setSelected(imovel.isDanoAreaServico());
			danoBanheiroCheck.setSelected(imovel.isDanoBanheiro());
			danoBoxCheck.setSelected(imovel.isDanoBox());
			danoCercaEletricaCheck.setSelected(imovel.isDanoCercaEletrica());
			danoChavesCheck.setSelected(imovel.isDanoChaves());
			danoChuveiroCheck.setSelected(imovel.isDanoChuveiro());
			danoControleCheck.setSelected(imovel.isDanoControle());
			danoCozinhaCheck.setSelected(imovel.isDanoCozinha());
			danoDispensaCheck.setSelected(imovel.isDanoDispensa());
			danoFechadurasCheck.setSelected(imovel.isDanoFechaduras());
			danoGaragemCheck.setSelected(imovel.isDanoGaragem());
			danoInfiltracaoCheck.setSelected(imovel.isDanoInfiltracao());
			danoJanelasCheck.setSelected(imovel.isDanoJanelas());
			danoLampadasCheck.setSelected(imovel.isDanoLampadas());
			danoMoveisVinculadosCheck.setSelected(imovel.isDanoMoveisVinculados());
			danoPiaCheck.setSelected(imovel.isDanoPia());
			danoPinturaExternaCheck.setSelected(imovel.isDanoPinturaExterna());
			danoPinturaInternaCheck.setSelected(imovel.isDanoPinturaInterna());
			danoPortaoCheck.setSelected(imovel.isDanoPortao());
			danoPortaoEletroCheck.setSelected(imovel.isDanoPortaoEletro());
			danoPortasCheck.setSelected(imovel.isDanoPortas());
			danoQuartoCheck.setSelected(imovel.isDanoQuarto());
			danoSalaCheck.setSelected(imovel.isDanoSala());
			danoTomadasCheck.setSelected(imovel.isDanoTomadas());
			danoVasoSanitarioCheck.setSelected(imovel.isDanoVasoSanitario());
		
			
			descricaoField.setText(readDescricao(imovel.getId()));
			descricaoField.setEditable(false);
			
			
			fotoGridPane = new GridPane();
			AnchorPane.setTopAnchor(fotoGridPane, 10d);
			AnchorPane.setLeftAnchor(fotoGridPane, 10d);
			AnchorPane.setRightAnchor(fotoGridPane, 10d);
			
			
		}else {
			logradouroLabel.setText("");
			numeroLabel.setText("");
			bairroLabel.setText("");
			cepLabel.setText("");
			dataAngariacaoLabel.setText("");
			
		}
	}
	
	@FXML
	private void handleEditImovel() throws IOException {
		Imovel selectedImovel = imovelTable.getSelectionModel().getSelectedItem();
		Endereco selectedEndereco = imovelTable.getSelectionModel().getSelectedItem().getEndereco();
		
		if (selectedImovel != null) {
			boolean okClicked = frontApp.showNovoImovel(selectedImovel, selectedEndereco);
			if (okClicked) {
				showImovel(selectedImovel);
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initStyle(StageStyle.UNDECORATED);
			DialogPane dialogPane = alert.getDialogPane();			
			dialogPane.getStylesheets().add(FileUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Nenhuma seleção");
			alert.setHeaderText("Nenhum Imóvel Selecionado");
			alert.setContentText("Por favor, selecione um imóvel na tabela.");
			alert.showAndWait();
		}
	}
	
	@FXML
	private void handleChecklist() throws IOException {
		Imovel selectedImovel = imovelTable.getSelectionModel().getSelectedItem();
		ImovelChecklistDTO checklist = new ImovelChecklistDTO();
		
		checklist.setDanoArCondicionado(selectedImovel.isDanoArCondicionado());
		checklist.setDanoAreaServico(selectedImovel.isDanoAreaServico());
		checklist.setDanoBanheiro(selectedImovel.isDanoBanheiro());
		checklist.setDanoBox(selectedImovel.isDanoBox());
		checklist.setDanoCercaEletrica(selectedImovel.isDanoCercaEletrica());
		checklist.setDanoChaves(selectedImovel.isDanoChaves());
		checklist.setDanoChuveiro(selectedImovel.isDanoChuveiro());
		checklist.setDanoControle(selectedImovel.isDanoControle());
		checklist.setDanoCozinha(selectedImovel.isDanoCozinha());
		checklist.setDanoDispensa(selectedImovel.isDanoDispensa());
		checklist.setDanoFechaduras(selectedImovel.isDanoFechaduras());
		checklist.setDanoGaragem(selectedImovel.isDanoGaragem());
		checklist.setDanoInfiltracao(selectedImovel.isDanoInfiltracao());
		checklist.setDanoJanelas(selectedImovel.isDanoJanelas());
		checklist.setDanoLampadas(selectedImovel.isDanoLampadas());
		checklist.setDanoMoveisVinculados(selectedImovel.isDanoMoveisVinculados());
		checklist.setDanoPia(selectedImovel.isDanoPia());
		checklist.setDanoPinturaExterna(selectedImovel.isDanoPinturaExterna());
		checklist.setDanoPinturaInterna(selectedImovel.isDanoPinturaInterna());
		checklist.setDanoPortao(selectedImovel.isDanoPortao());
		checklist.setDanoPortaoEletro(selectedImovel.isDanoPortaoEletro());
		checklist.setDanoPortas(selectedImovel.isDanoPortas());
		checklist.setDanoQuarto(selectedImovel.isDanoQuarto());
		checklist.setDanoSala(selectedImovel.isDanoSala());
		checklist.setDanoTomadas(selectedImovel.isDanoTomadas());
		checklist.setDanoVasoSanitario(selectedImovel.isDanoVasoSanitario());
		checklist.setObs(readObs(selectedImovel.getId()));
		checklist.setDataLaudo(selectedImovel.getDataLaudo());
		
		boolean okClicked = frontApp.showChecklist(checklist);
		if (okClicked) {
			selectedImovel.setDanoArCondicionado(checklist.isDanoArCondicionado());
			selectedImovel.setDanoAreaServico(checklist.isDanoAreaServico());
			selectedImovel.setDanoBanheiro(checklist.isDanoBanheiro());
			selectedImovel.setDanoBox(checklist.isDanoBox());
			selectedImovel.setDanoCercaEletrica(checklist.isDanoCercaEletrica());
			selectedImovel.setDanoChaves(checklist.isDanoChaves());
			selectedImovel.setDanoChuveiro(checklist.isDanoChuveiro());
			selectedImovel.setDanoControle(checklist.isDanoControle());
			selectedImovel.setDanoCozinha(checklist.isDanoCozinha());
			selectedImovel.setDanoDispensa(checklist.isDanoDispensa());
			selectedImovel.setDanoFechaduras(checklist.isDanoFechaduras());
			selectedImovel.setDanoGaragem(checklist.isDanoGaragem());
			selectedImovel.setDanoInfiltracao(checklist.isDanoInfiltracao());
			selectedImovel.setDanoJanelas(checklist.isDanoJanelas());
			selectedImovel.setDanoLampadas(checklist.isDanoLampadas());
			selectedImovel.setDanoMoveisVinculados(checklist.isDanoMoveisVinculados());
			selectedImovel.setDanoPia(checklist.isDanoPia());
			selectedImovel.setDanoPinturaExterna(checklist.isDanoPinturaExterna());
			selectedImovel.setDanoPinturaInterna(checklist.isDanoPinturaInterna());
			selectedImovel.setDanoPortao(checklist.isDanoPortao());
			selectedImovel.setDanoPortaoEletro(checklist.isDanoPortaoEletro());
			selectedImovel.setDanoPortas(checklist.isDanoPortas());
			selectedImovel.setDanoQuarto(checklist.isDanoQuarto());
			selectedImovel.setDanoSala(checklist.isDanoSala());
			selectedImovel.setDanoTomadas(checklist.isDanoTomadas());
			selectedImovel.setDanoVasoSanitario(checklist.isDanoVasoSanitario());
			selectedImovel.setDataLaudo(checklist.getDataLaudo());
			
			String docFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
			
			try {
				saveObs(checklist.getObs(), docFolder, selectedImovel.getId());
			} catch (IOException e) {
				System.out.println("NÃO FOI POSSÍVEL SALVAR O ARQUIVO DE OBS");
				e.printStackTrace();
			}
			ImovelRepository imvRepo = (ImovelRepository) context.getBean("imovelRepository");
			selectedImovel = imvRepo.save(selectedImovel);
			showImovel(selectedImovel);
		}
	}

	private void saveObs(String obs, String docFolder, Integer imovelId) throws IOException {
		BufferedWriter bf = new BufferedWriter(
				new FileWriter(new File(docFolder + "/Imoveis/" + imovelId + "/obs.txt")));
		bf.write(obs);
		bf.close();
	}
	
	@FXML
	private void handleRemoveImovel() {

		ImovelRepository imvRepo = (ImovelRepository) context.getBean("imovelRepository");
		Imovel selectedImovel = imovelTable.getSelectionModel().getSelectedItem();

		if (selectedImovel != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UNDECORATED);
			DialogPane dialogPane = alert.getDialogPane();			
			dialogPane.getStylesheets().add(FileUtils.fileToString( new File ("css/alerts.css") ));
			alert.setTitle("Exclusão de Imóvel");
			alert.setHeaderText("Confirmar Exclusão do Imóvel Selecionado?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				imovelData.remove(selectedImovel);
				imvRepo.delete(selectedImovel);

			}
			imovelTable.refresh();
		}
	}
	
	@FXML
	private void handleProprietario() {
	    Proprietario proprietario = imovelTable.getSelectionModel().getSelectedItem().getProprietario();
	    DadosBancarios db = proprietario.getDadosBancarios();
	        boolean okClicked = frontApp.showNovoProprietario(proprietario, db);
	        if (okClicked) {
	        	ProprietarioRepository propRepo = (ProprietarioRepository)context.getBean("proprietarioRepository");
	        	propRepo.save(proprietario);
	        	
	        }

	    
	}
	
	@FXML
	private void loadFotos() throws FileNotFoundException {
		List<File> imovelImages = fc.showOpenMultipleDialog(stage);
		
		Integer imovelId = imovelTable.getSelectionModel().getSelectedItem().getId();
		String docFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		new File(docFolder + "/Imoveis/" + imovelId).mkdir();
		Integer imageId = 1;
		for (File file : imovelImages) {
			
			FileInputStream is = new FileInputStream(file.getAbsolutePath());
			Image image = new Image(is);
			
			saveImage(image, imageId, imovelId, docFolder);
			imageId++;
			
		}
	}
	
	private void saveImage(Image image, Integer imageId, Integer imovelId, String docFolder) {	
		File outputFile = new File (docFolder + "/Imoveis/" + imovelId + "/" + imageId + ".jpg");
		BufferedImage bi = SwingFXUtils.fromFXImage(image, null);
		try {
			ImageIO.write(bi, "jpg", outputFile);
		}catch (IOException e) {
		      throw new RuntimeException(e);
	    }

				
	}
	private String readDescricao(Integer imovelId) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/Imoveis/" + imovelId + "/descricao.txt"));
		String descTemp = "";
		while (br.ready()) {
			descTemp = descTemp + br.readLine() + "\n";
		}
		br.close();
		return descTemp;
		
	}
	
	

	private String readObs(Integer imovelId) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/Imoveis/" + imovelId + "/obs.txt"));
		String obsTemp = "";
		while (br.ready()) {
			obsTemp = obsTemp + br.readLine() + "\n";
		}
		br.close();
		return obsTemp;
		
	}
	
	
	
	public void setMainApp(FrontApp frontApp) {
        this.frontApp = frontApp;
	}
}
