package com.fgwr.jpcorretora.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fgwr.jpcorretora.domain.Despesa;

public interface DespesaRepository extends JpaRepository <Despesa, Integer>{
	
	List<Despesa> findAllByDataPagamentoBetween(Date start, Date end);
	
	List<Despesa> findAllByDataVencimentoBetween(Date start, Date end);

}
