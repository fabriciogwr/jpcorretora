package com.fgwr.jpcorretora.views;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.fgwr.jpcorretora.repositories.DadosBancariosRepository;
import com.fgwr.jpcorretora.repositories.EnderecoRepository;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.repositories.ProprietarioRepository;
import com.fgwr.jpcorretora.services.exceptions.ObjectNotFoundException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView
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
    private TextField descricaoField;
    @FXML
    private TextField obsField;
    @FXML
    private Button checklistBtn;
    
    
    
    
private List<String> estadoImovelAux = new ArrayList<>();
    
    private Stage dialogStage;
    private boolean okClicked = false;
    
    private EstadoImovel[] estadoImovel = EstadoImovel.values();
	private Imovel imovel;
	private Proprietario proprietario;
	private Endereco endereco;
	
	FrontApp frontApp = new FrontApp();
    

	public List<Proprietario> getProprietarioData() {
		ProprietarioRepository propRepo = (ProprietarioRepository)context.getBean("proprietarioRepository");
    	List<Proprietario> proprietarios = propRepo.findAll();			
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
/*
        if (nomeField.getText() == null || nomeField.getText().length() == 0) {
            errorMessage += "Nome inválido\n"; 
        }
        if (emailField.getText() == null || emailField.getText().length() == 0) {
            errorMessage += "Email inválido\n"; 
        }
        if (cpfField.getText() == null || cpfField.getText().length() == 0) {
            errorMessage += "CPF inválido\n"; 
        }

        if (rgField.getText() == null || rgField.getText().length() == 0) {
            errorMessage += "RG inválido\n"; 
        }

        if (estadoImovelBox.getValue() == null) {
            errorMessage += "Selecione um Estado Imovel\n"; 
        }

        if (dataNascimentoField.getValue() == null) {
            errorMessage += "Data de Nascimento inválida\n";
        
        }
*/
        if (errorMessage.length() == 0) {
            return true;
        } else {
        	Alert alert = new Alert(AlertType.ERROR);
            	      alert.setTitle("Campos Inválidos");
            	      alert.setHeaderText("Por favor, corrija os campos inválidos");
            	      alert.setContentText(errorMessage);
                alert.showAndWait();
                
            return false;
        }
    }

	public void setImovel(Imovel imovel , Endereco endereco) {
		this.imovel = imovel;
		this.endereco = endereco;

		if (imovel.getId() == null) {
        proprietarioBox.setValue(null);
        logradouroField.setText("");
        numeroField.setText("");
        complementoField.setText("");
        dataAngariacaoField.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
        bairroField.setText("");
        cepField.setText("");
        
        estadoImovelBox.setValue(null);
        
        descricaoField.setText("");
		} else {
			this.endereco = imovel.getEndereco(); 
			proprietarioBox.setValue(imovel.getProprietario());
	        logradouroField.setText(endereco.getLogradouro());
	        numeroField.setText(endereco.getNumero());
	        complementoField.setText(endereco.getComplemento());
	        cidadeField.setText(endereco.getCidade());
	        estadoField.setText(endereco.getEstado());
	        obsField.setText(imovel.getObs());
	        corretorField.setText(imovel.getCorretor());
	        
	        if (imovel.getDataAngariacao() != null) {
	        	dataAngariacaoField.setValue(Instant.ofEpochMilli(imovel.getDataAngariacao().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
	            } else {
	            	dataAngariacaoField.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
	            }
	        
	        bairroField.setText(endereco.getBairro());
	        cepField.setText(endereco.getCep());
	        
	        estadoImovelBox.setValue(imovel.getEstadoImovel().getDescricao());
	        
	        descricaoField.setText(imovel.getDescricao());
		}
		
	}
	
	@FXML
	private void handleNovoProprietario() {
	    Proprietario proprietario = new Proprietario();
	    DadosBancarios db = new DadosBancarios();
	   
	        boolean okClicked = frontApp.showNovoProprietario(proprietario, db);
	        if (okClicked) {
	        	ProprietarioRepository propRepo = (ProprietarioRepository)context.getBean("proprietarioRepository");
	        	DadosBancariosRepository dbRepo = (DadosBancariosRepository)context.getBean("dadosBancariosRepository");
	      //  	dbRepo.save(db);
	        	propRepo.save(proprietario);
	        	this.proprietario = proprietario;
	        	
	        	System.out.println(proprietario.getNome());
	        	System.out.println(this.proprietario.getNome());
	        	
	            proprietarioBox.getItems().add(proprietario);
	            proprietarioBox.setValue(proprietario);
	        }

	    
	}
	
	@FXML
	private void handleChecklist() {
	    ImovelChecklistDTO checklist = new ImovelChecklistDTO();
	    
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
	    }
	        boolean okClicked = frontApp.showChecklist(checklist);
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
	        }

	    
	}
	
	
	
	public Proprietario findProprietario (Integer id) {
		ProprietarioRepository cliRepo = (ProprietarioRepository)context.getBean("proprietarioRepository");
    	Optional<Proprietario> obj = cliRepo.findById(id);
    	return obj.orElseThrow(() -> new ObjectNotFoundException("Não encontrado"));
    }
		
	 @FXML
	    private void handleOk() {
	        if (isInputValid()) {
	        	ImovelRepository imvRepo = (ImovelRepository)context.getBean("imovelRepository");
	        	EnderecoRepository endRepo = (EnderecoRepository)context.getBean("enderecoRepository");
	        	proprietario = findProprietario(proprietarioBox.getValue().getId());
	        	System.out.println(proprietario.getNome());
	        	
	        	endereco.setLogradouro(logradouroField.getText());
	        	endereco.setBairro(bairroField.getText());
	        	endereco.setCep(cepField.getText());
	        	endereco.setNumero(numeroField.getText());
	        	endereco.setComplemento(complementoField.getText());
	        	endereco.setTipo(TipoEndereco.IMOVELLOCACAO);
	        	endereco.setCidade(cidadeField.getText());
	        	endereco.setEstado(estadoField.getText());
	        	endRepo.save(endereco);
	        	
	        	imovel.setProprietario(proprietario);
	            imovel.setDataAngariacao(Date.from(dataAngariacaoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
	            imovel.setCorretor(corretorField.getText());
	            imovel.setDescricao(descricaoField.getText());
	            imovel.setObs(obsField.getText());
	            imovel.setEstadoImovel(EstadoImovel.valueOfDescricao(estadoImovelBox.getValue()));
	            
	            imovel.setEndereco(endereco);
	        	imovel.setProprietario(proprietario);
	        	
	        	
	        	proprietario.getImovel().add(imovel);
	        	endereco.setImovel(imovel);
	        	imvRepo.save(imovel);
	        	
	        	
		        //propRepo.save(prop);
		        
	            
	            
	            okClicked = true;
	            dialogStage.close();
	        }
	    }
}
