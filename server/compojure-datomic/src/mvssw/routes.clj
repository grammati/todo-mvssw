(ns mvssw.routes
  (:require [compojure.core :refer [GET routes defroutes context]]
            [compojure.route :as route]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.session :refer [wrap-session]]))


(defn wrap-authentication [handler]
  (fn [request]
    ()))

(def api-routes
  (context "/api" []
    (-> (routes
         (GET "/api/todo" [] "FIXME"))
        wrap-authentication)))

(defroutes app-routes
  api-routes
  (route/not-found "Not Found"))

(def handler
  (-> app-routes
      wrap-params
      wrap-cookies))


;; Local Variables:
;; mode: clojure
;; eval: (define-clojure-indent (context (quote defun)))
;; End:

