(ns leiningen.vcs-test
  (:require [leiningen.core.main :as main]
            [bultitude.core :as b]
            [clojure.java.shell :as shell]
            [clojure.string :as string]))

(defn- read-changed-files! []
  (let [exec (shell/sh "git" "ls-files" "--modified")]
    (if-not (= 0 (:exit exec))
      (main/abort "Unable to find vcs changed files." (:err exec))
      (string/split (:out exec) #"\n"))))

(defn modified-namespaces! []
  (for [filename (read-changed-files!)
        :let [ns-form (b/ns-form-for-file filename)]]
    (second ns-form)))
