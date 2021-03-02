package com.fgwr.jpcorretora.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Referencia;

@Repository
public interface ReferenciaRepository extends JpaRepository <Referencia, Integer> {

	List<Referencia> findByCliente(Cliente cliente);
}
