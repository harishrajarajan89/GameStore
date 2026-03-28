package com.gamestore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamestore.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game,Long>{
    List<Game>findByTitleContainingIgnoreCase(String title);
    List<Game>findByGenreIgnoreCase(String genre);
    List<Game>findByPlatformIgnoreCase(String platform);    
}
