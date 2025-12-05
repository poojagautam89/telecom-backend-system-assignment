package com.telecom.api.dto;

import java.util.List;

public class UsageSummaryDTO {

    private Long totalDataKb;
    private Long totalCallMin;
    private Long totalSms;

    // NEW field for daily breakdown (required in Sprint-3)
    private List<UsageDayDTO> days;

    public UsageSummaryDTO() {}

    public UsageSummaryDTO(Long data, Long calls, Long sms) {
        this.totalDataKb = data;
        this.totalCallMin = calls;
        this.totalSms = sms;
    }

    public Long getTotalDataKb() { return totalDataKb; }
    public void setTotalDataKb(Long totalDataKb) { this.totalDataKb = totalDataKb; }

    public Long getTotalCallMin() { return totalCallMin; }
    public void setTotalCallMin(Long totalCallMin) { this.totalCallMin = totalCallMin; }

    public Long getTotalSms() { return totalSms; }
    public void setTotalSms(Long totalSms) { this.totalSms = totalSms; }

    public List<UsageDayDTO> getDays() { return days; }
    public void setDays(List<UsageDayDTO> days) { this.days = days; }
}
