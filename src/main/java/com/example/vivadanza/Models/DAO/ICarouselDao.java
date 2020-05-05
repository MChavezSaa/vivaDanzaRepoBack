package com.example.vivadanza.Models.DAO;

import com.example.vivadanza.Models.Entity.Carousel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICarouselDao extends JpaRepository<Carousel, Long> {
}
