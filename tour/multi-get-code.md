---
title: Code for fetching multiple cells
---

```java
  private static void excercise(MiniFluo mini, FluoClient client) {

    Set<Column> columns = new LinkedHashSet<>();

    for(int c = 0; c < 100; c++) {
      columns.add(new Column("f", String.format("q%04d", c)));
    }

    try(Transaction tx = client.newTransaction()) {
      int value = 0;
      for(int r = 0; r < 100; r++) {
        String row = String.format("r%04d", r);
        for (Column column : columns) {
          tx.set(row, column, value+"");
          value++;
        }
      }

      tx.commit();
    }

    //fetch multiple columns from a single row
    try(Snapshot snap = client.newSnapshot()) {
      String row = String.format("r%04d", 42);

      long t1 = System.currentTimeMillis();

      for (Column column : columns) {
        snap.gets(row, column);
      }

      long t2 = System.currentTimeMillis();

      snap.gets(row, columns);

      long t3 = System.currentTimeMillis();

      System.out.printf("test1 time 1:%d  time2:%d\n",(t2-t1),(t3-t2));
    }


    //fetch the same columns from multiple rows
    try(Snapshot snap = client.newSnapshot()) {
      List<String> rows = Arrays.asList(String.format("r%04d", 42),
                                        String.format("r%04d", 21),
                                        String.format("r%04d", 84));

      long t1 = System.currentTimeMillis();

      for (String row : rows) {
        for (Column column : columns) {
          snap.gets(row, column);
        }
      }

      long t2 = System.currentTimeMillis();

      snap.gets(rows, columns);

      long t3 = System.currentTimeMillis();

      System.out.printf("test2 time 1:%d  time2:%d\n",(t2-t1),(t3-t2));
    }

    //fetch different columns from different rows
    try(Snapshot snap = client.newSnapshot()) {
      Random rand = new Random();
      //generate the row columns to fetch
      List<RowColumn> rowcols = new ArrayList<>();
      for(int i = 0; i < 100; i++) {
        String row = String.format("r%04d", rand.nextInt(100));
        Column col = new Column("f", String.format("q%04d", rand.nextInt(100)));
        rowcols.add(new RowColumn(row, col));
      }

      long t1 = System.currentTimeMillis();

      for (RowColumn rowColumn : rowcols) {
        snap.get(rowColumn.getRow(), rowColumn.getColumn());
      }

      long t2 = System.currentTimeMillis();

      snap.gets(rowcols);

      long t3 = System.currentTimeMillis();

      System.out.printf("test3 time 1:%d  time2:%d\n",(t2-t1),(t3-t2));
    }
  }
```

The program above outputs :

```
test1 time 1:294  time2:13
test2 time 1:651  time2:25
test3 time 1:153  time2:7
```
