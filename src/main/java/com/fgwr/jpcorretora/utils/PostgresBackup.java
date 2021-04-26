package com.fgwr.jpcorretora.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.StageStyle;

public class PostgresBackup {

	public static void realizaBackup(String path, String nome) throws IOException, InterruptedException {

		final List<String> comandos = new ArrayList<String>();
		comandos.add("C:\\Arquivos de programas\\PostgreSQL\\13\\bin\\pg_dump.exe"); // esse é meu caminho

		comandos.add("-U");
		comandos.add("postgres");
		comandos.add("-w");
		comandos.add("-F");
		comandos.add("t");
		comandos.add("-d");
		comandos.add("jpcorretora");
		comandos.add("-f");
		comandos.add(path + "\\" + nome);

		ProcessBuilder pb = new ProcessBuilder(comandos);

		try {
			final Process process = pb.start();

			final BufferedReader r = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String line = r.readLine();
			while (line != null) {
				System.err.println(line);
				line = r.readLine();
			}
			r.close();

			process.waitFor();
			process.destroy();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Base de Dados Salva");
			alert.setHeaderText("O Backup do banco de dados foi concluído. Em alguns instantes, a cópia dos dados adicionais será finalizada.");
			alert.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

	}

	public static void realizaRestore(String input) throws IOException, InterruptedException {
		final List<String> comandos = new ArrayList<String>();

		comandos.add("C:\\Arquivos de programas\\PostgreSQL\\13\\bin\\pg_restore.exe"); // testado no windows xp

		comandos.add("-U");
		comandos.add("postgres");
		comandos.add("-d");
		comandos.add("jpcorretora");

		comandos.add(input); // eu utilizei meu C:\ e D:\ para os testes e gravei o backup com sucesso.

		ProcessBuilder pb = new ProcessBuilder(comandos);

		try {
			final Process process = pb.start();

			final BufferedReader r = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String line = r.readLine();
			while (line != null) {
				System.err.println(line);
				line = r.readLine();
			}
			r.close();

			process.waitFor();
			process.destroy();
			JOptionPane.showMessageDialog(null, "Restore realizado com sucesso.");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

	}

}
