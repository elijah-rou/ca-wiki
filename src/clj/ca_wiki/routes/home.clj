(ns ca-wiki.routes.home
  (:use [ring.middleware.multipart-params]
        [ring.middleware.params]
        [hiccup.core])
  (:require [ca-wiki.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [noir.io :as nio])
  (:import java.util.Date [java.io File FileInputStream FileOutputStream]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn display-page [name]
    (let [n (str "docs/" name ".md")
          r (io/resource n)
          d (if (nil? r) "missing" (slurp r))]
    (if (nil? r)
        (layout/render
        "edit.html" )
        (layout/render
        "display.html" {:name name
                        :doc d}) )))

(defn save-page [req]
    (let [name (str (:title (:params req)) ".md")
         uri (io/resource "docs/")
         location (str uri name)
         content (:doc (:params req))
         pic (:pic (:params req))]
         ;(println location)
         ;(println (:body req))
         (println (:params req))
         (println req)
         ;(println pic)
         ;(println name)
         (spit location content)
         ;(spit (str (io/resource "images/") pic) )
         ;REDIRECT
        (response/see-other (str "/pages/" (:title (:params req))) )))

(defn image-view []
    (let [files  (file-seq (io/file (io/resource "images/")))
          f (filter #(.isFile %) files)
          final (map #(str "images/" %) (map #(.getName %) f))]
          (layout/render
          "image-view.html" {:images final})
    ))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn fetch-image [name]
    (let [files  (file-seq (io/file (io/resource "images/")))
          f (filter #(= (.getName %) name) (filter #(.isFile %) files))
          ;final (str "../resources/images/" (first (map #(.getName %) f)))
          ;final (first f)
          final (first (map #(.getPath %) f))
          ]
          ;(println files)
          ;(println (bean final))
          ;(println data)
          (response/file-response final)
          ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn edit-page [name]
    ;(println name)
    (let [uri (str (io/resource "docs/") name ".md")]
        ;(println uri)
         (layout/render
         "edit.html" {:name name
                      :doc (slurp uri)})
    )
)

(defn docs-page []
    (let [directory (io/file (io/resource "docs/"))
            ;OPTIMISE;;;;;;;;;;;
          timestamp (for [file (file-seq directory)] (.lastModified file))
          docs-pre (filter #(re-find #".md" (second %)) (sort (zipmap timestamp (for [file (file-seq directory)] (.getName file)))))
          docs-post (reverse (map #(clojure.string/replace % #".md" "") (map second docs-pre)))
          timestamp-form (map #(clojure.string/replace % #" (SAST+)" "") (for [ti (for [t (reverse (map first docs-pre))] (new Date t))] (.toString ti)))
          timedoc (zipmap timestamp-form docs-post)]
            ;OPTIMISE;;;;;;;;;;;
        ;(println docs-post)
        ;(println timestamp-form)
        ;(println timedoc)
        ;(println doc-content)
        (layout/render "docs.html" {:docs timedoc})
        ))

(defn delete-page [name]
    ;DO DELETE THINGS
    (layout/render "delete.html" {:name name})
    )

(defn delete-item [req]
    (let [files  (file-seq (io/file (io/resource "docs/")))
          name (str (:title (:params req)) ".md")
          rfile (filter #(= (.getName %) name) files)
          f (first rfile)]
    ;(println req)
    ;(println (slurp (.getPath file)))
    ;(println (.getName file))
    ;(println (map #(.getName %) rfile))
    ;(println (.isDirectory rfile))
    ;(println (.exists rfile))
    ;(println  rfile)
    (.setWritable f true)
    (.delete f)
    (response/see-other "/pages")
    ))

;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn upload-file [file]
    (let [file-name (file :filename)
          size (file :size)
          actual-file (file :tempfile)]
      (do
        (io/copy actual-file (File. (format "/home/eroussos/elijah.rou@gmail.com/CloudA-Syntheant/CloudAfrica/ca-wiki/resources/images/%s"
        file-name)))
        {:status 200
         :headers {"Content-Type" "text/html"}
         :body (html [:h1 file-name]
                     [:h1 size])})))



(defn save-file [req]
    (let [img (:img (:params req))
          _ (println "IMG " (:params req))])
    ; {{{tempfile :tempfile filename :filename} :img} :params :as params}
    ; (io/copy tempfile (io/file filename))
    ; (spit tempfile "images/")
    (response/see-other "/pages")
    )

(defn about-page []
  (layout/render "about.html"))

(comment
(defn epic []
    (let [files  (file-seq (io/file (io/resource "epiceditor/js")))
          f (filter #(= (.getName %) "epiceditor.min.js") (filter #(.isFile %) files))
          ;final (str "../resources/images/" (first (map #(.getName %) f)))
          ;final (first f)
          final (first (map #(.getPath %) f))
          ]
          ;(println files)
          ;(println (bean final))
          ;(println data)
          (response/file-response final)
          ))
)


(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/pages/:name/edit" [name] (edit-page name))
  (GET "/pages/:name" [name] (display-page name))
  (GET "/pages" [] (docs-page))
  (POST "/save" request (save-page request))
  (POST "/delete" request (delete-item request))
  (GET "/pages/:name/delete" [name] (delete-page name))
  (GET "/images" [] (image-view))
  (GET "/images/:name" [name] (fetch-image name))
  ;(POST "/filestorage" request (save-file request))
  (POST "/images/upload" {params :params}
       (let [file (get params :file)]
       (upload-file file)
       (docs-page)))
  ;(GET "scripts/epiceditor" [] (epic))
  )
