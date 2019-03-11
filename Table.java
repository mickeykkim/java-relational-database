import java.util.ArrayList;
import java.util.List;

class Table {
   private String name;
   private List<String> columns;
   private List<Record> records;

   Table() {
      this.name = "untitled";
      this.columns = new Arraylist<String>();
      this.records = new Arraylist<Record>();
   }

   Table(String name) {
      this.name = name;
      this.columns = new Arraylist<String>();
      this.records = new Arraylist<Record>();
   }

   void createTable(String name, String... columns) {
      Table table = new Table(name);
      setColumnNames(columns);
   }

   void setColumnNames(String... names) {
      for (int i = 0; i < names.length; i++) {
         this.columns.add(columns(i));
      }
   }

   Record select(int rowNum) {
      if (rowNum > records.length || rowNum < 0) {
         System.out.println("No such row in record.");
         throw new IndexOutOfBoundsException();
      }
      return this.records.get(rowNum);
   }

   void add(Record data) {
      if (data.length != columns.length) {
         System.out.println("Input data does not match table columns.");
         throw new NoSuchFieldException();
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
      if (data.length != this.columns.length) {
         System.out.println("Input data does not match table columns.");
         throw new NoSuchFieldException();
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
      this.records.select(idxRecord).setField(idxField, data);
   }

   //--- testing ---

   void testTableCreation() {

   }

   void testTableManipulation() {

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
