package com.katgrennan.currencyexchange.currencyconversionservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeServiceProxy proxy;

    // Deprecated - use convertCurrencyFeign instead
    // NOTE: keep for debugging/fallback in case Feign service fails
    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
                "http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                CurrencyConversionBean.class,
                uriVariables);

        CurrencyConversionBean res = responseEntity.getBody();

        return new CurrencyConversionBean(
                res.getId(),
                from,
                to,
                res.getConversionMultiple(),
                quantity,
                quantity.multiply(res.getConversionMultiple()),
                res.getPort());
    }

    // Use Feign proxy to get currency conversion from currency-exchange-service
    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrencyFeign(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {

        CurrencyConversionBean res = proxy.getExchangeValue(from, to);

        return new CurrencyConversionBean(
                res.getId(),
                from,
                to,
                res.getConversionMultiple(),
                quantity,
                quantity.multiply(res.getConversionMultiple()),
                res.getPort());
    }

}
