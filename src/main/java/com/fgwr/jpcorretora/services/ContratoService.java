package com.fgwr.jpcorretora.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.repositories.ContratoRepository;
import com.fgwr.jpcorretora.services.exceptions.ObjectNotFoundException;

@Service
public class ContratoService {

	@Autowired
	ContratoRepository repo;

	public Contrato insert(Contrato ctr) {
		Contrato contrato = ctr;
		repo.save(contrato);
		return contrato;
	}
	
	public Contrato find(Integer id) {

		Optional<Contrato> contrato = repo.findById(id);
		return contrato.orElseThrow(() -> new ObjectNotFoundException("Contrato não encontrada!")); 
		

	}

	public List<Contrato> findAll() {
		return repo.findAll();
	}
	
	
	
/*public List<Contrato> findCpfOuCnpj(String cpfOuCnpj) {
		
		List<Contrato> dup = repo.findCpfOuCnpj(cpfOuCnpj);
		if (dup == null) {
			throw new ObjectNotFoundException("Contratos não encontradas!");
		}
		return dup;
	}*/



	

	public Contrato findByCliente(Cliente cliente) {

		Contrato contrato = repo.findByCliente(cliente);
		if (contrato == null) {
			throw new ObjectNotFoundException("Contratos não encontradas!");
		}
		return contrato;
	}
}
