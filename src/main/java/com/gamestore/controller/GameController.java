package com.gamestore.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gamestore.dto.GameRequest;
import com.gamestore.dto.GameResponse;
import com.gamestore.service.GameService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;
    public GameController(GameService gameService){
        this.gameService =gameService;
    }

    @GetMapping
    public ResponseEntity<List<GameResponse>> getAllGames(@RequestParam(required =false) String search,@RequestParam(required = false) String genre){
        if (search!=null && !search.isBlank()){
            return ResponseEntity.ok(gameService.searchByTitle(search));
        }
        if (genre!=null && !genre.isBlank()){
            return ResponseEntity.ok(gameService.getByGenere(genre));
        }
        return ResponseEntity.ok(gameService.getAllGames());
    }
    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> getGame(@PathVariable Long id){
        return ResponseEntity.ok(gameService.getGameById(id));
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GameResponse>createGame(@Valid @RequestBody GameRequest request){
        return ResponseEntity.ok(gameService.createGame(request));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GameResponse>updateGame(@PathVariable Long id, @Valid @RequestBody GameRequest request){
        return ResponseEntity.ok(gameService.updateGame(id, request));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

}
