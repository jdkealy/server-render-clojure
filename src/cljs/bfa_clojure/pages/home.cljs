(ns bfa-clojure.pages.home
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [ajax.core :refer [POST GET]]
            [bfa-clojure.stores.tokens :as tokens]
            [bfa-clojure.components.topslider :as topslider]
            [goog.history.EventType :as EventType])
  )

(defn page []
  [:div
   [topslider/component]
   [:div [:a {:href "#/about"} "go to about page"]]])
