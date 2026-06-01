package com.sigafeliz.service;

import com.sigafeliz.dao.TemaDAO;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Tema;

import java.sql.SQLException;
import java.util.List;

public class TemaService {
    private static final TemaDAO dao = new TemaDAO();

    public static List<Tema> getTemasPorDisciplina(Disciplina d) throws SQLException {
        return dao.listarPorDisciplina(d);
    }

    public static int getTotalMinimoPorDisciplina(Disciplina d) throws SQLException {

        List<Tema> listTemas = getTemasPorDisciplina(d);
        int total = 0;

        for (Tema tema : listTemas) {
            if (tema.isObrigatorio()) {
               total += tema.getCargaMinima();
            }
        }

        return total;


    }


    public static void salvar(Tema t) throws SQLException {
        validarDependencia(t);
        dao.salvar(t);
    }

    public static void editar(Tema t, String tituloAntigo) throws SQLException {
        validarDependencia(t);
        dao.editar(t, tituloAntigo);
    }

    public static void excluir(Tema t) throws SQLException {
        dao.excluir(t.getDisciplina().getNome(), t.getTitulo());
    }

    /**
     * Valida se a dependência digitada existe no banco de dados para a mesma disciplina
     * antes de submeter a transação, evitando estouro de FK.
     */
    private static void validarDependencia(Tema t) throws SQLException {
        String depTitulo = t.getDependenciaTitulo();

        // Se não houver dependência informada, não há o que validar
        if (depTitulo == null || depTitulo.trim().isEmpty()) {
            return;
        }

        // 1. Validação lógica: Um tema não pode depender de si mesmo
        if (depTitulo.equalsIgnoreCase(t.getTitulo())) {
            throw new SQLException("Um tema não pode depender de si mesmo. Deixe o campo em branco ou aponte para outro tema.");
        }

        // 2. Validação no banco: Busca todos os temas cadastrados nesta disciplina
        List<Tema> temasExistentes = dao.listarPorDisciplina(t.getDisciplina());
        boolean encontrou = false;

        for (Tema existente : temasExistentes) {
            if (existente.getTitulo().equalsIgnoreCase(depTitulo)) {
                encontrou = true;

                // UX UPGRADE: Como o banco diferencia maiúsculas/minúsculas (Case-Sensitive),
                // se o usuário digitou "introdução" e no banco está "Introdução",
                // nós ajustamos automaticamente para a grafia exata do banco para não dar erro de FK.
                t.setDependenciaTitulo(existente.getTitulo());
                break;
            }
        }

        if (!encontrou) {
            throw new SQLException("O tema informado como dependência '" + depTitulo + "' não foi encontrado nesta disciplina. Verifique se o título foi digitado corretamente.");
        }
    }
}