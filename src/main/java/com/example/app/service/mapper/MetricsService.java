package com.example.app.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final MetricsEndpoint metricsEndpoint;

    public String getDiskInfoInGB(String tag) {
        Double systemDiskInfoInBytes = metricsEndpoint.metric(tag, null)
                .getMeasurements()
                .stream()
                .filter(Objects::nonNull)
                .findFirst()
                .map(MetricsEndpoint.Sample::getValue)
                .filter(Double::isFinite)
                .orElse(0.0D);
        Double systemDiskFreeInGB = systemDiskInfoInBytes / Math.pow(10, 9);
        return new DecimalFormat("#0.00").format(systemDiskFreeInGB).replace(",", ".");
    }

    public String getValueByTagAndTagValueRepresentation(String tag, String tagValueRepresentation) {
        Double cpuUsage = metricsEndpoint.metric(tag, null)
                .getMeasurements()
                .stream()
                .filter(Objects::nonNull)
                .filter((s) -> s.getStatistic().getTagValueRepresentation().contains(tagValueRepresentation))//tagValueRepresentation("max") != name of Statistic("MAX")
                .findFirst()
                .map(MetricsEndpoint.Sample::getValue)
                .filter(Double::isFinite)
                .orElse(0.0D);
        return new DecimalFormat("#0.00").format(cpuUsage).replace(",", ".");
    }
}