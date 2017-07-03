package com.qreal.wmp.generator.model;

import com.qreal.wmp.thrift.gen.TModel;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Model {
    private String name;

    private String userName;

    private Set<Node> nodes = new HashSet<>();

    public Model() {
    }

    /** Constructor-converter from Thrift TModel to Model.*/
    public Model(TModel tModel) {
        if (tModel.isSetName()) {
            name = tModel.getName();
        }

        if (tModel.isSetUser()) {
            userName = tModel.getUser();
        }

        if (tModel.isSetNodes()) {
            nodes = tModel.getNodes().stream().map(Node::new).collect(Collectors.toSet());
        }
    }

    /** Converter from Model to Thrift TModel.*/
    public TModel toTModel() {
        TModel tModel = new TModel();

        if (name != null) {
            tModel.setName(name);
        }

        if (userName != null) {
            tModel.setUser(userName);
        }

        if (nodes != null && !nodes.isEmpty()) {
            tModel.setNodes(nodes.stream().map(Node::toTNode).
                    collect(Collectors.toSet()));
        }

        return tModel;
    }
}
