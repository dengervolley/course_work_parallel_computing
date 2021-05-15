package com.coursework.tokenizers;

import com.coursework.loggers.ILogger;
import com.coursework.models.Token;
import com.coursework.suppliers.ITextSupplier;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TextTokenizer extends Tokenizer {

    private final String wordRegex = "[\\w']+";
    private final ITextSupplier textSupplier;
    public TextTokenizer(ITextSupplier textSupplier) {
        this.textSupplier = textSupplier;
    }

    @Override
    public List<Token> tokenize(String fileName) {
        List<String> textLines = textSupplier.getAllLines(fileName);
        return tokenizeLines(textLines, wordRegex, fileName);
    }


}
