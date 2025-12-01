package com.telecom.api.service;

import com.telecom.api.dto.UsageRequest;

public interface UsageService {
    void saveUsage(UsageRequest request);
}
