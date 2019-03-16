import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.*;

class Table {
   private String name;
   private ArrayList<ColumnID> columns;
   private LinkedHashMap<String,Record> records;
   private int keyColumn = -1;

   private static final String noSuchRecord = "No such record exists in table.";
   private static final String noSuchColumn = "No such column exists in table.";
   private static final String duplicateKey = "Duplicate key exists in table.";
   private static final String noKeySpecified = "No key specified in table.";

   Table() {
      this.name = "untitled";
      this.columns = new ArrayList<ColumnID>();
      this.records = new LinkedHashMap<String,Record>();
   }

   Table(String name) {
      this.name = name;
      this.columns = new ArrayList<ColumnID>();
      this.records = new LinkedHashMap<String,Record>();
   }

   Table(String name, ColumnID... columns) {
      this.name = name;
      this.columns = new ArrayList<ColumnID>();
      this.records = new LinkedHashMap<String,Record>();
      setColumnIDs(columns);
   }

   void setColumnIDs(ColumnID... columns) {
      checkIfUniqueKeyColumnExists(columns);
      for (ColumnID entry : columns) {
         this.columns.add(entry);
      }
   }

   int getColumnSize() {
      return this.columns.size();
   }

   int getRecordSize() {
      return this.records.size();
   }

   String getColumnName(int idx) {
      checkIfColumnExists(idx);
      return this.columns.get(idx).getName();
   }

   String getName() {
      return this.name;
   }

   void setName(String name) {
      this.name = name;
   }

   Record select(String key) {
      checkIfRecordExists(key);
      return this.records.get(key);
   }

   void add(Record data) {
      String key = data.getField(this.keyColumn);
      checkIfRecordsMatchColumns(data);
      checkIfDuplicateKey(key);
      this.records.put(key, data);
   }

   //TO DO: check if trying to update key but key already exists
   void update(String key, int idx, String input) {
      checkIfRecordExists(key);
      select(key).setField(idx, input);
   }

   void delete(String key) {
      checkIfRecordExists(key);
      this.records.remove(key);
   }

   // --- helper methods ---

   private void checkIfUniqueKeyColumnExists(ColumnID... values) {
      int colCnt = 0;
      this.columns.clear();
      for (ColumnID entry : values) {
         if (entry.containsKeys() == true && this.keyColumn == -1) {
            this.keyColumn = colCnt;
         } else if (entry.containsKeys() == true && this.keyColumn != -1) {
            this.columns.clear();
            System.out.println(duplicateKey);
            throw new IllegalArgumentException(); 
         }
         colCnt++;
      }
      checkIfKeyColumnExists();
   }

   private void checkIfKeyColumnExists() {
      if (this.keyColumn == -1) {
         this.columns.clear();
         System.out.println(noKeySpecified);
         throw new IllegalArgumentException(); 
      }
   }

   private void checkIfColumnExists(int idx) {
      if (idx >= this.columns.size() || idx < 0) {
         System.out.println(noSuchColumn);
         throw new IndexOutOfBoundsException();
      }
   }

   private void checkIfRecordExists(String key) {
      if (!this.records.containsKey(key)) {
         System.out.println(noSuchRecord);
         throw new IllegalArgumentException();
      }
   }

   private void checkIfDuplicateKey(String key) {
      if (this.records.containsKey(key)) {
         System.out.println(duplicateKey);
         throw new IllegalArgumentException();
      }
   }

   private void checkIfRecordsMatchColumns(Record data) {
      if (data.size() != this.columns.size()) {
         System.out.println("Input data does not match table columns.");
         throw new IllegalArgumentException();
      }
   }

   // --- testing ---

   private void testTableCreation() {
      //redirect System.out
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(baos);
      PrintStream console = System.out;
      System.setOut(out);
      //Begin tests
      Table test = new Table();
      assert(test.getName().equals("untitled"));
      String testName = "test";
      test.setName(testName);
      assert(test.getName().equals(testName));
      Table test0 = new Table(testName);
      assert(test0.getName().equals(testName));
      //checking column validity
      test0.setColumnIDs(
         new ColumnID("key", true), 
         new ColumnID("2", false), 
         new ColumnID("3"), 
         new ColumnID("4")
      );
      assert(test0.getColumnName(0).equals("key"));
      assert(test0.getColumnName(1).equals("2"));
      assert(test0.getColumnName(2).equals("3"));
      assert(test0.getColumnName(3).equals("4"));
      Table test1 = new Table(
         "title", 
         new ColumnID("X", true), 
         new ColumnID("Y", false), 
         new ColumnID("Z")
      );
      assert(test1.getColumnName(0).equals("X"));
      assert(test1.getColumnName(1).equals("Y"));
      assert(test1.getColumnName(2).equals("Z"));
      //reset System.out
      System.out.flush();
      System.setOut(console);
   }

   private void testTableManipulation() {
      //redirect System.out
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(baos);
      PrintStream console = System.out;
      System.setOut(out);
      //Begin tests
      //checking add records
      Table test1 = new Table();
      test1.setColumnIDs(
         new ColumnID("key", true), 
         new ColumnID("2", false), 
         new ColumnID("3"), 
         new ColumnID("4")
      );
      Record testR1 = new Record("key1", "b", "c", "d");
      test1.add(testR1);
      assert(test1.select("key1").getField(0).equals("key1"));
      assert(test1.select("key1").getField(1).equals("b"));
      assert(test1.select("key1").getField(2).equals("c"));
      assert(test1.select("key1").getField(3).equals("d"));
      Record testR2 = new Record("a", "b", "c");
      boolean caught = false;
      try { test1.add(testR2); } 
      catch (IllegalArgumentException e) { caught = true; }
      assert(caught == true);
      caught = false;
      //checking delete records
      test1.delete("key1");
      Record testR3 = new Record("key2", "w", "e", "r");
      test1.add(testR3);
      assert(test1.select("key2").getField(0).equals("key2"));
      assert(test1.select("key2").getField(1).equals("w"));
      assert(test1.select("key2").getField(2).equals("e"));
      assert(test1.select("key2").getField(3).equals("r"));
      test1.delete("key2");
      try { test1.delete("key2"); }
      catch (IllegalArgumentException e) { caught = true; }
      assert(caught == true);
      caught = false;
      //checking update records
      test1.add(testR1);
      test1.update("key1", 0, "x");
      assert(test1.select("key1").getField(0).equals("x"));
      try { test1.update("b", 0, "x"); }
      catch (IllegalArgumentException e) { caught = true; }
      assert(caught == true);
      caught = false;
      try { test1.update("key1", 4, "x"); }
      catch (IndexOutOfBoundsException e) { caught = true; }
      assert(caught == true);
      caught = false;
      //reset System.out
      System.out.flush();
      System.setOut(console);
   }

   void runTests() {
      testTableCreation();
      testTableManipulation();
   }

   public static void main(String[] args) {
      Table program = new Table();
      program.runTests();
   }
}
