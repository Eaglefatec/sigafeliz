package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.DiaRestrito;
import com.sigafeliz.model.SabadoLetivo;
import com.sigafeliz.model.Semestre;
import com.sigafeliz.service.MockDataService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;

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
        semestreAtual = MockDataService.getSemestreSelecionado();
        if (semestreAtual != null) {
            lblSemestreSelecionado.setText("Configurando: " + semestreAtual.getNome());
            // Inicia no mês de início do semestre
            mesExibido = YearMonth.from(semestreAtual.getDataInicio());
            renderizarCalendario();
        }
    }

    private void renderizarCalendario() {
        gridCalendario.getChildren().clear();
        lblMesAno.setText(mesExibido.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR")).toUpperCase() + " " + mesExibido.getYear());

        // Cabeçalho dos dias da semana (DOM a SAB)
        for (int i = 0; i < 7; i++) {
            Label header = new Label(diasSemana[i]);
            header.setPrefSize(65, 30);
            header.setAlignment(Pos.CENTER);
            header.setStyle("-fx-background-color: #eee; -fx-border-color: black; -fx-font-weight: bold; -fx-font-family: 'Monospaced';");
            gridCalendario.add(header, i, 0);
        }

        // Lógica de preenchimento dos dias
        LocalDate primeiroDiaMes = mesExibido.atDay(1);
        int diaDaSemanaInicial = primeiroDiaMes.getDayOfWeek().getValue() % 7; // Ajusta para 0=Dom, 1=Seg...
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

        // Verifica se a data está dentro do intervalo do semestre
        boolean dentroDoSemestre = (data.isEqual(semestreAtual.getDataInicio()) || data.isAfter(semestreAtual.getDataInicio())) &&
                (data.isEqual(semestreAtual.getDataFim()) || data.isBefore(semestreAtual.getDataFim()));

        if (!dentroDoSemestre) {
            lbl.setStyle("-fx-border-color: #dee2e6; -fx-background-color: #e9ecef; -fx-text-fill: #adb5bd; -fx-font-family: 'Monospaced';");
        } else {
            atualizarEstiloDinamico(lbl, data, coluna);
            lbl.setOnMouseClicked(event -> handleInteracaoDia(lbl, data, coluna));
        }
        return lbl;
    }

    private void atualizarEstiloDinamico(Label lbl, LocalDate data, int coluna) {
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
        if (coluna == 0) return; // Domingo não interativo

        if (coluna == 6) { // Sábado: Alterna Sábado Letivo
            boolean removido = semestreAtual.getSabadosLetivos().removeIf(s -> s.getData().equals(data));
            if (!removido) semestreAtual.addSabadoLetivo(new SabadoLetivo(semestreAtual, data));
        } else { // Dias de semana: Alterna Feriado
            boolean removido = semestreAtual.getDiasRestritos().removeIf(d -> d.getData().equals(data));
            if (!removido) {
                TextInputDialog dialog = new TextInputDialog("Feriado");
                dialog.setTitle("Restrição de Data");
                dialog.setHeaderText("Definir feriado para " + data);
                dialog.setContentText("Descrição:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(desc -> semestreAtual.addDiaRestrito(new DiaRestrito(semestreAtual, data, desc)));
            }
        }
        atualizarEstiloDinamico(lbl, data, coluna);
    }

    /**
     * Navega para o mês anterior, respeitando a data de início do semestre.
     */
    @FXML
    private void mesAnterior() {
        YearMonth limiteMinimo = YearMonth.from(semestreAtual.getDataInicio()); //

        if (mesExibido.isAfter(limiteMinimo)) {
            mesExibido = mesExibido.minusMonths(1);
            renderizarCalendario();
        } else {
            // Opcional: Feedback visual de que chegou ao limite
            System.out.println("Limite inicial do semestre atingido.");
        }
    }

    /**
     * Navega para o próximo mês, respeitando a data de término do semestre.
     */
    @FXML
    private void proximoMes() {
        YearMonth limiteMaximo = YearMonth.from(semestreAtual.getDataFim()); //

        if (mesExibido.isBefore(limiteMaximo)) {
            mesExibido = mesExibido.plusMonths(1);
            renderizarCalendario();
        } else {
            // Opcional: Feedback visual de que chegou ao limite
            System.out.println("Limite final do semestre atingido.");
        }
    }

    @FXML private void handleSalvarVoltar() { Main.loadView("SemestreLista.fxml"); }
}