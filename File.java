import java.util.ArrayList;
import java.util.List;
import java.io.*;

class File {
   private static final String UNITDELIM = "\t";
   private static final String RCRDDELIM = "\n";
   private static final String EXTENSION = ".mdb";

   private String filename;
   private int lineCnt = 0;

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

   void testFileCreation() {
      // make file objects
      File test0 = new File();
      assert(test0.getName().equals("untitled.mdb"));
      String testStr = "test";
      File testFile = new File(testStr);
      // make tables
      assert(testFile.getName().equals(testStr + ".mdb"));
      Table testTable = new Table();
      testTable.setColumnNames("1", "2", "3");
      Record testR1 = new Record("a", "b", "c");
      testTable.add(testR1);
      Record testR2 = new Record("x", "y", "z");
      testTable.add(testR2);
      String testOutputStr = testFile.writeTableToString(testTable);
      assert(testOutputStr.equals(
         "1" + UNITDELIM + "2" + UNITDELIM + "3" + RCRDDELIM +
         "a" + UNITDELIM + "b" + UNITDELIM + "c" + RCRDDELIM +
         "x" + UNITDELIM + "y" + UNITDELIM + "z" + RCRDDELIM
      ));
      testFile.writeStringToFile(testOutputStr);
   }

   void testFileParsing() {

   }

   void runTests() {
      testFileCreation();
   }

   public static void main(String[] args) {
      File program = new File();
      program.runTests();
   }
}
