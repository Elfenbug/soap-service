package com.example.soapservice.endpoint;

import com.example.soapservice.repository.CountryRepository;
import com.example.soapservice.client.CalcClient;
import com.example.soapservice.exception.BusinessException;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import soapservice.example.com.PopulationSumRequest;
import soapservice.example.com.PopulationSumResponse;

@Endpoint
public class PopulationCalcEndpoint {

    private static final String NS_NAME = "http://com.example.soapservice";
    private final CountryRepository countryRepo;
    private final CalcClient calcClient;

    public PopulationCalcEndpoint(CountryRepository countryRepo,
                                  CalcClient calcClient) {
        this.countryRepo = countryRepo;
        this.calcClient = calcClient;
    }

    @PayloadRoot(namespace = NS_NAME, localPart = "PopulationSumRequest")
    @ResponsePayload
    public PopulationSumResponse getPopulationSum(@RequestPayload PopulationSumRequest request) {
        PopulationSumResponse response = new PopulationSumResponse();
        int sum = 0;
        for (int i = 0; i < request.getIsoCode().size(); i++) {
            String isoCode = request.getIsoCode().get(i);
            int population = countryRepo.findCountry(isoCode)
                    .orElseThrow(() -> new BusinessException("Country with ISO code = " + isoCode + " was not found, try one of these: "
                            + countryRepo.getCountriesISOCodes()))
                    .getPopulation();
            sum = calcClient.add(sum, population);
        }
        response.setPopulationSum(sum);
        return response;
    }

}
