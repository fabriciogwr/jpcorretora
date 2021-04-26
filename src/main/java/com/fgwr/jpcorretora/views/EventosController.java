package com.fgwr.jpcorretora.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.repositories.ContratoRepository;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class EventosController {
	
    @FXML
    private TableView<Duplicata> pagamentosTable;
    @FXML
    private TableColumn<Duplicata, String> clientePColumn;
    @FXML
    private TableColumn<Duplicata, String> contratoPColumn;
    @FXML
    private TableColumn<Duplicata, String> parcelaPColumn;
    @FXML
    private TableColumn<Duplicata, String> vencimentoPColumn;
    @FXML
    private TableColumn<Duplicata, String> valorPColumn;
    @FXML
    private TableView<Contrato> contratosTable;
    @FXML
    private TableColumn<Contrato, String> numeroCColumn;
    @FXML
    private TableColumn<Contrato, String> clienteCColumn;
    @FXML
    private TableColumn<Contrato, String> vigenciaCColumn;
    @FXML
    private TableColumn<Contrato, String> imovelCColumn;
    @FXML
    private TableColumn<Contrato, String> fimCColumn;
    
    
    FrontApp frontApp = new FrontApp();
	ApplicationContext context = SpringContext.getAppContext();
	
	List<Contrato> filterContratos = new ArrayList<>();
	List<Duplicata> filterDups = new ArrayList<>();
    
	
    private List<Duplicata> getDuplicataData() {
    	DuplicataRepository dupRepo = (DuplicataRepository) context.getBean("duplicataRepository");
    	
    	List<Duplicata> dups = dupRepo.findAll();
    	return dups;
    }
    
    private List<Contrato> getContratoData() {
    	ContratoRepository contRepo = (ContratoRepository) context.getBean("contratoRepository");
    	List<Contrato> contratos = contRepo.findAll();
    	
		return contratos;		
    }
    
    
	public void initialize() {
    	Calendar cal = new GregorianCalendar();
    	cal = Calendar.getInstance();
    	cal.add(Calendar.DAY_OF_MONTH, -5);
    	Date vencimentoStart = cal.getTime();
    	
    	cal.add(Calendar.DAY_OF_MONTH, 13);
    	Date vencimentoEnd = cal.getTime();
    	
    	List<Duplicata> dups = getDuplicataData();
    	for (Duplicata duplicata : dups) {
			if (duplicata.getDataVencimento().after(vencimentoStart) && duplicata.getDataVencimento().before(vencimentoEnd)) {
				filterDups.add(duplicata);
			}
			
		}
    	
    	
    	
    	cal = Calendar.getInstance();
    	cal.add(Calendar.DAY_OF_MONTH, -5);
    	
    	Date start = cal.getTime();
    	cal.add(Calendar.DAY_OF_MONTH, 13);
    	
    	Date end = cal.getTime();
    	
    	List<Contrato> contratos = getContratoData();
    	
    	for (Contrato contrato : contratos) {
    		cal.setTime(contrato.getData());
    		cal.add(Calendar.MONTH, contrato.getQtdParcelas()-1);
    		Date dataFim = cal.getTime();
    		
    		if (dataFim.after(start) && dataFim.before(end)) {
				filterContratos.add(contrato);
				
			}
		}
    	
    	
    	
    	pagamentosTable.setItems(FXCollections.observableArrayList(filterDups));
    	
    	clientePColumn.setCellValueFactory(cellData -> cellData.getValue().cliente());
    	parcelaPColumn.setCellValueFactory(cellData -> cellData.getValue().parcela());
    	vencimentoPColumn.setCellValueFactory(cellData -> cellData.getValue().vencimento());
    	valorPColumn.setCellValueFactory(cellData -> cellData.getValue().valor());
    	contratoPColumn.setCellValueFactory(cellData -> cellData.getValue().contrato());
    	
    	
    	
    	contratosTable.setItems(FXCollections.observableArrayList(filterContratos));
    		clienteCColumn.setCellValueFactory(cellData -> cellData.getValue().cliente());
			numeroCColumn.setCellValueFactory(cellData -> cellData.getValue().num());
			vigenciaCColumn.setCellValueFactory(cellData -> cellData.getValue().vigencia());
			imovelCColumn.setCellValueFactory(cellData -> cellData.getValue().imovel());
			fimCColumn.setCellValueFactory(cellData -> cellData.getValue().dataFim());
			contratosTable.getSortOrder().add(fimCColumn);

		}
    public void setMainApp(FrontApp frontApp) {
		this.frontApp = frontApp;
	}   
    

    	
    }





