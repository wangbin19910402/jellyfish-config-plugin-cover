package com.kascend.idea.jellyfish.config;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ApplicationComponent;
import com.kascend.idea.jellyfish.config.action.ProjectFormatAction;

public class JellyfishConfigPluginRegistration implements ApplicationComponent {

    @NotNull
    @Override
    public String getComponentName() {
        return "JellyfishConfigPlugin";
    }

    @Override
    public void initComponent() {
//        ActionManager am = ActionManager.getInstance();
//        ProjectFormatAction projectFormatAction = new ProjectFormatAction();
//        am.registerAction("JellyfishConfigPlugin.ProjectFormatAction", projectFormatAction);
//
//        DefaultActionGroup windowM = (DefaultActionGroup) am.getAction("ToolsMenu");
//
//        windowM.addSeparator();
//        windowM.add(projectFormatAction);
    }

    @Override
    public void disposeComponent() {

    }
}
