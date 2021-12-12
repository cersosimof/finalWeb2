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

//    @Query("SELECT '*' FROM Instrumento WHERE marca = 'Parquer' AND precio BETWEEN 10 AND 200")
    List<Instrumento> findByMarcaAndPrecioBetween(String marca, double p1, double p2);

    List<Instrumento> findByMarcaAndPrecioBetweenOrderByPrecioAsc(String marca, double p1, double p2);

    List<Instrumento> findByMarcaAndPrecioBetweenOrderByPrecioDesc(String marca, double p1, double p2);

    List<Instrumento> findByMarca(String marca);

    List<Instrumento> findByPrecioBetweenOrderByPrecioAsc(Double p1, Double p2);
    List<Instrumento> findByPrecioBetweenOrderByPrecioDesc(Double p1, Double p2);


    List<Instrumento> findByPrecioBetween(Double s, Double s1);


    @Query(" SELECT MAX(precio) FROM Instrumento")
    double getMaxPrecio();

    @Query(" SELECT Min(precio) FROM Instrumento")
    double getMinPrecio();
}
