import java.util.ArrayList;
import java.util.List;
import java.io.*;

class Record {
   private List<String> row;

   Record() {
      this.row = new ArrayList<>();
   }

   Record(String... data) {
      this.row = new ArrayList<>();
      for (int i = 0; i < data.length; i++) {
         this.row.add(data[i]);
      }
   }

   void add(String data) {
      this.row.add(data);
   }

   void clear() {
      this.row.clear();
   }

   int size() {
      return this.row.size();
   }

   String getField(int idx) {
      checkRecordExists(idx);
      return this.row.get(idx);
   }

   void setField(int idx, String data) {
      checkRecordExists(idx);
      this.row.set(idx, data);
   }

   // --- helper methods ---

   private void checkRecordExists(int idx) {
      if (idx >= this.row.size() || idx < 0) {
         System.out.println("No such field in record.");
         throw new IndexOutOfBoundsException();
      }
   }

   // ---  testing ---

   // tests add(string), length(), and clear() methods
   private void basicTests() {
      Record test = new Record();
      test.add("test1");
      test.add("test2");
      assert(test.size() == 2);
      test.clear();
      Record testArr = new Record("test1", "test2");
      assert(testArr.size() == 2);
   }

   // tests getField(int) setField(int, String)
   private void getsetTests() {
      //redirect System.out
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(baos);
      PrintStream console = System.out;
      System.setOut(out);
      //Begin tests
      Record test = new Record();
      boolean caught = false;
      test.add("test1");
      test.add("test2");
      assert(test.getField(0).equals("test1"));
      assert(test.getField(1).equals("test2"));
      //out of bounds call
      try { test.getField(2); } 
      catch (IndexOutOfBoundsException e) { caught = true; }
      assert(caught == true);
      caught = false;
      try { test.getField(-1); } 
      catch (IndexOutOfBoundsException e) { caught = true; }
      assert(caught == true);
      caught = false;
      test.setField(0, "update1");
      assert(test.getField(0).equals("update1"));
      //check nothing is incorrectly pushed down in the list
      assert(test.getField(1).equals("test2"));
      try { test.getField(2); } 
      catch (IndexOutOfBoundsException e) { caught = true; }
      assert(caught == true);
      caught = false;
      test.setField(1, "update2");
      assert(test.getField(0).equals("update1"));
      assert(test.getField(1).equals("update2"));
      try { test.getField(2); } 
      catch (IndexOutOfBoundsException e) { caught = true; }
      assert(caught == true);
      caught = false;
      Record testArr = new Record("test1", "test2");
      assert(testArr.size() == 2);
      assert(testArr.getField(0).equals("test1"));
      assert(testArr.getField(1).equals("test2"));
      //reset System.out
      System.out.flush();
      System.setOut(console);
   }

   private void runTests() {
      basicTests();
      getsetTests();
   }

   public static void main(String[] args) {
      Record program = new Record();
      program.runTests();
   }
}
