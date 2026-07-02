package com.example.demo4.service;

import com.example.demo4.model.Notificacion;
import com.example.demo4.model.Usuario;
import com.example.demo4.repository.NotificacionRepository;
import com.example.demo4.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Notificacion> obtenerNotificaciones(Long idUsuario) {
        return notificacionRepository.findByUsuarioIdUsuarioOrderByFechaCreacionDesc(idUsuario);
    }

    public long contarNoLeidas(Long idUsuario) {
        return notificacionRepository.countByUsuarioIdUsuarioAndLeidaFalse(idUsuario);
    }

    public void marcarLeida(Long idNotificacion) {
        Notificacion notificacion = notificacionRepository.findById(idNotificacion)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        notificacion.setLeida(true);
        notificacionRepository.save(notificacion);
    }

    public void marcarTodasLeidas(Long idUsuario) {
        List<Notificacion> noLeidas = notificacionRepository
                .findByUsuarioIdUsuarioAndLeidaFalse(idUsuario);
        noLeidas.forEach(n -> n.setLeida(true));
        notificacionRepository.saveAll(noLeidas);
    }

    public Notificacion crearNotificacion(Long idUsuario, String mensaje, String tipo) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setMensaje(mensaje);
        notificacion.setTipo(tipo);
        return notificacionRepository.save(notificacion);
    }
}
