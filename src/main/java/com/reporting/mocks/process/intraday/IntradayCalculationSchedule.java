package com.reporting.mocks.process.intraday;

import com.reporting.mocks.model.risks.RiskType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    WARNING: Not thread safe
 */
public class IntradayCalculationSchedule {
    private Integer marketCounter;                          // current counter periods
    private Map<Integer, List<RiskType>> risksSchedule;     // association between counter periods and lists of risks
    private List<Integer> moduloSchedule;                   // list of counters used to determine which counter periods are active
    private Integer maxSchedule;                            // maximum period, at which point the counter resets to 0

    public IntradayCalculationSchedule() {
        this.marketCounter = 0;
        this.risksSchedule = new HashMap<>();
        this.moduloSchedule = new ArrayList<>();
        this.maxSchedule = 0;
    }

    public void add(Integer period, RiskType riskType) {
        if (risksSchedule.containsKey(period)) {
            risksSchedule.get(period).add(riskType);
        }
        else {
            List<RiskType> newRiskSchedule = new ArrayList<>();
            newRiskSchedule.add(riskType);
            risksSchedule.put(period, newRiskSchedule);
            moduloSchedule.add(period);
            maxSchedule = period > maxSchedule ? period : maxSchedule;
        }
    }

    public List<RiskType> getRisksToRun() {
        List<RiskType> risksToRun = new ArrayList<>();
        for(Integer period : moduloSchedule) {
            if (marketCounter % period == 0) {
                risksToRun.addAll(this.risksSchedule.get(period));
            }
        }
        return risksToRun;
    }

    public void increment() {
        if (marketCounter + 1 > maxSchedule) {
            marketCounter = 1;
        }
        else {
            marketCounter++;
        }
    }
}
