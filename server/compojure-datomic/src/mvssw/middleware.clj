(ns mvssw.middleware
  (:require [clojure.string :as string]
            [ring.util.codec :as codec]
            [mvssw.model.user :as user]))


(defn extract-basic-auth [request]
  (if-let [header (get-in request [:headers "authorization"])]
    (if-let [[_ ^String value] (re-matches #"\s*Basic (.*)$" header)]
      (try
       (let [[username password]
             (-> value
                 codec/base64-decode
                 (String. "UTF-8")      ; I guess...
                 (string/split #":" 2))]
         {:username username
          :password password})
       (catch Exception e
         nil                            ; Decoding error?
         )))))

(defn wrap-basic-auth
  "Middleware that extracts HTTP Basic auth credentials from the
  authorization header, if present, and puts them into the request
  under the key :basic-auth, with the value being a map
  containing :username and :password."
  [handler]
  (fn [request]
    (handler
     (if-let [auth (extract-basic-auth request)]
       (assoc request :basic-auth auth)
       request))))

(defn wrap-authenticate [handler]
  (fn [request]
    (if-let [{:keys [username password]} (:basic-auth request)]
      (let [user (user/authenticate (:db request) username password)
            resp (handler (assoc-in request [:session :user] user))]
        (assoc-in resp [:session :user] user))
      (handler request))))

(defn wrap-require-authentication [handler]
  (fn [request]
    (if (get-in request [:session :user])
      (handler request)
      {:status 401
       :headers {"WWW-Authenticate" "Basic"}})))

(def wrap-authentication
  (comp wrap-require-authentication
        wrap-authenticate
        wrap-basic-auth))
