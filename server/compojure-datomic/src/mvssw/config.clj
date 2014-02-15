(ns mvssw.config
  (:refer-clojure :exclude [int]))


;;; Hmmm, I seem to put this same file in nearly every clojure
;;; project I do ...

(defn config
  ([name]
     (config name nil))
  ([name default]
     (or (System/getenv name)
         (System/getProperty name)
         default)))

(defn int
  ([name]
     (int name nil))
  ([name default]
     (when-let [v (config name default)]
       (Long/valueOf v))))
