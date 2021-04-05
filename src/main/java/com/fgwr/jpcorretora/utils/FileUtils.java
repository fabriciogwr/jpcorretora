package com.fgwr.jpcorretora.utils;

import java.io.File;
import java.net.MalformedURLException;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.StringUtils;

import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Recibo;

public class FileUtils {

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
}
