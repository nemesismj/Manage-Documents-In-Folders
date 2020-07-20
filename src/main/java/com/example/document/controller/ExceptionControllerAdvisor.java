package com.example.document.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvisor {

    Logger log = LoggerFactory.getLogger(ExceptionControllerAdvisor.class);
    //TODO:Handel Each exception and response with descriptive error page
    @ExceptionHandler(Exception.class)
    public String handelAllExceptions(Exception e) {
        log.error("IN handelAllExceptions - error page visited, found some error via using application");
        return "error";
    }


}
