package com.fgwr.jpcorretora.views;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.utils.DirectoryCopy;
import com.fgwr.jpcorretora.utils.FilesUtils;
import com.fgwr.jpcorretora.utils.PostgresBackup;
import com.fgwr.jpcorretora.utils.StringsUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ManutencaoController {

	FrontApp frontApp = new FrontApp();
	private Stage dialogStage;
	private Stage primaryStage;
	ApplicationContext context = SpringContext.getAppContext();
	String docFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
	File selectedDirectory = new File(docFolder + "\\Backups");
	File selectedRestoreDirectory = null;

	@FXML
	private Label pathLabel;
	@FXML
	private Button alteraDestBtn;
	@FXML
	private CheckBox iDadosImoveisChk;
	@FXML
	private CheckBox iRecibosChk;
	@FXML
	private CheckBox iContratosChk;
	@FXML
	private Button realizarBackupBtn;
	@FXML
	private Label restorePathLabel;
	@FXML
	private Button selectBackupBtn;
	@FXML
	private CheckBox rDadosImoveisChk;
	@FXML
	private CheckBox rRecibosChk;
	@FXML
	private CheckBox rContratosChk;
	@FXML
	private Button restaurarBackupBtn;
	

	@FXML
	private void initialize() {

		pathLabel.setText(docFolder + "\\Backups");
		restorePathLabel.setText("");

		alteraDestBtn.setOnAction(e -> {
			alteraDestino();
		});

		realizarBackupBtn.setOnAction(e -> {
			try {
				realizaBackup();
			} catch (IOException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		selectBackupBtn.setOnAction(e -> {
			alteraOrigem();
		});

		restaurarBackupBtn.setOnAction(e -> {
			try {
				restauraBackup();
			} catch (IOException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

	}

	public void setMainApp(FrontApp frontApp) {
		this.frontApp = frontApp;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	@FXML
	private void alteraDestino() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Selecionar local do backup");
		directoryChooser.setInitialDirectory(new File(docFolder + "\\Backups"));
		selectedDirectory = directoryChooser.showDialog(primaryStage);
		pathLabel.setText(selectedDirectory.getAbsolutePath());
	}

	@FXML
	private void realizaBackup() throws IOException, InterruptedException {
		Calendar cal = Calendar.getInstance();
		String dataHora = StringsUtils.formatarDataBackup(cal.getTime());
		String nome =  dataHora + ".tar";
		new File(docFolder + "/Backups/" + dataHora).mkdir();
		PostgresBackup.realizaBackup(selectedDirectory.getAbsolutePath() + "\\" + dataHora, nome);
		if (iDadosImoveisChk.isSelected()) {
			Path src = Path.of(docFolder, "\\Imoveis");
			Path dest = Path.of(selectedDirectory + "\\" + dataHora + "\\Imoveis");
			DirectoryCopy.copyFolder(src, dest);
		}
		
		if (iContratosChk.isSelected()) {
			Path src = Path.of(docFolder, "\\Contratos");
			Path dest = Path.of(selectedDirectory + "\\" + dataHora + "\\Contratos");
			DirectoryCopy.copyFolder(src, dest);
		}
		
		if (iRecibosChk.isSelected()) {
			Path src = Path.of(docFolder, "\\Recibos");
			Path dest = Path.of(selectedDirectory + "\\" + dataHora + "\\Recibos");
			DirectoryCopy.copyFolder(src, dest);
		}
		
	}

	@FXML
	private void alteraOrigem() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selecionar local do backup");
		fileChooser.setInitialDirectory(new File(docFolder + "\\Backups"));
		selectedRestoreDirectory = fileChooser.showOpenDialog(primaryStage);
		restorePathLabel.setText(selectedRestoreDirectory.getAbsolutePath());
	}

	@FXML
	private void restauraBackup() throws IOException, InterruptedException {
		if (selectedRestoreDirectory != null) {
			System.out.println(selectedRestoreDirectory.getAbsolutePath());
			PostgresBackup.realizaRestore(selectedRestoreDirectory.getAbsolutePath());
			if (rDadosImoveisChk.isSelected()) {
				Path src = Path.of(selectedRestoreDirectory.getAbsoluteFile().getParent(), "\\Imoveis");
				FileUtils.deleteDirectory(new File(docFolder + "\\Imoveis"));
				Path dest = Path.of(docFolder + "\\Imoveis");
				DirectoryCopy.copyFolder(src, dest);
			}
			
			if (rContratosChk.isSelected()) {
				Path src = Path.of(selectedRestoreDirectory.getAbsoluteFile().getParent(), "\\Contratos");
				FileUtils.deleteDirectory(new File(docFolder + "\\Contratos"));
				Path dest = Path.of(docFolder + "\\Contratos");
				DirectoryCopy.copyFolder(src, dest);
			}
			
			if (rRecibosChk.isSelected()) {
				Path src = Path.of(selectedRestoreDirectory.getAbsoluteFile().getParent(), "\\Recibos");
				FileUtils.deleteDirectory(new File(docFolder + "\\Recibos"));
				Path dest = Path.of(docFolder + "\\Recibos");
				DirectoryCopy.copyFolder(src, dest);
			}
			
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Nenhum arquivo de backup selecionado");
			alert.setHeaderText("Selecione um arquivo para restaurar");
			alert.showAndWait();
		}
	}

}
