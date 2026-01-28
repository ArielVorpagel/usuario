package com.javanauta.usuario.infrastrcture.repository;


import com.javanauta.usuario.infrastrcture.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
