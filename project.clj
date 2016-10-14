(defproject lein-vcs-test "0.1.0-SNAPSHOT"
  :description "Leiningen optimizations for testing with Version Controlled Systems"
  :url "https://github.com/rymndhng/lein-vcs-test"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :eval-in-leiningen true
  :vcs-test {:commit-base "master"}
  :plugins [[lein-vcs-test "0.1.0-SNAPSHOT"]
            [jonase/eastwood "0.2.3"]])
