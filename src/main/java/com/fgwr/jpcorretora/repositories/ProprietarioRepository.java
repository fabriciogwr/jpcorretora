package com.fgwr.jpcorretora.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fgwr.jpcorretora.domain.Proprietario;

public interface ProprietarioRepository extends JpaRepository<Proprietario, Integer>{
	
	List<Proprietario> findByActive(Boolean active);

}
