package app.controller.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

public class ExcelWriter {

    public boolean iterationsHeaderWrote = false;
    private final Workbook workbook;
    private final Sheet sheet;
    private Integer currentRowIndex = 0;

    public ExcelWriter() {
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet();
    }

    public void algorithmName(String name) {
        Row row = sheet.createRow(currentRowIndex);
        Cell cell = row.createCell(0);
        cell.setCellValue(name);
        currentRowIndex++;
    }

    public void writeParameters(Map<String, Double> parameters) {
        int currentCellIndex = 0;
        Row nameRow = sheet.createRow(currentRowIndex);
        Row valueRow = sheet.createRow(currentRowIndex + 1);
        for (Map.Entry<String, Double> entry : parameters.entrySet()) {
            Cell nameCell = nameRow.createCell(currentCellIndex);
            Cell valueCell = valueRow.createCell(currentCellIndex);
            nameCell.setCellValue(entry.getKey());
            valueCell.setCellValue(entry.getValue());
            currentCellIndex++;
        }
        currentRowIndex = currentRowIndex + 2;
    }

    private void writeIterationsHeader() {
        if (!iterationsHeaderWrote) {
            iterationsHeaderWrote = true;
            Row headerRow = sheet.createRow(currentRowIndex);
            headerRow.createCell(0).setCellValue("Iteration number");
            headerRow.createCell(1).setCellValue("Solution distance");
            currentRowIndex++;
        }
    }


    public void writeIteration(long iteration, double solutionDistance) {
        writeIterationsHeader();

        Row row = sheet.createRow(currentRowIndex);
        row.createCell(0).setCellValue(iteration);
        row.createCell(1).setCellValue(solutionDistance);
        currentRowIndex++;
    }

    public void writeResult(double solutionDistance, int problemSize, long time) {
        Row nameRow = sheet.createRow(currentRowIndex);
        Row valueRow = sheet.createRow(currentRowIndex+1);
        nameRow.createCell(0).setCellValue("Result");
        nameRow.createCell(1).setCellValue("Problem size");
        nameRow.createCell(2).setCellValue("Solution distance");
        nameRow.createCell(3).setCellValue("Calculation time ms");

        valueRow.createCell(1).setCellValue(problemSize);
        valueRow.createCell(2).setCellValue(solutionDistance);
        valueRow.createCell(3).setCellValue(time);

        currentRowIndex = currentRowIndex + 3;
    }

    public void saveFile(String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("results/" + fileName + ".xlsx");
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}
