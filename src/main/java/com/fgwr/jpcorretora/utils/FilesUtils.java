package com.fgwr.jpcorretora.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.StringUtils;

import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Recibo;
import com.fgwr.jpcorretora.domain.ReciboAvulso;

public class FilesUtils {

	public static String fileToString(File file) {
		try {
			return file.toURI().toURL().toString();
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	public static String pathRecibos(Recibo rec) {
		String[] nomeArr = StringUtils.split(rec.getCliente().getNome());
		String docFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		String path = (docFolder + "\\Recibos\\" + rec.getId().toString() + " - " + nomeArr[0] + " " + nomeArr[1] + ".pdf");
		return path;
	}
	
	public static String pathContratos(Contrato c) {
		String[] nomeArr = StringUtils.split(c.getCliente().getNome());
		String docFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		String path = (docFolder + "\\Contratos\\" + c.getId().toString() + " - " + nomeArr[0] + " " + nomeArr[1] + ".pdf");
		return path;
	}
	
	public static String pathReceitas() {
		String docFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		String path = (docFolder + "\\Receitas\\");
		return path;
	}
	
	public static String pathContratos(Contrato c, boolean edit) {
		String[] nomeArr = StringUtils.split(c.getCliente().getNome());
		String docFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		String path = (docFolder + "\\Contratos\\" + c.getId().toString() + " - " + nomeArr[0] + " " + nomeArr[1] + ".pdf");
		if (edit) {
			File tmp = new File(path);
			if (tmp.exists()) {
				Integer lengthNome = nomeArr[0].length() + nomeArr[1].length() + 1;
				Integer pathNoExt = path.length() - 4;
				StringBuilder sb = new StringBuilder(path);
				sb.insert(path.length() - 4, " - a");
				path = sb.toString();
				tmp = new File(path);
				if (tmp.exists()) {
					sb.replace(path.length() -5, path.length() -5, "b");
					path = sb.toString();
					tmp = new File(path);
					if (tmp.exists()) {
						sb.replace(path.length() -5, path.length() -5, "c");
						path = sb.toString();
						tmp = new File(path);
					}
				}
				
				
			}
		}
		
		return path;
	}

	public static String pathRecibosAvulso(ReciboAvulso rec) {
		String dataHora = StringsUtils.formatarDataArquivo(rec.getDataPgto());
		String[] nomeArr = StringUtils.split(rec.getRecebedor());
		String docFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		String path = (docFolder + "\\Recibos\\" + dataHora + " - " + nomeArr[0] + " " + nomeArr[1] + ".pdf");
		return path;
	}
}
