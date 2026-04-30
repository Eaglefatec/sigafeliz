package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.Semestre;
import com.sigafeliz.service.SemestreService; // Import do novo Service
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
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
        // Busca do novo Service
        semestreAtual = SemestreService.getSemestreSelecionado();

        if (semestreAtual != null) {
            try {
                // Carrega os dados reais do banco para o calendário atual
                SemestreService.carregarDetalhes(semestreAtual);
            } catch (SQLException e) {
                mostrarAlertaErro("Erro de Conexão", "Não foi possível carregar os feriados: " + e.getMessage());
            }

            lblSemestreSelecionado.setText("Configurando: " + semestreAtual.getNome());
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

        boolean feriado = semestreAtual.getDiasRestritos().stream().anyMatch(d -> d.getData().equals(data));
        boolean letivo = semestreAtual.getSabadosLetivos().stream().anyMatch(s -> s.getData().equals(data));

        if (feriado) {
            style += "-fx-background-color: #f8d7da; -fx-text-fill: #721c24; -fx-font-weight: bold;";
        } else if (letivo) {
            style += "-fx-background-color: #cfe2f3; -fx-text-fill: #084298; -fx-font-weight: bold;";
        } else {
            style += "-fx-background-color: white;";
        }
        lbl.setStyle(style);
    }

    private void handleInteracaoDia(Label lbl, LocalDate data, int coluna) {
        if (coluna == 0) return; // Não há iteração no domingo

        try {
            if (coluna == 6) {
                // Persiste e altera a cor do Sábado
                SemestreService.alternarSabadoLetivo(semestreAtual, data);
            } else {
                // Lógica de Feriado/Dias Restritos
                boolean isFeriado = semestreAtual.getDiasRestritos().stream().anyMatch(d -> d.getData().equals(data));

                if (isFeriado) {
                    // Remove do banco e da memória
                    SemestreService.removerDiaRestrito(semestreAtual, data);
                } else {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Restrição de Data");
                    dialog.setHeaderText("Definir feriado para " + data);
                    dialog.setContentText("Descrição (Ex: Sexta Santa):");

                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent() && !result.get().trim().isEmpty()) {
                        // Salva no banco e na memória
                        SemestreService.adicionarDiaRestrito(semestreAtual, data, result.get().trim());
                    }
                }
            }
            // Atualiza visualmente se a operação no banco deu certo
            atualizarEstiloDinamico(lbl, data);

        } catch (SQLException e) {
            mostrarAlertaErro("Erro de Banco de Dados", "Ocorreu um erro ao salvar a alteração:\n" + e.getMessage());
        }
    }

    @FXML
    private void mesAnterior() {
        YearMonth limiteMinimo = YearMonth.from(semestreAtual.getDataInicio());
        if (mesExibido.isAfter(limiteMinimo)) {
            mesExibido = mesExibido.minusMonths(1);
            renderizarCalendario();
        }
    }

    @FXML
    private void proximoMes() {
        YearMonth limiteMaximo = YearMonth.from(semestreAtual.getDataFim());
        if (mesExibido.isBefore(limiteMaximo)) {
            mesExibido = mesExibido.plusMonths(1);
            renderizarCalendario();
        }
    }

    @FXML
    private void handleSalvarVoltar() {
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