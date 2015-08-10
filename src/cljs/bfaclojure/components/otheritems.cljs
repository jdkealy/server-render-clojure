(ns bfaclojure.components.otheritems
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [bfaclojure.stores.otheritems :as otheritems]
            [goog.history.EventType :as EventType])
  )

(defn markup []
  [:div
   [:h2 [:span "talking about dis dig. you got dis dig ? "]]
   (map (fn [i]
          [:div {:key (:name i)}
           (:name i)
           ]
          ) @otheritems/state)])

(defn do-get-items []
  (otheritems/fetch-items))

(def component
  (with-meta markup {:component-did-mount do-get-items}))
