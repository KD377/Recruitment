package com.example.PromoCodes.controller;

import com.example.PromoCodes.entity.Currency;
import com.example.PromoCodes.entity.PromoCode;
import com.example.PromoCodes.service.PromoCodeService;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PromoCodeController.class)
public class PromoCodeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromoCodeService promoCodeService;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetAllPromoCodes() throws Exception {
        List<PromoCode> promoCodes = Arrays.asList(new PromoCode(1L, "TEST", new Date(), new BigDecimal("10.00"), Currency.USD, 100, 0));
        when(promoCodeService.getAllPromoCodes()).thenReturn(promoCodes);

        mockMvc.perform(get("/api/promo-code")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].code").value("TEST"));
    }

    @Test
    void testAddPromoCode() throws Exception {
        PromoCode promoCode = new PromoCode(1L, "TEST", new Date(), new BigDecimal("20.00"), Currency.USD, 50, 0);
        when(promoCodeService.createPromoCode(any(PromoCode.class))).thenReturn(promoCode);

        mockMvc.perform(post("/api/promo-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"TEST\",\"discountValue\":\"20.00\",\"currency\":\"USD\",\"maxUsages\":50,\"usages\": 0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("TEST"));
    }

    @Test
    void testGetPromoCodeDetailsByCode() throws Exception {
        Optional<PromoCode> promoCode = Optional.of(new PromoCode(1L, "TEST", new Date(), new BigDecimal("15.00"), Currency.USD, 150, 0));
        when(promoCodeService.getPromoCodeByCode("TEST")).thenReturn(promoCode);

        mockMvc.perform(get("/api/promo-code/TEST")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usages").value(0))
                .andExpect(jsonPath("$.code").value("TEST"));
    }

    @Test
    void testGetPromoCodeDetailsByCode_CodeDoesNotExist() throws Exception {
        when(promoCodeService.getPromoCodeByCode("TEST")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/promo-code/TEST")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("PromoCode: TEST does not exist"));
    }

    @Test
    void testAddPromoCode_CurrencyIsNotInEnum() throws Exception {
        mockMvc.perform(post("/api/promo-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"TEST\",\"discountValue\":\"20.00\",\"currency\":\"US\",\"maxUsages\":50,\"usages\": 0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.currency").value("Invalid value for 'currency': 'US'. Expected type: [USD, PLN, EUR]"));
    }

    @Test
    void testAddPromoCode_OneValueNotProvided() throws Exception {
        mockMvc.perform(post("/api/promo-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"TEST\",\"currency\":\"USD\",\"maxUsages\":50,\"usages\": 0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.discountValue").value("Discount value must be provided"));
    }

    @Test
    void testAddPromoCode_WrongType() throws Exception {
        mockMvc.perform(post("/api/promo-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"TEST\",\"discountValue\":\"test\",\"currency\":\"USD\",\"maxUsages\":50,\"usages\": 0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.discountValue").value("Invalid value for 'discountValue': 'test'. Expected type: BigDecimal"));
    }


}
