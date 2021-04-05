package com.fgwr.jpcorretora.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Proprietario;

@Repository
public interface ImovelRepository extends JpaRepository <Imovel, Integer>{
	
	Imovel findByContrato(Contrato contrato);

	List<Imovel> findByProprietario(Proprietario p);

}
