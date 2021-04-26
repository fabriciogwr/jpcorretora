package com.fgwr.jpcorretora.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Testemunha;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.ContratoRepository;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.services.ClienteService;
import com.fgwr.jpcorretora.services.ContratoPdfGen;
import com.fgwr.jpcorretora.services.DuplicataService;
import com.fgwr.jpcorretora.services.ImovelService;
import com.fgwr.jpcorretora.utils.FilesUtils;
import com.fgwr.jpcorretora.utils.StringsUtils;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class NovoContratoController {
	

	private Stage dialogStage;
	private boolean okClicked = false;
	Double primeiraParcela;
	ContratoPdfGen pdfGen = new ContratoPdfGen(); 
	
	ObservableList<Imovel> imoveis;
	ObservableList<Cliente> clientes;
	@FXML
	private ComboBox<Cliente> clienteBox;
	@FXML
	private ComboBox<Imovel> imovelBox;
	@FXML
	private TextField tempoLocacaoField;
	@FXML
	private TextField valorField;
	@FXML
	private TextField vencimentosField;
	@FXML
	private TextField primeiraParcelaField;
	@FXML
	private TextField testemunha1Field;
	@FXML
	private TextField testemunha1CpfField;
	@FXML
	private TextField testemunha2Field;
	@FXML
	private TextField testemunha2CpfField;
	@FXML
	private Label primeiraParcelaLabel;
	
	ApplicationContext context = SpringContext.getAppContext();
	
	private Contrato contrato;
	
	public List<Imovel> getImovelData() {
		ImovelRepository imRepo = (ImovelRepository)context.getBean("imovelRepository");
    	List<Imovel>imoveis = imRepo.findAll();

			imoveis.removeIf(imovel -> imovel.getContrato() != null);
			
    	return imoveis;
	}
	
	public List<Cliente> getClienteData() {
		ClienteRepository cliRepo = (ClienteRepository)context.getBean("clienteRepository");
		List<Cliente> clientes = cliRepo.findAll();
		return clientes;
	}
	
	public void initialize () {
		
	imovelBox.setItems(FXCollections.observableArrayList(getImovelData()));
	
	Callback<ListView<Imovel>, ListCell<Imovel>> imovelCellFactory = new Callback<ListView<Imovel>, ListCell<Imovel>>() {

	    @Override
	    public ListCell<Imovel> call(ListView<Imovel> l) {
	        return new ListCell<Imovel>() {

	            @Override
	            protected void updateItem(Imovel imovel, boolean empty) {
	                super.updateItem(imovel, empty);
	                if (imovel == null || empty) {
	                    setGraphic(null);
	                } else {
	                    setText(imovel.getProprietario().getNome() + " - " + imovel.getEndereco().getLogradouro() + ", " + imovel.getEndereco().getNumero());
	                }
	            }
	        }; 
	    }
	};

	imovelBox.setButtonCell(imovelCellFactory.call(null));
	imovelBox.setCellFactory(imovelCellFactory);
	
	clienteBox.setItems(FXCollections.observableArrayList(getClienteData()));
	
	Callback<ListView<Cliente>, ListCell<Cliente>> clienteCellFactory = new Callback<ListView<Cliente>, ListCell<Cliente>>() {

	    @Override
	    public ListCell<Cliente> call(ListView<Cliente> l) {
	        return new ListCell<Cliente>() {

	            @Override
	            protected void updateItem(Cliente cliente, boolean empty) {
	                super.updateItem(cliente, empty);
	                if (cliente == null || empty) {
	                    setGraphic(null);
	                } else {
	                    setText(cliente.getNome());
	                }
	            }
	        }; 
	    }
	};

	clienteBox.setButtonCell(clienteCellFactory.call(null));
	clienteBox.setCellFactory(clienteCellFactory);
	
Calendar cal = Calendar.getInstance();
	
	Date now = new Date();
	
	PauseTransition pause = new PauseTransition(Duration.seconds(1));
	vencimentosField.textProperty().addListener((observable, oldValue, newValue) -> {
		cal.setTime(now);
		 pause.setOnFinished(event -> correct(newValue, cal, now));
	        pause.playFromStart();
		
	});
	
}
	
	public void correct(String vencimentos, Calendar cal, Date now) {
		if (vencimentos.isBlank() || Integer.parseInt(vencimentos) == 0) {
			primeiraParcelaField.setText("");
		}else if (!valorField.getText().isBlank() && !vencimentos.isBlank() && Integer.parseInt(vencimentos) > 0 && Integer.parseInt(vencimentos) > cal.get(Calendar.DAY_OF_MONTH)) {
			Double pc1 = (Double.parseDouble(valorField.getText()) / 100) * 1;
			Double pc2 = (Double.parseDouble(valorField.getText()) / 100) * 2;
			
			Double valorPorDia = pc1/cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			Double diferencaDeVencimento = pc2 + (valorPorDia * (Integer.parseInt(vencimentos) - cal.get(Calendar.DAY_OF_MONTH)));
			primeiraParcela = Double.parseDouble(valorField.getText()) + diferencaDeVencimento;
		    primeiraParcelaField.setText("R$ " + StringsUtils.formatarDecimals(primeiraParcela));
			
		} else if (!valorField.getText().isBlank() && !vencimentos.isBlank() && Integer.parseInt(vencimentos) <= cal.get(Calendar.DAY_OF_MONTH)){
			Double pc1 = (Double.parseDouble(valorField.getText()) / 100) * 1;
			Double pc2 = (Double.parseDouble(valorField.getText()) / 100) * 2;
				Double valorPorDia = pc1/cal.getActualMaximum(Calendar.DAY_OF_MONTH);				
				LocalDate date = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				
				cal.add(Calendar.MONTH, 1);
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), Integer.parseInt(vencimentos));
				LocalDate date2 = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				
				Double diferencaDeVencimento = pc2 + (valorPorDia * Math.toIntExact(ChronoUnit.DAYS.between(date, date2)));
				
				primeiraParcela = Double.parseDouble(valorField.getText()) + diferencaDeVencimento;
			    primeiraParcelaField.setText("R$ " + StringsUtils.formatarDecimals(primeiraParcela));
				
			} else {
				primeiraParcelaField.setText("");
			}
	}
	
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public boolean isOkClicked() {
        return okClicked;
    }
    
    @FXML
	public void handleOnKeyPressed(KeyEvent e) throws FileNotFoundException {
		KeyCode code = e.getCode();		
		
		if (code == KeyCode.ENTER) {
			handleOk();
		}
		if (code == KeyCode.ESCAPE) {
			handleCancel();
		}
	}
    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    
    
    public void setContrato(Contrato contrato) {
    	this.contrato = contrato;
    	
    }
    
    
    @FXML
    private void handleOk() throws FileNotFoundException {
        if (isInputValid()) {
        	
        	DuplicataService ds = (DuplicataService)context.getBean("duplicataService");
        	ContratoRepository contRepo = (ContratoRepository)context.getBean("contratoRepository");
        	DuplicataRepository dupRepo = (DuplicataRepository)context.getBean("duplicataRepository");
        	ClienteRepository cliRepo = (ClienteRepository)context.getBean("clienteRepository");
        	ImovelRepository imRepo = (ImovelRepository)context.getBean("imovelRepository");
        	ImovelService is = (ImovelService)context.getBean("imovelService");
        	ClienteService cs = (ClienteService)context.getBean("clienteService");
        	Date date = new Date();
        	
        	contrato.setCliente(clienteBox.getValue());
        	contrato.setImovel(imovelBox.getValue());
        	contrato.setData(date);
        	contrato.setId(null);
        	contrato.setQtdParcelas(Integer.parseInt(tempoLocacaoField.getText()));
        	contrato.setValorDeCadaParcela(Double.parseDouble(valorField.getText()));
        	
        	List<Duplicata> dups; 
        	if(vencimentosField.getText().isBlank() ) {
        		dups = ds.preencherDuplicata(contrato, clienteBox.getValue());
        		
        	} else {
        		dups = ds.preencherDuplicata(contrato, Integer.parseInt(vencimentosField.getText()), clienteBox.getValue());
        	}
        
        	Cliente cliente = cs.find(clienteBox.getValue().getId());
        	cliente.setContrato(contrato);
        
        	Imovel imovel = is.find(imovelBox.getValue().getId());
        	imovel.setContrato(contrato);
        	
        	contrato.setActive(true);
        	contrato.setCliente(cliente);
        	contrato.setImovel(imovel);
        	contrato.setDuplicatas(dups);
        	
        	contrato = contRepo.save(contrato);
        	cliRepo.save(cliente);     	
        	dupRepo.saveAll(dups);        	
        	imRepo.save(imovel);
        	
        	Testemunha t1 = new Testemunha(testemunha1Field.getText(), testemunha1CpfField.getText());
        	Testemunha t2 = new Testemunha(testemunha2Field.getText(), testemunha2CpfField.getText());
        	try {
				pdfGen.geraContrato(contrato, t1, t2);
				//new TableHeader().manipulatePdf(path);
			} catch (Exception e) {
				System.out.println("Falha ao iniciar comando gerar contrato");
				e.printStackTrace();
			}
        	okClicked = true;
        	
        	dialogStage.close();
        	
        }
    }
    
    private boolean isInputValid() {
        String errorMessage = "";

        if (clienteBox.getValue() == null) {
            errorMessage += "Selecione um cliente\n"; 
        }
        if (imovelBox.getValue() == null) {
            errorMessage += "Selecione um Imovel\n"; 
        }
        if (tempoLocacaoField.getText().isBlank()) {
            errorMessage += "Informe um tempo de locação\n"; 
        }

        if (valorField.getText().isBlank()) {
            errorMessage += "Informe o valor da mensalidade\n"; 
        }

        if (vencimentosField.getText().length() >= 3) {
            errorMessage += "Data de vencimentos inválida\n"; 
        }
        
        if (testemunha1Field.getText() == null || testemunha1Field.getText().isBlank()) {
            errorMessage += "Informe a testemunha 1\n"; 
        }
        
        if (testemunha1CpfField.getText() == null || testemunha1CpfField.getText().isBlank() || testemunha1CpfField.getText().length() != 11) {
            errorMessage += "CPF da testemunha 1 inválido\n"; 
        }
        
        if (testemunha1Field.getText() == null || testemunha2Field.getText().isBlank()) {
            errorMessage += "Informe a testemunha 2\n"; 
        }
        
        if (testemunha2Field.getText() == null || testemunha2CpfField.getText().isBlank() || testemunha2CpfField.getText().length() != 11) {
            errorMessage += "CPF da testemunha 2 inválido\n"; 
        }

        

        if (errorMessage.length() == 0) {
            return true;
        } else {
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.initStyle(StageStyle.UNIFIED);
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