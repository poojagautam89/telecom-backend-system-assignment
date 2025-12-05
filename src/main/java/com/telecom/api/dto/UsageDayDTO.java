package com.telecom.api.dto;

import lombok.Data;

@Data
public class UsageDayDTO {
    private String day; // YYYY-MM-DD
    private Long dataKb;
    private Long callMin;
    private Long smsCount;

    public UsageDayDTO(){}
    public UsageDayDTO(String day, Long dataKb, Long callMin, Long smsCount){
        this.day = day; this.dataKb = dataKb; this.callMin = callMin; this.smsCount = smsCount;
    }
    // getters/setters
    public String getDay(){return day;} public void setDay(String d){this.day=d;}
    public Long getDataKb(){return dataKb;} public void setDataKb(Long v){this.dataKb=v;}
    public Long getCallMin(){return callMin;} public void setCallMin(Long v){this.callMin=v;}
    public Long getSmsCount(){return smsCount;} public void setSmsCount(Long v){this.smsCount=v;}
}
