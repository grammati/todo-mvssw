(ns mvssw.datastore
  (:require [datomic.api :as datomic]))


(def schema
  [{:db/id                 #db/id[:db.part/db]
    :db/ident              :user/username
    :db/valueType          :db.type/string
    :db/cardinality        :db.cardinality/one
    :db/doc                "Username"
    :db/index              true
    :db.install/_attribute :db.part/db}
   
   {:db/id                 #db/id[:db.part/db]
    :db/ident              :user/password
    :db/valueType          :db.type/string
    :db/cardinality        :db.cardinality/one
    :db/doc                "Hashed Password"
    :db/index              false
    :db.install/_attribute :db.part/db}

   {:db/id                 #db/id[:db.part/db]
    :db/ident              :todo/title
    :db/valueType          :db.type/string
    :db/cardinality        :db.cardinality/one
    :db/doc                "TODO Title"
    :db/index              true
    :db.install/_attribute :db.part/db}

   {:db/id                 #db/id[:db.part/db]
    :db/ident              :todo/owner
    :db/valueType          :db.type/ref
    :db/cardinality        :db.cardinality/one
    :db/doc                "TODO Owner"
    :db/index              true
    :db.install/_attribute :db.part/db}])

(defn create [connection-string]
  (datomic/create-database connection-string))

(defn connect [connection-string]
  (datomic/connect connection-string))

(defn ensure-schema [connection]
  @(datomic/transact connection schema))
