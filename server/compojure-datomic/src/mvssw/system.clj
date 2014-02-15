(ns mvssw.system
  (:require [com.stuartsierra.component :as component]
            [mvssw.server :as server]
            [mvssw.datastore :as datastore]))


(defrecord HttpServer [config server]
  component/Lifecycle
  (start [component]
    (println "Starting HTTP Server")
    (let [stop-fn (server/start config)]
      (println "  - HTTP Server started on port " (:port config))
      (assoc component :server {:stop-fn stop-fn})))
  (stop [component]
    (println "Stopping HTTP Server")
    (when-let [stop-fn (get-in  component [:server :stop-fn])]
      (stop-fn)
      (println "  - HTTP Server stopped"))
    (dissoc component :server)))

(defn http-server [config]
  (map->HttpServer {:config config}))


(defrecord Datastore [connection-string schema connection]
  component/Lifecycle
  (start [component]
    (println "Starting Datastore")
    (datastore/create connection-string)
    (let [connection (datastore/connect connection-string)]
      (println "  - Connected")
      (datastore/ensure-schema connection schema)
      (println "  - Created schema")
      (assoc component :connection connection)))
  (stop [component]
    ;; Nothing needs to be explicitly closed for datomic, as far as I
    ;; know.
    (println "Stopping Datastore")
    (dissoc component :connection)
    ))

(defn datastore [connection-string schema]
  (map->Datastore {:connection-string connection-string :schema schema}))


(defrecord App [config datastore http-server]
  component/Lifecycle
  (start [this]
    (component/start-system this))
  (stop [this]
    (component/stop-system this)))

(defn mvssw-app [config]
  (-> (map->App {:config config
                 :datastore (datastore (:connection-string config) {})
                 :http-server (http-server config)})
      (component/using [:datastore :http-server])))







