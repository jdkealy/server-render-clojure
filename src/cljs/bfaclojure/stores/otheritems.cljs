(ns bfaclojure.stores.otheritems
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [ajax.core :refer [POST GET]]
            [goog.events :as events]
            [bfaclojure.config :as config]
            [goog.history.EventType :as EventType]))

(def state (atom []))

(defn handler [response]
  (reset! state response))

(defn fetch-items []
  (do
    (if (= 0 (count @state))
      (GET (str config/api-server "/otheritems") {
                                             :keywords? true,
                                             :response-format :json,
                                             :handler handler}))))
