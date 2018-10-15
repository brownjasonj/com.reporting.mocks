package com.reporting.mocks.persistence.Mongo;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.id.CalculationContextId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface CalculationContextRepository extends MongoRepository<CalculationContext, CalculationContextId> {
    CalculationContext findCalculationContextByCalculationContextId(CalculationContextId calculationContextId);
    List<CalculationContext> findCalculationContextByPricingGroup(PricingGroup pricingGroup);
}
