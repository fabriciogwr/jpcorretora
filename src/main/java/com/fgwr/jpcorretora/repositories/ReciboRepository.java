package com.fgwr.jpcorretora.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Despesa;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Recibo;

@Repository
public interface ReciboRepository extends JpaRepository <Recibo, Integer>{
	
	Recibo findByDuplicata(Duplicata dup);
	Recibo findByDespesa(Despesa ds);
	
	Recibo findByCliente(Cliente cli);

}
