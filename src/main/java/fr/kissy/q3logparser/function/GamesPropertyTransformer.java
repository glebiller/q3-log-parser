package fr.kissy.q3logparser.function;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.google.common.collect.Maps;
import fr.kissy.q3logparser.dto.Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class GamesPropertyTransformer implements Maps.EntryTransformer<String, String, Game> {
    private Kryo kryo;
    private String outputGamesDirectory;

    public GamesPropertyTransformer(String outputGamesDirectory, Kryo kryo) {
        this.kryo = kryo;
        this.outputGamesDirectory = outputGamesDirectory;
    }

    @Override
    public Game transformEntry(String key, String value) {
        try {
            return kryo.readObject(new Input(new FileInputStream(outputGamesDirectory + key + File.separator + "game.bin")), Game.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
