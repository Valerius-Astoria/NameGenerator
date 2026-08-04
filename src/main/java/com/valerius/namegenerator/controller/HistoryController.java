package com.valerius.namegenerator.controller;

import com.valerius.namegenerator.model.GeneratedResult;
import com.valerius.namegenerator.model.User;
import com.valerius.namegenerator.repository.GeneratedResultRepository;
import com.valerius.namegenerator.security.CurrentUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

/**
 * MVC controller for the writer's generation archive.
 *
 * <ul>
 *   <li>{@code GET /history}: the logged-in user's past generations as cards, newest first.</li>
 *   <li>{@code GET /history/{id}}: full record (names, annotation, profile summary). Lookups are
 *       scoped to the current user, so other writers' records answer 404.</li>
 * </ul>
 */
@Controller
@RequestMapping("/history")
public class HistoryController {

    private final GeneratedResultRepository generatedResultRepository;
    private final CurrentUserService currentUserService;

    public HistoryController(
            GeneratedResultRepository generatedResultRepository,
            CurrentUserService currentUserService) {
        this.generatedResultRepository = generatedResultRepository;
        this.currentUserService = currentUserService;
    }

    /** Lists the current user's generation history as clickable cards. */
    @GetMapping
    public String history(Authentication authentication, Model model) {
        User user = currentUserService.require(authentication);
        model.addAttribute("results",
                generatedResultRepository.findAllByProfile_UserOrderByCreatedAtDesc(user));
        return "history";
    }

    /** Shows one archived generation in full; 404 unless it belongs to the current user. */
    @GetMapping("/{id}")
    public String historyDetail(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {
        User user = currentUserService.require(authentication);
        GeneratedResult result = generatedResultRepository.findByIdAndProfile_User(id, user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No such record in your archive."));
        model.addAttribute("result", result);
        return "history-detail";
    }
}
