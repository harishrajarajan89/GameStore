package com.gamestore.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gamestore.dto.GameRequest;
import com.gamestore.dto.GameResponse;
import com.gamestore.exception.ResourceNotFoundException;

import com.gamestore.model.Game;
import com.gamestore.repository.GameRepository;

import jakarta.transaction.Transactional;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }
    public List<GameResponse>getAllGames(){
        List<Game>games=gameRepository.findAll();
        List<GameResponse> response =new ArrayList<>();
        for(Game game:games){
            response.add(toResponse(game));
        }
        return response;
    }
    private GameResponse toResponse(Game game) {
        GameResponse gr = new GameResponse();
        gr.setId(game.getId());
        gr.setTitle(game.getTitle());
        gr.setDescription(game.getDescription());
        gr.setPrice(game.getPrice());
        gr.setImageUrl(game.getImageUrl());
        gr.setGenre(game.getGenre());
        gr.setPlatform(game.getPlatform());
        gr.setStock(game.getStock());
        gr.setCreatedAt(game.getCreatedAt());
        return gr;
    }
    public GameResponse getGameById(Long id){
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        return toResponse(game);
    }
    public List<GameResponse>searchByTitle(String title){
        List<Game> games = gameRepository.findByTitleContainingIgnoreCase(title);
        List<GameResponse>responses=new ArrayList<>();
        for(Game game:games){
            responses.add(toResponse(game));
        }
        return responses;
    }
    public List<GameResponse> getByGenere (String genre){
        List<Game> games = gameRepository.findByGenreIgnoreCase(genre);
        List<GameResponse>responses=new ArrayList<>();
        for(Game game:games){
            responses.add(toResponse(game));
        }
        return responses;
    }
    @Transactional
    public GameResponse createGame(GameRequest request){
        Game game = Game.builder().title(request.getTitle()).description(request.getDescription())
                    .price(request.getPrice()).imageUrl(request.getImage()).genre(request.getGenre())
                    .platform(request.getPlatform()).stock(request.getStock() != null ? request.getStock() : 0)
                    .build();
        return toResponse(gameRepository.save(game));
    }
    @Transactional
    public GameResponse updateGame(Long id, GameRequest request){
        Game game = gameRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Game not found "+id));
        game.setTitle(request.getTitle());
        game.setDescription(request.getDescription());
        game.setPrice(request.getPrice());
        game.setImageUrl(request.getImage());
        game.setGenre(request.getGenre());
        game.setPlatform(request.getPlatform());
        game.setStock(request.getStock());
        game.setUpdatedAt(LocalDateTime.now());
        return toResponse(gameRepository.save(game));
    
    }
    @Transactional
    public void deleteGame(Long id){
        gameRepository.deleteById(id);
    }

    public Game getGameEntityById(Long id){
        return gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Game not found: " + id));
    }
}
