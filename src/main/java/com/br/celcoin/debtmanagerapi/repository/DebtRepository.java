package com.br.celcoin.debtmanagerapi.repository;

import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.model.enums.DebtStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long>, JpaSpecificationExecutor<Debt> {
    @Query("SELECT d.id FROM Debt d WHERE d.status = :status")
    Page<Long> findIdsByStatus(@Param("status") DebtStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"installments"})
    List<Debt> findByIdIn(List<Long> ids);
}
