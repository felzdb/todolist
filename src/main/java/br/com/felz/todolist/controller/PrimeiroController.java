package br.com.felz.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/primeiraRota")
public class PrimeiroController {

    @GetMapping("/mensagem")
    public String primeiraMensagem() {
        return "Funcionou";
    }
}
