package com.valerius.namegenerator.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Renders the themed {@code error.html} page for missing routes and ownership-scoped 404s.
 */
@ControllerAdvice
public class HtmlErrorAdvice {

    @ExceptionHandler({ResponseStatusException.class, NoResourceFoundException.class})
    public ModelAndView handleNotFound(Exception ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "This dossier is not in the archive.";
        if (ex instanceof ResponseStatusException rse) {
            HttpStatus resolved = HttpStatus.resolve(rse.getStatusCode().value());
            if (resolved != null) {
                status = resolved;
            }
            if (rse.getReason() != null && !rse.getReason().isBlank()) {
                message = rse.getReason();
            }
        }
        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(status);
        mav.addObject("status", status.value());
        mav.addObject("error", message);
        return mav;
    }
}
