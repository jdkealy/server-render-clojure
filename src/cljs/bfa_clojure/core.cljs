(ns bfa-clojure.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType])
    (:import goog.History))

(defn layout []
  [:div#app
   [:div#main_container.transparent-header
    [:header#header.fixed
     [:nav {:className "page-container"}
      [:a {:className "logo active" :href "#/"} ]
      [:span#hamburger.glyphicon.glyphicon-menu-hamburger]
      [:ul
       [:li
        [:a {:href "/#about"}
         "about"]]
       [:li
        [:a {:href "/#book"}
         "book now"]]]]]
    [:article.home.page.transparent-header {:style {:margin-top "100px"}}
     "HELLO!"
     ]]])

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [layout] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))

(defn ^:export render-me-to-s []
  ; Render component to markup without reactid
  (reagent.core/render-to-static-markup [layout])
  ; Or render component to ready to-go react markup
  (reagent.core/render-to-string [layout]))
