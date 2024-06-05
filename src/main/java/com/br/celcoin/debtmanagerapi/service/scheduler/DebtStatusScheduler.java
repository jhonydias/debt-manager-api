package com.br.celcoin.debtmanagerapi.service.scheduler;

import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.model.enums.DebtStatus;
import com.br.celcoin.debtmanagerapi.repository.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DebtStatusScheduler {

    private static final int PAGE_SIZE = 100; // Tamanho do lote

    private final DebtRepository debtRepository;

    @Autowired
    public DebtStatusScheduler(DebtRepository debtRepository) {
        this.debtRepository = debtRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")  // Executa todos os dias à meia-noite
    public void checkAndUpdateAllDebtStatus() {
        int pageNumber = 0;
        Page<Debt> debtPage;

        do {
            debtPage = debtRepository.findAll(PageRequest.of(pageNumber, PAGE_SIZE));
            for (Debt debt : debtPage.getContent()) {
                if (debt.isOverdue() && debt.getStatus() != DebtStatus.OVERDUE) {
                    debt.setStatus(DebtStatus.OVERDUE);
                    debtRepository.save(debt);
                }
            }
            pageNumber++;
        } while (debtPage.hasNext());
    }
}
