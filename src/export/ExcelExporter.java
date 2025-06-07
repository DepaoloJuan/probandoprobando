package com.centroestetico.export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.centroestetico.agenda.TurnoVista;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExporter {

    public static void exportarTurnos(List<TurnoVista> turnos, Path destino) throws Exception {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Turnos");

            /* ---- estilos básicos ---- */
            CellStyle headerStyle = wb.createCellStyle();
            Font bold = wb.createFont(); bold.setBold(true);
            headerStyle.setFont(bold);

            /* ---- cabecera ---- */
            Row head = sheet.createRow(0);
            String[] cols = {"Fecha", "Hora", "Cliente", "Servicio", "Empleado", "Estado"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = head.createCell(i);
                c.setCellValue(cols[i]);
                c.setCellStyle(headerStyle);
            }

            /* ---- datos ---- */
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            int rowNum = 1;
            for (TurnoVista t : turnos) {
                Row r = sheet.createRow(rowNum++);
                r.createCell(0).setCellValue(t.fecha.format(df));
                r.createCell(1).setCellValue(t.hora);
                r.createCell(2).setCellValue(t.cliente);
                r.createCell(3).setCellValue(t.servicio);
                r.createCell(4).setCellValue(t.empleado);
                r.createCell(5).setCellValue(t.estado);
            }

            /* ---- auto-size ---- */
            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);

            try (FileOutputStream out = new FileOutputStream(destino.toFile())) {
                wb.write(out);
            }
        }
    }
}

