package com.coursework.api.config;


import com.coursework.api.persistence.IFileSaver;
import com.coursework.api.persistence.ServerFileSaver;
import com.coursework.indices.InverseIndex;
import com.coursework.loggers.ConsoleLogger;
import com.coursework.loggers.ILogger;
import com.coursework.persistence.FilePersistenceProvider;
import com.coursework.persistence.IPersistenceProvider;
import com.coursework.suppliers.FileTextSupplier;
import com.coursework.suppliers.ITextSupplier;
import com.coursework.tokenizers.ITokenizer;
import com.coursework.tokenizers.TextTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private static final int NUM_THREADS = 4;
    private static final InverseIndex index = new InverseIndex(getTokenizer(),
            getPersistenceProvider(), NUM_THREADS, getLogger());

    @Bean
    public static ITokenizer getTokenizer() {
        return new TextTokenizer(getTextSupplier());
    }

    @Bean
    public static ITextSupplier getTextSupplier() {
        return new FileTextSupplier(getLogger());
    }

    @Bean
    public static ILogger getLogger() {
        return new ConsoleLogger();
    }

    @Bean
    public static IPersistenceProvider getPersistenceProvider() {
        return new FilePersistenceProvider(getLogger());
    }

    @Bean
    public InverseIndex getIndex() {
        return index;
    }

    @Bean
    public IFileSaver getFileSaver() {
        return new ServerFileSaver();
    }
}
