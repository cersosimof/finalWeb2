package net.nuevaCasaMusica.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import net.nuevaCasaMusica.model.Instrumento;
import net.nuevaCasaMusica.service.IInstrumentosService;
import net.nuevaCasaMusica.service.db.InstrumentoServiceJpa;
import net.nuevaCasaMusica.util.Utileria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import net.nuevaCasaMusica.model.Perfil;
import net.nuevaCasaMusica.model.Usuario;
import net.nuevaCasaMusica.service.ICategoriasService;
import net.nuevaCasaMusica.service.IUsuariosService;

@Controller
public class HomeController {

    @Autowired
    private ICategoriasService serviceCategorias;

    @Autowired
    private IInstrumentosService iInstrumentosService;
//
//    @Autowired
//    private IVacantesService serviceVacantes;

    @Autowired
    private IUsuariosService serviceUsuarios;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InstrumentoServiceJpa instrumentosService;

    @GetMapping("/")
    public String mostrarHome() {
        return "home";
    }

    @GetMapping("/alta")
    public String AltaNuevoInstrumento(@ModelAttribute("instrumento") Instrumento instrumento, Model model) {
        model.addAttribute("categorias", serviceCategorias.buscarTodas());
        return "instrumento/altaNuevoInstrumento";
    }

    @PostMapping("/alta")
    public String DarAlta(Instrumento instrumento, BindingResult result, Model model, @RequestParam("subidaImagen") MultipartFile multipartFile) {

        try {

            if (result.hasErrors()) {
                return "instrumento/altaNuevoInstrumento";
            }

            if (!multipartFile.isEmpty()) {
                String ruta = "/tmp/instrumentos/";

                String nombreImagen = Utileria.guardarArchivo(multipartFile, ruta);
                if (nombreImagen != null) {
                    instrumento.setImagen(nombreImagen);
                }
            } else {
                if (instrumento.getImagen() == null) {
                    instrumento.setImagen("noImage.png");
                }
            }

            instrumentosService.guardarInstrumento(instrumento);

            return "redirect:/";

        } catch (Exception e) {
            return "error";
        }
    }


    @GetMapping("/index")
    public String mostrarIndex(Authentication authentication, HttpSession session) {

        // Como el usuario ya ingreso, ya podemos agregar a la session el objeto usuario.
        String username = authentication.getName();

        for (GrantedAuthority rol : authentication.getAuthorities()) {
            System.out.println("ROL: " + rol.getAuthority());
        }

        if (session.getAttribute("usuario") == null) {
            Usuario usuario = serviceUsuarios.buscarPorUsername(username);
            session.setAttribute("usuario", usuario);
        }

        return "redirect:/";
    }

    /**
     * Método que muestra el formulario para que se registren nuevos usuarios.
     *
     * @param usuario
     * @return
     */
    @GetMapping("/signup")
    public String registrarse(Usuario usuario) {
        return "formRegistro";
    }

    // SE USA
    // METODO PARA DAR DE ALTA USUARIO
    @PostMapping("/signup")
    public String guardarRegistro(Usuario usuario, RedirectAttributes attributes) {
        String pwdPlano = usuario.getPassword();
        String pwdEncriptado = passwordEncoder.encode(pwdPlano);
        usuario.setPassword(pwdEncriptado);
        usuario.setEstatus(1);
        usuario.setFechaRegistro(new Date());
        Perfil perfil = new Perfil();
        // TODO: Ver de hacer que el usuario se pueda cambiar
        perfil.setId(3); // Perfil USUARIO
        usuario.setPerfiles(perfil);
        serviceUsuarios.guardar(usuario);

        attributes.addFlashAttribute("msg", "Bienvenido " + usuario.getNombre() + ", ingresar para ver todas las opciones.");

        return "redirect:/login";
    }

//    @GetMapping("/search")
//    public String buscar(@ModelAttribute("search") Vacante vacante, Model model) {
//
//        vacante.setEstatus("Aprobada");
//        // Personalizamos el tipo de busqueda...
//        ExampleMatcher matcher = ExampleMatcher.matching().
//                // and descripcion like '%?%'
//                        withMatcher("descripcion", ExampleMatcher.GenericPropertyMatchers.contains());
//
//        Example<Vacante> example = Example.of(vacante, matcher);
//        List<Vacante> lista = serviceVacantes.buscarByExample(example);
//        model.addAttribute("vacantes", lista);
//        return "home";
//    }

    /**
     * Metodo que muestra la vista de la pagina de Acerca
     *
     * @return
     */
    @GetMapping("/about")
    public String mostrarAcerca() {
        return "acerca";
    }

    /**
     * Método que muestra el formulario de login personalizado.
     *
     * @return
     */
    @GetMapping("/login")
    public String mostrarLogin() {
        return "formLogin";
    }

    /**
     * Método personalizado para cerrar la sesión del usuario
     *
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, null, null);
        return "redirect:/";
    }

    /**
     * Utileria para encriptar texto con el algorito BCrypt
     *
     * @param texto
     * @return
     */
    @GetMapping("/bcrypt/{texto}")
    @ResponseBody
    public String encriptar(@PathVariable("texto") String texto) {
        return texto + " Encriptado en Bcrypt: " + passwordEncoder.encode(texto);
    }

