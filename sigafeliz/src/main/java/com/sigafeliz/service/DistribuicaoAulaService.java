package com.sigafeliz.service;

import com.sigafeliz.dao.TemaDAO;
import com.sigafeliz.model.Prioridade;
import com.sigafeliz.model.Tema;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Colocar em: com/sigafeliz/service/DistribuicaoAulaService.java
 *
 * Algoritmo:
 *  1. Aloca a carga MÍNIMA de todos os temas (já feito no construtor de Tema).
 *  2. Calcula vagas restantes: (aulasPorSemana × totalSemanas) - somaMinimos.
 *  3. Preenche as vagas por PRIORIDADE (ALTA → MEDIA → BAIXA) até o MÁXIMO.
 *  4. Salva aulas_alocadas no banco.
 */
public class DistribuicaoAulaService {

    private final TemaDAO temaDAO;

    public DistribuicaoAulaService() {
        this.temaDAO = new TemaDAO();
    }

    public DistribuicaoAulaService(TemaDAO temaDAO) {
        this.temaDAO = temaDAO;
    }

    // ------------------------------------------------------------------
    // Ponto de entrada: busca do banco e salva o resultado
    // ------------------------------------------------------------------
    public List<Tema> distribuir(String disciplinaNome, int totalSemanas) throws SQLException {
        List<Tema> temas = temaDAO.buscarPorDisciplina(disciplinaNome);
        int aulasPorSemana = temaDAO.buscarAulasPorSemana(disciplinaNome);

        List<Tema> resultado = calcularDistribuicao(temas, aulasPorSemana, totalSemanas);

        for (Tema t : resultado) {
            temaDAO.salvarAulasAlocadas(t);
        }

        return resultado;
    }

    // ------------------------------------------------------------------
    // Lógica pura — sem banco (útil para testes e preview na tela)
    // ------------------------------------------------------------------
    public List<Tema> calcularDistribuicao(List<Tema> temas, int aulasPorSemana, int totalSemanas) {
        if (temas == null || temas.isEmpty()) return new ArrayList<>();

        int capacidadeTotal = aulasPorSemana * totalSemanas;

        // PASSO 1: mínimos já alocados no construtor de Tema
        int totalMinimos = temas.stream().mapToInt(Tema::getCargaMinima).sum();

        // PASSO 2: vagas restantes após alocar os mínimos
        int vagasRestantes = capacidadeTotal - totalMinimos;

        // PASSO 3: preenche por prioridade até o máximo
        if (vagasRestantes > 0) {
            List<Tema> porPrioridade = new ArrayList<>(temas);
            porPrioridade.sort(Comparator.comparingInt(t -> prioridadeParaInt(t.getPrioridade())));

            for (Tema t : porPrioridade) {
                if (vagasRestantes <= 0) break;
                if (t.getVagasRestantes() <= 0) continue;

                int adicionar = Math.min(t.getVagasRestantes(), vagasRestantes);
                t.setAulasAlocadas(t.getAulasAlocadas() + adicionar);
                vagasRestantes -= adicionar;
            }
        }

        return temas;
    }

    // ------------------------------------------------------------------
    // Converte enum Prioridade para inteiro (menor = mais prioritário)
    // ------------------------------------------------------------------
    private int prioridadeParaInt(Prioridade prioridade) {
        if (prioridade == null) return 99;
        return switch (prioridade) {
            case ALTA  -> 1;
            case MEDIA -> 2;
            case BAIXA -> 3;
        };
    }
}