package com.fgwr.jpcorretora.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Contrato;

@Repository
public interface ContratoRepository extends JpaRepository <Contrato, Integer> {

	@Transactional(readOnly=true)
	Contrato findByCliente(Cliente cliente);
	
	List<Contrato> findByActive(Boolean active);
}
