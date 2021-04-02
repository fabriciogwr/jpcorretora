package com.fgwr.jpcorretora;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fgwr.jpcorretora.services.PopularBanco;

import javafx.application.Application;

@SpringBootApplication
public class JpcorretoraApplication implements CommandLineRunner {
	
	@Autowired
	PopularBanco pb;
	
	
	
	public static void main(String[] args) throws MalformedURLException {
			String docFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
			new File(docFolder + "/Recibos").mkdir();
			new File(docFolder + "/Imoveis").mkdir();
			new File(docFolder + "/Contratos").mkdir();
			
		Application.launch(FrontApp.class, args);
		

	}
 
	@Override
	public void run(String... args) throws Exception {

		//pb.popularBanco();
	}



}