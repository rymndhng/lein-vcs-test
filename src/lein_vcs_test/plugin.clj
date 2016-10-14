(ns lein-vcs-test.plugin
  (:require [leiningen.core.main :as main]
            [bultitude.core :as b]
            [clojure.java.shell :as shell]
            [clojure.string :as string]))

(def ^:dynamic *commit-base* "master")

(def log-unchanged (delay (main/info "lein-vcs-test: No changed files in vcs.")))

(defn- read-changed-files! []
  ;; TODO: allow setting the diff base
  (let [exec (shell/sh "git" "diff" "--name-only" "--diff-filter=AMR"  *commit-base*)]
    (cond (not= 0 (:exit exec))
          (main/abort "Unable to find vcs changed files." (:exit exec) (:err exec))

          (= "" (:out exec))
          (deref log-unchanged)

          :else
          (string/split (:out exec) #"\n"))))

(defn modified-namespaces! []
  (for [filename (read-changed-files!)
        :let [ns-form (b/ns-form-for-file filename)]]
    (second ns-form)))

(defn test-namespace-of [ns]
  (symbol (str ns "-test")))

(defn selector-form [modified-namespaces]
  `(fn [test#]
     (contains? '~modified-namespaces (symbol (str (:ns test#))))))

(defn middleware
  "Should autoload by leiningen."
  [project & args]
  (binding [*commit-base* (or (get-in project [:vcs-test :commit-base] *commit-base*))]
    (doto (assoc-in project [:test-selectors :vcs]
                    (->> (modified-namespaces!)
                         (filter some?)
                         (mapcat (fn [x] [x (test-namespace-of x)]))
                         set
                         selector-form)))))
