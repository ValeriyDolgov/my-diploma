package com.example.app.service;

import com.example.app.service.constants.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventsEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final MetricsEndpoint metricsEndpoint;
    private final AuditEventsEndpoint auditEventsEndpoint;

    public Double getValueByTagAndTagValueRepresentation(String tag, String tagValueRepresentation) {
        return metricsEndpoint.metric(tag, null)
                .getMeasurements()
                .stream()
                .filter(Objects::nonNull)
                .filter((s) -> s.getStatistic().getTagValueRepresentation().contains(tagValueRepresentation))//tagValueRepresentation("max") != name of Statistic("MAX")
                .findFirst()
                .map(MetricsEndpoint.Sample::getValue)
                .filter(Double::isFinite)
                .orElse(0.0D);
    }

    public String getFormattedStringFromDoubleValue(Double value) {
        return new DecimalFormat("#0.00").format(value).replace(",", ".");
    }

    public OffsetDateTime getFormattedOffsetDateTimeFromString(String date) {
        return OffsetDateTime.parse(date + Strings.MILLISECONDS, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public Double getGbFromBytesValue(Double valueInMb) {
        return valueInMb / Math.pow(10, 9);
    }

    public String getDiskSpace(String tag, String tagRepresentationValue) {
        return getFormattedStringFromDoubleValue(getGbFromBytesValue(getValueByTagAndTagValueRepresentation(tag, tagRepresentationValue)));
    }

    public String getFormattedValue(String tag, String tagRepresentationValue) {
        return getFormattedStringFromDoubleValue(getValueByTagAndTagValueRepresentation(tag, tagRepresentationValue));
    }

    public List<AuditEvent> getListOfAuditEventsByPrincipal(String principal) {
        return auditEventsEndpoint.events(principal, null, null).getEvents();
    }

    public List<AuditEvent> getListOfAuditEventsByPrincipalAndDate(String principal, String date) {
        if (date != null) {
            return auditEventsEndpoint.events(principal, getFormattedOffsetDateTimeFromString(date), null).getEvents();
        } else return auditEventsEndpoint.events(principal, null, null).getEvents();

    }
}