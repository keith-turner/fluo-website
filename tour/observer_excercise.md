---
title: Observer Excercises
---

Some things to try with observers.

## Exercise 1

Try creating overlapping transactions that modify the same observed column.  One of the transactions
should fail.  This is really the same exercise as [collisions](/tour/collisions).  Just want to
reinforce the collision implication when using strong notifications.

## Exercise 2

Create an observer chain.  Have each observer add one to the row and value that triggered it and set
those to the new column.

 * Have *Observer1* observe column *C1* and set column *C2*.
 * Have *Observer2* observe column *C2* and set column *C3*.
 * Have *Observer3* observe column *C3* and set column *C4*.
 * Insert some data using column *C1*.
 * Wait for observers.
 * Scan and print everything

## Exercise 3

Rewrite exercise 2 such that there is only one observer class and the observed column and modified
column is configured.  To accomplish this, first call
[setParameters(Map\<String,String\>)][setparams] when creating the observer configuration before
initialization. Pass in a map with some parameters that specify the column names.   Second, in the
observers `init(Context c)` method call [getParamters()][getparams] and setup the observers two
columns using these parameters.

 * Configure *Observer1* to observe column *C1* and set column *C2*.
 * Configure *Observer1* to observe column *C2* and set column *C3*.
 * Configure *Observer1* to observe column *C3* and set column *C4*.
 * Insert some data using column *C1*.
 * Wait for observers.
 * Scan and print everything.

[setparams]: /apidocs/fluo/{{ site.latest_fluo_release }}/org/apache/fluo/api/config/ObserverSpecification.html#setParameters-java.util.Map-
[getparams]: /apidocs/fluo/{{ site.latest_fluo_release }}/org/apache/fluo/api/observer/Observer.Context.html#getParameters--
