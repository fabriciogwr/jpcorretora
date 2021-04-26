package com.fgwr.jpcorretora.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.domain.Corretor;

@Repository
public interface CorretorRepository extends JpaRepository<Corretor, Integer> {
	
	@Transactional(readOnly=true)
	Corretor findByCpfOuCnpj(String cpfOuCnpj);
	
	List<Corretor> findByActive(Boolean active);

}
