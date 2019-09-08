package com.seko0716.es.plugin.pipeline.actions.input;

import com.seko0716.es.plugin.pipeline.actions.Action;
import org.apache.logging.log4j.util.Supplier;

import java.util.List;
import java.util.Map;

public interface Input extends Supplier<List<Map<String, Object>>> , Action {

    default String getGroup(){
        return "input";
    }
}
