package com.kascend.idea.jellyfish.config.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * Date: 2019/08/16
 *
 * @author junhui.si
 **/
public class TextBoxes2 extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Messages.showInputDialog(
                project,
                "What is your name?",
                "Input your name",
                Messages.getQuestionIcon());
    }
}
