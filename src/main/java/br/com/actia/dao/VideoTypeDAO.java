/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.actia.dao;

import br.com.actia.model.VideoType;
import java.util.List;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public interface VideoTypeDAO {
    /**
     * Faz a inserção ou atualização de um BusStop na base de dados.
     * 
     * @param VideoType
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    VideoType save(VideoType videoType);
    /**
     * Exclui o registro de VideoType na base de dados
     *
     * @param videoType
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(VideoType videoType);

    /**
     * @return Lista com todos os VideoTypes cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<VideoType> getAll();

    /**
     * @param name Filtro da pesquisa de VideoTypes.
     * @return Lista de VideoTypes com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<VideoType> getVideoTypeByName(String name);
    
    
    /**
     * @param id filtro da pesquisa.
     * @return VideoType com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    VideoType findById(Integer id);
}
