(ns bfa-clojure.stores.tokens
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [goog.events :as events]
              [bfa-clojure.config :as config]
              [ajax.core :refer [POST GET]]
              [goog.history.EventType :as EventType])
    (:import goog.History))

;; -------------------------

(def get-token-url
  (str config/bfa-server "/api/get_token.json"))

(def token-state (atom ""))

(defn token-handler [response]
  (reset! token-state (:api_key response)))

(defn get-token []
  (when (= @token-state "")
    (do
      (GET get-token-url {
                          :keywords? true,
                          :response-format :json,
                          :handler token-handler}))))
