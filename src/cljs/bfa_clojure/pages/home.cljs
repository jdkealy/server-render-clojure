(ns bfa-clojure.pages.home
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [ajax.core :refer [POST GET]]
            [bfa-clojure.stores.tokens :as tokens]
            [goog.history.EventType :as EventType])
  )

(defn page []
  [:div [:h2 ""]
   [:div [:a {:href "#/about"} "go to about page"]]])
