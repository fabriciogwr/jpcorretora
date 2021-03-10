package com.fgwr.jpcorretora;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fgwr.jpcorretora.services.PopularBanco;

import javafx.application.Application;

@SpringBootApplication
public class JpcorretoraApplication implements CommandLineRunner {
	
	@Autowired
	PopularBanco pb;
	
	public static void main(String[] args) {
		Application.launch(FrontApp.class, args);

	}
 
	@Override
	public void run(String... args) throws Exception {

		//pb.popularBanco();
	}



}