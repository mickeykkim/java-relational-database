import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class File {
   private static final String UNITDELIM = "\t";
   private static final String RCRDDELIM = "\n";
   private static final String KEYATTRIB = "*";
   private static final String EXTENSION = ".dbf";
   private static final String DEF_FNAME = "untitled";
   private static final String DEF_FPATH = "/databases/";
   private static final String CENCODING = "UTF-8";
   private static final String USER_PATH = System.getProperty("user.dir");

   private String filename;
   private String filepath;
   private String dirpath;
   private int lineCnt = 0;

   File() {
      this.filename = DEF_FNAME + EXTENSION;
      this.dirpath = USER_PATH + DEF_FPATH;
      this.filepath = this.dirpath + this.filename;
   }

   File(String file) {
      this.filename = file + EXTENSION;
      this.dirpath = USER_PATH + DEF_FPATH;
      this.filepath = this.dirpath + this.filename;
   }

   File(String folder, String file) {
      if (!folder.endsWith("/")) {
         folder = folder + "/";
      }
      this.filename = file + EXTENSION;
      this.dirpath = USER_PATH + folder;
      this.filepath = this.dirpath + this.filename;
   }

   String getName() {
      return this.filename;
   }

   String getFilePath() {
      return this.filepath;
   }

   void writeStringToFile(String input) {
      Path path = Paths.get(this.dirpath);
      if (!Files.exists(path)) {
         try {
            Files.createDirectories(path); 
         } 
         catch (IOException e) {
            e.printStackTrace(); 
         }
      }
      try {
         Files.write(Paths.get(this.filepath), input.getBytes(CENCODING)); 
      }
      catch (IOException e) {
         e.printStackTrace(); 
      }
   }

   // --- table handling ---

   String writeTableToString(Table table) {
      StringBuilder output = new StringBuilder();
      output.append(table.getName() + RCRDDELIM);
      writeTableColumns(table, output);
      writeTableRecords(table, output);
      return output.toString();
   }

   void writeTableColumns(Table table, StringBuilder output) {
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

   void writeTableRecords(Table table, StringBuilder output) {
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
            readTableRecords(outputTable, line);
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

   void readTableRecords(Table outputTable, String line) {
      Record newRecord = new Record();
      String[] fields = line.split(UNITDELIM);
      for (String entry : fields) {
         newRecord.add(entry);
      }
      outputTable.add(newRecord);
   }

   // --- database handling ---
   /*
   void writeDatabaseToString(Database data) {

   }

   Database readDatabaseFromFile(String filename){

   }
   */
   // --- testing ---

   private void testTableFileCreation() {
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

   private void testTableFileParsing() {
      String testStr = "test";
      File testFile = new File(testStr);
      Table testOut = new Table();
      boolean caught = false;
      try { testOut = testFile.readFileToTable(this.dirpath + testStr + EXTENSION); }
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

   private void testDatabaseFileCreation() {
      // Begin tests
      // create database
      Database testDB = new Database();   
      String testNameDB = "test_database";
      testDB.setName(testNameDB);
      String testFolderDB = "test_folder";
      testDB.setFolder(testFolderDB);
      // create table1
      String testNameT1 = "test_table1";
      Table testTable1 = new Table(
         testNameT1, 
         new ColumnID("key", true), 
         new ColumnID("2", false), 
         new ColumnID("3")
      );
      Record testT1R1 = new Record("key1", "1", "1");
      Record testT1R2 = new Record("key2", "1", "2");
      testTable1.add(testT1R1);
      testTable1.add(testT1R2);
      // create table2
      String testNameT2 = "test_table2";
      Table testTable2 = new Table(
         testNameT2, 
         new ColumnID("key", true), 
         new ColumnID("2", false), 
         new ColumnID("3")
      );
      Record testT2R1 = new Record("key1", "1", "1");
      Record testT2R2 = new Record("key2", "1", "2");
      testTable2.add(testT2R1);
      testTable2.add(testT2R2);
      // add tables to database
      testDB.add(testTable1);
      testDB.add(testTable2);
   }

   private void testDatabaseFileParsing() {

   }

   private void runTests() {
      testTableFileCreation();
      testTableFileParsing();
      testDatabaseFileCreation();
      testDatabaseFileParsing();
   }

   public static void main(String[] args) {
      File program = new File();
      program.runTests();
   }
}
