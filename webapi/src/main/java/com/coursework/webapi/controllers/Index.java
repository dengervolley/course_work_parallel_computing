package com.coursework.webapi.controllers;

import com.coursework.indices.InverseIndex;
import com.coursework.models.IndexItem;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Index {
    private InverseIndex index;



    @PostMapping("/build")
    List<IndexItem> getIndexItems(){

    }


}
