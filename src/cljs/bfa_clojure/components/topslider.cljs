(ns bfa-clojure.components.topslider
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [ajax.core :refer [POST GET]]
            [bfa-clojure.stores.tokens :as tokens]
            [bfa-clojure.stores.contents :as content]
            [goog.history.EventType :as EventType]))


(def carousel-position (atom 0))
(def looping (atom false))

(defn prev-carousel-item []
  (let [items @content/state
        num-items (count items)
        current-position @carousel-position
        prev-position (if (= @carousel-position 0)
                        (- num-items 1)
                        (- current-position 1))]
    (reset! carousel-position prev-position)
))


(defn next-carousel-item []
  (let [items @content/state
        num-items (count items)
        current-position @carousel-position
        next-position (if (= @carousel-position  (- num-items 1))
                        0
                        (+ current-position 1))]
    (reset! carousel-position next-position)))

(def next-carousel-item-throttled
  (.throttle (.-_ js/window) next-carousel-item 2000))

(def prev-carousel-item-throttled
  (.throttle (.-_ js/window) prev-carousel-item 2000))

(defn next-carousel [e]
  (.preventDefault e)
  (next-carousel-item-throttled))

(defn prev-carousel [e]
  (.preventDefault e)
  (prev-carousel-item-throttled))

(defn with-positions [content]
  (let [pos @carousel-position
        num-items (count content)
        prev-num (if (= pos 0)
                   (- num-items 1)
                   (- pos 1))
        prev (get content prev-num)
        current (get content @carousel-position)
        next-num (if (>= pos (- num-items 1))
                   0
                   (+ 1 pos))
        next (get content next-num)]
    [prev current next]))

(defn carousel []
  [:div.content-slider.hero-slider
   (let [content (content/get-data)
         browser-width (.-innerWidth js/window)
         with-positions (with-positions @content/state)]
     [:div.slider-container
      [:style
       (str ".prev-slide {left:" (* browser-width -1) ";}"
            ".current-slide {left:0px;}"
            ".next-slide {left:" browser-width ";}")]
      [:div.slide-controls {:style {:width browser-width}}
       [:a.prev {:href ""
                 :on-click prev-carousel
                 } "PREV"]
       [:a.next {:href ""
                 :on-click next-carousel
                 } "NEXT"]]
      (map-indexed (fn [i e]
                     (let [classname (case i
                                       0 "prev-slide"
                                       1 "current-slide"
                                       2 "next-slide")]
                       [:div.slider-item
                        {:key (str (:id e) "_item")
                         :className classname
                         :style
                         {:position "absolute"
                          :top "0px"
                          :height "500px"
                          :width browser-width}}
                        [:div
                         [:div.info
                          [:h1
                           (:top_text e)
                           ]
                          [:p
                           (:sub_text e)]]
                         [:div {:style {:background-repeat "no-repeat"
                                        :background-position "center"
                                        :background-size "cover"
                                        :width (str browser-width "px")
                                        :height "500px"
                                        :background-image (str "url('" (:image e) "')") }}]]]
                       )

                     ) with-positions)
      [:ul.slick-dots
       (map-indexed (fn [i item]
              [:li.slick-active
               [:button {::on-click (fn []
                                      (reset! carousel-position i)
                                      )}]]) @content/state)]])]

  )

(declare throttled-loop)

(defn carousel-loop []
  (when @looping
    (.setTimeout js/window (fn []
                             (next-carousel-item-throttled)
                             (throttled-loop)
                             ) 10000)))

(def throttled-loop
  (.debounce (.-_ js/window) carousel-loop 10000))

(defn start []
  (reset! looping true)
  (throttled-loop))

(defn stop []
  (reset! looping false))

(def component
  (with-meta carousel
    {:component-will-mount start
     :component-will-unmount stop}))
