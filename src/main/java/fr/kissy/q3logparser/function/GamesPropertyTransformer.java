package fr.kissy.q3logparser.function;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.istack.internal.Nullable;
import fr.kissy.q3logparser.dto.Game;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

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
    public Game transformEntry(@Nullable String key, @Nullable String value) {
        try {
            return kryo.readObject(new Input(new FileInputStream(outputGamesDirectory + key + File.separator + "game.bin")), Game.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