    /**
     * Metodo que agrega al modelo datos genéricos para todo el controlador
     *
     * @param model
     */
    @ModelAttribute
    public void setGenericos(Model model) {
        model.addAttribute("categorias", serviceCategorias.buscarTodas());
        model.addAttribute("marcas", instrumentosService.buscarTodasLasMarcas());
        model.addAttribute("lista", instrumentosService.getAllInstrumentos());
        model.addAttribute("min", instrumentosService.getMinPrecio());
        model.addAttribute("max", instrumentosService.getMaxPrecio());
    }

    /**
     * InitBinder para Strings si los detecta vacios en el Data Binding los settea a NULL
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /**
     * FILTROS CONTROLLER
     */

    @GetMapping("/filtro/{marca}/{minmax}/{ascdesc}")
    public String filtros(Model model, HttpServletRequest request, @PathVariable("marca") String marca, @PathVariable("minmax") String minmax, @PathVariable("ascdesc") String ascdesc) {

//    String marcaSeleccionada = request.getParameter("marca");
//    String parametrosPrecio = request.getParameter("precio");
//    String orden = request.getParameter("orden");

//    List<Instrumento> listaInstrumentos = new ArrayList<>();
//
//    if (marcaSeleccionada != null && parametrosPrecio != null) {
//        int i = 1;
//    } else if(marcaSeleccionada != null) {
//        if(marcaSeleccionada == "") {
//            listaInstrumentos = instrumentosService.getAllInstrumentos();
//        } else {
//            listaInstrumentos = instrumentosService.buscarTodosDeCiertaMarca(marcaSeleccionada);
//        }
//    } else if (parametrosPrecio != null) {
//
//    }



        List<Instrumento> listaInstrumentos = new ArrayList<>();

        if(marca.equals("0") && minmax.equals("0") && ascdesc.equals("0")) {
            // NO HAY NADA SELECCIONADO
            listaInstrumentos = instrumentosService.getAllInstrumentos();
        } else if (!marca.equals("0") && minmax.equals("0") && ascdesc.equals("0")) {
            // SOLO MARCA
            listaInstrumentos = instrumentosService.buscarTodosDeCiertaMarca(marca);
        } else if(!marca.equals("0") && !minmax.equals("0") && ascdesc.equals("0")) {
            // MARCA Y PRECIO
            double min = Double.parseDouble(minmax.split("-")[0]);
            double max = Double.parseDouble(minmax.split("-")[1]);
            listaInstrumentos = instrumentosService.buscarTodosDeCiertaMarcaYRangoPrecio(marca, min, max);
        } else if(!marca.equals("0") && !minmax.equals("0") && !ascdesc.equals("0")) {
            // MARCA Y PRECIO Y ASCDESC
            double min = Double.parseDouble(minmax.split("-")[0]);
            double max = Double.parseDouble(minmax.split("-")[1]);
            if(ascdesc.equals("asc")) {
                listaInstrumentos = instrumentosService.buscarTodosDeCiertaMarcaYRangoPrecioASC(marca, min, max);
            } else {
                listaInstrumentos = instrumentosService.buscarTodosDeCiertaMarcaYRangoPrecioDESC(marca, min, max);
            }
        } else if(marca.equals("0") && !minmax.equals("0") && !ascdesc.equals("0")) {
            // PRECIO Y ASCDESC
            double min = Double.parseDouble(minmax.split("-")[0]);
            double max = Double.parseDouble(minmax.split("-")[1]);

            if(ascdesc.equals("asc")) {
                listaInstrumentos = instrumentosService.buscarConRangoASC(min, max);
            } else {
                listaInstrumentos = instrumentosService.buscarConRangoDESC(min, max);
            }
        } else if (marca.equals("0") && !minmax.equals("0") && ascdesc.equals("0")) {
            double min = Double.parseDouble(minmax.split("-")[0]);
            double max = Double.parseDouble(minmax.split("-")[1]);
            listaInstrumentos = instrumentosService.traerInstrumentosEntrePrecios(min, max);
        } else if(marca.equals("0") && minmax.equals("0") && !ascdesc.equals("0")) {
            if(ascdesc.equals("asc")){
                listaInstrumentos = instrumentosService.traerTodosLosInstrumentosASC();
            } else if (ascdesc.equals("desc")){
                listaInstrumentos = instrumentosService.traerTodosLosInstrumentosDESC();
            }
        }

        if(!minmax.equals("0")) {
            model.addAttribute("min", Double.parseDouble(minmax.split("-")[0]));
            model.addAttribute("max", Double.parseDouble(minmax.split("-")[1]));
        } else {
            model.addAttribute("min", instrumentosService.getMinPrecio());
            model.addAttribute("max", instrumentosService.getMaxPrecio());
        }

        model.addAttribute("lista", listaInstrumentos);

        model.addAttribute("categorias", serviceCategorias.buscarTodas());
        return "home";
    }

}
