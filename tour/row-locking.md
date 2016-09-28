---
title: Row Locking
---

Fluo relies on Accumulo's conditional mutations to implement cross node
transactions.  Conditional mutations lock entire rows on the server side when
checking conditions.  These row locks can impact the performance of your
transactions, so its something to be aware of when designing a schema.

The following code demonstrate the impact of schema design on performance. The
code adds lots of edges to a single node in a graph using many transactions and
threads. All of the edges are added to a single row.

These performance problems may not occur on a single node with a single client,
because Fluo clients batch a lot of operations related to committing.  To make
the problem more apparent, the following code creates three clients and three
loaders.

```java
  public static class EdgeLoader implements Loader {

    private String node1;
    private String node2;

    public EdgeLoader(String node1, String node2) {
      this.node1 = node1;
      this.node2 = node2;
    }

    @Override
    public void load(TransactionBase tx, Context ctx) throws Exception {
      tx.set(node1, new Column("edge", node2), "");
    }
  }

  private static void excercise(MiniFluo mini, FluoClient client) {

    String node1 = "n00000";

    long t1 = System.currentTimeMillis();

    try (LoaderExecutor le1 = client.newLoaderExecutor();
        FluoClient client2 = FluoFactory.newClient(mini.getClientConfiguration());
        LoaderExecutor le2 = client2.newLoaderExecutor();
        FluoClient client3 = FluoFactory.newClient(mini.getClientConfiguration());
        LoaderExecutor le3 = client2.newLoaderExecutor()) {

      int start = 1;

      for(LoaderExecutor le : Arrays.asList(le1, le2, le3)) {
        for (int i = 1; i < 10000; i++) {
          String node2 = String.format("%05d", i+start);
          le.execute(new EdgeLoader(node1, node2));
        }

        start+=10000;
      }
    }

    long t2 = System.currentTimeMillis();

    System.out.println(t2 - t1);
  }
```

Try running the code above and note the time.  Then change the load function to
the following and run the code again.  You should see a significant decrease in
the time it takes.  The change below spreads the edges over many rows.

```java
    @Override
    public void load(TransactionBase tx, Context ctx) throws Exception {
      tx.set(node1+":"+node2, new Column("edge", ""), "");
    }
```



