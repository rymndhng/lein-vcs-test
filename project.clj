(defproject lein-vcs-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :eval-in-leiningen true
  :middleware [leiningen.vcs-test.plugin/middleware]
  :vcs-test {:commit-base "master"}
  :plugins [[lein-vcs-test "0.1.0-SNAPSHOT"]])
