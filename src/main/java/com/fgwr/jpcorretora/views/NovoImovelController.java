package com.fgwr.jpcorretora.views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.filechooser.FileSystemView;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Endereco;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Proprietario;
import com.fgwr.jpcorretora.dto.ImovelChecklistDTO;
import com.fgwr.jpcorretora.enums.EstadoImovel;
import com.fgwr.jpcorretora.enums.TipoEndereco;
import com.fgwr.jpcorretora.repositories.EnderecoRepository;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.repositories.ProprietarioRepository;
import com.fgwr.jpcorretora.services.exceptions.ObjectNotFoundException;
import com.fgwr.jpcorretora.utils.FilesUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NovoImovelController {

	ApplicationContext context = SpringContext.getAppContext();

	@FXML
	private ComboBox<Proprietario> proprietarioBox;
	@FXML
	private TextField logradouroField;
	@FXML
	private TextField numeroField;
	@FXML
	private TextField complementoField;
	@FXML
	private DatePicker dataAngariacaoField;
	@FXML
	private TextField bairroField;
	@FXML
	private TextField corretorField;
	@FXML
	private TextField cepField;
	@FXML
	private TextField cidadeField;
	@FXML
	private TextField estadoField;
	@FXML
	private ChoiceBox<String> estadoImovelBox;
	@FXML
	private TextArea descricaoField;

	private String obs;
	@FXML
	private Button checklistBtn;
	@FXML
	private DatePicker dataLaudoField;

	private List<String> estadoImovelAux = new ArrayList<>();

	private Stage dialogStage;
	private boolean okClicked = false;

	private EstadoImovel[] estadoImovel = EstadoImovel.values();
	private Imovel imovel;
	private Proprietario proprietario;
	private Endereco endereco;

	FrontApp frontApp = new FrontApp();
	ImovelChecklistDTO checklist = new ImovelChecklistDTO();

	public List<Proprietario> getProprietarioData() {
		ProprietarioRepository propRepo = (ProprietarioRepository) context.getBean("proprietarioRepository");
		List<Proprietario> proprietarios = propRepo.findByActiveOrderByNomeAsc(true);
		return proprietarios;
	}

	@FXML
	private void initialize() {

		for (EstadoImovel estadoImovel : estadoImovel) {
			estadoImovelAux.add(estadoImovel.getDescricao());
		}

		estadoImovelBox.setItems(FXCollections.observableArrayList(estadoImovelAux));

		proprietarioBox.setItems(FXCollections.observableArrayList(getProprietarioData()));

		Callback<ListView<Proprietario>, ListCell<Proprietario>> proprietarioCellFactory = new Callback<ListView<Proprietario>, ListCell<Proprietario>>() {

			@Override
			public ListCell<Proprietario> call(ListView<Proprietario> l) {
				return new ListCell<Proprietario>() {

					@Override
					protected void updateItem(Proprietario proprietario, boolean empty) {
						super.updateItem(proprietario, empty);
						if (proprietario == null || empty) {
							setGraphic(null);
						} else {
							setText(proprietario.getNome());
						}
					}
				};
			}

		};

		proprietarioBox.setButtonCell(proprietarioCellFactory.call(null));
		proprietarioBox.setCellFactory(proprietarioCellFactory);

	}

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

	private boolean isInputValid() {
		String errorMessage = "";
		
		if (logradouroField.getText() == null || logradouroField.getText().length() == 0) {
			errorMessage += "Logradouro inválido\n";
		}
		if (numeroField.getText() == null || numeroField.getText().length() == 0 || numeroField.getText().matches("[a-zA-Z_]+")) {
			errorMessage += "Número Inválido\n";
		}
		
		if (cepField.getText() == null || cepField.getText().length() != 8 || cepField.getText().matches("[a-zA-Z_]+")) {
			errorMessage += "CEP Inválido\n";
		}
		if (bairroField.getText() == null || bairroField.getText().length() == 0) {
			errorMessage += "Bairro inválido\n";
		}

		if (estadoImovelBox.getValue() == null) {
			errorMessage += "Selecione um Estado para o Imovel\n";
		}

		if (dataAngariacaoField.getValue() == null && dataAngariacaoField.getEditor().getText().length() == 0) {
			errorMessage += "Data de Angariação inválida\n";

		}
		
		if (dataLaudoField.getValue() == null && dataLaudoField.getEditor().getText().length() == 0) {
			errorMessage += "Data do laudo de vistoria inválida\n";
			
		}
		
		if (corretorField.getText() == null || corretorField.getText().length() == 0) {
			errorMessage += "Corretor inválido\n";
		
		}
		 
		if (errorMessage.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initStyle(StageStyle.UNDECORATED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Campos Inválidos");
			alert.setHeaderText("Por favor, corrija os campos inválidos");
			alert.setContentText(errorMessage);
			alert.showAndWait();

			return false;
		}
	}

	public void setImovel(Imovel imovel, Endereco endereco) throws IOException {
		this.imovel = imovel;
		this.endereco = endereco;

		if (imovel.getId() == null) {
			proprietarioBox.setValue(null);
			logradouroField.setText("");
			numeroField.setText("");
			complementoField.setText("");
			dataLaudoField.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
			dataAngariacaoField.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
			bairroField.setText("");
			cepField.setText("");

			estadoImovelBox.setValue(null);

		} else {
			this.endereco = imovel.getEndereco();
			proprietarioBox.setValue(imovel.getProprietario());
			logradouroField.setText(endereco.getLogradouro());
			numeroField.setText(endereco.getNumero());
			complementoField.setText(endereco.getComplemento());
			cidadeField.setText(endereco.getCidade());
			estadoField.setText(endereco.getEstado());
			corretorField.setText(imovel.getCorretor());
			descricaoField.setText(readDescricao(imovel.getId()));

			
			if (imovel.getDataLaudo() != null) {
				dataLaudoField.setValue(Instant.ofEpochMilli(imovel.getDataLaudo().getTime())
						.atZone(ZoneId.systemDefault()).toLocalDate());
			} else {
				dataLaudoField.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
			}

			if (imovel.getDataAngariacao() != null) {
				dataAngariacaoField.setValue(Instant.ofEpochMilli(imovel.getDataAngariacao().getTime())
						.atZone(ZoneId.systemDefault()).toLocalDate());
			} else {
				dataAngariacaoField.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
			}

			bairroField.setText(endereco.getBairro());
			cepField.setText(endereco.getCep());

			estadoImovelBox.setValue(imovel.getEstadoImovel().getDescricao());

		}

	}

	@FXML
	private void handleNovoProprietario() {
		Proprietario proprietario = new Proprietario();
		DadosBancarios db = new DadosBancarios();

		boolean okClicked = frontApp.showNovoProprietario(proprietario, db);
		if (okClicked) {
			ProprietarioRepository propRepo = (ProprietarioRepository) context.getBean("proprietarioRepository");
			proprietario = propRepo.save(proprietario);
			this.proprietario = proprietario;
			proprietarioBox.getItems().add(proprietario);
			proprietarioBox.setValue(proprietario);
		}

	}
	
	@FXML
	private void handleChecklist() throws IOException {
		checklist.setControl(true);
		boolean okClicked = false;
		
		if (imovel.getId() != null) {
			checklist.setDanoArCondicionado(imovel.isDanoArCondicionado());
			checklist.setDanoAreaServico(imovel.isDanoAreaServico());
			checklist.setDanoBanheiro(imovel.isDanoBanheiro());
			checklist.setDanoBox(imovel.isDanoBox());
			checklist.setDanoCercaEletrica(imovel.isDanoCercaEletrica());
			checklist.setDanoChaves(imovel.isDanoChaves());
			checklist.setDanoChuveiro(imovel.isDanoChuveiro());
			checklist.setDanoControle(imovel.isDanoControle());
			checklist.setDanoCozinha(imovel.isDanoCozinha());
			checklist.setDanoDispensa(imovel.isDanoDispensa());
			checklist.setDanoFechaduras(imovel.isDanoFechaduras());
			checklist.setDanoGaragem(imovel.isDanoGaragem());
			checklist.setDanoInfiltracao(imovel.isDanoInfiltracao());
			checklist.setDanoJanelas(imovel.isDanoJanelas());
			checklist.setDanoLampadas(imovel.isDanoLampadas());
			checklist.setDanoMoveisVinculados(imovel.isDanoMoveisVinculados());
			checklist.setDanoPia(imovel.isDanoPia());
			checklist.setDanoPinturaExterna(imovel.isDanoPinturaExterna());
			checklist.setDanoPinturaInterna(imovel.isDanoPinturaInterna());
			checklist.setDanoPortao(imovel.isDanoPortao());
			checklist.setDanoPortaoEletro(imovel.isDanoPortaoEletro());
			checklist.setDanoPortas(imovel.isDanoPortas());
			checklist.setDanoQuarto(imovel.isDanoQuarto());
			checklist.setDanoSala(imovel.isDanoSala());
			checklist.setDanoTomadas(imovel.isDanoTomadas());
			checklist.setDanoVasoSanitario(imovel.isDanoVasoSanitario());
			checklist.setObs(readObs(imovel.getId()));
			checklist.setDataLaudo(imovel.getDataLaudo());

		}
		if (this.okClicked) {
			checklist.setDanoArCondicionado(imovel.isDanoArCondicionado());
			checklist.setDanoAreaServico(imovel.isDanoAreaServico());
			checklist.setDanoBanheiro(imovel.isDanoBanheiro());
			checklist.setDanoBox(imovel.isDanoBox());
			checklist.setDanoCercaEletrica(imovel.isDanoCercaEletrica());
			checklist.setDanoChaves(imovel.isDanoChaves());
			checklist.setDanoChuveiro(imovel.isDanoChuveiro());
			checklist.setDanoControle(imovel.isDanoControle());
			checklist.setDanoCozinha(imovel.isDanoCozinha());
			checklist.setDanoDispensa(imovel.isDanoDispensa());
			checklist.setDanoFechaduras(imovel.isDanoFechaduras());
			checklist.setDanoGaragem(imovel.isDanoGaragem());
			checklist.setDanoInfiltracao(imovel.isDanoInfiltracao());
			checklist.setDanoJanelas(imovel.isDanoJanelas());
			checklist.setDanoLampadas(imovel.isDanoLampadas());
			checklist.setDanoMoveisVinculados(imovel.isDanoMoveisVinculados());
			checklist.setDanoPia(imovel.isDanoPia());
			checklist.setDanoPinturaExterna(imovel.isDanoPinturaExterna());
			checklist.setDanoPinturaInterna(imovel.isDanoPinturaInterna());
			checklist.setDanoPortao(imovel.isDanoPortao());
			checklist.setDanoPortaoEletro(imovel.isDanoPortaoEletro());
			checklist.setDanoPortas(imovel.isDanoPortas());
			checklist.setDanoQuarto(imovel.isDanoQuarto());
			checklist.setDanoSala(imovel.isDanoSala());
			checklist.setDanoTomadas(imovel.isDanoTomadas());
			checklist.setDanoVasoSanitario(imovel.isDanoVasoSanitario());
			checklist.setObs(obs);
			checklist.setDataLaudo(imovel.getDataLaudo());

		}
		okClicked = frontApp.showChecklist(checklist);
		if (okClicked) {
			
			imovel.setDanoArCondicionado(checklist.isDanoArCondicionado());
			imovel.setDanoAreaServico(checklist.isDanoAreaServico());
			imovel.setDanoBanheiro(checklist.isDanoBanheiro());
			imovel.setDanoBox(checklist.isDanoBox());
			imovel.setDanoCercaEletrica(checklist.isDanoCercaEletrica());
			imovel.setDanoChaves(checklist.isDanoChaves());
			imovel.setDanoChuveiro(checklist.isDanoChuveiro());
			imovel.setDanoControle(checklist.isDanoControle());
			imovel.setDanoCozinha(checklist.isDanoCozinha());
			imovel.setDanoDispensa(checklist.isDanoDispensa());
			imovel.setDanoFechaduras(checklist.isDanoFechaduras());
			imovel.setDanoGaragem(checklist.isDanoGaragem());
			imovel.setDanoInfiltracao(checklist.isDanoInfiltracao());
			imovel.setDanoJanelas(checklist.isDanoJanelas());
			imovel.setDanoLampadas(checklist.isDanoLampadas());
			imovel.setDanoMoveisVinculados(checklist.isDanoMoveisVinculados());
			imovel.setDanoPia(checklist.isDanoPia());
			imovel.setDanoPinturaExterna(checklist.isDanoPinturaExterna());
			imovel.setDanoPinturaInterna(checklist.isDanoPinturaInterna());
			imovel.setDanoPortao(checklist.isDanoPortao());
			imovel.setDanoPortaoEletro(checklist.isDanoPortaoEletro());
			imovel.setDanoPortas(checklist.isDanoPortas());
			imovel.setDanoQuarto(checklist.isDanoQuarto());
			imovel.setDanoSala(checklist.isDanoSala());
			imovel.setDanoTomadas(checklist.isDanoTomadas());
			imovel.setDanoVasoSanitario(checklist.isDanoVasoSanitario());
			imovel.setDataLaudo(checklist.getDataLaudo());

			obs = checklist.getObs();
			this.okClicked = true;
		}
	}

	public Proprietario findProprietario(Integer id) {
		ProprietarioRepository cliRepo = (ProprietarioRepository) context.getBean("proprietarioRepository");
		Optional<Proprietario> obj = cliRepo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Não encontrado"));
	}

	@FXML
	private void handleOk() throws IOException {
		if (isInputValid()) {

			if (!okClicked ){
				imovel.setDanoArCondicionado(false);
				imovel.setDanoAreaServico(false);
				imovel.setDanoBanheiro(false);
				imovel.setDanoBox(false);
				imovel.setDanoCercaEletrica(false);
				imovel.setDanoChaves(false);
				imovel.setDanoChuveiro(false);
				imovel.setDanoControle(false);
				imovel.setDanoCozinha(false);
				imovel.setDanoDispensa(false);
				imovel.setDanoFechaduras(false);
				imovel.setDanoGaragem(false);
				imovel.setDanoInfiltracao(false);
				imovel.setDanoJanelas(false);
				imovel.setDanoLampadas(false);
				imovel.setDanoMoveisVinculados(false);
				imovel.setDanoPia(false);
				imovel.setDanoPinturaExterna(false);
				imovel.setDanoPinturaInterna(false);
				imovel.setDanoPortao(false);
				imovel.setDanoPortaoEletro(false);
				imovel.setDanoPortas(false);
				imovel.setDanoQuarto(false);
				imovel.setDanoSala(false);
				imovel.setDanoTomadas(false);
				imovel.setDanoVasoSanitario(false);
				imovel.setDataLaudo(Date.from(dataLaudoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				obs = "Sem Observações";
			}
			
			ImovelRepository imvRepo = (ImovelRepository) context.getBean("imovelRepository");
			EnderecoRepository endRepo = (EnderecoRepository) context.getBean("enderecoRepository");
			proprietario = findProprietario(proprietarioBox.getValue().getId());

			endereco.setLogradouro(logradouroField.getText().trim());
			endereco.setBairro(bairroField.getText().trim());
			endereco.setCep(cepField.getText().trim());
			endereco.setNumero(numeroField.getText().trim());
			endereco.setComplemento(complementoField.getText().trim());
			endereco.setTipo(TipoEndereco.IMOVELLOCACAO);
			endereco.setCidade(cidadeField.getText().trim());
			endereco.setEstado(estadoField.getText().trim());
			endRepo.save(endereco);

			imovel.setProprietario(proprietario);

			if (dataAngariacaoField.getEditor().getText().isBlank()) {
				imovel.setDataAngariacao(
						Date.from(dataAngariacaoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			} else if (!dataAngariacaoField.getEditor().getText().isBlank()) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				try {
					Date date = formatter.parse(dataAngariacaoField.getEditor().getText().trim());
					imovel.setDataAngariacao(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			imovel.setActive(true);
			
			imovel.setCorretor(corretorField.getText().trim());
			imovel.setDescricao(descricaoField.getText());
			imovel.setEstadoImovel(EstadoImovel.valueOfDescricao(estadoImovelBox.getValue()));
			if (dataLaudoField.getEditor().getText().isBlank()) {
				imovel.setDataLaudo(
						Date.from(dataLaudoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			} else if (!dataLaudoField.getEditor().getText().isBlank()) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

				try {
					Date date = formatter.parse(dataLaudoField.getEditor().getText().trim());
					imovel.setDataLaudo(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			imovel.setEndereco(endereco);
			imovel.setProprietario(proprietario);

			proprietario.getImovel().add(imovel);
			endereco.setImovel(imovel);
			imovel = imvRepo.save(imovel);

			String docFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
			new File(docFolder + "/Imoveis/" + imovel.getId()).mkdir();
			new File(docFolder + "/Imoveis/" + imovel.getId() + "/descricao.txt").delete();
			new File(docFolder + "/Imoveis/" + imovel.getId() + "/descricao.txt").createNewFile();
			new File(docFolder + "/Imoveis/" + imovel.getId() + "/obs.txt").delete();
			new File(docFolder + "/Imoveis/" + imovel.getId() + "/obs.txt").createNewFile();
			
			if (descricaoField.getText() == null || descricaoField.getText().length() == 0) {
				saveDescricao("Sem Descrição", docFolder, imovel.getId());
			} else {
				saveDescricao(descricaoField.getText(), docFolder, imovel.getId());
			}

			saveDescricao(descricaoField.getText(), docFolder, imovel.getId());
			if (!okClicked) {
			saveObs("Sem Observações", docFolder, imovel.getId());
			} else {
				saveObs(obs, docFolder, imovel.getId());
			}
			
			okClicked = true;
			dialogStage.close();
		}
	}

	private void saveDescricao(String descricao, String docFolder, Integer imovelId) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(
				new File(docFolder + "/Imoveis/" + imovelId + "/descricao.txt"));
		BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(outputStream));
		bf.write(descricao);
		bf.close();
	}

	private String readDescricao(Integer imovelId) throws IOException {
		BufferedReader br = new BufferedReader(
				new FileReader(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/Imoveis/"
						+ imovelId + "/descricao.txt"));
		String descTemp = "";
		while (br.ready()) {
			descTemp = descTemp + br.readLine() + "\n";
		}
		br.close();
		return descTemp;

	}

	private void saveObs(String obs, String docFolder, Integer imovelId) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(new File(docFolder + "/Imoveis/" + imovelId + "/obs.txt"));
		BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(outputStream));
		bf.write(obs);
		bf.close();
	}

	private String readObs(Integer imovelId) throws IOException {
		BufferedReader br = new BufferedReader(
				new FileReader(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/Imoveis/"
						+ imovelId + "/obs.txt"));
		String obsTemp = "";
		while (br.ready()) {
			obsTemp = obsTemp + br.readLine() + "\n";
		}
		br.close();
		return obsTemp;

	}
	
	@FXML
	public void handleOnKeyPressed(KeyEvent e) throws IOException {
		KeyCode code = e.getCode();		
		
		if (code == KeyCode.ENTER) {
			handleOk();
		}
		if (code == KeyCode.ESCAPE) {
			handleCancel();
		}
	}
    
}
