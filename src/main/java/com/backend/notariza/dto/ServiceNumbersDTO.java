package com.backend.notariza.dto;

import java.util.Arrays;

public class ServiceNumbersDTO {

    private String service;

    private int no_of_services;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getNo_of_services() {
        return no_of_services;
    }

    public void setNo_of_services(int no_of_services) {
        this.no_of_services = no_of_services;
    }

    @Override
    public String toString() {
        return "ServiceNumbersDTO{" +
                "service='" + service + '\'' +
                ", no_of_services=" + no_of_services +
                '}';
    }
}
