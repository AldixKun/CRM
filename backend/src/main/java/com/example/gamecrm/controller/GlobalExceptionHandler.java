package com.example.gamecrm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        // ESTO IMPRIMIRÁ EL ERROR REAL EN LA CONSOLA EN CASO DE HABERLO
        System.out.println("ERROR CRÍTICO CAPTURADO");
        ex.printStackTrace();
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error en el servidor: " + ex.getMessage());
    }
}