---
title: Excercise Two Solution
---

A simple approach to solve this problem is to keep track of two counts for each tag :

 * The degree, the total number of users that referenced it.
 * The number of users that mention another user AND both users reference the tag.

One way to incrementally compute the above counts is to update these counts for each edge thats
added.  When an edge between a tag and a user is added, look for links between existing users.  For
example assume *#tag1* links to *@user1* and *@user2*.  When a link from *#tag1* to *@user3* is
added, we can look for edges between *@user3* and the existing users *@user1* and *@user2*.  If
links are found, then update the counts for *#tag1*.

When a new edge between users is added, look for tags the users have in common.  For example assume
*@user9* links to *#tag1* and *#tag2* and *@user6* links to *#tag4* and *#tag2*.  If a new edge is
added between *@user9* and *@user6*, then since *#tag2* is common its count should be incremented.

When processing new edges, the following two problems could arise from concurrency.

 * Double counting an edge.
 * Never counting an edge.

For example assume that the edges *@user1*,*#tag1* and *@user1*,*@user2* were added at the same
time. Also assume an edge already existed between *@user2* and *#tag1*.  Its possible that
processing of the two new edges could miss each other or both see each other.  One simple solution
to this is to have the processing of an edge write something to each node.  This prevents the edges
from processing concurrently and forces one to follow the other.   However, this solution may lead
to lots transactional collisions for high degree nodes.

Locking the node helps prevent the problem of edges missing each other.  However there is still the
problem of double counting.  To solve this problem, each edge could be given a state of NEW or
PROCESSED.  When processing a new edge, only consider other processed edges.  Do not consider other new edges,
these edges will be accounted for when they are processed.  After an edge is processed by an
observer, set its state to PROCESSED.

TODO outline what observers and loader should do.

 * TeetLoader
 * EdgeObserver
 * TagObserver

