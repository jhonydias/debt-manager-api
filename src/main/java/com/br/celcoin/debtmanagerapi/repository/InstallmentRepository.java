package com.br.celcoin.debtmanagerapi.repository;

import com.br.celcoin.debtmanagerapi.model.entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long> {
}
