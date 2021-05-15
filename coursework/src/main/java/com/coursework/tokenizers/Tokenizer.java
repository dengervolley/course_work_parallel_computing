package com.coursework.tokenizers;

import com.coursework.models.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public abstract class Tokenizer implements ITokenizer {
    protected List<Token> tokenizeLines(List<String> text, String regex, String fileName) {
        var allMatches = new ArrayList<Token>();
        var pattern = Pattern.compile(regex);
        for (var line : text) {
            var matcher = pattern.matcher(line);
            while (matcher.find()) {
                var existingToken = allMatches.stream()
                        .filter(x -> x.value().equals(matcher.group()))
                        .findAny()
                        .orElse(null);
                if (existingToken != null) {
                    existingToken.matches().get(fileName).add(matcher.start());
                } else {
                    var newToken = new Token(matcher.group());
                    newToken.matches().put(fileName, new ArrayList<>(Arrays.asList(matcher.start())));
                    allMatches.add(newToken);
                }
            }
        }
        return allMatches;
    }
}
