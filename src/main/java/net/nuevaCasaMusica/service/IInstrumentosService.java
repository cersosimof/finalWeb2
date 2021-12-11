package net.nuevaCasaMusica.service;

import net.nuevaCasaMusica.model.Instrumento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IInstrumentosService {
    void guardar(Instrumento ins);
    void eliminar(Integer idIns);
    List<Instrumento> buscarTodos();
    Optional<Instrumento> buscarPorId(Integer idIns);
    Page<Instrumento> buscarTodos(Pageable page);
}
