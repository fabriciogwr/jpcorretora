package com.fgwr.jpcorretora.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Proprietario;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.services.exceptions.ObjectNotFoundException;

@Service
public class ImovelService {

	@Autowired
	ImovelRepository repo;

	public Imovel insert(Imovel imv) {
		Imovel imovel = imv;
		repo.save(imovel);
		return imovel;
	}
	
	public Imovel find(Integer id) {

		Optional<Imovel> imovel = repo.findById(id);
		return imovel.orElseThrow(() -> new ObjectNotFoundException("Imovel não encontrado!")); 
		

	}
	
	public List<Imovel> findAll() {
		return repo.findAll();
	}
	
	
	

	public List<Imovel> findByProprietario(Proprietario p ) {
		
		List<Imovel> imvAllProp = repo.findByProprietario(p);
		return imvAllProp;
	}

	
	public Imovel findByContrato(Contrato cont) {
		return repo.findByContrato(cont);
	}
	
}
