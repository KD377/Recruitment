package com.example.PromoCodes.controller;

import com.example.PromoCodes.dto.SalesReport;
import com.example.PromoCodes.entity.Currency;
import com.example.PromoCodes.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalesReportController.class)
public class SalesReportControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseService purchaseService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getSalesReport_ShouldReturnSalesReports() throws Exception {
        SalesReport report1 = new SalesReport(Currency.USD, new BigDecimal("100"),new BigDecimal("20"), 10L);
        SalesReport report2 = new SalesReport(Currency.EUR, new BigDecimal("850"),new BigDecimal("200"), 8L);
        List<SalesReport> reports = Arrays.asList(report1, report2);

        when(purchaseService.generateSalesReport()).thenReturn(reports);

        mockMvc.perform(get("/api/sales-report")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[0].totalAmount").value("100"))
                .andExpect(jsonPath("$[0].totalDiscount").value(20))
                .andExpect(jsonPath("$[0].numberOfPurchases").value(10))
                .andExpect(jsonPath("$[1].currency").value("EUR"))
                .andExpect(jsonPath("$[1].totalAmount").value("850"))
                .andExpect(jsonPath("$[0].totalDiscount").value(20))
                .andExpect(jsonPath("$[1].numberOfPurchases").value(8));
    }
}
