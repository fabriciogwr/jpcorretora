package com.fgwr.jpcorretora.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fgwr.jpcorretora.domain.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria,Integer>{

	List<Categoria> findByTipo(Integer tipo);

	Categoria findByNome(String nome);
}
