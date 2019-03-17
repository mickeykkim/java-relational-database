import java.util.List;
import java.io.*;

class File {
   private static final String UNITDELIM = "\t";
   private static final String RCRDDELIM = "\n";
   private static final String KEYATTRIB = "*";
   private static final String EXTENSION = ".dbt";

   private String filename;
   private String filepath;
   private String dirpath;
   private int lineCnt = 0;

   File() {
      this.filename = "untitled" + EXTENSION;
      this.filepath = "/databases/" + this.filename;
      this.dirpath = "/databases/";
   }

   File(String file) {
      this.filename = file + EXTENSION;
      this.filepath = "/databases/" + this.filename;
      this.dirpath = "/databases/";
   }

   File(String folder, String file) {
      if (!folder.endsWith("/")) {
         folder = folder + "/";
      }
      this.filename = file + EXTENSION;
      this.filepath = folder + this.filename;
      this.dirpath = folder;
   }

   String getName() {
      return this.filename;
   }

   String writeTableToString(Table table) {
      StringBuilder output = new StringBuilder();
      output.append(table.getName() + RCRDDELIM);
      writeColumns(table, output);
      writeRecords(table, output);
      return output.toString();
   }

   void writeColumns(Table table, StringBuilder output) {
      int colsz = table.getColumnSize();
      int keyColumn = table.getKeyColumn();
      for (int i = 0; i < colsz; i++) {
         if (i == keyColumn) {
               output.append(KEYATTRIB);
         }
         output.append(table.getColumnName(i));
         if (i < colsz - 1) {
            output.append(UNITDELIM);
         }
      }
      output.append(RCRDDELIM);
   }

   void writeRecords(Table table, StringBuilder output) {
      int colsz = table.getColumnSize();
      List<String> recordKeys = table.getKeyList();
      for (String entry : recordKeys) {
         for (int i = 0; i < colsz; i++) {
            output.append(table.select(entry).getField(i));
            if (i < colsz - 1) {
               output.append(UNITDELIM);
            }
         }
         output.append(RCRDDELIM);
      }
   }

   void writeStringToFile(String input) {
      try (PrintStream ps = new PrintStream(this.filename)) { 
         ps.print(input); 
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }
   }

   Table readFileToTable(String filepath) throws Exception {
      BufferedReader bReader = new BufferedReader(new FileReader(filepath));
      String line;
      String tableName = new String();
      Table outputTable = new Table();
      this.lineCnt = 0;
      while ((line = bReader.readLine()) != null) {
         if (this.lineCnt == 0) {
            tableName = line;
         } else if (this.lineCnt == 1) {
            outputTable = readColumnsToTable(outputTable, line, tableName);
         } else {
            readRecords(outputTable, line);
         }
         this.lineCnt++;
      }
      bReader.close();
      return outputTable;
   }

   Table readColumnsToTable(Table outputTable, String line, String tableName) {
      String[] headers = line.split(UNITDELIM);
      ColumnID[] columns = new ColumnID[headers.length];
      for (int i = 0; i < headers.length; i++) {
         if (headers[i].startsWith("*")) {
            columns[i] = new ColumnID(headers[i].replace(KEYATTRIB, ""), true);
         } else {
            columns[i] = new ColumnID(headers[i], false);
         }
      }
      return new Table(tableName, columns);
   }

   void readRecords(Table outputTable, String line) {
      Record newRecord = new Record();
      String[] fields = line.split(UNITDELIM);
      for (String entry : fields) {
         newRecord.add(entry);
      }
      outputTable.add(newRecord);
   }

   // --- testing ---

   private void testFileCreation() {
      // make file objects
      File test0 = new File();
      assert(test0.getName().equals("untitled" + EXTENSION));
      String testStr = "test";
      File testFile = new File(testStr);
      assert(testFile.getName().equals(testStr + EXTENSION));
      // make tables
      Table testTable = new Table(testStr);
      testTable.setColumnIDs(
         new ColumnID("1", true), 
         new ColumnID("2", false), 
         new ColumnID("3")
      );
      Record testR1 = new Record("a", "b", "c");
      testTable.add(testR1);
      Record testR2 = new Record("x", "y", "z");
      testTable.add(testR2);
      String testOutputStr = testFile.writeTableToString(testTable);
      assert(testOutputStr.equals(
         testStr + RCRDDELIM +
         KEYATTRIB + "1" + UNITDELIM + "2" + UNITDELIM + "3" + RCRDDELIM +
         "a" + UNITDELIM + "b" + UNITDELIM + "c" + RCRDDELIM +
         "x" + UNITDELIM + "y" + UNITDELIM + "z" + RCRDDELIM
      ));
      testFile.writeStringToFile(testOutputStr);
   }

   private void testFileParsing() {
      String testStr = "test";
      File testFile = new File(testStr);
      Table testOut = new Table();
      boolean caught = false;
      try { testOut = testFile.readFileToTable(testStr + EXTENSION); }
      catch (Exception e) { caught = true; }
      assert(caught == false);
      assert(testFile.getName().equals(testStr + EXTENSION));
      String testInputFile = testFile.writeTableToString(testOut);
      assert(testInputFile.equals(
         testStr + RCRDDELIM +
         KEYATTRIB + "1" + UNITDELIM + "2" + UNITDELIM + "3" + RCRDDELIM +
         "a" + UNITDELIM + "b" + UNITDELIM + "c" + RCRDDELIM +
         "x" + UNITDELIM + "y" + UNITDELIM + "z" + RCRDDELIM
      ));
   }

   private void runTests() {
      testFileCreation();
      testFileParsing();
   }

   public static void main(String[] args) {
      File program = new File();
      program.runTests();
   }
}
