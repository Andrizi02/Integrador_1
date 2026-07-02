package com.example.demo4.service;


import com.example.demo4.model.Resena;
import com.example.demo4.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    public List<Resena> porProducto(Long idProducto) {
        return resenaRepository.findByProductoIdProducto(idProducto);
    }

    public Resena guardar(Resena resena) {
        return resenaRepository.save(resena);
    }
}
