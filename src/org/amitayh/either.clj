(ns org.amitayh.either
  (:require [clojure.core.match :refer [match]]))

(defn success [value] [value nil])

(defn failure [error] [nil error])

(defn bind [[value error :as either] f]
  (if (nil? error) (f value) either))

(defn fmap [either f]
  (bind either (comp success f)))

(defmacro chain [bindings body]
  (match bindings
    [x y] `(fmap ~y (fn [~x] ~body))
    [x y & rest] `(bind ~y (fn [~x] (chain ~rest ~body)))))
