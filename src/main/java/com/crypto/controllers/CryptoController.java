package com.crypto.controllers;

import com.crypto.models.Crypto;
import com.crypto.services.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EnableScheduling
@RestController
public class CryptoController {
    private String s1 = "BTC";
    private String s2 = "USD";

    @Autowired
    CryptoService cryptoService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void test(){
        Crypto crypto = cryptoService.parseCurrency(s1, s2);
        cryptoService.save(crypto);
    }

    @PostMapping(value = "/crypto/{s1}/{s2}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> lastPrice(@PathVariable String s1, @PathVariable String s2){
        if ((s1.equals("BTC") || s1.equals("ETH") || s1.equals("XRP")) && s2.equals("USD")){
            this.s1 = s1;
            this.s2 = s2;
            Crypto crypto = cryptoService.parseCurrency(s1, s2);
            cryptoService.save(crypto);
            return ResponseEntity.ok(crypto);
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("{\"success\":false,\"error\":\"Incorrect currency name. First value can be: BTC, ETH, XRP and second value - USD\"}");
        }
    }

    @GetMapping(value = "/cryptocurrencies/minprice", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMinPrice(@RequestParam String name){
        if (name.equals("BTC") || name.equals("ETH") || name.equals("XRP")){
            return ResponseEntity.ok(cryptoService.findMinByCurrencyName(name));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("{\"success\":false,\"error\":\"Incorrect currency name. Can be: BTC, ETH, XRP\"}");
    }

    @GetMapping(value = "/cryptocurrencies/maxprice", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMaxPrice(@RequestParam String name){
        if (name.equals("BTC") || name.equals("ETH") || name.equals("XRP")){
            return ResponseEntity.ok(cryptoService.findMaxByCurrencyName(name));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("{\"success\":false,\"error\":\"Incorrect currency name. Can be: BTC, ETH, XRP\"}");
    }

    @GetMapping("/cryptocurrencies")
    public List<Crypto> getPrices(@RequestParam HashMap<String, String> params){
        String name = params.get("name");
        int size;
        int page;
        if (params.containsKey("size")){
            size = Integer.parseInt(params.get("size"));
        } else {
            size = 10;
        }
        if (params.containsKey("page")){
            page = Integer.parseInt(params.get("page"));
        } else {
            page = 0;
        }
        return cryptoService.findAll(name, page, size);
    }

    @GetMapping("/cryptocurrencies/csv")
    public void createCSV(){
        Crypto maxBTC = cryptoService.findMaxByCurrencyName("BTC");
        Crypto minBTC = cryptoService.findMinByCurrencyName("BTC");
        Crypto maxETH = cryptoService.findMaxByCurrencyName("ETH");
        Crypto minETH = cryptoService.findMinByCurrencyName("ETH");
        Crypto maxXRP = cryptoService.findMaxByCurrencyName("XRP");
        Crypto minXRP = cryptoService.findMinByCurrencyName("XRP");
        List<Crypto> cryptos = new ArrayList<>();
        cryptos.add(maxBTC);
        cryptos.add(minBTC);
        cryptos.add(maxETH);
        cryptos.add(minETH);
        cryptos.add(maxXRP);
        cryptos.add(minXRP);
        cryptoService.createCSV(cryptos);
    }
}
