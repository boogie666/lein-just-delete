(ns leiningen.just-delete
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))


(defn process-path [path project]
  (cond
    (string? path)  path
    (keyword? path) (project path)
    (vector? path) (str/join (java.io.File/separator) (map #(process-path % project) path))
    (list? path) (str/join "" (map #(process-path % project) path))))

(defn delete-recursively [fname]
  (doseq [f (reverse (file-seq (clojure.java.io/file fname)))]
    (clojure.java.io/delete-file f)))

(defn just-delete
  "just deletes"
  [project]
  (let [files (map #(process-path % project) (get project :just-delete))]
    (doseq [f files]
      (if (.exists (io/as-file f))
        (delete-recursively f)))))
