(ns bfa-clojure.prod
  (:require [bfa-clojure.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
