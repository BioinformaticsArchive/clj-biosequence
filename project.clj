(defproject clj-biosequence "0.1.2-SNAPSHOT"
  :description "Library for the manipulation of biological sequences."
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/data.xml "0.0.6"]
                 [org.clojure/data.zip "0.1.1"]
                 [com.h2database/h2 "1.2.141"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [clj-http "0.7.4"]
                 [fs "1.3.2"]
                 [org.clojars.hozumi/clj-commons-exec "1.0.6"]]
  :plugins [[codox "0.6.4"]]
  :codox {:src-dir-uri "https://github.com/s312569/clj-biosequence/blob/master/"
          :src-linenum-anchor-prefix "L"})