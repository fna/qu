(ns ^:integration integration.test.loader
  "The 'integration_test' dataset is designed to simplify testing.
  Its data values are simple and map to the row number.

  The tests below are to ensure data is loaded correctly."
  (:require [clojure.test :refer :all]
            [qu.test-util :refer :all]
            [qu.data :as data]
            [qu.loader :as loader]))

(def db "integration_test")
(def coll "incomes")

(use-fixtures :once (mongo-setup-fn db))

(deftest ^:integration test-dates
  (testing "stored as dates in MongoDB"
    (let [query {:query {} :fields {} :limit 100 :skip 0 :sort {}}
          result (data/get-find db coll query)]
      (doseq [datum (:data result)]
        (does= (class (:date_observed datum))
               org.joda.time.DateTime)))))

(deftest ^:integration test-median-pre-aggregations
  (testing "they work!"
    (let [query {:query {} :fields {} :limit 100 :skip 0 :sort {}}
          result (data/get-find db "incomes_by_state" query)
          medians (->> result
                       :data
                       (map :median_tax_return)
                       sort)]
      (does= medians [3.0 7.0 13.0 16.5]))))

;; (run-tests)
