import java.io.*;

class File {
   private static final String UNITDELIM = "\t";
   private static final String RCRDDELIM = "\n";
   private static final String EXTENSION = ".mdb";

   private String filename;
   private int lineCnt;

   File() {
      this.filename = "untitled" + EXTENSION;
   }

   File(String file) {
      this.filename = file + EXTENSION;
   }

   String getName() {
      return this.filename;
   }

   String writeTableToString(Table table) {
      StringBuilder output = new StringBuilder();
      int colsz = table.getColumnSize();
      int recsz = table.getRecordSize();
      // table name
      output.append(table.getName() + RCRDDELIM);
      // columns
      for (int i = 0; i < colsz; i++) {
         output.append(table.getColumnName(i));
         if (i < colsz - 1) {
            output.append(UNITDELIM);
         }
      }
      output.append(RCRDDELIM);
      // records
      for (int i = 0; i < recsz; i++) {
         for (int j = 0; j < colsz; j++) {
            output.append(table.select(i).getField(j));
            if (j < colsz - 1) {
               output.append(UNITDELIM);
            }
         }
         output.append(RCRDDELIM);
      }
      return output.toString();
   }

   void writeStringToFile(String input) {
      try (PrintStream ps = new PrintStream(filename)) { 
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
            this.lineCnt++;
         } else if (this.lineCnt == 1) {
            String[] headers = line.split(UNITDELIM);
            outputTable = new Table(tableName, headers);
            this.lineCnt++;
         } else {
            Record newRecord = new Record();
            String[] fields = line.split(UNITDELIM);
            for (String entry : fields) {
               newRecord.add(entry);
            }
            outputTable.add(newRecord);
            this.lineCnt++;
         }
      }
      return outputTable;
   }

   void testFileCreation() {
      // make file objects
      File test0 = new File();
      assert(test0.getName().equals("untitled" + EXTENSION));
      String testStr = "test";
      File testFile = new File(testStr);
      assert(testFile.getName().equals(testStr + EXTENSION));
      // make tables
      Table testTable = new Table(testStr);
      testTable.setColumnNames("1", "2", "3");
      Record testR1 = new Record("a", "b", "c");
      testTable.add(testR1);
      Record testR2 = new Record("x", "y", "z");
      testTable.add(testR2);
      String testOutputStr = testFile.writeTableToString(testTable);
      assert(testOutputStr.equals(
         testStr + RCRDDELIM +
         "1" + UNITDELIM + "2" + UNITDELIM + "3" + RCRDDELIM +
         "a" + UNITDELIM + "b" + UNITDELIM + "c" + RCRDDELIM +
         "x" + UNITDELIM + "y" + UNITDELIM + "z" + RCRDDELIM
      ));
      testFile.writeStringToFile(testOutputStr);
   }

   void testFileParsing() {
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
         "1" + UNITDELIM + "2" + UNITDELIM + "3" + RCRDDELIM +
         "a" + UNITDELIM + "b" + UNITDELIM + "c" + RCRDDELIM +
         "x" + UNITDELIM + "y" + UNITDELIM + "z" + RCRDDELIM
      ));
   }

   void runTests() {
      testFileCreation();
      testFileParsing();
   }

   public static void main(String[] args) {
      File program = new File();
      program.runTests();
   }
}
