package com.salarios.jpa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salarios.jpa.models.Funcionario;
import com.salarios.jpa.repositories.FuncionarioRepository;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    public List<Funcionario> listarFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public Funcionario salvarFuncionario(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public void removerFuncionario(String nome) {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        funcionarios.removeIf(f -> f.getNome().equals(nome));
        funcionarioRepository.deleteAll(funcionarios);
    }

    public void imprimirFuncionarios(List<Funcionario> funcionarios) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        System.out.println("Lista de Funcionários:");
        for (Funcionario f : funcionarios) {
            System.out.println(formatarFuncionario(f, dateFormatter, decimalFormat));
        }
    }

    private String formatarFuncionario(Funcionario f, DateTimeFormatter dateFormatter, DecimalFormat decimalFormat) {
        return "Nome: " + f.getNome() +
                ", Data de Nascimento: " + f.getDataNascimento().format(dateFormatter) +
                ", Salário: " + decimalFormat.format(f.getSalario()) +
                ", Função: " + f.getFuncao();
    }

    public void aumentarSalario(BigDecimal percentual) {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        funcionarios.forEach(f -> f.setSalario(f.getSalario().multiply(BigDecimal.valueOf(1).add(percentual))));
        funcionarioRepository.saveAll(funcionarios);
    }

    public Map<String, List<Funcionario>> agruparPorFuncao() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        return funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));
    }

    public void imprimirFuncionariosPorFuncao(Map<String, List<Funcionario>> funcionariosPorFuncao) {
        System.out.println("\nFuncionários agrupados por função:");
        for (String funcao : funcionariosPorFuncao.keySet()) {
            System.out.println("Função: " + funcao);
            for (Funcionario f : funcionariosPorFuncao.get(funcao)) {
                System.out.println("\t" + f);
            }
        }
    }

    public void imprimirAniversariantes(int... meses) {
        Set<Integer> mesesSet = Arrays.stream(meses).boxed().collect(Collectors.toSet());
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        System.out.println("\nFuncionários que fazem aniversário nos meses especificados:");
        funcionarios.stream()
                .filter(f -> mesesSet.contains(f.getDataNascimento().getMonthValue()))
                .forEach(System.out::println);
    }

    public Funcionario encontrarFuncionarioMaisVelho() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        return Collections.min(funcionarios, Comparator.comparing(Funcionario::getDataNascimento));
    }

    public void imprimirFuncionarioMaisVelho(Funcionario funcionarioMaisVelho) {
        int idade = calcularIdade(funcionarioMaisVelho.getDataNascimento(), LocalDate.now());
        System.out.println("\nFuncionário com maior idade: Nome: " + funcionarioMaisVelho.getNome() + ", Idade: " + idade);
    }

    private int calcularIdade(LocalDate nascimento, LocalDate hoje) {
        return hoje.getYear() - nascimento.getYear();
    }

    public void imprimirFuncionariosOrdemAlfabetica() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        System.out.println("\nLista de Funcionários por ordem alfabética:");
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .forEach(System.out::println);
    }

    public void imprimirTotalSalarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        System.out.println("\nTotal dos salários dos funcionários: " + decimalFormat.format(totalSalarios));
    }

    public void imprimirSalariosMinimos() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        System.out.println("\nSalários mínimos por funcionário:");
        for (Funcionario f : funcionarios) {
            BigDecimal salariosMinimos = f.getSalario().divide(SALARIO_MINIMO, 2, BigDecimal.ROUND_HALF_UP);
            System.out.println(f.getNome() + " ganha " + decimalFormat.format(salariosMinimos) + " salários mínimos.");
        }
    }
}
