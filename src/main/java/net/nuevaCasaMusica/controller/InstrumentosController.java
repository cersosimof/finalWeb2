package net.nuevaCasaMusica.controller;

import net.nuevaCasaMusica.model.Instrumento;
import net.nuevaCasaMusica.service.ICategoriasService;
import net.nuevaCasaMusica.service.IInstrumentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/instrumentos")
public class InstrumentosController {

    @Autowired
    IInstrumentosService iInstrumentosService;

    @Autowired
    private ICategoriasService serviceCategorias;

    @GetMapping("/index")
    public String mostrarIndex(Model model) {
        List<Instrumento> instrumentos = iInstrumentosService.buscarTodos();
        model.addAttribute("instrumentos", instrumentos);
        return "instrumento/listaInstrumentos";
    }

    @GetMapping("/indexPaginate")
    public String mostrarIndexPaginado(Model model, Pageable page) {
        Page<Instrumento> lista = iInstrumentosService.buscarTodos(page);
        model.addAttribute("instrumentos", lista);
        return "instrumento/listaInstrumentos";
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable("id") int idIns, RedirectAttributes attributes) {
        iInstrumentosService.eliminar(idIns);
        attributes.addFlashAttribute("msg", "El instrumento fue eliminado con exito.");
        return "redirect:/instrumentos/indexPaginate";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable("id") int idIns, Model model) {
        Optional<Instrumento> instrumento = iInstrumentosService.buscarPorId(idIns);
        model.addAttribute("categorias", serviceCategorias.buscarTodas());
        model.addAttribute("instrumento", instrumento);
        return "instrumento/altaNuevoInstrumento";
    }

    @GetMapping("/verProducto/{id}")
    public String verProducto(@PathVariable("id") int idIns, Model model) {
        Optional<Instrumento> instrumento = iInstrumentosService.buscarPorId(idIns);

        List<Instrumento> listaInstrumentos = iInstrumentosService.buscarTodos();

        for (Instrumento i : listaInstrumentos) {
            if(i.getId() == idIns){
                model.addAttribute("producto", i);
            }
        }

        return "instrumento/verInstrumento";
    }


}
