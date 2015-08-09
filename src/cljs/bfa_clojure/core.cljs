(ns bfa-clojure.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [ajax.core :refer [POST GET]]
              [bfa-clojure.config :as config]
              [bfa-clojure.stores.tokens :as tokens]
              [bfa-clojure.pages.home :as home]
              [bfa-clojure.pages.eventsp :as eventsp]
              [bfa-clojure.pages.about :as about]
              [goog.history.EventType :as EventType])
    (:import goog.History))

(defn current-page []
  [:div [(session/get :current-page)]])

(defn layout []
  (let [d (tokens/get-token)]
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
       [current-page]]]]))

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home/page))

(secretary/defroute "/events" []
  (session/put! :current-page #'eventsp/page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about/page))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

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
