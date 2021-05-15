package com.coursework.tokenizers;

import com.coursework.models.Token;

import java.util.List;

public interface ITokenizer {
    List<Token> tokenize(String fileName);
}
