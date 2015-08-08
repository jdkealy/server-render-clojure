(ns bfa-clojure.config
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [goog.events :as events]
              [ajax.core :refer [POST GET]]
              [goog.history.EventType :as EventType])
    (:import goog.History))

;; -------------------------
;; Views

(def bfa-server
  "http://ec2-54-157-253-221.compute-1.amazonaws.com")
