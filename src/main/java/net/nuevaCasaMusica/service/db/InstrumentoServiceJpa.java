package net.nuevaCasaMusica.service.db;

import net.nuevaCasaMusica.model.Instrumento;
import net.nuevaCasaMusica.model.Respuesta;
import net.nuevaCasaMusica.repository.InstrumentosRepository;
import net.nuevaCasaMusica.service.IInstrumentosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class InstrumentoServiceJpa implements IInstrumentosService {

    @Autowired
    private InstrumentosRepository IR;

    Logger logger = LoggerFactory.getLogger(InstrumentoServiceJpa.class);

    public List<Instrumento> getAllInstrumentos() {

        Iterable<Instrumento> listaInstrumentos = IR.findAll();

        return (List<Instrumento>) listaInstrumentos;

    }

    public void guardarInstrumento(Instrumento instrumento) {

        logger.info("Se ingreso a controlador agregar instrumento");
        Respuesta res = new Respuesta();

        try {
            IR.save(instrumento);
            res.setEstado("OK");
            res.setMsg("El instrumento se agrego correctamente");
        } catch (Exception e) {
            logger.error("Error al agregar el instrumento");
            res.setEstado("ERROR");
            res.setMsg("El instrumento se agregar correctamente");
        }

    }

    @Override
    public void guardar(Instrumento ins) {

    }

    @Override
    public void eliminar(Integer idIns) {
        IR.deleteById(idIns);
    }

    @Override
    public List<Instrumento> buscarTodos() {
        return IR.findAll();
    }

    @Override
    public Optional<Instrumento> buscarPorId(Integer idIns) {
        return IR.findById(idIns);
    }

    @Override
    public Page<Instrumento> buscarTodos(Pageable page) {
        return IR.findAll(page);
    }
}
