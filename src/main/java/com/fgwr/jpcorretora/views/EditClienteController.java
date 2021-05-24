package com.fgwr.jpcorretora.views;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Corretor;
import com.fgwr.jpcorretora.enums.Banco;
import com.fgwr.jpcorretora.enums.EstadoCivil;
import com.fgwr.jpcorretora.enums.TipoConta;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.CorretorRepository;
import com.fgwr.jpcorretora.services.ClienteService;
import com.fgwr.jpcorretora.utils.FilesUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EditClienteController {
	
    @FXML
    private TextField nomeField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telefonePrefField;
    @FXML
    private TextField telefoneAltField;
    @FXML
    private DatePicker dataNascimentoField;
    @FXML
    private TextField cpfField;
    @FXML
    private TextField rgField;
    @FXML
    private ChoiceBox<String> estadoCivilBox;
    @FXML
    private TextField profissaoField;
    @FXML
    private TextField agenciaField;
    @FXML
    private TextField contaField;
    @FXML
    private TextField titularField;
    @FXML
    private TextField obsField;
    @FXML
    private ComboBox<String> bancoBox;
    @FXML
    private ComboBox<String> tipoContaBox;
    @FXML
    private ComboBox<Corretor> corretorBox;
    @FXML
    private TextField pixField;

    private List<String> estadoCivilAux = new ArrayList<>();
    
    private List<String> bancoAux = new ArrayList<>();
    
    private List<String> tipoContaAux = new ArrayList<>();

    ApplicationContext context = SpringContext.getAppContext();
    private Stage dialogStage;
    private Cliente cliente;
    private boolean okClicked = false;
    
    private EstadoCivil[] estadoCivil = EstadoCivil.values();
    private Banco[] banco = Banco.values();
    private TipoConta[] tipoConta = TipoConta.values();
    
    
    @FXML
	public void handleOnKeyPressed(KeyEvent e) {
		KeyCode code = e.getCode();		
		
		if (code == KeyCode.ENTER) {
			handleOk();
		}
		if (code == KeyCode.ESCAPE) {
			handleCancel();
		}
	}
    
    private List<Corretor> getCorretorData() {
    	CorretorRepository crrRepo = (CorretorRepository)context.getBean("corretorRepository");
		List<Corretor> allCrr = crrRepo.findAll();
    	return allCrr;
    	
    }
    
    
    @FXML
    private void initialize() {
    	
    	for (EstadoCivil estadoCivil : estadoCivil) {
			estadoCivilAux.add(estadoCivil.getDescricao());
		}
    	
    	for (Banco banco : banco) {
    		bancoAux.add(banco.getFullCod() + " - " + banco.getDescricao());	
    	}
    	
    	for (TipoConta tipoConta : tipoConta) {
    		tipoContaAux.add(tipoConta.getDesc());
    		
    	}
    	
    	estadoCivilBox.setItems(FXCollections.observableArrayList(estadoCivilAux));
    	bancoBox.setItems(FXCollections.observableArrayList(bancoAux));
    	tipoContaBox.setItems(FXCollections.observableArrayList(tipoContaAux));
    	
    	corretorBox.setItems(FXCollections.observableArrayList(getCorretorData()));
    	
    	Callback<ListView<Corretor>, ListCell<Corretor>> corretorCellFactory = new Callback<ListView<Corretor>, ListCell<Corretor>>() {

    	    @Override
    	    public ListCell<Corretor> call(ListView<Corretor> l) {
    	        return new ListCell<Corretor>() {

    	            @Override
    	            protected void updateItem(Corretor corretor, boolean empty) {
    	                super.updateItem(corretor, empty);
    	                if (corretor == null || empty) {
    	                    setGraphic(null);
    	                } else {
    	                    setText(corretor.getNome());
    	                }
    	            }
    	        }; 
    	    }
    	};

    	corretorBox.setButtonCell(corretorCellFactory.call(null));
    	corretorBox.setCellFactory(corretorCellFactory);
    	
    	
    	    	
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        nomeField.setText(cliente.getNome());
        emailField.setText(cliente.getEmail());
        
        
		telefonePrefField.setText(cliente.getTelefonePref());
		if (!cliente.getTelefoneAlt().isBlank()) {
			telefoneAltField.setText(cliente.getTelefoneAlt());
			
		} else {
			telefoneAltField.setText("");
		}
        
        if (cliente.getDataNascimento() != null) {
        dataNascimentoField.setValue(Instant.ofEpochMilli(cliente.getDataNascimento().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
        	dataNascimentoField.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        
        cpfField.setText(cliente.getCpfOuCnpj());
        rgField.setText(cliente.getRg());
        if (cliente.getEstadoCivil() != null ) {
        estadoCivilBox.setValue(cliente.getEstadoCivil().getDescricao());
        } else {
        	estadoCivilBox.setValue(null);
        }
        profissaoField.setText(cliente.getProfissao());
        
        if (cliente.getDadosBancarios().getBanco() != null) {
        agenciaField.setText(cliente.getDadosBancarios().getAgencia());
        titularField.setText(cliente.getDadosBancarios().getTitular());
        contaField.setText(cliente.getDadosBancarios().getConta());
        pixField.setText(cliente.getDadosBancarios().getPix());
        obsField.setText(cliente.getObs());
		
        
        bancoBox.setValue(cliente.getDadosBancarios().getBanco().getFullCod() + " - " + cliente.getDadosBancarios().getBanco().getDescricao());
        tipoContaBox.setValue(cliente.getDadosBancarios().getTipo().getDesc());
        } else {
        	bancoBox.setValue(null);
        	tipoContaBox.setValue(null);
        }
       
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    
    @FXML
    private void handleOk() {
        if (isInputValid()) {
        	ApplicationContext context = SpringContext.getAppContext();
        	ClienteRepository repo = (ClienteRepository)context.getBean("clienteRepository");
        	cliente.setNome(nomeField.getText());
            cliente.setEmail(emailField.getText());
            cliente.setTelefonePref(telefonePrefField.getText());

			if (!telefoneAltField.getText().isBlank()) {
				cliente.setTelefoneAlt(telefoneAltField.getText());
			} else {
				cliente.setTelefoneAlt("");
			}
			if (dataNascimentoField.getEditor().getText().isBlank()) {
				cliente.setDataNascimento(
						Date.from(dataNascimentoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			} else if (!dataNascimentoField.getEditor().getText().isBlank()) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				try {
		            Date date = formatter.parse(dataNascimentoField.getEditor().getText());
		            cliente.setDataNascimento(date);
		        } catch (ParseException e) {
		            e.printStackTrace();
		        }
			}
            cliente.setCpfOuCnpj(cpfField.getText());
            cliente.setRg(rgField.getText());
            cliente.setEstadoCivil(EstadoCivil.valueOfDescricao(estadoCivilBox.getValue()));
            cliente.setProfissao(profissaoField.getText());
            
            if (cliente.getDadosBancarios().getBanco() != null ) {
            cliente.getDadosBancarios().setAgencia(agenciaField.getText());
            cliente.getDadosBancarios().setConta(contaField.getText());
            cliente.getDadosBancarios().setTitular(titularField.getText());
            cliente.getDadosBancarios().setBanco(Banco.valueOfDescricao(bancoBox.getValue().substring(6)));
            cliente.getDadosBancarios().setTipo(TipoConta.valueOfDescricao(tipoContaBox.getValue()));
            }
            cliente.setObs(obsField.getText());
            repo.save(cliente);
            
            okClicked = true;
            dialogStage.close();
        }
    }

    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (nomeField.getText() == null || nomeField.getText().length() == 0) {
			errorMessage += "Nome inválido\n";
		}
		
        if (cpfField.getText() == null || cpfField.getText().length() == 0) {
			errorMessage += "Digite um CPF\n";
		} else if (cpfField.getText().length() != 11 || cpfField.getText().matches("[a-zA-Z_]+")) {
			errorMessage += "CPF inválido, digite somente números\n";
		}
        
        if (cpfField.getText() != cliente.getCpfOuCnpj()) {
        ClienteService cliServ= (ClienteService)context.getBean("clienteService");
		Cliente cli = cliServ.findByCpfOuCnpj(cpfField.getText());
		if (cli != null ) {
			errorMessage += "CPF já cadastrado para o cliente " + cli.getNome() + "\n";
		}
        }
		

		if (rgField.getText() == null || rgField.getText().length() == 0  || rgField.getText().matches("[a-zA-Z_]+")) {
			errorMessage += "RG inválido\n";
		}

		if (estadoCivilBox.getValue() == null) {
			errorMessage += "Selecione um Estado Civil\n";
		}

		if ((dataNascimentoField.getValue() == null || dataNascimentoField.getEditor().getText().isBlank()) && dataNascimentoField.getEditor().getText().length() < 8) {
			errorMessage += "Data de Nascimento inválida\n";

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
}