(ns bfa-clojure.stores.events
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [goog.events :as events]
              [bfa-clojure.config :as config]
              [ajax.core :refer [POST GET]]
              [goog.history.EventType :as EventType])
    (:import goog.History))

(def events-state (atom []))

(defn handler [response]
  (reset! events-state response))

(defn get-data []
  (do
    (if (= 0 (count @events-state))
      (GET (str config/bfa-server "/contents?content_area.json=top") {
                                                              :keywords? true,
                                                              :response-format :json,
                                                              :handler handler}))))
