package com.example.soapservice.endpoint;

import com.example.soapservice.exception.BusinessException;
import com.example.soapservice.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import soapservice.example.com.Country;
import soapservice.example.com.CountryRequest;
import soapservice.example.com.CountryResponse;

@Endpoint
public class CountryEndpoint {

    private  static final String NAMESPACE_URI = "http://com.example.soapservice";


    private final CountryRepository repository;

    @Autowired
    public CountryEndpoint(CountryRepository repository) {
        this.repository = repository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CountryRequest")
    @ResponsePayload
    public CountryResponse findCountry(@RequestPayload CountryRequest request) {
        CountryResponse response = new CountryResponse();
        Country country = repository.findCountry(request.getIsoCode()).orElseThrow(
                ()-> new BusinessException(String.format("Country with isoCode \"%s\" not found.", request.getIsoCode()))
        );
        response.setCountry(country);

        return response;
    }
}
