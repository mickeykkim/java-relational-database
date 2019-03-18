import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.io.*;

class Database {
   private String name;
   private String folder = "/databases/";
   private LinkedHashMap<String,Table> tables;

   private static final String noSuchTable = "No such table exists in database.";
   private static final String duplicateKey = "Duplicate table names in database.";
   private static final String sizeMismatch = "Records are not the same size.";

   Database() {
      this.name = "untitled";
      this.tables = new LinkedHashMap<String,Table>();
   }

   Database(String name) {
      this.name = name;
      this.tables = new LinkedHashMap<String,Table>();
   }

   Database(String name, String folder) {
      this.name = name;
      this.folder = folder;
      this.tables = new LinkedHashMap<String,Table>();
   }

   Database(String name, String folder, Table... tables) {
      this.name = name;
      this.folder = folder;
      this.tables = new LinkedHashMap<String,Table>();
      setTables(tables);
   }

   void setTables(Table... tables) {
      this.tables.clear();
      for (Table entry : tables) {
         checkIfDuplicateKeyAndClear(entry.getName(), true);
         this.tables.put(entry.getName(), entry);
      }
   }

   String getName() {
      return this.name;
   }

   void setName(String name) {
      this.name = name;
   }

   String getFolder() {
      return this.folder;
   }

   void setFolder(String folder) {
      this.folder = folder;
   }

   void add(Table table) {
      String key = table.getName();
      checkIfDuplicateKeyAndClear(table.getName(), false);
      this.tables.put(key, table);
   }

   Table select(String key) {
      checkIfTableExists(key);
      return this.tables.get(key);
   }

   void delete(String key) {
      checkIfTableExists(key);
      this.tables.remove(key);
   }

   void update(String tableName, String recordName, Record newRecord) {
      checkIfTableExists(tableName);
      Record currRecord = select(tableName).select(recordName);
      checkValidRecordUpdate(currRecord, newRecord);
      for (int i = 0; i < currRecord.size(); i++) {
         select(tableName).update(recordName, i, newRecord.getField(i));
      }
   }

   List<String> getKeyList() {
      ArrayList<String> keys = new ArrayList<>(tables.keySet());
      return keys;
   }

   // --- helper methods ---

   private void checkValidRecordUpdate(Record currRecord, Record newRecord) {
      if (currRecord.size() != newRecord.size()) {
         System.out.println(sizeMismatch);
         throw new IndexOutOfBoundsException();
      }
   }

   private void checkIfTableExists(String key) {
      if (!this.tables.containsKey(key)) {
         System.out.println(noSuchTable);
         throw new IllegalArgumentException();
      }
   }

   private void checkIfDuplicateKeyAndClear(String key, boolean clear) {
      if (this.tables.containsKey(key)) {
         if (clear == true) this.tables.clear();
         System.out.println(duplicateKey);
         throw new IllegalArgumentException();
      }
   }

   // --- testing ---

   private void testDatabaseCreation() {
      // redirect System.out
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(baos);
      PrintStream console = System.out;
      System.setOut(out);
      // Begin tests
      // create database
      Database testDB = new Database();
      assert(testDB.getName().equals("untitled"));      
      String testNameDB = "test_database";
      testDB.setName(testNameDB);
      assert(testDB.getName().equals(testNameDB));
      String testFolderDB = "test_folder";
      testDB.setFolder(testFolderDB);
      assert(testDB.getFolder().equals(testFolderDB));
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
      assert(testDB.select(testTable1.getName()) == testTable1);
      testDB.add(testTable2);
      assert(testDB.select(testTable2.getName()) == testTable2);
      List<String> testDBKeys = testDB.getKeyList();
      assert(testDBKeys.get(0) == testNameT1);
      assert(testDBKeys.get(1) == testNameT2);
      // update tables
      Record testUpdateR = new Record("key1", "3", "3");
      testDB.update(testTable1.getName(), "key1", testUpdateR);
      assert(testDB.select(testTable1.getName()).select("key1").getField(1).equals("3"));
      assert(testDB.select(testTable1.getName()).select("key1").getField(2).equals("3"));
      // delete tables
      testDB.delete(testTable2.getName());
      boolean caught = false;
      try { testDB.select(testTable2.getName()); } 
      catch (IllegalArgumentException e) { caught = true; }
      assert(caught == true);
      caught = false;
      try { testDB.delete(testTable2.getName()); } 
      catch (IllegalArgumentException e) { caught = true; }
      assert(caught == true);
      caught = false;
      //reset System.out
      System.out.flush();
      System.setOut(console);
   }

   private void runTests() {
      testDatabaseCreation();
   }

   public static void main(String[] args) {
      Database program = new Database();
      program.runTests();
   }
}
