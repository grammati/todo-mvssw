(ns mvssw.server
  (:require [org.httpkit.server :as server]
            [mvssw.routes :as routes]
            [mvssw.config :as config]))


(defn start [config datastore]
  (let [config (merge {:port   (config/int "HTTP_PORT" 8888)
                       :thread (config/int "HTTP_SERVER_THREADS" 64)}
                      config)]
    (server/run-server (routes/make-handler datastore) config)))
