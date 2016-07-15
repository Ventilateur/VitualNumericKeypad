package dictionaryManager;

public class Words {
        private String word;
        private int rank;

        public Words(String word, int rank) {
            this.word = word;
            this.rank = rank;
        }

        public String getWord() { return word; }

        public int getRank() { return rank; }
}
