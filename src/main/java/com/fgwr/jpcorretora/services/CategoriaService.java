package com.fgwr.jpcorretora.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fgwr.jpcorretora.domain.Categoria;
import com.fgwr.jpcorretora.repositories.CategoriaRepository;
import com.fgwr.jpcorretora.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	CategoriaRepository repo;

	public Categoria insert(Categoria ctr) {
		Categoria categoria = ctr;
		repo.save(categoria);
		return categoria;
	}
	
	public Categoria find(Integer id) {

		Optional<Categoria> categoria = repo.findById(id);
		return categoria.orElseThrow(() -> new ObjectNotFoundException("Categoria não encontrada!")); 
		

	}

	public List<Categoria> findAll() {
		return repo.findAll();
	}
	
	
	
/*public List<Categoria> findCpfOuCnpj(String cpfOuCnpj) {
		
		List<Categoria> dup = repo.findCpfOuCnpj(cpfOuCnpj);
		if (dup == null) {
			throw new ObjectNotFoundException("Categorias não encontradas!");
		}
		return dup;
	}*/



	

	public Categoria findByNome(String nome) {

		Categoria categoria = repo.findByNome(nome);
		if (categoria == null) {
			throw new ObjectNotFoundException("Categorias não encontradas!");
		}
		return categoria;
	}
}
