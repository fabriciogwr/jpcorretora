package com.fgwr.jpcorretora.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Imovel;

@Repository
public interface ImovelRepository extends JpaRepository <Imovel, Integer>{
	
	Imovel findByContrato(Contrato contrato);

}
