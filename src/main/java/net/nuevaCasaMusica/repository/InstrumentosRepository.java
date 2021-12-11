package net.nuevaCasaMusica.repository;

import net.nuevaCasaMusica.model.Instrumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InstrumentosRepository extends JpaRepository<Instrumento, Integer> {

    List<Instrumento> findAllByOrderByPrecioAsc();
    List<Instrumento> findAllByOrderByPrecioDesc();

    @Query("SELECT marca FROM Instrumento GROUP BY marca")
    List<String> groupMarcas();

    List<Instrumento> findByMarca(String marca);

    List<Instrumento> findByPrecioBetween(double p1, double p2);


}
