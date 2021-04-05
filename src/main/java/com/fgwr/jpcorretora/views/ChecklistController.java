package com.fgwr.jpcorretora.views;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.dto.ImovelChecklistDTO;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ChecklistController {

	@FXML
	private Button okBtn;
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
	private TextArea obsField;
	@FXML
	private Label dataLaudoLabel;
	@FXML
	private DatePicker dataLaudoPicker;

	private Stage dialogStage;
	private boolean okClicked = false;
	
	ImovelChecklistDTO checklist;

	@FXML
	private void initialize() {
		
	}

	@FXML
	public void handleOnKeyPressed(KeyEvent e) {
		KeyCode code = e.getCode();

		danoSalaCheck.setFocusTraversable(false);
		
		
		if (code == KeyCode.ENTER) {
			okBtn.fire();
		}
		if (code == KeyCode.ESCAPE) {
			handleCancel();
		}
	}
		
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
		
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleCancel() {
		checklist.setObs("Sem Observações");
		dialogStage.close();
	}
	
	public void setChecklist(ImovelChecklistDTO checklist) {
		
		if (checklist.isControl()) {
			dataLaudoLabel.setVisible(false);
			dataLaudoPicker.setVisible(false);
		}
		this.checklist = checklist;
		
		if (checklist.getDataLaudo() != null) {
		dataLaudoPicker.setValue(Instant.ofEpochMilli(checklist.getDataLaudo().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
		} else {
			dataLaudoPicker.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
			
		}
		
		danoArCondicionadoCheck.setSelected(checklist.isDanoArCondicionado());
		danoAreaServicoCheck.setSelected(checklist.isDanoAreaServico());
		danoBanheiroCheck.setSelected(checklist.isDanoBanheiro());
		danoBoxCheck.setSelected(checklist.isDanoBox());
		danoCercaEletricaCheck.setSelected(checklist.isDanoCercaEletrica());
		danoChavesCheck.setSelected(checklist.isDanoChaves());
		danoChuveiroCheck.setSelected(checklist.isDanoChuveiro());
		danoControleCheck.setSelected(checklist.isDanoControle());
		danoCozinhaCheck.setSelected(checklist.isDanoCozinha());
		danoDispensaCheck.setSelected(checklist.isDanoDispensa());
		danoFechadurasCheck.setSelected(checklist.isDanoFechaduras());
		danoGaragemCheck.setSelected(checklist.isDanoGaragem());
		danoInfiltracaoCheck.setSelected(checklist.isDanoInfiltracao());
		danoJanelasCheck.setSelected(checklist.isDanoJanelas());
		danoLampadasCheck.setSelected(checklist.isDanoLampadas());
		danoMoveisVinculadosCheck.setSelected(checklist.isDanoMoveisVinculados());
		danoPiaCheck.setSelected(checklist.isDanoPia());
		danoPinturaExternaCheck.setSelected(checklist.isDanoPinturaExterna());
		danoPinturaInternaCheck.setSelected(checklist.isDanoPinturaInterna());
		danoPortaoCheck.setSelected(checklist.isDanoPortao());
		danoPortaoEletroCheck.setSelected(checklist.isDanoPortaoEletro());
		danoPortasCheck.setSelected(checklist.isDanoPortas());
		danoQuartoCheck.setSelected(checklist.isDanoQuarto());
		danoSalaCheck.setSelected(checklist.isDanoSala());
		danoTomadasCheck.setSelected(checklist.isDanoTomadas());
		danoVasoSanitarioCheck.setSelected(checklist.isDanoVasoSanitario());

		obsField.setText(checklist.getObs());
	}
	
	@FXML
    private void handleOk() {
		
		checklist.setDanoArCondicionado(danoArCondicionadoCheck.isSelected());
		checklist.setDanoAreaServico(danoAreaServicoCheck.isSelected());
		checklist.setDanoBanheiro(danoBanheiroCheck.isSelected());
		checklist.setDanoBox(danoBoxCheck.isSelected());
		checklist.setDanoCercaEletrica(danoCercaEletricaCheck.isSelected());
		checklist.setDanoChaves(danoChavesCheck.isSelected());
		checklist.setDanoChuveiro(danoChuveiroCheck.isSelected());
		checklist.setDanoControle(danoControleCheck.isSelected());
		checklist.setDanoCozinha(danoCozinhaCheck.isSelected());
		checklist.setDanoDispensa(danoDispensaCheck.isSelected());
		checklist.setDanoFechaduras(danoFechadurasCheck.isSelected());
		checklist.setDanoGaragem(danoGaragemCheck.isSelected());
		checklist.setDanoInfiltracao(danoInfiltracaoCheck.isSelected());
		checklist.setDanoJanelas(danoJanelasCheck.isSelected());
		checklist.setDanoLampadas(danoLampadasCheck.isSelected());
		checklist.setDanoMoveisVinculados(danoMoveisVinculadosCheck.isSelected());
		checklist.setDanoPia(danoPiaCheck.isSelected());
		checklist.setDanoPinturaExterna(danoPinturaExternaCheck.isSelected());
		checklist.setDanoPinturaInterna(danoPinturaInternaCheck.isSelected());
		checklist.setDanoPortao(danoPortaoCheck.isSelected());
		checklist.setDanoPortaoEletro(danoPortaoEletroCheck.isSelected());
		checklist.setDanoPortas(danoPortasCheck.isSelected());
		checklist.setDanoQuarto(danoQuartoCheck.isSelected());
		checklist.setDanoSala(danoSalaCheck.isSelected());
		checklist.setDanoTomadas(danoTomadasCheck.isSelected());
		checklist.setDanoVasoSanitario(danoVasoSanitarioCheck.isSelected());
		
		if (dataLaudoPicker.getEditor().getText().isBlank()) {
			checklist.setDataLaudo(
					Date.from(dataLaudoPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		} else if (!dataLaudoPicker.getEditor().getText().isBlank()) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			try {
				Date date = formatter.parse(dataLaudoPicker.getEditor().getText());
				checklist.setDataLaudo(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if ( obsField.getText() == null || obsField.getText().isBlank()) {
			checklist.setObs("Sem Observações");
		} else {
		checklist.setObs(obsField.getText());
		}
		
		
            okClicked = true;
            dialogStage.close();
        //    return checklist;
        
    }

}
