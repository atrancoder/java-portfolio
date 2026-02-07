// ================================
// Java Internship-Friendly Portfolio Website
// Tech: Spring Boot (Java) + HTML + simple REST game logic
// ================================
// This is a simplified single-file example to show structure.
// In a real project, you'd split this into multiple files.

// ----------------
// 1. Spring Boot App
// ----------------

package com.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.*;

@SpringBootApplication
public class PortfolioApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortfolioApplication.class, args);
    }
}

// ----------------
// 2. Family Feud Game Config (EDIT THIS)
// ----------------

class FeudQuestion {
    public String question;
    public Map<String, Integer> answers; // answer -> points

    public FeudQuestion(String question, Map<String, Integer> answers) {
        this.question = question;
        this.answers = answers;
    }
}

@RestController
@RequestMapping("/api/feud")
class FeudController {

    // ===== CONFIG: Edit questions + points here =====
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
            "answers", q.answers.keySet(),
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

// ----------------
// 3. Page Controller (Tabs)
// ----------------

@Controller
class PageController {

    @GetMapping("/")
    public String home(Model model) {
        return "index"; // About Me
    }

    @GetMapping("/resume")
    public String resume() {
        return "resume";
    }

    @GetMapping("/projects")
    public String projects() {
        return "projects";
    }
}

// ----------------
// 4. HTML Templates (Thymeleaf)
// Place these in src/main/resources/templates/
// ----------------

/* ================================
   index.html  (About Me Tab)
==================================
<!DOCTYPE html>
<html>
<head>
  <title>About Me</title>
</head>
<body>
  <nav>
    <a href="/">About Me</a> |
    <a href="/resume">Resume</a> |
    <a href="/projects">Projects</a>
  </nav>

  <h1>Alex Tran</h1>
  <p>Software Developer seeking internships.</p>
  <p>Short professional bio here.</p>
</body>
</html>
*/

/* ================================
   resume.html (Resume Tab)
==================================
<!DOCTYPE html>
<html>
<body>
  <nav>
    <a href="/">About Me</a> |
    <a href="/resume">Resume</a> |
    <a href="/projects">Projects</a>
  </nav>

  <h2>Resume</h2>
  <a href="/resume.pdf">Download Resume</a>
</body>
</html>
*/

/* ================================
   projects.html (Projects + Family Feud Game)
==================================
<!DOCTYPE html>
<html>
<body>
  <nav>
    <a href="/">About Me</a> |
    <a href="/resume">Resume</a> |
    <a href="/projects">Projects</a>
  </nav>

  <h2>Projects</h2>

  <h3>Family Feud Java Game</h3>
  <p>
    <!-- SPACE FOR DESCRIPTION -->
    This is a Java-backed Family Feud style game demonstrating REST APIs,
    game logic, and frontend-backend integration.
  </p>

  <button onclick="loadQuestion()">Start / Load Question</button>

  <h4 id="question"></h4>
  <p>Score: <span id="score">0</span></p>

  <input id="guess" placeholder="Enter your guess" />
  <button onclick="submitGuess()">Guess</button>

  <div id="result"></div>

  <button onclick="nextQuestion()">Next Question</button>

  <script>
    async function loadQuestion() {
      const res = await fetch('/api/feud/current');
      const data = await res.json();
      document.getElementById('question').innerText = data.question;
      document.getElementById('score').innerText = data.score;
    }

    async function submitGuess() {
      const g = document.getElementById('guess').value;
      const res = await fetch('/api/feud/guess?guess=' + g, { method: 'POST' });
      const data = await res.json();
      document.getElementById('result').innerText = JSON.stringify(data);
      document.getElementById('score').innerText = data.score;
    }

    async function nextQuestion() {
      await fetch('/api/feud/next', { method: 'POST' });
      loadQuestion();
    }
  </script>
</body>
</html>
*/

// ----------------
// Why this is great for internships:
// - Shows Java + Spring Boot
// - REST API design
// - Frontend/Backend integration
// - Configurable game logic
// - Multi-page website with tabs
// ----------------
