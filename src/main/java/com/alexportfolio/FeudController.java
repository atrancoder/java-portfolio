package com.alexportfolio;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/feud")
public class FeudController {

    private static List<FeudQuestion> QUESTIONS = List.of(
            new FeudQuestion(
                    "Name something people do before going to bed",
                    Map.of(
                            "brush teeth", 30,
                            "check phone", 25,
                            "shower", 20,
                            "set alarm", 15,
                            "read", 10
                    )
            ),
            new FeudQuestion(
                    "Name a popular fast food restaurant",
                    Map.of(
                            "mcdonalds", 35,
                            "burger king", 25,
                            "taco bell", 20,
                            "kfc", 10,
                            "subway", 10
                    )
            )
    );

    private int currentIndex = 0;
    private int score = 0;
    private Set<String> found = new HashSet<>();

    @GetMapping("/current")
    public Map<String, Object> getCurrentQuestion() {
        FeudQuestion q = QUESTIONS.get(currentIndex);
        return Map.of(
                "question", q.question,
                "score", score,
                "found", found
        );
    }

    @PostMapping("/guess")
    public Map<String, Object> submitGuess(@RequestParam String guess) {
        FeudQuestion q = QUESTIONS.get(currentIndex);
        String g = guess.toLowerCase().trim();

        if (q.answers.containsKey(g) && !found.contains(g)) {
            found.add(g);
            int pts = q.answers.get(g);
            score += pts;
            return Map.of("result", "correct", "points", pts, "score", score);
        }

        if (found.contains(g)) {
            return Map.of("result", "already", "score", score);
        }

        return Map.of("result", "wrong", "score", score);
    }

    @PostMapping("/next")
    public Map<String, Object> nextQuestion() {
        currentIndex = (currentIndex + 1) % QUESTIONS.size();
        found.clear();
        return Map.of("status", "next", "questionIndex", currentIndex);
    }
}