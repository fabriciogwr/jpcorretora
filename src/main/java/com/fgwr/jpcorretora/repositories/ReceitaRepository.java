package com.fgwr.jpcorretora.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fgwr.jpcorretora.domain.Receita;

public interface ReceitaRepository extends JpaRepository <Receita, Integer>{
	
	List<Receita> findAllByDataRecebimentoBetween(Date start, Date end);
	
	List<Receita> findAllByDataVencimentoBetween(Date start, Date end);

}
