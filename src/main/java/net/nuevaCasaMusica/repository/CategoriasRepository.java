package net.nuevaCasaMusica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import net.nuevaCasaMusica.model.Categoria;

public interface CategoriasRepository extends JpaRepository<Categoria, Integer> {
	
}
