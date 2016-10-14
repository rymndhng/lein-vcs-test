# lein-vcs-test

A Leiningen plugin to only test namespaces that have been modified. When working
with larger repositories, you'll want to get fast feedback on changed
namespaces. With VCS workflows, you typically have a commit-base (i.e. develop/master).

lein-vcs-test uses the underlying vcs system to determine which files have
changed since the base commit and tries to find test namespaces that correspond
to these namespaces.

## Installation

Put `[lein-vcs-test "0.1.0"]` into the `:plugins` vector of your project.clj.

## Usage

This plugin hooks into the existing lein test by adding a custom selector. To
run tests that have only changed since the commit-base, use:

lein test :vcs

## Configuration

By default, the commit-base is set to "master". You can change this by setting a custom commit-base.

``` clojure
(defproject myproject
  :plugins [[lein-vcs-test "0.1.2"]]
  :vcs-test {:commit-base "develop"})
```

## License

Copyright © 2016 rymndhng

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
