package com.example.demo4.repository;

import com.example.demo4.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioIdUsuarioOrderByFechaCreacionDesc(Long idUsuario);
    List<Notificacion> findByUsuarioIdUsuarioAndLeidaFalse(Long idUsuario);
    long countByUsuarioIdUsuarioAndLeidaFalse(Long idUsuario);
}