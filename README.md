# lein-vcs-test

A Leiningen plugin to filter files consumed in linting and testing based on
changes in version control.

Inspired by `arc diff` in Phabricator's arcanist command.

## Motivation

When working with larger repositories, you will have many namespaces. Most of
the time, your fixes do not touch all the namespaces. Linting and testing the
entire project can take a long time. Instead, we should have the ability to only
lint and test files that have changed (i.e. for a pull request builder).

lein-vcs-test uses the underlying vcs system to determine which files have been
touched compared to a commit-base (by default `master`). lein-vcs-test tries to
find test namespaces that correspond to these changed clojure files.

## Installation

Put `[lein-vcs-test "0.1.0"]` into the `:plugins` vector of your project.clj.

## Usage

This plugin hooks into the existing lein test by adding a custom selector. To
run tests that have only changed since the commit-base, use:

lein test :vcs

This also has support for the eastwood linter. To enable this feature
(regardless of selector), add the following entry to eastwood's configuration:


``` clojure
(defproject myproject
  :eastwood {:lein-vcs-test/enabled true}
)
```

## Configuration

By default, the commit-base is set to "master". You can change this by setting a custom commit-base.

``` clojure
(defproject myproject
  :plugins [[lein-vcs-test "0.1.1"]]
  :vcs-test {:commit-base "develop"})
```

## License

Copyright © 2016 rymndhng

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
