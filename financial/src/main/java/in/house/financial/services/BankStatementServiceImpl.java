package in.house.financial.services;

import com.spire.pdf.PdfDocument;
import in.house.financial.interfaces.BankStatementService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.utilities.PdfTable;
import com.spire.pdf.utilities.PdfTableExtractor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BankStatementServiceImpl implements BankStatementService {

       @Override
        public String readStatementFile(String path) throws IOException {
           path = "D:/table.pdf";
            // Validate path
           test(path);
            File pdfFile = new File(path);
            if (!pdfFile.exists()) {
                throw new IllegalArgumentException("PDF file not found at path: " + path);
            }
           PDDocument pdf = PDDocument.load(pdfFile);
            // Use a try-with-resources block for proper resource handling
           ObjectExtractor oe = new ObjectExtractor(pdf);
           SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
           Page page = oe.extract(1);

           // extract text from the table after detecting
           List<Table> table = sea.extract(page);
           for(Table tables: table) {
               List<List<RectangularTextContainer>> rows = tables.getRows();

               for(int i=0; i<rows.size(); i++) {

                   List<RectangularTextContainer> cells = rows.get(i);

                   for(int j=0; j<cells.size(); j++) {
                       System.out.print(cells.get(j).getText()+"|");
                   }
               }
           }
           return "guess success";

       }

       public void test(String path) throws IOException {

           //Load a sample PDF document
           PdfDocument pdf = new PdfDocument(path);

           //Create a StringBuilder instance
           StringBuilder builder = new StringBuilder();
           //Create a PdfTableExtractor instance
           PdfTableExtractor extractor = new PdfTableExtractor(pdf);

           //Loop through the pages in the PDF
           for (int pageIndex = 0; pageIndex < pdf.getPages().getCount(); pageIndex++) {
               //Extract tables from the current page into a PdfTable array
               PdfTable[] tableLists = extractor.extractTable(pageIndex);

               //If any tables are found
               if (tableLists != null && tableLists.length > 0) {
                   //Loop through the tables in the array
                   for (PdfTable table : tableLists) {
                       //Loop through the rows in the current table
                       for (int i = 0; i < table.getRowCount(); i++) {
                           //Loop through the columns in the current table
                           for (int j = 0; j < table.getColumnCount(); j++) {
                               //Extract data from the current table cell and append to the StringBuilder
                               String text = table.getText(i, j);
                               builder.append(text + " | ");
                           }
                           builder.append("\r\n");
                       }
                   }
               }
           }

           //Write data into a .txt document
           FileWriter fw = new FileWriter("ExtractTable.txt");
           fw.write(builder.toString());
           fw.flush();
           fw.close();
       }

}

