/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabhangman.model.game;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 *
 * @author 2103021
 */
public class HangmanRedisGame extends HangmanGame{
    
    private String word;
    private char[] guessedWord;
    private String winner="";
    private boolean gameFinished=false;
    private String gameId;
    private StringRedisTemplate template;

    public HangmanRedisGame(String word, int gameId, StringRedisTemplate template) {
        super(word);
        this.gameId = "game:"+gameId;
        this.template = template;
        this.word = (String)template.opsForHash().get(this.gameId, "completeWord");
        String wordToGuess = (String)template.opsForHash().get(this.gameId, "discoveredWord");
        guessedWord=wordToGuess.toCharArray();
    }
    
    
    /**
     * @pre gameFinished==false
     * @param l new letter
     * @return the secret word with all the characters 'l' revealed
     */
    public String addLetter(char l){
        this.word = (String)template.opsForHash().get(gameId, "completeWord");
        String wordToGuess = (String)template.opsForHash().get(this.gameId, "discoveredWord");
        guessedWord=wordToGuess.toCharArray();
        for (int i=0;i<word.length();i++){
            if (word.charAt(i)==l){
                guessedWord[i]=l;
            }            
        }
        template.opsForHash().put(gameId,new String(guessedWord),"discoveredWord");
        return new String(guessedWord);
    }
    
    public synchronized boolean tryWord(String playerName,String s){
        this.word = (String)template.opsForHash().get(gameId, "completeWord");
        if (s.toLowerCase().equals(word)){
            winner=playerName;
            gameFinished=true;
            guessedWord=word.toCharArray();
            template.opsForHash().put(gameId,new String(guessedWord),"discoveredWord");
            template.opsForHash().put(gameId,"ended","status");
            return true;
        }
        return false;
    }
    
    public boolean gameFinished(){
        return gameFinished;
    }
    
    /**
     * @pre gameFinished=true;
     * @return winner's name
     */
    public String getWinnerName(){
        return winner;
    }
    
    public String getCurrentGuessedWord(){
        return new String(guessedWord);
    }
}
