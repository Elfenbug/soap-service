package com.example.soapservice.client;

import com.example.soapservice.calc.Add;
import com.example.soapservice.calc.AddResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;

public class CalcClient extends WebServiceGatewaySupport {

    public int add(int a, int b) {
        Add add = new Add();
        add.setIntA(a);
        add.setIntB(b);
        return ((AddResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri(), add,
                        webServiceMessage -> ((SoapMessage) webServiceMessage)
                                .setSoapAction("http://tempuri.org/Add")))
                .getAddResult();
    }

}

