package com.luminar.service;

import java.util.List;
import com.luminar.dto.ClientProfitReportDTO;

public interface ReportService {

    List<ClientProfitReportDTO> getClientProfitReport();
}
