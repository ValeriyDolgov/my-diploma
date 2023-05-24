package com.example.app.controller;

import com.example.app.service.MetricsService;
import com.example.app.service.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/events")
    public String getAuditEventsByPrinciple(@RequestParam("principal") String principal, Model model) {
        model.addAttribute("email", principal);
        model.addAttribute("listOfAuditEvents", metricsService.getListOfAuditEvents(principal));
        return "admin/listOfAuditEvents";
    }
}
