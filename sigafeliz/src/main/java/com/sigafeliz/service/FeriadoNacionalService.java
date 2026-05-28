package com.sigafeliz.service;

import com.sigafeliz.dao.FeriadoNacionalDAO;

import java.sql.SQLException;
import java.time.LocalDate;

public class FeriadoNacionalService {

    private static final FeriadoNacionalDAO dao = new FeriadoNacionalDAO();

    /**
     * Varre o ano corrente e os próximos 2 anos subsequentes para garantir que
     * os próximos 3 semestres letivos estarão totalmente cobertos por feriados mapeados.
     */
    public static void inicializarFeriadosProximosSemestres() {
        int anoAtual = LocalDate.now().getYear();

        // Loop que garante o ano atual e mais os próximos 2 anos à frente (3 anos civis totais)
        for (int i = 0; i <= 2; i++) {
            int anoAlvo = anoAtual + i;
            try {
                if (!dao.existeFeriadoNoAno(anoAlvo)) {
                    System.out.println("[Feriados] Mapeando e populando feriados para o ano civil: " + anoAlvo);
                    cadastrarFeriadosDoAno(anoAlvo);
                }
            } catch (SQLException e) {
                System.err.println("Erro ao inicializar feriados nacionais para o ano de " + anoAlvo + ": " + e.getMessage());
            }
        }
    }

    private static void cadastrarFeriadosDoAno(int ano) throws SQLException {
        // --- 1. FERIADOS NACIONAIS FIXOS DO BRASIL ---
        dao.salvar(LocalDate.of(ano, 1, 1), "Confraternização Universal (Ano Novo)");
        dao.salvar(LocalDate.of(ano, 4, 21), "Tiradentes");
        dao.salvar(LocalDate.of(ano, 5, 1), "Dia Mundial do Trabalho");
        dao.salvar(LocalDate.of(ano, 9, 7), "Independência do Brasil");
        dao.salvar(LocalDate.of(ano, 10, 12), "Nossa Senhora Aparecida (Padroeira do Brasil)");
        dao.salvar(LocalDate.of(ano, 11, 2), "Finados");
        dao.salvar(LocalDate.of(ano, 11, 15), "Proclamação da República");
        dao.salvar(LocalDate.of(ano, 11, 20), "Dia Nacional de Zumbi e da Consciência Negra");
        dao.salvar(LocalDate.of(ano, 12, 25), "Natal");

        // --- 2. FERIADOS MÓVEIS DO BRASIL (DEPENDENTES DA PÁSCOA) ---
        LocalDate pascoa = calcularDataPascoa(ano);

        LocalDate segundaCarnaval = pascoa.minusDays(48);
        LocalDate tercaCarnaval = pascoa.minusDays(47);
        LocalDate sextaPaixao = pascoa.minusDays(2); // Sexta-feira Santa
        LocalDate corpusChristi = pascoa.plusDays(60);

        dao.salvar(segundaCarnaval, "Carnaval (Segunda-Feira)");
        dao.salvar(tercaCarnaval, "Carnaval (Terça-Feira)");
        dao.salvar(sextaPaixao, "Sexta-Feira da Paixão");
        dao.salvar(corpusChristi, "Corpus Christi");
    }

    /**
     * Calcula o Domingo de Páscoa usando o algoritmo Meeus/Jones/Butcher.
     */
    private static LocalDate calcularDataPascoa(int ano) {
        int a = ano % 19;
        int b = ano / 100;
        int c = ano % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int L = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * L) / 451;
        int mes = (h + L - 7 * m + 114) / 31;
        int dia = ((h + L - 7 * m + 114) % 31) + 1;
        return LocalDate.of(ano, mes, dia);
    }
}