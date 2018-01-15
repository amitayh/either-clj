# either-clj [![Build Status](https://travis-ci.org/amitayh/either-clj.svg?branch=master)](https://travis-ci.org/amitayh/either-clj) [![codecov](https://codecov.io/gh/amitayh/either-clj/branch/master/graph/badge.svg)](https://codecov.io/gh/amitayh/either-clj)

Simple error handling for Clojure. Representing success/failure value (disjoint union)
by a 2 elements vector - where the first value is the result (`nil` if failed), and the second
value is the error (`nil` if successful). Plus, the library provides a few useful utilities for
working with these values.

This is inspired by the `either` monad (in languages such as [Scala](http://www.scala-lang.org/api/2.12.0/scala/util/Either.html)
or [Haskell](https://hackage.haskell.org/package/base-4.10.1.0/docs/Data-Either.html)), and is
meant to be used for functional error handling, instead of throwing exceptions

## Getting started

### Installation

Add the necessary dependency to your project:

[![Clojars Project](https://img.shields.io/clojars/v/org.amitayh/either.svg)](https://clojars.org/org.amitayh/either)

### Basic usage

```clojure
(require '[org.amitayh.either :refer :all])

(defn div [a b]
  (if (zero? b)
    (failure :division-by-zero)
    (success (/ a b))))

(div 1 2) ; Returns [1/2 nil]
(div 1 0) ; Returns [nil :division-by-zero]
```

### Mapping

Since `either`s are [functors](https://en.wikipedia.org/wiki/Functor), they support "mapping"
(transforming the internal value by a function)

```clojure
(def result1 (success 1))
(def result2 (failure :error))

(fmap result1 inc) ; Returns [2 nil]
(fmap result2 inc) ; Returns [nil :error]
```

### Binding

Since `either`s are [monads](https://en.wikipedia.org/wiki/Monad_(functional_programming)),
they support monadic binding ("flat mapping")

```clojure
(defn to-upper [str]
  (if (empty? str)
    (failure :empty-str)
    (-> str .toUpperCase success)))

(def result1 (success "hello world"))
(def result2 (success ""))
(def result3 (failure :error))

(bind result1 to-upper) ; Returns ["HELLO WORLD" nil]
(bind result2 to-upper) ; Returns [nil :empty-str]
(bind result3 to-upper) ; Returns [nil :error]
```

### Chaining macro

In many cases you want to chain several `bind` operations together (similar to Scala's for
comprehension or Haskell's do notation). This is what the `chain` macro is used for:

```clojure
(chain
  [foo (success 1)
   bar (success (+ foo 2))]
 (+ foo bar)) ; Returns [4 nil]
```

For more examples, look at the [tests](test/org/amitayh/either_test.clj)

## License

Copyright © 2018 Amitay Horwitz

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
