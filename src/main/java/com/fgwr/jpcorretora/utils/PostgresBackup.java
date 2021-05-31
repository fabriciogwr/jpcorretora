package com.fgwr.jpcorretora.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

public class PostgresBackup {

	static String dir = System.getProperty("user.dir");

	public static void realizaBackup(String path, String nome) throws IOException, InterruptedException {

		final List<String> comandos = List.of("C:\\Arquivos de programas\\PostgreSQL\\13\\bin\\pg_dump.exe", "-U",
				"postgres", "-w", "-F", "t", "-d", "jpcorretora", "-f", path + "\\" + nome);

		ProcessBuilder pb = new ProcessBuilder(comandos);
		pb.environment().put("PGPASSWORD", "jpcorretora");

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
			alert.setHeaderText(
					"O Backup do banco de dados foi concluído. Em alguns instantes, a cópia dos dados adicionais será finalizada.");
			alert.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

	}

	public static void realizaRestore(String input) throws IOException, InterruptedException {
		final List<String> comandos = List.of("C:\\Arquivos de programas\\PostgreSQL\\13\\bin\\pg_restore.exe", "-U",
				"postgres", "-d", "jpcorretora", input);

		final List<String> drops = List.of("C:\\Arquivos de programas\\PostgreSQL\\13\\bin\\psql.exe", "-U", "postgres",
				"-d", "jpcorretora", "-f", dir + "\\postgres\\drops.sql");

		ProcessBuilder pbDrops = new ProcessBuilder(drops);
		pbDrops.environment().put("PGPASSWORD", "jpcorretora");

		try {
			final Process process = pbDrops.start();

			final BufferedReader r = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String line = r.readLine();
			while (line != null) {
				System.err.println(line);
				line = r.readLine();
			}
			r.close();

			process.waitFor();
			process.destroy();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		ProcessBuilder pb = new ProcessBuilder(comandos);
		pb.environment().put("PGPASSWORD", "jpcorretora");

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
			alert.setTitle("Base de Dados Restaurada");
			alert.setHeaderText(
					"A restauração do banco de dados foi concluída. Em alguns instantes, a cópia dos dados adicionais será finalizada.");
			alert.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

	}

}
