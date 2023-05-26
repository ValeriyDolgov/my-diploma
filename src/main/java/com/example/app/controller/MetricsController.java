package com.example.app.controller;

import com.example.app.service.MetricsService;
import com.example.app.service.constants.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/admin/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService metricsService;

    @GetMapping
    public String getMetrics(Model model) {
        String diskFreeSpace = metricsService.getDiskSpace(Tags.DISK_FREE_TAG, Tags.VALUE_TAG_VALUE_REPRESENTATION);
        String diskTotalSpace = metricsService.getDiskSpace(Tags.DISK_TOTAL_TAG, Tags.VALUE_TAG_VALUE_REPRESENTATION);
        Double percentOfUsedSpaceOnDisk = 100 - (Double.parseDouble(diskFreeSpace) * 100) / Double.parseDouble(diskTotalSpace);
        model.addAttribute("diskFree", diskFreeSpace);
        model.addAttribute("diskTotal", diskTotalSpace);
        model.addAttribute("percentsOfUsedSpace", metricsService.getFormattedStringFromDoubleValue(
                percentOfUsedSpaceOnDisk));
        model.addAttribute("maxRenderPageTime", metricsService.getFormattedValue(
                Tags.HTTP_SERVER_REQUEST_TAG, Tags.MAX_TAG_VALUE_REPRESENTATION));
        model.addAttribute("cpuUsage", metricsService.getFormattedValue(
                Tags.SYSTEM_CPU_USAGE_TAG, Tags.VALUE_TAG_VALUE_REPRESENTATION));
        return "admin/usage";
    }

    @GetMapping("/events/all")
    public String getAuditEventsByPrinciple(Model model) {
        model.addAttribute("listOfAuditEvents", metricsService.getListOfAuditEventsByPrincipal(null));
        return "admin/list-of-audit-events-all";
    }

    @GetMapping("/events/detailed")
    public String getAuditEventsByPrincipleAndAfterDate(@RequestParam(value = "principal") String principal,
                                                        @RequestParam("timestamp") Optional<String> timestamp,
                                                        Model model) {
        String date = timestamp.orElse(null);
        model.addAttribute("principal", principal);
        model.addAttribute("listOfAuditEvents", metricsService.getListOfAuditEventsByPrincipalAndDate(principal,
                date));
        return "admin/list-of-audit-events-by-principal-and-date";
    }
}
