package com.salarios.jpa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.salarios.jpa.models.Funcionario;
import com.salarios.jpa.services.FuncionarioService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    public List<Funcionario> listarFuncionarios() {
        return funcionarioService.listarFuncionarios();
    }

    @PostMapping
    public ResponseEntity<Funcionario> adicionarFuncionario(@RequestBody Funcionario funcionario) {
        Funcionario novoFuncionario = funcionarioService.salvarFuncionario(funcionario);
        return new ResponseEntity<>(novoFuncionario, HttpStatus.CREATED);
    }

    @DeleteMapping("/{nome}")
    public ResponseEntity<Void> removerFuncionario(@PathVariable String nome) {
        funcionarioService.removerFuncionario(nome);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/aumentar-salario")
    public ResponseEntity<Void> aumentarSalario(@RequestParam BigDecimal percentual) {
        funcionarioService.aumentarSalario(percentual);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/por-funcao")
    public Map<String, List<Funcionario>> agruparPorFuncao() {
        return funcionarioService.agruparPorFuncao();
    }

    @GetMapping("/aniversariantes")
    public void imprimirAniversariantes(@RequestParam int[] meses) {
        funcionarioService.imprimirAniversariantes(meses);
    }

    @GetMapping("/mais-velho")
    public Funcionario encontrarFuncionarioMaisVelho() {
        return funcionarioService.encontrarFuncionarioMaisVelho();
    }

    @GetMapping("/ordem-alfabetica")
    public void imprimirFuncionariosOrdemAlfabetica() {
        funcionarioService.imprimirFuncionariosOrdemAlfabetica();
    }

    @GetMapping("/total-salarios")
    public void imprimirTotalSalarios() {
        funcionarioService.imprimirTotalSalarios();
    }

    @GetMapping("/salarios-minimos")
    public void imprimirSalariosMinimos() {
        funcionarioService.imprimirSalariosMinimos();
    }
}
