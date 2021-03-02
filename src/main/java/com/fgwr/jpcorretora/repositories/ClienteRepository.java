package com.fgwr.jpcorretora.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	@Transactional(readOnly=true)
	Cliente findByCpfOuCnpj(String cpfOuCnpj);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM telefone_cli WHERE cliente_id = :id", nativeQuery = true)
	void deleteByCliente_Id(@Param("id") Integer id);
	
	

}
