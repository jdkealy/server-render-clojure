(ns bfa-clojure.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [ajax.core :refer [POST GET]]
              [goog.history.EventType :as EventType])
    (:import goog.History))

;; -------------------------
;; Views

(def bfa-server
  "http://ec2-54-157-253-221.compute-1.amazonaws.com/"
  )

(defn home-page []
  [:div [:h2 "HELLo WORLD"]
   [:div [:a {:href "#/about"} "go to about page"]]])

(defn about-page []
  [:div [:h2 "About bfa-clojure"]
   [:div [:a {:href "#/"} "go to the home page"]]])

(def click-count (atom 0))

(defn my-account []
  [:div
   "The atom " [:code "click-count"] " has value: "
   @click-count ". "
   [:input {:type "button" :value "Click me!"
            :on-click #(swap! click-count inc)}]])

(def events-state (atom {}))

(defn handler [response]
  (.log js/console (str response)))

(defn get-data []
  (do
    (.log js/console (str "update! " (js/Date.)))
    (GET (str bfa-server "contents?content_area=home_top") {:response-format :json
                                                            :handler handler
             :keywords?       true})))

(defn events []
  (let [d (get-data)]
    [:div [:h2 "events"]
     (println events-state)
     ]
    ))

(defn photos []
  [:div [:h2 "photos"]
   [:div [:a {:href "#/"} "go to the home page"]]])

(defn photo []
  [:div [:h2 "photos"]
   [:div [:a {:href "#/"} "go to the home page"]]])

(defn downloads []
  [:div [:h2 "downloads"]
   [:div [:a {:href "#/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

(defn layout []
  [:div#app
   [:div#header
    [:a {:href "#/"} "BFA"]
    [:a {:href "#/events"} "EVENTS"]
    [:a {:href "#/my-account"} "MY ACCOUNT"]
    ]
   [:h2 "HI"]
   [current-page]
   ]
  )

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/my-account" []
  (session/put! :current-page #'my-account))

(secretary/defroute "/events" []
  (session/put! :current-page #'events))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

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
