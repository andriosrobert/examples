(defproject websocket-example "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :dependencies [[bidi "2.1.2"]
                 [com.cemerick/piggieback "0.2.2"]
                 [com.taoensso/timbre "4.10.0"]
                 [macchiato/hiccups "0.4.1"]
                 [macchiato/core "0.2.11"]
                 [macchiato/env "0.0.6"]
                 [mount "0.1.12"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 ;; needed for JDK 9 compatibility
                 [javax.xml.bind/jaxb-api "2.3.0"]]
  :min-lein-version "2.0.0"
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
  :plugins [[lein-doo "0.1.7"]
            [macchiato/lein-npm "0.6.4"]
            [lein-figwheel "0.5.14"]
            [lein-cljsbuild "1.1.5"]]
  :npm {:dependencies [[source-map-support "0.4.6"]]
        :write-package-json true}
  :source-paths ["src" "target/classes"]
  :clean-targets ["target"]
  :target-path "target"
  :profiles
  {:dev
   {:npm {:package {:main "target/out/websocket-example.js"
                    :scripts {:start "node target/out/websocket-example.js"}}}
    :dependencies [[figwheel-sidecar "0.5.14"]]
    :cljsbuild
    {:builds {:dev
              {:source-paths ["env/dev" "src"]
               :figwheel     true
               :compiler     {:main          websocket-example.app
                              :output-to     "target/out/websocket-example.js"
                              :output-dir    "target/out"
                              :target        :nodejs
                              :optimizations :none
                              :pretty-print  true
                              :source-map    true
                              :source-map-timestamp false}}}}
    :figwheel
    {:http-server-root "public"
     :nrepl-port 7000
     :reload-clj-files {:clj false :cljc true}
     :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
    :source-paths ["env/dev"]
    :repl-options {:init-ns user}}
   :test
   {:cljsbuild
    {:builds
     {:test
      {:source-paths ["env/test" "src" "test"]
       :compiler     {:main websocket-example.app
                      :output-to     "target/test/websocket-example.js"
                      :target        :nodejs
                      :optimizations :none
                      :pretty-print  true
                      :source-map    true}}}}
    :doo {:build "test"}}
   :release
   {:npm {:package {:main "target/release/websocket-example.js"
                    :scripts {:start "node target/release/websocket-example.js"}}}
    :cljsbuild
    {:builds
     {:release
      {:source-paths ["env/prod" "src"]
       :compiler     {:main          websocket-example.app
                      :output-to     "target/release/websocket-example.js"
                      :language-in   :ecmascript5
                      :target        :nodejs
                      :optimizations :simple
                      :pretty-print  false}}}}}}
  :aliases
  {"build" ["do"
            ["clean"]
            ["npm" "install"]
            ["figwheel" "dev"]]
   "package" ["do"
              ["clean"]
              ["npm" "install"]
              ["with-profile" "release" "npm" "init" "-y"]
              ["with-profile" "release" "cljsbuild" "once"]]
   "test" ["do"
           ["npm" "install"]
           ["with-profile" "test" "doo" "node"]]})
