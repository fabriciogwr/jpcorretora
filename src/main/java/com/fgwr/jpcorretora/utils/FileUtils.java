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
}
