package com.br.celcoin.debtmanagerapi.repository.specification;

import com.br.celcoin.debtmanagerapi.model.dto.request.DebtSeachRequestDto;
import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import org.springframework.data.jpa.domain.Specification;

public class DebtSpecification {

    private DebtSpecification() {}

    public static Specification<Debt> search(DebtSeachRequestDto dto) {
        Specification<Debt> specification = Specification.where(null);

        if (dto.creditorName() != null && !dto.creditorName().isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("creditorName")), "%" + dto.creditorName().toLowerCase() + "%"));
        }
        if (dto.status() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), dto.status()));
        }
        if (dto.startDate() != null && dto.endDate() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("dueDate"), dto.startDate(), dto.endDate()));
        }
        if (dto.principalAmount() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get("principalAmount"), dto.principalAmount()));
        }
        return specification;

    }
}
