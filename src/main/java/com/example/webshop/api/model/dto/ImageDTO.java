package com.example.webshop.api.model.dto;

public class ImageDTO {
        private Long id;
        private String url;
        private int position;


        public ImageDTO(Long id, String url, int position) {
            this.id = id;
            this.url = url;
            this.position = position;
        }


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
