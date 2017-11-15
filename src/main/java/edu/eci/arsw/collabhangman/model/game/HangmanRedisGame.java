/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabhangman.model.game;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 *
 * @author 2103021
 */
public class HangmanRedisGame extends HangmanGame {

    private String word;
    private char[] guessedWord;
    private String winner = "";
    private boolean gameFinished = false;
    private String gameId;
    private StringRedisTemplate template;

    @Autowired
    RedisScript<Boolean> script;

    public HangmanRedisGame(String word, int gameId, StringRedisTemplate template) {
        super(word);
        this.gameId = "game:" + gameId;
        this.template = template;
        this.word = (String) template.opsForHash().get(this.gameId, "completeWord");
        String wordToGuess = (String) template.opsForHash().get(this.gameId, "discoveredWord");
        guessedWord = wordToGuess.toCharArray();
    }

    @Bean
    public RedisScript<Boolean> script() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<Boolean>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("script.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

    /**
     * @pre gameFinished==false
     * @param l new letter
     * @return the secret word with all the characters 'l' revealed
     */
    public String addLetter(char l) {
        this.word = (String) template.opsForHash().get(gameId, "completeWord");
        String wordToGuess = (String) template.opsForHash().get(this.gameId, "discoveredWord");
        guessedWord = wordToGuess.toCharArray();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == l) {
                guessedWord[i] = l;
            }
        }
        template.opsForHash().put(gameId, "discoveredWord", new String(guessedWord));
        return new String(guessedWord);
    }

    public synchronized boolean tryWord(String playerName, String s) {
        this.word = (String) template.opsForHash().get(gameId, "completeWord");
        if (s.toLowerCase().equals(word)) {
            winner = playerName;
            gameFinished = true;

            template.execute(new SessionCallback< List< Object>>() {
                @SuppressWarnings("unchecked")

                @Override

                public < K, V> List<Object> execute(final RedisOperations< K, V> operations)
                        throws DataAccessException {
                    operations.watch((K) (gameId + " discoveredword"));
                    operations.multi();
                    guessedWord = word.toCharArray();
                    template.opsForHash().put(gameId, "discoveredWord", new String(guessedWord));
                    template.opsForHash().put(gameId, "status", "ended");
                    return operations.exec();
                }
            });

            return true;
        }
        return false;
    }

    public boolean gameFinished() {
        return gameFinished;
    }

    /**
     * @pre gameFinished=true;
     * @return winner's name
     */
    public String getWinnerName() {
        return winner;
    }

    public String getCurrentGuessedWord() {
        return new String(guessedWord);
    }
}
