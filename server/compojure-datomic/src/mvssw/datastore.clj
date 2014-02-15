(ns mvssw.datastore
  (:require [datomic.api :as datomic]))


(defn create [connection-string]
  (datomic/create-database connection-string))

(defn connect [connection-string]
  (datomic/connect connection-string))

(defn ensure-schema [connection schema]
  )
