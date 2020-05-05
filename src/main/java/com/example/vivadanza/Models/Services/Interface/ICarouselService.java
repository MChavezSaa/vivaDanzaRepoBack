package com.example.vivadanza.Models.Services.Interface;

import com.example.vivadanza.Models.Entity.Carousel;
import com.example.vivadanza.Models.Entity.Foto;

import java.util.List;

public interface ICarouselService {
    List<Carousel> findAll();
    Carousel save(Carousel carousel);
    void delete(Carousel carousel);
    Carousel findbyId(Long id);
}
