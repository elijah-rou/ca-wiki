(ns ca-wiki.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[ca-wiki started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[ca-wiki has shut down successfully]=-"))
   :middleware identity})
