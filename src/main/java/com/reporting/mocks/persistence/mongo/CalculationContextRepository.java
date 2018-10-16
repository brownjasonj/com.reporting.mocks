package com.reporting.mocks.persistence.mongo;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.id.CalculationContextId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CalculationContextRepository extends MongoRepository<CalculationContext, CalculationContextId> {
    CalculationContext findCalculationContextByCalculationContextId(CalculationContextId calculationContextId);
    List<CalculationContext> findCalculationContextByPricingGroup(PricingGroup pricingGroup);
}
