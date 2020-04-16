package lk.sliit.easylist.function;


import java.io.Serializable;

public class Resultset implements Comparable<Resultset>, Serializable {
        private String rs;
        private float support;

        public Resultset(String rs, float support) {
            this.rs = rs;
            this.support = support;
        }

        public String getRs() {
            return rs;
        }

        public float getSupport() {
            return support;
        }

        @Override
        public String toString() {
            return  rs +
                    "  " + support
                    ;
        }

        @Override
        public int compareTo(Resultset o) {
            if (support>o.support)
                return  -1;

            else if(support<o.support)
                return  +1;

            else
                return 0;
        }

    }


