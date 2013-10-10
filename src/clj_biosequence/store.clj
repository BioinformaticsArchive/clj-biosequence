(in-ns 'clj-biosequence.core)

(defrecord storeReader [conn stm]

  biosequenceReader

  (biosequence-seq [this]
    (map #(ps/declob (:src %))
         (ps/store-result-map (:stm this) "select * from object")))

  java.io.Closeable

  (close [this]
    (.close (:stm this))
    (.close (:conn this))))

(defrecord biosequenceStore [path]

  biosequenceIO

  (bs-reader [this]
    (let [c (ps/store-connection (:db this))]
      (->storeReader c
                     (ps/store-statement c)))))
(defn new-store
  [path]
  (ps/init-store (->biosequenceStore
                  (ps/index-file-name path))))
(defn load-store
  "Loads a fastaStore."
  [dir]
  (let [file (first (fs/glob (str dir "/" "*.h2.db")))
        db-file (second (re-find  #"(.+)\.h2.db" (fs/absolute-path file)))]
    (if (not (nil? db-file))
      (assoc (->biosequenceStore dir)
        :db
        (ps/make-db-connection db-file false))
      (throw (Throwable. "DB file not found!")))))

(defn get-biosequence [a s]
  "Returns a biosequence object from a store implementing the biosequence"
  (ps/get-object a s))

(defn update-biosequences
  "Updates an object in the store with a current connection."
  ([l s] (update-biosequences l s true))
  ([l s t]
     (ps/update-records (pmap #(hash-map :id (accession %)
                                         :src (pr-str %))
                              l) s t)))

(defn save-biosequences
  "Saves an object to a store."
  ([l s] (save-biosequences l s true))
  ([l s t]
   (if (seq? l)
     (ps/save-records (pmap #(hash-map :id (accession %)
                                       :src (pr-str %)) l)
                      s t)
     (throw (Throwable. (str "Argument " l " is not a sequence."))))))

(defn index-biosequence-file
  [file]
  (let [s (new-store file)]
    (try
      (do
        (with-open [r (bs-reader file)]
          (save-biosequences (biosequence-seq r) s false))
        s)
      (catch Exception e
        (println e)
        (fs/delete-dir (fs/parent (:path s)))))))