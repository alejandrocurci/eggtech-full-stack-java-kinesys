package com.kinesysApp.kinesys.servicios;

import com.kinesysApp.kinesys.entidades.ObraSocial;
import com.kinesysApp.kinesys.entidades.Profesional;
import com.kinesysApp.kinesys.entidades.Usuario;
import com.kinesysApp.kinesys.entidades.Zona;
import com.kinesysApp.kinesys.enumeraciones.Especialidad;
import com.kinesysApp.kinesys.enumeraciones.Sexo;
import com.kinesysApp.kinesys.excepciones.ExcepcionKinessysProfesional;
import com.kinesysApp.kinesys.excepciones.ExcepcionKinesysPaciente;
import com.kinesysApp.kinesys.modelos.busqueda.BusquedaProfesional;
import com.kinesysApp.kinesys.repositorios.ProfesionalRepositorio;
import com.kinesysApp.kinesys.repositorios.ZonaRepositorio;
import com.kinesysApp.kinesys.roles.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ProfesionalServicio {

    @Autowired
    private ProfesionalRepositorio profesionalRepositorio;

    @Autowired
    private ZonaRepositorio zonaRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ZonaServicio zonaServicio;
    @Autowired
    private RolServicio rolServicio;
    @Autowired
    private ObraSocialServicio obraSocialServicio;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional
    public void crear(Long dni,
                      String nombre,
                      String apellido,
                      Integer edad,
                      Long telefono,
                      String email,
                      Especialidad especialidad,
                      Integer matricula,
                      Sexo sexo,
                      Zona zona,
                      ObraSocial obraSocial,
                      Usuario usuario) {

        Profesional profesional = new Profesional();
        String rolnombre = "PROFESIONAL";
        Rol rol=rolServicio.buscarPorNombre(rolnombre);
        profesional.setRolProfesional(rol);
        profesional.setDni(dni);
        profesional.setNombre(nombre);
        profesional.setApellido(apellido);
        profesional.setEdad(edad);
        profesional.setEspecialidad(especialidad);
        profesional.setTelefono(telefono);
        profesional.setEmail(email);
        profesional.setMatricula(matricula);
        profesional.setSexo(sexo);

        //seteamos el usuario
        profesional.setUsuarioProfesional(usuarioServicio.crear(usuario.getNombreU(), usuario.getClave(),rol));

        profesional.setZonaProfesionales(Arrays.asList(zona));

        profesional.setObraSocialProfesionales(Arrays.asList(obraSocial));

        profesionalRepositorio.save(profesional);
    }

    @Transactional(readOnly = true)
    public List<Profesional> buscarTodos() {
        return profesionalRepositorio.findAll();
    }

    @Transactional
    public void eliminarProfesional(String idProfesional) throws ExcepcionKinessysProfesional {
        profesionalRepositorio.deleteById(idProfesional);
    }

    @Transactional(readOnly = true)
    public Profesional buscarPorId(String idProfesional) {
        return profesionalRepositorio.findById(idProfesional).orElse(null);
    }

    public void validarProfesional(Long dni,
                                   String nombre,
                                   String apellido,
                                   Integer edad,
                                   Especialidad especialidad,
                                   Long telefono,
                                   String email,
                                   Integer matricula,
                                   Sexo sexo,
                                   Zona zona,
                                   ObraSocial obraSocial,
                                   Usuario usuario) throws ExcepcionKinessysProfesional {

        if (dni == null || dni <= 0) {
            throw new ExcepcionKinessysProfesional("El dni no puede ser nulo");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new ExcepcionKinessysProfesional("El nombre no puede ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ExcepcionKinessysProfesional("El apellido no puede ser vacio");
        }
        if (edad == null) {
            throw new ExcepcionKinessysProfesional("La edad no puede ser vacia");
        }
        if (edad <= 0) {
            throw new ExcepcionKinessysProfesional("La edad debe ser mayor que 0");
        }
        if (edad >= 120) {
            throw new ExcepcionKinessysProfesional("La edad debe ser menor que 120");
        }
        if (telefono == null) {
            throw new ExcepcionKinessysProfesional("El telefono no puede ser vacio");
        }

    }

    @Transactional
    public void modificar(String idProfesional, Long dni, String nombre, String apellido, Integer edad, Especialidad especialidad,
                          Long telefono, String email, Integer matricula, Sexo sexo) throws ExcepcionKinesysPaciente {
        // modificar usuario, obras sociales, zona se hacen aparte
        // obras sociales requieren agregar y eliminar de la lista
        // idem zona

        // VALIDAR PARAMETROS
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        if (profesional != null) {
            profesional.setRolProfesional(rolServicio.buscarPorNombre("PROFESIONAL"));
            profesional.setDni(dni);
            profesional.setNombre(nombre);
            profesional.setApellido(apellido);
            profesional.setEdad(edad);
            profesional.setEspecialidad(especialidad);
            profesional.setTelefono(telefono);
            profesional.setEmail(email);
            profesional.setMatricula(matricula);
            profesional.setSexo(sexo);
            profesionalRepositorio.save(profesional);
        }
    }

    // FUNCIONALIDAD PARA PERFIL

    @Transactional
    public void actualizarNombre(String idProfesional, String nombreNuevo) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        profesional.setNombre(nombreNuevo);
        profesionalRepositorio.save(profesional);
    }

    @Transactional
    public void actualizarApellido(String idProfesional, String apellidoNuevo) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        profesional.setApellido(apellidoNuevo);
        profesionalRepositorio.save(profesional);
    }

    @Transactional
    public void actualizarDni(String idProfesional, Long dniNuevo) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        profesional.setDni(dniNuevo);
        profesionalRepositorio.save(profesional);
    }

    @Transactional
    public void actualizarTelefono(String idProfesional, Long telefonoNuevo) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        profesional.setTelefono(telefonoNuevo);
        profesionalRepositorio.save(profesional);
    }

    @Transactional
    public void actualizarEdad(String idProfesional, Integer edadNueva) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        profesional.setEdad(edadNueva);
        profesionalRepositorio.save(profesional);
    }

    @Transactional
    public void actualizarSexo(String idProfesional, Sexo sexo) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        profesional.setSexo(sexo);
        profesionalRepositorio.save(profesional);
    }

    @Transactional
    public void actualizarEspecialidad(String idProfesional, Especialidad especialidad) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        profesional.setEspecialidad(especialidad);
        profesionalRepositorio.save(profesional);
    }

    @Transactional
    public void actualizarEmail(String idProfesional, String emailNuevo) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        profesional.setEmail(emailNuevo);
        profesionalRepositorio.save(profesional);
    }

    @Transactional
    public void actualizarMatricula(String idProfesional, Integer matriculaNueva) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        profesional.setMatricula(matriculaNueva);
        profesionalRepositorio.save(profesional);
    }

    @Transactional
    public void actualizarUsuario(String idProfesional, String usuarioNuevo) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        profesional.getUsuarioProfesional().setNombreU(usuarioNuevo);
        profesionalRepositorio.save(profesional);
    }

    @Transactional
    public void actualizarClave(String idProfesional, String claveNueva) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        profesional.getUsuarioProfesional().setClave(encoder.encode(claveNueva));
        profesionalRepositorio.save(profesional);
    }

    // ZONAS
    @Transactional
    public void agregarZona(String idProfesional, Zona zonaNueva) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        profesional.getZonaProfesionales().add(zonaNueva);
        profesionalRepositorio.save(profesional);
    }

    @Transactional
    public void eliminarZona(String idProfesional, String idZona) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        Zona zonaParaEliminar = zonaServicio.buscarPorId(idZona);
        List<Zona> zonas = profesional.getZonaProfesionales();
        zonas.remove(zonaParaEliminar);
        profesional.setZonaProfesionales(zonas);
        profesionalRepositorio.save(profesional);
    }

    // OBRAS SOCIALES
    @Transactional
    public void agregarObraSocial(String idProfesional, String idObraSocial) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        ObraSocial obraParaAgregar = obraSocialServicio.buscarPorId(idObraSocial);
        profesional.getObraSocialProfesionales().add(obraParaAgregar);
        profesionalRepositorio.save(profesional);
    }

    @Transactional
    public void eliminarObraSocial(String idProfesional, String idObraSocial) {
        Profesional profesional = profesionalRepositorio.findById(idProfesional).orElse(null);
        ObraSocial obraParaEliminar = obraSocialServicio.buscarPorId(idObraSocial);
        List<ObraSocial> obras = profesional.getObraSocialProfesionales();
        obras.remove(obraParaEliminar);
        profesional.setObraSocialProfesionales(obras);
        profesionalRepositorio.save(profesional);
    }
     
    // FUNCIONALIDAD DE FILTRADO POR OBRA SOCIAL, PROVINCIA Y ESPECIALIDAD
    @Transactional(readOnly = true)
    public List<Profesional> buscarPorFiltro(BusquedaProfesional busqueda) throws ExcepcionKinessysProfesional {

        List<Profesional> profesionales=profesionalRepositorio.findAll();
        List<Profesional> listaFiltrado= new ArrayList<>();

        if(!busqueda.getIdObraSocial().isEmpty()){
            ObraSocial oS=  obraSocialServicio.buscarPorId(busqueda.getIdObraSocial());
            profesionales.removeIf((p)-> !p.getObraSocialProfesionales().contains(oS));

        }
        if(busqueda.getEspecialidad() != null){
            profesionales.removeIf((p)-> !p.getEspecialidad().name().equals(busqueda.getEspecialidad().name()));
        }

        if(busqueda.getProvincia() != null){
            for (Profesional p:profesionales) {
                for (Zona z:p.getZonaProfesionales()) {
                    if(z.getProvincia().toString().equals(busqueda.getProvincia().toString())){
                        listaFiltrado.add(p);
                        break;
                    }
                }
            }
            if(listaFiltrado.isEmpty()){
                throw  new ExcepcionKinessysProfesional(" No hay profesionales con esas caracteristicas");
            }else {
                return  listaFiltrado;
            }

        }
        if(profesionales.isEmpty()){
            throw  new ExcepcionKinessysProfesional(" No hay profesionales con esas caracteristicas");
        }else {
            return profesionales;
        }


    }

    // PAGINACION
    public Page<Profesional> buscarPagina(Pageable pageable, List<Profesional> profesionales) {
        int tamano = pageable.getPageSize();
        int paginaActual = pageable.getPageNumber();
        int itemInicial = tamano * paginaActual;
        List<Profesional> lista;

        if (profesionales.size() < itemInicial) {
            lista = Collections.emptyList();
        } else {
            int indiceHasta = Math.min(itemInicial + tamano, profesionales.size());
            lista = profesionales.subList(itemInicial, indiceHasta);
        }

        Page<Profesional> paginacion = new PageImpl<>(lista, PageRequest.of(paginaActual, tamano), profesionales.size());
        return paginacion;
    }
}
