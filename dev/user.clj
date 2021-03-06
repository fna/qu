(ns user
  (:require [alembic.still :refer [distill]]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint]]
            [clojure.repl :refer :all]
            [clojure.tools.namespace.repl :refer [refresh refresh-all set-refresh-dirs]]
            [com.stuartsierra.component :as component]
            [qu.main :as main]
            [qu.app :refer [new-qu-system]]
            [qu.app.options :refer [inflate-options]]
            [qu.util :refer :all]
            [qu.loader :as loader :refer [load-dataset]]))

(set-refresh-dirs "src/" "dev/")

(def system (atom nil))

(defn init
  ([] (init {}))
  ([options]
     (reset! system (new-qu-system (combine (main/default-options) options)))))

(defn start
  []
  (swap! system component/start)
  (swap! system assoc :running true))

(defn stop
  []
  (swap! system component/stop)
  (swap! system assoc :running false))

(defn go
  ([] (go {}))
  ([options]
     (init options)
     (start)))

(defn reset
  []
  (stop)
  (refresh :after 'user/go))

(defn load-sample-data
  []
  (if (:running @system)
    (load-dataset "county_taxes")
    (println "System not running")))
