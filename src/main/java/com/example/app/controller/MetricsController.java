package com.example.app.controller;

import com.example.app.service.Tags;
import com.example.app.service.mapper.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DecimalFormat;

@Controller
@RequestMapping("/admin/metricsTest")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService metricsService;

    @GetMapping
    public String getMetrics(Model model) {
        String diskFreeSpace = metricsService.getDiskInfoInGB(Tags.DISK_FREE_TAG);
        String diskTotalSpace = metricsService.getDiskInfoInGB(Tags.DISK_TOTAL_TAG);
        Double percentOfUsedSpaceOnDisk = (Double.parseDouble(diskFreeSpace) * 100) / Double.parseDouble(diskTotalSpace);
        model.addAttribute("diskFree", metricsService.getDiskInfoInGB(Tags.DISK_FREE_TAG));
        model.addAttribute("diskTotal", metricsService.getDiskInfoInGB(Tags.DISK_TOTAL_TAG));
        model.addAttribute("percentsOfUsedSpace", new DecimalFormat("#0.00")
                .format(percentOfUsedSpaceOnDisk));
        model.addAttribute("maxRenderPageTime", metricsService.getValueByTagAndTagValueRepresentation(
                Tags.HTTP_SERVER_REQUEST_TAG, Tags.MAX_TAG_VALUE_REPRESENTATION));
        model.addAttribute("cpuUsage", metricsService.getValueByTagAndTagValueRepresentation(
                Tags.SYSTEM_CPU_USAGE_TAG, Tags.VALUE_TAG_VALUE_REPRESENTATION));
        return "admin/list";
    }
}
