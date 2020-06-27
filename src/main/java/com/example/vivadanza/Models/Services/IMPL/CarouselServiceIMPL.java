package com.example.vivadanza.Models.Services.IMPL;

import com.example.vivadanza.Models.DAO.ICarouselDao;
import com.example.vivadanza.Models.Entity.Carousel;
import com.example.vivadanza.Models.Services.Interface.ICarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CarouselServiceIMPL  implements ICarouselService {
   @Autowired
    private ICarouselDao carouselDao;

    @Transactional
    public List<Carousel> findAll() {
        return carouselDao.findAll();
    }

    @Transactional
    public Carousel save(Carousel carousel) {
        return carouselDao.save(carousel);
    }

    @Transactional
    public void delete(Carousel carousel) {
        carouselDao.delete(carousel);
    }

    @Transactional
    public Carousel findbyId(Long id) {
        return carouselDao.findById(id).get();
    }
}
