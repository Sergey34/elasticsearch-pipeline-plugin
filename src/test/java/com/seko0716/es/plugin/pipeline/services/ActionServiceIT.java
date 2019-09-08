package com.seko0716.es.plugin.pipeline.services;

import com.seko0716.es.plugin.pipeline.actions.Action;
import org.elasticsearch.test.ESTestCase;
import org.junit.Assert;

public class ActionServiceIT extends ESTestCase {

    public void testLoadActions() {
        String aClass = "com.seko0716.es.plugin.pipeline.actions.filter.RandomFilterErr";
        Action action = ActionService.loadAction(aClass, Action.class);
        Assert.assertEquals(aClass, action.getActionName());
    }
}
