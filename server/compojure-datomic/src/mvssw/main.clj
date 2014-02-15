(ns mvssw.main
  (:require [mvssw.server :as server]))

(defn -main [& args]
  (server/start))
