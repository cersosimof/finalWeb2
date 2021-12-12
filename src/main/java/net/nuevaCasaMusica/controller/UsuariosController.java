package net.nuevaCasaMusica.controller;

import java.util.Date;
import java.util.List;

import net.nuevaCasaMusica.model.Categoria;
import net.nuevaCasaMusica.model.Perfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import net.nuevaCasaMusica.model.Usuario;
import net.nuevaCasaMusica.service.IUsuariosService;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {
	
    @Autowired
	private IUsuariosService serviceUsuarios;

	@Autowired
	private PasswordEncoder passwordEncoder;

    @GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Usuario> lista = serviceUsuarios.buscarRegistrados();
    	model.addAttribute("usuarios", lista);
		return "usuarios/listUsuarios";
	}
    
	// SE USA
    @GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {
    	serviceUsuarios.eliminar(idUsuario);
		attributes.addFlashAttribute("msg", "El usuario fue eliminado con exito.");
		return "redirect:/usuarios/index";
	}

	@GetMapping("/crearNuevoUsuario")
	public String crearNuevoUsuario(Usuario usuario) {
//		List<Usuario> lista = serviceUsuarios.buscarRegistrados();
//		model.addAttribute("usuarios", lista);
		return "usuarios/formUsuario";
	}

	@PostMapping("/crearNuevoUsuario")
	public String guardar(Usuario usuario, BindingResult result, Model model, RedirectAttributes attributes) {

		if (result.hasErrors()){
			System.out.println("Existieron errores");
			return "usuarios/formCategoria";
		}

		String pwdPlano = usuario.getPassword();
		String pwdEncriptado = passwordEncoder.encode(pwdPlano);
		usuario.setPassword(pwdEncriptado);
		usuario.setEstatus(1);
		usuario.setFechaRegistro(new Date());
		Perfil perfil = new Perfil();
		perfil.setId(Integer.parseInt(usuario.getRol()));
		usuario.agregar(perfil);
		serviceUsuarios.guardar(usuario);



		attributes.addFlashAttribute("msg", "Los datos del usuario fueron guardados!");

		//return "redirect:/categorias/index";
		return "redirect:/usuarios/index";
	}

	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idUser, Model model) {
		Usuario user = serviceUsuarios.buscarPorId(idUser);
		model.addAttribute("usuario", user);
		return "usuarios/formUsuario";
	}

}
