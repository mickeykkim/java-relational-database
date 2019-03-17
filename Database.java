import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.io.*;

class Database {
   private String name;
   private String folder;
   private LinkedHashMap<String,Table> tables;

   private static final String EXTENSION = ".dbf";
   private static final String noSuchRecord = "No such table exists in database.";
   private static final String duplicateKey = "Duplicate table names in database.";

   Database() {
      this.name = "untitled";
      this.folder = "databases";
      this.tables = new LinkedHashMap<String,Table>();
   }

   Database(String name) {
      this.name = name;
      this.folder = "databases";
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
         if (this.tables.containsKey(entry.name())) {
            this.tables.clear();
            System.out.println(duplicateKey);
            throw new IllegalArgumentException(); 
         }
         this.tables.add(entry.getName(), entry);
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
      this.tables.put(key, table);
   }

   Table select(String key) {
      checkIfTableExists(key);
      return this.tables.get(key);
   }

   // --- helper methods ---

   private void checkIfTableExists(String key) {
      if (!this.tables.containsKey(key)) {
         System.out.println(noSuchTable);
         throw new IllegalArgumentException();
      }
   }

   // --- testing ---

   private void testDatabaseCreation() {

   }

   private void runTests() {
      testDatabaseCreation();
   }

   public static void main(String[] args) {
      Database program = new Database();
      program.runTests();
   }
}
