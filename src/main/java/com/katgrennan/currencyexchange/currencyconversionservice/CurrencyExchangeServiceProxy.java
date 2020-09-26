package com.katgrennan.currencyexchange.currencyconversionservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Feign proxy interface for currency-exchange-service
// (URI for external service is discovered through Ribbon)
@FeignClient(name = "netflix-zuul-api-gateway-server")
// Ribbon for client-side load-balancing
@RibbonClient(name = "currency-exchange-service")
public interface CurrencyExchangeServiceProxy {

    // Method signature from currency-exchange-service
    @GetMapping("/currency-exchange-service/currency-exchange/from/{from}/to/{to}")
    CurrencyConversionBean getExchangeValue(@PathVariable String from, @PathVariable String to);

}
