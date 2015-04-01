package br.com.actia.dao;

import br.com.actia.model.Video;
import java.util.List;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public interface VideoDAO {
    /**
     * Faz a inserção ou atualização de um BusStop na base de dados.
     * 
     * @param Video
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    Video save(Video video);
    /**
     * Exclui o registro de Video na base de dados
     *
     * @param video
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(Video video);

    /**
     * @return Lista com todos os Videos cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<Video> getAll();

    /**
     * @param name Filtro da pesquisa de Videos.
     * @return Lista de Videos com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<Video> getVideoByName(String name);
    
    
    /**
     * @param id filtro da pesquisa.
     * @return Video com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    Video findById(Integer id);
}
