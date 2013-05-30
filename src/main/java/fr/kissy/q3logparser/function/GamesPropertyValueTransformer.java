package fr.kissy.q3logparser.function;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class GamesPropertyValueTransformer implements Function<String, List<String>> {
    public static final GamesPropertyValueTransformer INSTANCE = new GamesPropertyValueTransformer();
    public static final Character GAME_PROPERTY_SEPARATOR = '|';

    @Override
    public List<String> apply(String s) {
        return Lists.newArrayList(StringUtils.split(s, GAME_PROPERTY_SEPARATOR));
    }
}
