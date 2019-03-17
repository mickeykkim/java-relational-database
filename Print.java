import java.util.List;

class Print {
   private int[] colWidth;

   private static final String X_DIV = "+";
   private static final String H_DIV = "-";
   private static final String V_DIV = "|";
   private static final String EMPTY = " ";
   private static final String NEWLN = "\n";
   private static final int XPADD = 2;
   private static final int HPADD = XPADD/2;

   Print() {
      this.colWidth = new int[0];
   }

   // Returns string   +---+---+---+
   // in the format:   | 1 | 2 | 3 |
   //                  +---+---+---+
   //                  | a | b | c |
   //                  | x | y | z |
   //                  +---+---+---+
   String printTableToString(Table inputTable) {
      String horDiv = generateHorizontalDivider(inputTable);
      StringBuilder tableStringBuilder = new StringBuilder();
      tableStringBuilder.append(horDiv);
      // i = -1 for column headers; i = 0..recsz for records
      for (int i = -1; i < inputTable.getRecordSize(); i++) {
         tableStringBuilder.append(generateDataString(inputTable, i));
         if (i == -1) {
            tableStringBuilder.append(horDiv);
         }
      }
      tableStringBuilder.append(horDiv);
      return tableStringBuilder.toString();
   }

   // --- helper methods ---

   // return a copy of the selected width
   private int getColWidth(int i) {
      int ret = this.colWidth[i];
      return ret;
   }

   // set initial column widths from column names
   private void setInitialWidths(Table inputTable, int colNum) {
      this.colWidth = new int[colNum];
      for (int i = 0; i < colNum; i++) {
         this.colWidth[i] = inputTable.getColumnName(i).length();
      }
   }

   // get max column widths from record fields
   private void setMaxWidths(Table inputTable) {
      int colsz = inputTable.getColumnSize();
      setInitialWidths(inputTable, colsz);
      List<String> recordKeys = inputTable.getKeyList();
      for (String entry : recordKeys) {
         for (int i = 0; i < colsz; i++) {
            this.colWidth[i] = 
               Math.max(
                  this.colWidth[i], 
                  inputTable.select(entry).getField(i).length()
               );
         }
      }
   }

   // Returns string like "+---+---+---+\n"
   private String generateHorizontalDivider(Table inputTable) {
      StringBuilder horDivBuilder = new StringBuilder();
      for (int i = 0; i < inputTable.getColumnSize(); i++) {
         horDivBuilder.append(X_DIV);
         for (int j = 0; j < getColWidth(i) + XPADD; j++) {
            horDivBuilder.append(H_DIV);
         }
      }
      horDivBuilder.append(X_DIV + NEWLN);
      return horDivBuilder.toString();
   }

   // Returns string like "| data 1 | data 2 | data 3 |\n"
   // idxData = -1 for column headers; idxData >= 0 for records
   private String generateDataString(Table inputTable, int idxData) {
      int colsz = inputTable.getColumnSize();
      if (idxData > colsz || idxData < -1) {
         System.out.println("No such data in table.");
         throw new IndexOutOfBoundsException();
      }
      StringBuilder dataBuilder = new StringBuilder();
      String currField;
      List<String> recordKeys = inputTable.getKeyList();
      for (int i = 0; i < inputTable.getColumnSize(); i++) {
         if (idxData == -1) {
            currField = inputTable.getColumnName(i);
         } else {
            currField = inputTable.select(recordKeys.get(idxData)).getField(i);
         }
         dataBuilder.append(V_DIV + EMPTY + currField);
         // fill right side of data with empty spaces
         for (int j = 0; j < getColWidth(i) - currField.length() + HPADD; j++) {
            dataBuilder.append(EMPTY);
         }
      }
      dataBuilder.append(V_DIV + NEWLN);
      return dataBuilder.toString();
   }

   // --- testing ---

   private void testPrintingMethods() {
      Print testPrint = new Print();
      String testStr = "test_table";
      // make tables
      Table testTable = new Table(testStr);
      testTable.setColumnIDs(
         new ColumnID("1", true), 
         new ColumnID("2", false), 
         new ColumnID("3")
      );
      Record testR1 = new Record("a", "b", "c");
      testTable.add(testR1);
      Record testR2 = new Record("dd", "eee", "fff");
      testTable.add(testR2);
      Record testR3 = new Record("gg", "hh", "iiii");
      testTable.add(testR3);
      // set and test widths
      testPrint.setInitialWidths(testTable, testTable.getColumnSize());
      assert(testPrint.getColWidth(0)==1);
      assert(testPrint.getColWidth(1)==1);
      assert(testPrint.getColWidth(2)==1);
      testPrint.setMaxWidths(testTable);
      assert(testPrint.getColWidth(0)==2);
      assert(testPrint.getColWidth(1)==3);
      assert(testPrint.getColWidth(2)==4);
      // test sub strings
      assert(testPrint.generateHorizontalDivider(testTable).equals(
         "+----+-----+------+\n"
      ));
      assert(testPrint.generateDataString(testTable, -1).equals(
         "| 1  | 2   | 3    |\n"
      ));
      assert(testPrint.generateDataString(testTable, 0).equals(
         "| a  | b   | c    |\n"
      ));
      assert(testPrint.generateDataString(testTable, 1).equals(
         "| dd | eee | fff  |\n"
      ));
      assert(testPrint.generateDataString(testTable, 2).equals(
         "| gg | hh  | iiii |\n"
      ));
      // test full print
      assert(testPrint.printTableToString(testTable).equals(
         "+----+-----+------+\n" +
         "| 1  | 2   | 3    |\n" +
         "+----+-----+------+\n" +
         "| a  | b   | c    |\n" +
         "| dd | eee | fff  |\n" +
         "| gg | hh  | iiii |\n" +
         "+----+-----+------+\n"
      ));
   }

   private void runTests() {
      testPrintingMethods();
   }

   public static void main(String[] args) {
      Print program = new Print();
      program.runTests();
   }
}
