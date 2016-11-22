package ft;

//Normally using * with imports is a bad practice, however in this case it makes experimenting with
//Fluo easier.

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.apache.fluo.api.client.*;
import org.apache.fluo.api.client.scanner.*;
import org.apache.fluo.api.config.*;
import org.apache.fluo.api.data.*;
import org.apache.fluo.api.mini.MiniFluo;
import org.apache.fluo.api.observer.*;

public class Main {
  public static void main(String[] args) throws Exception {

    String tmpDir = Files.createTempDirectory(Paths.get("target"), "mini").toString();
    // System.out.println("tmp dir : "+tmpDir);

    FluoConfiguration fluoConfig = new FluoConfiguration();
    fluoConfig.setApplicationName("class");
    fluoConfig.setMiniDataDir(tmpDir);

    preInit(fluoConfig);

    System.out.print("Starting MiniFluo ... ");

    try (MiniFluo mini = FluoFactory.newMiniFluo(fluoConfig);
        FluoClient client = FluoFactory.newClient(mini.getClientConfiguration())) {

      System.out.println("started.");

      excercise(mini, client);
    }
  }

  private static void preInit(FluoConfiguration fluoConfig) {
    //this method does not need to be changed for earlier excercises in tour
    //TODO try adjusting max tx mem commit mem
  }

  private static void excercise(MiniFluo mini, FluoClient client) {
    
    
    Column cols[] = new Column[]{new Column("fam1","qual1"), new Column("fam1","qual2"), new Column("fam1","qual3")};

    int count = 0;
    long t1 = System.currentTimeMillis();
    for (int k = 0; k < 1000; k++) {
      
      try (LoaderExecutor le = client.newLoaderExecutor()) {
        for (int i = 0; i < 10; i++) {
          final int start = count;
          count+=1000;
          le.execute((tx, ctx) -> {
            for (int j = 0; j < 333; j++) {
              String row = String.format("%06x", j + start);
              tx.set(row, cols[0], Integer.toHexString(Math.abs(row.hashCode())));
              tx.set(row, cols[1], Integer.toHexString(Math.abs(row.hashCode()*3)));
              tx.set(row, cols[2], Integer.toHexString(Math.abs(row.hashCode()*5)));
            }
          });
        }
      }
    }
    long t2 = System.currentTimeMillis();
    System.out.println("time1 " + (t2 - t1));
    
    HashSet<Column> colSet = new HashSet<>();
    colSet.add(cols[0]);
    colSet.add(cols[2]);
    colSet.add(new Column("fam1","qual4"));
    
    for(int i = 0; i< 100; i++){
      scan(client, cols, colSet);
    }
  }

  private static void scan(FluoClient client, Column[] cols, HashSet<Column> colSet) {
    long t1;
    long t2;
    try(Snapshot snap = client.newSnapshot()) {
      t1 = System.currentTimeMillis();
      CellScanner cellScanner = snap.scanner().fetch(cols[0],cols[2]).build();
      int count = 0;
      for (RowColumnValue rcv : cellScanner) {
       if(colSet.contains(rcv.getColumn())){
         count++;
       }
       
       rcv.getRow().hashCode();
      }
      
      System.out.println("count "+count);
      t2 = System.currentTimeMillis();
      System.out.println("time2 "+(t2 - t1));
    }
  }
}
