package fr.kissy.q3logparser;

import fr.kissy.q3logparser.app.DataPathApp;
import fr.kissy.q3logparser.config.ApplicationConfig;
import fr.kissy.q3logparser.generator.impl.IndexGenerator;
import joptsimple.OptionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.JOptCommandLinePropertySource;
import org.springframework.core.env.PropertySource;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
@SuppressWarnings("UnusedDeclaration")
public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        OptionParser parser = new OptionParser();
        parser.accepts("data-directory", "The data directory, default to '_data'").withRequiredArg().defaultsTo("_data");
        parser.accepts("logs-directory", "The logs directory, default to '_logs'").withRequiredArg().defaultsTo("_logs");
        parser.accepts("matches-directory", "The matches directory inside the data directory, default to 'matches'").withRequiredArg().defaultsTo("matches");
        parser.accepts("stats-directory", "The stats directory inside the data directory, default to 'stats'").withRequiredArg().defaultsTo("stats");
        parser.accepts("alias-file", "The alias file, default to '${data-directory}/alias.properties'").withRequiredArg().defaultsTo("${data-directory}/alias.properties");
        parser.accepts("data-path", "The games log or stats directory or files to parse, default to 'games.log'").withRequiredArg().defaultsTo("games.log");
        parser.accepts("generate-matches", "Generate the matches page from the data file, default to 'true'").withRequiredArg().defaultsTo("true");
        parser.accepts("generate-index", "Generate the index page from the data file, default to 'true'").withRequiredArg().defaultsTo("true");
        parser.accepts("generate-stats", "Generate the stats page from the data file, default to 'true'").withRequiredArg().defaultsTo("true");
        parser.accepts("archive", "Archive the parsed files in the logs directory, default to 'false'").withRequiredArg().defaultsTo("false");

        PropertySource propertySource = new JOptCommandLinePropertySource(parser.parse(args));
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.getEnvironment().getPropertySources().addFirst(propertySource);
        applicationContext.register(ApplicationConfig.class);
        applicationContext.refresh();
        applicationContext.getBean(DataPathApp.class).walkAndProcess();
        applicationContext.close();
    }
}
