package com.teste.campanha.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teste.campanha.modelos.Campanha;
import com.teste.campanha.modelos.Cliente;

@Repository
public interface RepositorioCliente extends JpaRepository<Cliente, Long>{
	
	@Query(value =
			"SELECT * " +
			"FROM cliente " +
			"WHERE email = ?1"
		, nativeQuery = true)
	public List<Cliente> clientesPorEmail(String email);
	
	@Query(value =
			"SELECT * " +
			"FROM cliente " +
			"WHERE time_id = ?1"
		, nativeQuery = true)
	public List<Cliente> clientesPorTime(String timeId);

}
