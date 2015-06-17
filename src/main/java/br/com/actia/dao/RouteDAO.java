package br.com.actia.dao;

import br.com.actia.model.Route;
import java.util.List;

public interface RouteDAO {
    /**
     * Faz a inserção ou atualização de um Route na base de dados.
     * 
     * @param Route
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    Route save(Route route);
    /**
     * Exclui o registro de Route na base de dados
     *
     * @param route
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(Route route);

    /**
     * @return Lista com todos os Routes cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<Route> getAll();

    /**
     * @param name Filtro da pesquisa de Routes.
     * @return Lista de Routes com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<Route> getRouteByName(String name);
    
    
    /**
     * @param id filtro da pesquisa.
     * @return Route com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    Route findById(Integer id);
    
}
