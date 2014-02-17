(ns mvssw.model.todo
  (:require [datomic.api :as datomic]))


(defn get-by-user [db user]
  (some-> '{:find [?todo]
            :in [$ ?owner]
            :where [[?todo :todo/owner ?owner]]}
          (datomic/q db (:db/id user))))

(defn create [conn title owner]
  (let [id (datomic/tempid :db.part/user)
        result
        @(datomic/transact conn
                           [{:db/id id
                             :todo/title title
                             :todo/owner (:db/id owner)}])]
    {:id (datomic/resolve-tempid (:db-after result) (:tempids result) id)
     :title title
     :owner (:user/username owner)}))
