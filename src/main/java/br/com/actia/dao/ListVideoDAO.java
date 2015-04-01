package br.com.actia.dao;

import br.com.actia.model.ListVideo;
import java.util.List;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public interface ListVideoDAO {
/**
     * Faz a inserção ou atualização de um ListVideo na base de dados.
     * 
     * @param listVideo
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    ListVideo save(ListVideo listVideo);
    /**
     * Exclui o registro de ListVideo na base de dados
     *
     * @param listVideo
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(ListVideo listVideo);

    /**
     * @return Lista com todos os ListVideos cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<ListVideo> getAll();

    /**
     * @param name Filtro da pesquisa de ListVideos.
     * @return Lista de POIs com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<ListVideo> getListVideoByName(String name);
    
    /**
     * @param id filtro da pesquisa.
     * @return ListVideo com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    ListVideo findById(Integer id);
}
