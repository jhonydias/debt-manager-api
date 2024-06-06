package com.br.celcoin.debtmanagerapi.scheduler;

import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.model.enums.DebtStatus;
import com.br.celcoin.debtmanagerapi.repository.DebtRepository;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DebtStatusScheduler {

    private static final int PAGE_SIZE = 100; // Tamanho do lote

    private final DebtRepository debtRepository;

    @Autowired
    public DebtStatusScheduler(DebtRepository debtRepository) {
        this.debtRepository = debtRepository;
    }

    @Scheduled(cron = "${scheduler.cron}")
    @SchedulerLock(name = "DebtStatusScheduler_checkAndUpdateAllDebtStatus", lockAtMostFor = "PT30M", lockAtLeastFor = "PT1M")
    public void checkAndUpdateAllDebtStatus() {
        int pageNumber = 0;
        Page<Long> debtIdPage;

        do {
            debtIdPage = debtRepository.findIdsByStatus(DebtStatus.PENDING, PageRequest.of(pageNumber, PAGE_SIZE));
            List<Debt> debts = debtRepository.findByIdIn(debtIdPage.getContent());

            for (Debt debt : debts) {
                if (debt.isOverdue() && debt.getStatus() != DebtStatus.OVERDUE) {
                    debt.setStatus(DebtStatus.OVERDUE);
                    debtRepository.save(debt);
                }
            }
            pageNumber++;
        } while (debtIdPage.hasNext());
    }
}
