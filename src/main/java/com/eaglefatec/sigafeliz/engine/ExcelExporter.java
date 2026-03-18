package com.eaglefatec.sigafeliz.engine;

import com.eaglefatec.sigafeliz.model.ScheduledAula;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Exports a ScheduleResult to an Excel .xlsx file.
 * Columns per US07: Nº Aula, Data, Tema, Marcador de Prova, Dia da Semana, Observações
 */
public class ExcelExporter {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Writes the schedule to an xlsx file.
     *
     * @param aulas the list of scheduled aulas
     * @param file  the target .xlsx file
     * @throws IOException if writing fails
     */
    public void export(List<ScheduledAula> aulas, File file) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Cronograma");

            // Header style — dark teal background with bold white text
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Create header row
            Row header = sheet.createRow(0);
            String[] columns = { "Nº Aula", "Data", "Tema", "Marcador de Prova", "Dia da Semana", "Observações" };
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data styles
            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle evalStyle = workbook.createCellStyle();
            evalStyle.setAlignment(HorizontalAlignment.CENTER);
            Font evalFont = workbook.createFont();
            evalFont.setBold(true);
            evalFont.setColor(IndexedColors.RED.getIndex());
            evalStyle.setFont(evalFont);

            // Fill data rows
            int rowNum = 1;
            for (ScheduledAula aula : aulas) {
                Row row = sheet.createRow(rowNum++);

                Cell numCell = row.createCell(0);
                numCell.setCellValue(aula.getAulaNumber());
                numCell.setCellStyle(centerStyle);

                Cell dateCell = row.createCell(1);
                dateCell.setCellValue(aula.getDate().format(DATE_FMT));
                dateCell.setCellStyle(centerStyle);

                Cell temaCell = row.createCell(2);
                temaCell.setCellValue(aula.getTemaTitle());

                Cell provaCell = row.createCell(3);
                if (aula.isEvaluation()) {
                    provaCell.setCellValue("PROVA");
                    provaCell.setCellStyle(evalStyle);
                } else {
                    provaCell.setCellValue("");
                    provaCell.setCellStyle(centerStyle);
                }

                Cell dayCell = row.createCell(4);
                dayCell.setCellValue(aula.getDayOfWeek() != null ? aula.getDayOfWeek() : "");
                dayCell.setCellStyle(centerStyle);

                Cell obsCell = row.createCell(5);
                obsCell.setCellValue(aula.getObservation() != null ? aula.getObservation() : "");
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write file
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }
}
