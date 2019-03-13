import java.util.ArrayList;
import java.util.List;
import java.io.*;

class Table {
   private String name;
   private List<String> columns;
   private List<Record> records;

   Table() {
      this.name = "untitled";
      this.columns = new ArrayList<String>();
      this.records = new ArrayList<Record>();
   }

   Table(String name) {
      this.name = name;
      this.columns = new ArrayList<String>();
      this.records = new ArrayList<Record>();
   }

   Table(String name, String... columns) {
      this.name = name;
      this.columns = new ArrayList<String>();
      this.records = new ArrayList<Record>();
      setColumnNames(columns);
   }

   void setColumnNames(String... names) {
      this.columns.clear();
      for (String entry : names) {
         this.columns.add(entry);
         //System.out.println(entry);
      }
   }

   int getColumnSize() {
      return this.columns.size();
   }

   int getRecordSize() {
      return this.records.size();
   }

   String getColumnName(int idx) {
      if (idx >= getColumnSize() || idx < 0) {
            System.out.println("No such column in table.");
            throw new IndexOutOfBoundsException();
      }
      return this.columns.get(idx);
   }

   String getName() {
      return this.name;
   }

   void setName(String name) {
      this.name = name;
   }

   Record select(int rowNum) {
      if (rowNum > records.size() || rowNum < 0) {
         System.out.println("No such row in record.");
         throw new IndexOutOfBoundsException();
      }
      return this.records.get(rowNum);
   }

   void add(Record data) {
      if (data.size() != columns.size()) {
         System.out.println("Input data does not match table columns.");
         throw new IllegalArgumentException();
      }
      this.records.add(data);
   }

   void insert(int idx, Record data) {
      if (!checkRecordDataExists(idx)) return;
      if (!checkRecordInputSize(data)) return;
      this.records.add(idx, data);
   }

   void delete(int idx) {
      if (!checkRecordDataExists(idx)) return;
      this.records.remove(idx);
   }

   void update(int idxRecord, int idxField, String data) {
      select(idxRecord).setField(idxField, data);
   }

   private boolean checkRecordDataExists(int idx) {
      if (idx >= this.records.size() || idx < 0) {
         System.out.println("No such record in table.");
         throw new IndexOutOfBoundsException();
      }
      return true;
   }

   private boolean checkRecordInputSize(Record data) {
      if (data.size() != this.columns.size()) {
         System.out.println("Input data does not match table columns.");
         throw new IllegalArgumentException();
      }
      return true;
   }

   //--- testing ---

   void testTableCreation() {
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
      test0.setColumnNames("1", "2", "3", "4");
      assert(test0.getColumnName(0).equals("1"));
      assert(test0.getColumnName(1).equals("2"));
      assert(test0.getColumnName(2).equals("3"));
      assert(test0.getColumnName(3).equals("4"));
      Table test1 = new Table("title", "X", "Y", "Z");
      assert(test1.getColumnName(0).equals("X"));
      assert(test1.getColumnName(1).equals("Y"));
      assert(test1.getColumnName(2).equals("Z"));
      //reset System.out
      System.out.flush();
      System.setOut(console);
   }

   void testTableManipulation() {
      //redirect System.out
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(baos);
      PrintStream console = System.out;
      System.setOut(out);
      //Begin tests
      //checking add records
      Table test1 = new Table();
      test1.setColumnNames("1", "2", "3", "4");
      Record testR1 = new Record("a", "b", "c", "d");
      test1.add(testR1);
      assert(test1.select(0).getField(0).equals("a"));
      assert(test1.select(0).getField(1).equals("b"));
      assert(test1.select(0).getField(2).equals("c"));
      assert(test1.select(0).getField(3).equals("d"));
      Record testR2 = new Record("a", "b", "c");
      boolean caught = false;
      try { test1.add(testR2); } 
      catch (IllegalArgumentException e) { caught = true; }
      assert(caught == true);
      caught = false;
      //checking delete records
      test1.delete(0);
      Record testR3 = new Record("q", "w", "e", "r");
      test1.add(testR3);
      assert(test1.select(0).getField(0).equals("q"));
      assert(test1.select(0).getField(1).equals("w"));
      assert(test1.select(0).getField(2).equals("e"));
      assert(test1.select(0).getField(3).equals("r"));
      test1.delete(0);
      try { test1.delete(0); }
      catch (IndexOutOfBoundsException e) { caught = true; }
      assert(caught == true);
      caught = false;
      //checking insert records
      test1.add(testR1);
      test1.add(testR1);
      test1.insert(1, testR3);
      assert(test1.select(1).getField(0).equals("q"));
      assert(test1.select(1).getField(1).equals("w"));
      assert(test1.select(1).getField(2).equals("e"));
      assert(test1.select(1).getField(3).equals("r"));
      try { test1.insert(4, testR3); }
      catch (IndexOutOfBoundsException e) { caught = true; }
      assert(caught == true);
      caught = false;
      //checking update records
      test1.delete(0);
      test1.delete(0);
      test1.add(testR1);
      test1.update(0, 0, "x");
      assert(test1.select(0).getField(0).equals("x"));
      try { test1.update(2, 0, "x"); }
      catch (IndexOutOfBoundsException e) { caught = true; }
      assert(caught == true);
      caught = false;
      try { test1.update(0, 4, "x"); }
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
