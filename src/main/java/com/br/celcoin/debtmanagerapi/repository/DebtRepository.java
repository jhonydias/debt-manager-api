package com.br.celcoin.debtmanagerapi.repository;

import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {
}
