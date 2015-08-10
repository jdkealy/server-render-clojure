(ns bfaclojure.components.items
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [bfaclojure.stores.items :as items]
            [goog.history.EventType :as EventType])
  )

(defn markup []
  [:div
   [:h2 [:span "YO, You got dis gig ?"]]
   (map (fn [i]
          [:div {:key (:name i)}
           (:name i)
           ]
          ) @items/state)])

(defn doalert []
  (items/fetch-items))

(def component
  (with-meta markup {:component-did-mount doalert}))
