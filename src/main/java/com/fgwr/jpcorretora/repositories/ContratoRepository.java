package com.fgwr.jpcorretora.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.domain.Contrato;

@Repository
public interface ContratoRepository extends JpaRepository <Contrato, Integer> {

	@Transactional(readOnly=true)
	Contrato findByCliente(String cliente);
}
