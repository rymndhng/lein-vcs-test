(ns lein-vcs-test.plugin
  (:require [leiningen.core.main :as main]
            [bultitude.core :as b]
            [clojure.java.shell :as shell]
            [clojure.string :as string]))

(def ^:dynamic *commit-base* "origin/master")

(def log-unchanged (delay (main/info "lein-vcs-test: No changed files in vcs.")))

(defn- read-changed-files! []
  (let [exec (shell/sh "git" "diff" "--name-only" "--diff-filter=AMR"  (str *commit-base* "..head"))]
    (cond (not= 0 (:exit exec))
          (main/warn "Unable to find vcs changed files. Skipping lein-vcs-test. Cause:\n" (:exit exec) (:err exec))

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
  [project]
  (binding [*commit-base* (get-in project [:vcs-test :commit-base] *commit-base*)]
    (let [namespaces (filter some? (modified-namespaces!))
          patch-eastwood? (get-in project [:eastwood :lein-vcs-test/enabled])]

      (when (empty? namespaces)
        (main/warn "lein-vcs-test: No vcs-test namespaces found."))

      ;; patch projects!
      (-> project
          ;; lein eastwood
          (update-in [:eastwood :namespaces]
                     (fn [old-namespaces] (if patch-eastwood? namespaces old-namespaces)))

          ;; lein test
          (assoc-in [:test-selectors :vcs] (selector-form
                                             (set (mapcat (fn [x] [x (test-namespace-of x)])  namespaces))))))))
