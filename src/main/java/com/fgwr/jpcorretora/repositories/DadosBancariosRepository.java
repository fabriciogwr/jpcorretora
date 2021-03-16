package com.fgwr.jpcorretora.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Proprietario;

@Repository
public interface DadosBancariosRepository extends JpaRepository <DadosBancarios, Integer>{

	DadosBancarios findByCliente(Cliente cliente);

	DadosBancarios findByProprietario(Proprietario proprietarioAux);

}
