(defproject mvssw-compojure-datomic "0.1.0-SNAPSHOT"
  
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [http-kit "2.1.16"]
                 [ring/ring-core "1.2.1"]
                 [compojure "1.1.6"]
                 [com.datomic/datomic-free "0.9.4556"]
                 [com.stuartsierra/component "0.2.1"]
                 [ring/ring-json "0.2.0"]
                 [crypto-password "0.1.1"]]

  :profiles
  {:dev {:jvm-opts ["-Dmvssw.datomic.connection-string"]
         :dependencies [[org.clojure/tools.namespace "0.2.4"]]
         :source-paths ["src" "dev"]
         ;;:main user
         }}
  
  :main mvssw.main
  )
