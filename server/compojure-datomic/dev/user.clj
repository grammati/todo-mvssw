(ns user
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.namespace.repl :as repl]
            [mvssw.system :as system]))

(def system nil)

(def config
  {:connection-string "datomic:mem://mvssw"
   :port              8888})

(defn init []
  (alter-var-root #'system
                  (fn [_]
                    (system/mvssw-app config))))

(defn start []
  (alter-var-root #'system component/start))

(defn stop []
  (alter-var-root #'system
                  (fn [s] (when s (component/stop s)))))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (repl/refresh :after 'user/go))
