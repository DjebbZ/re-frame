(ns re-frame.subs
 (:require
  [re-frame.db :refer [app-db]]
  [re-frame.utils :refer [first-in-vector warn]]))


;; maps from handler-id to handler-fn
(def ^:private key->fn (atom {}))


(defn register
  "register a hander function for an id"
  [key-v handler-fn]
  (if (contains? @key->fn key-v)
   (warn "re-frame: overwriting subscription-handler for: " key-v))   ;; allow it, but warn.
  (swap! key->fn assoc key-v handler-fn))


(defn subscribe
  "returns a reagent/reaction which observes a part of the "
  [v]
  (let [key-v       (first-in-vector v)
       handler-fn  (get @key->fn key-v)]
    (if (nil? handler-fn)
      (warn "re-frame: no subscription handler registered for: \"" key-v "\".  Returning nil.")
      (handler-fn app-db v))))
