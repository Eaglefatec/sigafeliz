package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.DiaRestrito;
import com.sigafeliz.model.Semestre;
import com.sigafeliz.service.SemestreService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

public class SemestreEdicaoController {

    @FXML private Label lblSemestreSelecionado;
    @FXML private Label lblMesAno;
    @FXML private GridPane gridCalendario;

    private Semestre semestreAtual;
    private YearMonth mesExibido;
    private final String[] diasSemana = {"DOM", "SEG", "TER", "QUA", "QUI", "SEX", "SAB"};

    @FXML
    public void initialize() {
        semestreAtual = SemestreService.getSemestreSelecionado();
        if (semestreAtual != null) {
            try {
                SemestreService.carregarDetalhes(semestreAtual);
            } catch (SQLException e) {
                mostrarAlertaErro("Erro de Conexão", "Erro ao carregar detalhes: " + e.getMessage());
            }
            lblSemestreSelecionado.setText("Semestre: " + semestreAtual.getNome());
            mesExibido = YearMonth.from(semestreAtual.getDataInicio());
            renderizarCalendario();
        }
    }

    private void renderizarCalendario() {
        gridCalendario.getChildren().clear();
        lblMesAno.setText(mesExibido.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR")).toUpperCase() + " " + mesExibido.getYear());

        for (int i = 0; i < 7; i++) {
            Label header = new Label(diasSemana[i]);
            header.setPrefSize(65, 30);
            header.setAlignment(Pos.CENTER);
            header.setStyle("-fx-background-color: #eee; -fx-border-color: black; -fx-font-weight: bold; -fx-font-family: 'Monospaced';");
            gridCalendario.add(header, i, 0);
        }

        LocalDate primeiroDiaMes = mesExibido.atDay(1);
        int diaDaSemanaInicial = primeiroDiaMes.getDayOfWeek().getValue() % 7;
        int diasNoMes = mesExibido.lengthOfMonth();

        int linha = 1;
        int coluna = diaDaSemanaInicial;

        for (int dia = 1; dia <= diasNoMes; dia++) {
            LocalDate dataAtual = mesExibido.atDay(dia);
            Label slot = criarSlotDia(dataAtual, coluna);
            gridCalendario.add(slot, coluna, linha);

            coluna++;
            if (coluna > 6) {
                coluna = 0;
                linha++;
            }
        }
    }

    private Label criarSlotDia(LocalDate data, int coluna) {
        Label lbl = new Label(String.valueOf(data.getDayOfMonth()));
        lbl.setPrefSize(65, 65);
        lbl.setAlignment(Pos.CENTER);

        boolean dentroDoSemestre = (data.isEqual(semestreAtual.getDataInicio()) || data.isAfter(semestreAtual.getDataInicio())) &&
                (data.isEqual(semestreAtual.getDataFim()) || data.isBefore(semestreAtual.getDataFim()));

        if (!dentroDoSemestre) {
            lbl.setStyle("-fx-border-color: #dee2e6; -fx-background-color: #e9ecef; -fx-text-fill: #adb5bd; -fx-font-family: 'Monospaced';");
        } else {
            atualizarEstiloDinamico(lbl, data);
            lbl.setOnMouseClicked(event -> handleInteracaoDia(lbl, data, coluna));
        }
        return lbl;
    }

    private void atualizarEstiloDinamico(Label lbl, LocalDate data) {
        String style = "-fx-border-color: black; -fx-font-family: 'Monospaced'; -fx-cursor: hand; ";

        // Busca o feriado se existir
        Optional<DiaRestrito> feriadoOpt = semestreAtual.getDiasRestritos().stream()
                .filter(d -> d.getData().equals(data)).findFirst();

        boolean letivo = semestreAtual.getSabadosLetivos().stream().anyMatch(s -> s.getData().equals(data));

        if (feriadoOpt.isPresent()) {
            style += "-fx-background-color: #f8d7da; -fx-text-fill: #721c24; -fx-font-weight: bold;";
            // ADICIONA TOOLTIP COM O NOME DO FERIADO
            lbl.setTooltip(new Tooltip(feriadoOpt.get().getDescricao()));
        } else if (letivo) {
            style += "-fx-background-color: #cfe2f3; -fx-text-fill: #084298; -fx-font-weight: bold;";
            lbl.setTooltip(new Tooltip("Sábado Letivo"));
        } else {
            style += "-fx-background-color: white;";
            lbl.setTooltip(null); // Remove tooltip se não for mais restrição
        }
        lbl.setStyle(style);
    }

    private void handleInteracaoDia(Label lbl, LocalDate data, int coluna) {
        if (coluna == 0) return;

        try {
            if (coluna == 6) {
                SemestreService.alternarSabadoLetivo(semestreAtual, data);
            } else {
                // BUSCA O FERIADO ATUAL PARA PREENCHER O DIALOG
                Optional<DiaRestrito> feriadoExistente = semestreAtual.getDiasRestritos().stream()
                        .filter(d -> d.getData().equals(data)).findFirst();

                String descricaoInicial = feriadoExistente.map(DiaRestrito::getDescricao).orElse("");

                TextInputDialog dialog = new TextInputDialog(descricaoInicial);
                dialog.setTitle("Configurar Feriado");
                dialog.setHeaderText("Dia " + data);
                dialog.setContentText("Nome do Feriado (deixe vazio para remover):");

                Optional<String> result = dialog.showAndWait();

                // SÓ FAZ ALGO SE O USUÁRIO CLICAR EM "OK"
                if (result.isPresent()) {
                    String novaDesc = result.get().trim();

                    if (feriadoExistente.isPresent()) {
                        // Se já existia, removemos o antigo primeiro
                        SemestreService.removerDiaRestrito(semestreAtual, data);
                    }

                    if (!novaDesc.isEmpty()) {
                        // Se digitou algo, adicionamos (ou re-adicionamos com nome novo)
                        SemestreService.adicionarDiaRestrito(semestreAtual, data, novaDesc);
                    }
                }
            }
            atualizarEstiloDinamico(lbl, data);

        } catch (SQLException e) {
            mostrarAlertaErro("Erro", "Falha na operação: " + e.getMessage());
        }
    }

    @FXML private void mesAnterior() {
        if (mesExibido.isAfter(YearMonth.from(semestreAtual.getDataInicio()))) {
            mesExibido = mesExibido.minusMonths(1);
            renderizarCalendario();
        }
    }

    @FXML private void proximoMes() {
        if (mesExibido.isBefore(YearMonth.from(semestreAtual.getDataFim()))) {
            mesExibido = mesExibido.plusMonths(1);
            renderizarCalendario();
        }
    }

    @FXML private void handleSalvarVoltar() { Main.loadView("SemestreLista.fxml"); }

    @FXML
    private void handleCancelar() {
        // Volta para a lista sem salvar (embora as alterações pontuais já tenham ido ao banco)
        // Em um sistema real, poderíamos usar transações para permitir um rollback aqui.
        Main.loadView("SemestreLista.fxml");
    }

    private void mostrarAlertaErro(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}