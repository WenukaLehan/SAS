package com.wlghost.sas.Domain;

public class TcSubject {
        private String id;
        private String name;

        public TcSubject() {
            // Default constructor required for calls to DataSnapshot.getValue(Item.class)
        }

        public TcSubject(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }


}
