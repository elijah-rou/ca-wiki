(ns ca-wiki.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [ca-wiki.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[ca-wiki started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[ca-wiki has shut down successfully]=-"))
   :middleware wrap-dev})
