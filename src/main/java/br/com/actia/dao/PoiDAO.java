package br.com.actia.dao;

import br.com.actia.model.Poi;
import java.util.List;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public interface PoiDAO {
    /**
     * Faz a inserção ou atualização de um POI na base de dados.
     * 
     * @param poi
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    Poi save(Poi poi);
    /**
     * Exclui o registro de POI na base de dados
     *
     * @param poi
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(Poi poi);

    /**
     * @return Lista com todos os POIs cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<Poi> getAll();

    /**
     * @param name Filtro da pesquisa de POIs.
     * @return Lista de POIs com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<Poi> getPoiByName(String name);
    
    /**
     * 
     * @param latitude
     * @param longitude
     * @param radius
     * @return Lista de POIs com filtro por Latitude e Longitude de acordo com um raio
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    //List<Poi> getPoiByLatLong(Double latitude, Double longitude, Double radius);

    /**
     * @param id filtro da pesquisa.
     * @return Poi com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    Poi findById(Integer id);

}
