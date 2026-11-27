package cl.duoc.embotelladora.dao;

import cl.duoc.embotelladora.model.Cliente;
import cl.duoc.embotelladora.model.Despacho;

import java.util.List;

public interface DespachoDAO {

    List<Cliente> obtenerClientes() throws PersistenciaException;
    Cliente buscarClientePorRut(String rut) throws PersistenciaException;
    void insertarCliente(Cliente cliente) throws PersistenciaException;
    List<Despacho> obtenerDespachos() throws PersistenciaException;
    Despacho buscarDespachoPorNumero(String numeroDespacho) throws PersistenciaException;
    void insertarDespacho(Despacho despacho) throws PersistenciaException;
    void actualizarEstado(String numeroDespacho, String nuevoEstado) throws PersistenciaException;
}
