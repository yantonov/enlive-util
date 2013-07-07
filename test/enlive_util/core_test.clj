(ns enlive-util.core-test
  (:use net.cgrand.enlive-html)
  (:require [net.cgrand.xml :as xml])
  (:use clojure.test)
  (:use enlive-util.core))

(defn- test-step [expected pred node]
  (= expected (boolean (pred (xml/xml-zip node)))))

(defn- elt 
  ([tag] (elt tag nil))
  ([tag attrs & content]
     {:tag tag
      :attrs attrs
      :content content}))

(deftest add-attr-prefix-test
  (are [_1 _2] (test-step true
                          (attribute= :href _1)
                          ((add-attr-prefix :href _2)
                           (elt :a {:href "#"})))
       "prefix#" "prefix"
       "#" nil
       "#" ""))

(deftest add-attr-suffix-test
  (are [_1 _2] (test-step true
                          (attribute= :href _1)
                          ((add-attr-suffix :href _2)
                           (elt :a {:href "#"})))
       "#suffix" "suffix"
       "#" nil
       "#" ""))

(deftest add-context-path-test
  (are [_1 _2 _3 _4] (test-step _1
                                (attribute= :href _4)
                                ((add-context-path :href _3)
                                 (elt :a {:href _2})))
       true "/test" "" "/test"
       true "/" "/app" "/app/"
       true "/" "" "/"
       true "http://test" "/app" "http://test"))

(deftest apply-if-test
  (are [_1 _old _new _condition _actual]
    (test-step _1
               (attribute= :href _actual)
               ((apply-if _condition (set-attr :href _new))
                (elt :a {:href _old})))
    true "#" "#new" true "#new"
    true "#" "#new" false "#"))

