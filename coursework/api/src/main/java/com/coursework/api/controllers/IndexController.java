package com.coursework.api.controllers;

import com.coursework.indices.InverseIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    private final InverseIndex index;

    @Autowired
    public IndexController(InverseIndex index){
        this.index = index;
    }

    @PutMapping("/")
    public void buildIndex(){
        this.index.buildIndex();
    }

}
