(ns ca-wiki.web
    (:require [compojure.core :refer [defroutes GET]]
            [ring.adapter.jetty :as ring]
            [hiccup.page :as page]
            [clojure.java.io :as io]))

(defn index[]
    (page/html5
        [:head
            [:title "HI"]]
            [:body
                [:div {:id "content"} "HI"]]))

(defroutes routes
    (GET "/" [] (index)))

(defn -main []
    (ring/run-jetty #'routes {:port 8000 :join? false}))

(with-open [wrtr (io/writer "../pages/test2.md")] (.write wrtr "SAMPLE"))
