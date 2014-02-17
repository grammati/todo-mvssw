(ns mvssw.routes
  (:require [compojure.core :refer [GET POST routes defroutes context]]
            [compojure.route :as route]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [mvssw.middleware :refer [wrap-authentication]]
            [mvssw.model.todo :as todo]))


(def wrap-json (comp wrap-json-response wrap-json-body))

(defn wrap-datastore-connection [handler connection]
  (fn [request]
    (handler (assoc request
               :connection connection
               :db (datomic.api/db connection)))))

(defn todo-list [request]
  (todo/get-by-user (:db request) (get-in request [:session :user])))

(defn todo-create [request]
  (todo/create (get-in request [:body :title])
               (get-in request [:session :user])))

(def api-routes
  (context "/api" []
    (-> (routes
         (GET "/api/todo"  [] todo-list)
         (POST "/api/todo" [] todo-create))
        wrap-json
        wrap-authentication)))

(defroutes app-routes
  api-routes
  (route/not-found "Not Found"))

(defn make-handler [datastore]
  (-> app-routes
      (wrap-datastore-connection (:connection datastore))
      wrap-params
      wrap-cookies))


;; Local Variables:
;; mode: clojure
;; eval: (define-clojure-indent (context (quote defun)))
;; End:

