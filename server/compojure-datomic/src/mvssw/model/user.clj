(ns mvssw.model.user
  (:require [datomic.api :as datomic]
            [ring.util.codec :as codec]
            [crypto.password.bcrypt :as hash]))


(defn get-by-username [db username]
  (some-> '{:find [?e]
            :in [$ ?username]
            :where [[?e :user/username ?username]]}
          (datomic/q db username)
          first
          first
          (->> (datomic/entity db)
               datomic/touch)))

(defn authenticate [db username password]
  (when-let [user (get-by-username db username)]
    (when (hash/check password (:user/password user))
      user)))

(defn create [conn username password]
  (if-let [user (get-by-username (datomic/db conn) username)]
    false
    (do
      (let [id (datomic/tempid :db.part/user)
            result
            @(datomic/transact conn
                               [{:db/id id
                                 :user/username username
                                 :user/password (hash/encrypt password)}])]
        {:id (datomic/resolve-tempid (:db-after result) (:tempids result) id)}))))
