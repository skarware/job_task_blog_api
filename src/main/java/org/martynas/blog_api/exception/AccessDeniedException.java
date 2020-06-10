package org.martynas.blog_api.exception;

public class AccessDeniedException extends RuntimeException {

        public AccessDeniedException() {
            super("Unauthorized Access Denied. Posts can be modified only by owners.");
        }

    }
