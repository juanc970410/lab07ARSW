/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabhangman.persistence.stub;

import edu.eci.arsw.collabhangman.model.game.entities.User;
import edu.eci.arsw.collabhangman.persistence.PersistenceException;
import edu.eci.arsw.collabhangman.persistence.UsersRepository;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 *
 * @author 2103021
 */
@Service
public abstract class UsersRepositoryStubMongo implements UsersRepository{
    /**
     *
     * @param id
     * @return
     * @throws edu.eci.arsw.collabhangman.persistence.PersistenceException
     */
    @Override
    public abstract User findById(Integer id) throws PersistenceException;
    
    
    /**
     *
     * @param puntaje
     * @return
     * @throws edu.eci.arsw.collabhangman.persistence.PersistenceException
     */
    
    @Override
    public abstract Set<User> getAllUsers(Integer puntaje) throws PersistenceException;
}
