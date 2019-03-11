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

   void createTable(String name, String... columns) {
      Table table = new Table(name);
      setColumnNames(columns);
   }

   void setColumnNames(String... names) {
      for (String entry : names) {
         this.columns.add(entry);
      }
   }

   String getName(){
      return this.name;
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
         throw new IndexOutOfBoundsException();
      }
      this.records.add(data);
   }

   void checkRecordDataExists(int idx) {
      if (idx >= this.records.size() || idx < 0) {
         System.out.println("No such record in table.");
         throw new IndexOutOfBoundsException();
      }
   }

   void checkRecordInputSize(Record data) {
      if (data.size() != this.columns.size()) {
         System.out.println("Input data does not match table columns.");
         throw new IndexOutOfBoundsException();
      }
   }

   void insert(int idx, Record data) {
      checkRecordDataExists(idx);
      checkRecordInputSize(data);
      this.records.add(idx, data);
      delete(idx + 1);
   }

   void delete(int idx) {
      checkRecordDataExists(idx);
      this.records.remove(idx);
   }

   void update(int idxRecord, int idxField, String data) {
      select(idxRecord).setField(idxField, data);
   }

   //--- testing ---

   void testTableCreation() {
      //redirect System.out
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(baos);
      PrintStream console = System.out;
      System.setOut(out);
      //table creation
      Table test = new Table();
      assert(test.getName().equals("untitled"));
      String testName = "test";
      Table test1 = new Table(testName);
      assert(test1.getName().equals(testName));
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
