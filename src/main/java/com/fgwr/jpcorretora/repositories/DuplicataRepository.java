package com.fgwr.jpcorretora.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Duplicata;

@Repository
public interface DuplicataRepository extends JpaRepository<Duplicata, Integer>{

	//@Query("select dup from Duplicata dup where dup.contrato.id = :contrato")
	//public List<Duplicata> findByContrato(@Param("contrato") Integer contrato);

	@Query("select dup from Duplicata dup where dup.contrato.id = (select id from Contrato where cliente.id = (select id from Cliente where cpf_ou_cnpj= :cpfoucnpj))")
	public List<Duplicata> findCpfOuCnpj(@Param("cpfoucnpj") String cpfOuCnpj);
	
	@Transactional(readOnly=true)
	@Query("select dup from Duplicata dup where dup.contrato.id = (select id from Contrato where cliente.id = (select id from Cliente where nome= :nome))")
	public List<Duplicata> findCliente(@Param("nome") String nome);
	
	List<Duplicata> findByEstado(Integer id);
	
	List<Duplicata> findByDataVencimento(Date data);
	
	List<Duplicata> findByContrato(Contrato contrato);

	public List<Duplicata> findByCliente(Cliente cliente);

	public List<Duplicata> findByClienteAndEstadoNot(Cliente cliente, Integer estado);
	
}
