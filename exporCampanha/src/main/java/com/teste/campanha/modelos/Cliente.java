package com.teste.campanha.modelos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(columnDefinition = "text")
	private String nome;
	
	@Column(columnDefinition = "text")
	private String email;

	@Temporal(TemporalType.TIMESTAMP)
	private Date nascimento;
	
	public Long getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@JsonSerialize
	@JsonDeserialize
	@Transient
	private Long timeId;

	@ManyToOne
	private Time time;
	
	public Time getTime() {
		return time;
	}

	@OneToMany(mappedBy = "cliente")
	private List<CampanhaCliente> campanhasCliente = new ArrayList<>();
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}
	
	public Long getTimeId() {
		return timeId;
	}

	public void setTimeId(Long timeId) {
		this.timeId = timeId;
	}
	
	public void setTime(Time time) {
		this.time = time;
	}
}
