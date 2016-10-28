---
layout: recipes-doc
title: Serializing Data
version: 1.0.0-incubating
---
Various Fluo Recipes deal with POJOs and need to serialize them.  The
serialization mechanism is configurable and defaults to using [Kryo][1].

## Custom Serialization

In order to use a custom serialization method, two steps need to be taken.  The
first step is to implement [SimpleSerializer][2].  The second step is to
configure Fluo Recipes to use the custom implementation.  This needs to be done
before initializing Fluo.  Below is an example of how to do this.

```java
  FluoConfiguration fluoConfig = ...;
  //assume MySerializer implements SimpleSerializer
  SimpleSerializer.setSetserlializer(fluoConfig, MySerializer.class);
  //initialize Fluo using fluoConfig
```

## Kryo Factory

If using the default Kryo serializer implementation, then creating a
KryoFactory implementation can lead to smaller serialization size.  When Kryo
serializes an object graph, it will by default include the fully qualified
names of the classes in the serialized data.  This can be avoided by
[registering classes][3] that will be serialized.  Registration is done by
creating a KryoFactory and then configuring Fluo Recipes to use it.   The
example below shows how to do this.

For example assume the POJOs named `Node` and `Edge` will be serialized and
need to be registered with Kryo.  This could be done by creating a KryoFactory
like the following.

```java

package com.foo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;

import com.foo.data.Edge;
import com.foo.data.Node;

public class MyKryoFactory implements KryoFactory {
  @Override
  public Kryo create() {
    Kryo kryo = new Kryo();
    
    //Explicitly assign each class a unique id here to ensure its stable over
    //time and in different environments with different dependencies.
    kryo.register(Node.class, 9);
    kryo.register(Edge.class, 10);
    
    //instruct kryo that these are the only classes we expect to be serialized
    kryo.setRegistrationRequired(true);
    
    return kryo;
  }
}
```

Fluo Recipes must be configured to use this factory.  The following code shows
how to do this.

```java
  FluoConfiguration fluoConfig = ...;
  KryoSimplerSerializer.setKryoFactory(fluoConfig, MyKryoFactory.class);
  //initialize Fluo using fluoConfig
```

[1]: https://github.com/EsotericSoftware/kryo
[2]: {{ site.api_static }}/fluo-recipes-core/1.0.0-incubating/org/apache/fluo/recipes/core/serialization/SimpleSerializer.html
[3]: https://github.com/EsotericSoftware/kryo#registration
