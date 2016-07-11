(ns user
  (:require [mount.core :as mount]
            ca-wiki.core))

(defn start []
  (mount/start-without #'ca-wiki.core/repl-server))

(defn stop []
  (mount/stop-except #'ca-wiki.core/repl-server))

(defn restart []
  (stop)
  (start))


