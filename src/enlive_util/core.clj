(ns enlive-util.core
  (:require [net.cgrand.enlive-html :as enlive]))

(defn apply-if [condition enlive-fn]
  (if (true? condition)
    enlive-fn
    (fn [node] node)))

(defn get-attr [node attribute]
  (get (:attrs node) attribute))

(defn attribute=
  "Check if given attribute has given value"
  [name value]
  (enlive/pred #(= (get-attr % name) value)))

(defn add-attr-prefix
  "Adds prefix to attibute value."
  [attr-name prefix]
  (fn [node]
    (let [attrs (:attrs node {})
          new-attrs (assoc attrs
                      attr-name
                      (str prefix (attrs attr-name "")))]
      (assoc node :attrs new-attrs))))

(defn add-attr-suffix
  "Adds prefix to attibute value."
  [attr-name suffix]
  (fn [node]
    (let [attrs (:attrs node {})
          new-attrs (assoc attrs
                      attr-name
                      (str (attrs attr-name "") suffix))]
      (assoc node :attrs new-attrs))))

(defn add-context-path
  "Add servlet context path if needed"
  [attr-name context-path]
  (fn [node]
    (if (-> node
            (:attrs)
            (get attr-name)
            (. startsWith "/"))
      ((add-attr-prefix attr-name context-path) node)
      node)))

