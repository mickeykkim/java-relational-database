import java.util.ArrayList;
import java.util.List;

class Record {
   private List<String> row;

   Record() {
      this.row = new ArrayList<>();
   }

   private void add(String input) {
      this.row.add(input);
   }

   private void clear() {
      this.row.clear();
   }

   int length() {
      return this.row.size();
   }

   String getField(int idx) {
      if (idx >= this.row.size()) {
         throw new IndexOutOfBoundsException();
      }
      return this.row.get(idx);
   }

   void setField(int idx, String input) {
      this.row.add(idx, input);
      this.row.remove(idx + 1);
   }

   // ---  testing ---

   // tests add(string), length(), and clear() methods
   void basicTests() {
      Record test = new Record();
      test.add("test1");
      test.add("test2");
      assert(test.length() == 2);
      test.clear();
   }

   // tests getField(int) setField(int, String)
   void getsetTests() {
      Record test = new Record();
      boolean caught = false;
      test.add("test1");
      test.add("test2");
      assert(test.getField(0).equals("test1"));
      assert(test.getField(1).equals("test2"));
      //out of bounds call
      try { test.getField(2); } 
      catch (Exception e) { caught = true; }
      assert(caught == true);
      caught = false;
      try { test.getField(-1); } 
      catch (Exception e) { caught = true; }
      assert(caught == true);
      caught = false;
      test.setField(0, "update1");
      assert(test.getField(0).equals("update1"));
      assert(test.getField(1).equals("test2"));
      //check nothing is incorrectly pushed down in the list
      try { test.getField(2); } 
      catch (Exception e) { caught = true; }
      assert(caught == true);
      caught = false;
      test.setField(1, "update2");
      assert(test.getField(0).equals("update1"));
      assert(test.getField(1).equals("update2"));
      try { test.getField(2); } 
      catch (Exception e) { caught = true; }
      assert(caught == true);
      caught = false;
   }

   void run(String[] args) {
      basicTests();
      getsetTests();
   }

   public static void main(String[] args) {
      Record program = new Record();
      program.run(args);
   }

}