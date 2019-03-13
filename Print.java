import java.io.*;
import java.math.*;

class Print {
   private int[] colWidth;

   Print() {
      this.colWidth = new int[0];
   }

   // return a copy of the selected width
   int getColWidth(int i) {
      int ret = this.colWidth[i];
      return ret;
   }

   // set initial column widths from column names
   void setInitialWidths(Table inputTable, int colNum) {
      this.colWidth = new int[colNum];
      for (int i = 0; i < colNum; i++) {
         this.colWidth[i] = inputTable.getColumnName(i).length();
      }
   }

   // get max column widths from record fields
   void setMaxWidths(Table inputTable) {
      int colNum = inputTable.getColumnSize();
      setInitialWidths(inputTable, colNum);
      for (int i = 0; i < inputTable.getRecordSize(); i++) {
         for (int j = 0; j < colNum; j++) {
            this.colWidth[j] = 
               Math.max(this.colWidth[j], inputTable.select(i).getField(j).length());
         }
      }
   }

   void testSetWidths() {
      Print testPrint = new Print();
      String testStr = "test";
      // make tables
      Table testTable = new Table(testStr);
      testTable.setColumnNames("1", "2", "3");
      Record testR1 = new Record("a", "b", "c");
      testTable.add(testR1);
      Record testR2 = new Record("dd", "eee", "fff");
      testTable.add(testR2);
      Record testR3 = new Record("gg", "hh", "iiii");
      testTable.add(testR3);
      testPrint.setInitialWidths(testTable, testTable.getColumnSize());
      assert(testPrint.getColWidth(0)==1);
      assert(testPrint.getColWidth(1)==1);
      assert(testPrint.getColWidth(2)==1);
      testPrint.setMaxWidths(testTable);
      assert(testPrint.getColWidth(0)==2);
      assert(testPrint.getColWidth(1)==3);
      assert(testPrint.getColWidth(2)==4);
   }

   void runTests() {
      testSetWidths();
   }

   public static void main(String[] args) {
      Print program = new Print();
      program.runTests();
   }
}
