(ns bfa-clojure.pages.about
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [goog.events :as events]
            [goog.history.EventType :as EventType]))

(defn page []
  [:div [:h2 "About bfa-clojure"]
   [:div [:a {:href "#/"} "go to the home page"]]])
