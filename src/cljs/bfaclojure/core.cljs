(ns bfaclojure.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [bfaclojure.stores.clickcount :as clickcount]
              [bfaclojure.stores.items :as itemsstore]
              [bfaclojure.stores.otheritems :as otheritemsstore]
              [bfaclojure.components.items :as items]
              [bfaclojure.components.otheritems :as otheritems]
              [goog.history.EventType :as EventType])
    (:import goog.History))

;; -------------------------
;; Views

(defn home []
  [:div
   [:div [:a {:href "#/about"} "go to about page"]]
   [items/component]
   ])

(defn about []
  [:div.about-wrap
   [:div [:a {:href "#/"} "go to home page"]]
   [otheritems/component]
   [:span.span-wrap @clickcount/click-count]
   [:input.input-wrap {:type "button" :value "Click me!"
            :on-click #(swap! clickcount/click-count inc)}]])


(def home-page
  (with-meta home  {}))

(def about-page
  (with-meta about {}))

(defn current-page []
  [:div.current-page-wrap [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(defn load-current-page [route]
  (case route
    "HOME" #'home-page
    "ABOUT" #'about-page
    #'about-page))



(defn get-current-page [sec-route]
  (if  (.-current_page js/window)
    (let [page (.-current_page js/window)]
      (session/put! :current-page (load-current-page page))
      )
    (session/put! :current-page (load-current-page sec-route))
    )
  )

(secretary/defroute "/" []
  ;#'home-page
  (get-current-page "HOME")
  )

(secretary/defroute "/about" []
  ;#'about-page
  (session/put! :current-page (load-current-page "ABOUT")))

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

(defn layout []
  [:div
   [current-page]])

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [layout]  (.getElementById js/document "app")))

(def string-to-atoms-map
  {"click_counter" clickcount/click-count
   "items" itemsstore/state
   "otheritems" otheritemsstore/state})

(defn override-atom-states []
  (let [overrides (js->clj (.-atom-state js/window))]
    (doall (map (fn [e]
                  (reset! (get string-to-atoms-map e) (clojure.walk/keywordize-keys (get overrides e)))
                  ) (vec (keys overrides))))))
(defn init! []
  (override-atom-states)
  (hook-browser-navigation!)
  (mount-root))

(defn ^:export render-me-to-s [route props]
  (get-current-page route)
  (reset! clickcount/click-count 100)
  ; Render component to markup without reactid
  (reagent.core/render-to-string [layout ]))
