package com.coursework.api.controllers;

import com.coursework.indices.InverseIndex;
import com.coursework.models.IndexItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/index")
public class IndexController {
    private final InverseIndex index;

    @Autowired
    public IndexController(InverseIndex index){
        this.index = index;
    }

    @PutMapping("/")
    public void buildIndex(){
        this.index.buildIndex();
        this.index.saveToFile("index/inverse-index.json");
    }

    @GetMapping("/")
    public List<IndexItem> getIndex(){ return this.index.getIndex(); }

}
