---
title: Fetching multiple cells
---

Each call to get a row column results in a RPC to an Accumulo server.  In the cases where there are
many row columns to get, Fluo provides more specialized get methods that make less RPC calls.

 * In a single transactions generate 100 rows each with 100 columns such that each row has the same
   columns.  Use integers for the row and columns names.
 * Get 100 columns from a single row in the following ways.  Time each method and compare the times.
   * In a loop call  [gets(CharSequence, Column)][get]
   * Call [gets(CharSequence, Set\<Column\>)][getsc]
 * Get 100 columns from 3 rows in the following ways.  Time each method and compare times.
   * For three rows, loop over 100 columns calling  [gets(CharSequence, Column)][get]
   * Call [gets(Collection\<? extends CharSequence\>, Set\<Column\>)][getmc]
 * Generate 100 row column pairs, where each pair is a random row and a random column. Get each pair
   in the following ways and compare the times.
   * For each pair call [gets(CharSequence, Column)][get]
   * Call [gets(Collection\<RowColumn\>)][getrc]

[get]: /apidocs/fluo/{{ site.latest_fluo_release }}/org/apache/fluo/api/client/SnapshotBase.html#gets-java.lang.CharSequence-org.apache.fluo.api.data.Column-
[getsc]: /apidocs/fluo/{{ site.latest_fluo_release }}/org/apache/fluo/api/client/SnapshotBase.html#gets-java.lang.CharSequence-java.util.Set-
[getmc]: /apidocs/fluo/{{ site.latest_fluo_release }}/org/apache/fluo/api/client/SnapshotBase.html#gets-java.util.Collection-java.util.Set-
[getrc]: /apidocs/fluo/{{ site.latest_fluo_release }}/org/apache/fluo/api/client/SnapshotBase.html#gets-java.util.Collection-

