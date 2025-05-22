package com.tiborbodi.uniquefilenames.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * Controller for serving the Javadoc HTML documentation.
 * Provides an endpoint to redirect to the Javadoc index page.
 */
@Controller
@RequestMapping("/api/v1")
public class JavadocController {

    /**
     * Redirects to the Javadoc index.html page.
     * @return a redirect string to the Javadoc index
     * @throws IOException if an I/O error occurs
     */
    @GetMapping("/doc")
    public String serveJavadocIndex() throws IOException {
        return "redirect:/api/v1/doc/index.html";
    }

}

